package es.eriktorr.hdfs.tools

import cats.effect.{Blocker, ExitCode, IO, IOApp}
import cats.implicits._
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

object HdfsToS3App extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      logger <- Slf4jLogger.fromName[IO]("hdfsToS3")
      _ <- logger.info(
        s"${BuildInfo.name}, version ${BuildInfo.version}, built at ${BuildInfo.builtAtString}"
      )
      params <- HdfsToS3Params.from(args)(logger)
      contextResource = for {
        context <- HdfsToS3Context(params.configFile)
      } yield context
      result <- Blocker[IO].use { blocker =>
        for {
          exitCode <- blocker.blockOn {
            contextResource
              .use(_.hdfsToS3.syncFolder(params.source, params.destination))
              .as(ExitCode.Success)
              .logErrors(logger)
              .handleError(_ => ExitCode.Error)
          }
        } yield exitCode
      }
      _ <- logger.info("Goodbye.")
    } yield result
}
