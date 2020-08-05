package es.eriktorr.hdfs.tools

import cats.effect.IO
import es.eriktorr.hdfs.tools.model.{HdfsPath, S3Path}
import io.chrisdavenport.log4cats.SelfAwareStructuredLogger

sealed case class HdfsToS3Params(configFile: Option[String], source: HdfsPath, destination: S3Path)

object HdfsToS3Params extends OptionParser {
  def from(
    args: List[String]
  )(implicit logger: SelfAwareStructuredLogger[IO]): IO[HdfsToS3Params] = {
    val options = optionsFrom(args)
    val configFile = options.get(Symbol("config"))
    for {
      source <- paramFrom(options, "source")
      destination <- paramFrom(options, "destination")
      _ <- options.get(OptionParser.unknown) match {
        case Some(value) => logger.warn(s"Unknown options ignored: $value")
        case None => IO.pure(())
      }
    } yield new HdfsToS3Params(configFile, HdfsPath(source), S3Path(destination))
  }

  private def paramFrom(options: OptionMap, paramName: String) =
    options.get(Symbol(paramName)) match {
      case Some(source) => IO.pure(source)
      case None =>
        IO.raiseError(new IllegalArgumentException(s"Missing required parameter: $paramName"))
    }
}
