package es.eriktorr.hdfs.tools

import cats.effect.{Blocker, ExitCode, IO, IOApp, Resource}
import cats.implicits._
import es.eriktorr.hdfs.tools.effects.ErrorHandlingSyntax._
import es.eriktorr.hdfs.tools.model.{HdfsPath, S3Path}
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

object HdfsToS3App extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    def programResource: Resource[IO, HdfsToS3Context] =
      for {
        context <- HdfsToS3Context()
      } yield context

    for {
      logger <- Slf4jLogger.fromName[IO]("hdfs-to-s3")
      _ <- logger.info(
        s"${BuildInfo.name}, version ${BuildInfo.version}, built at ${BuildInfo.builtAtString}"
      )
      result <- Blocker[IO].use { blocker =>
        for {
          exitCode <- blocker.blockOn {
            programResource
              .use(_.hdfsToS3.syncFolder(HdfsPath(""), S3Path("")))
              .as(ExitCode.Success)
              .logErrors(logger)
              .handleError(_ => ExitCode.Error)
          }
        } yield exitCode
      }
      _ <- logger.info("Goodbye.")
    } yield result
  }
}
