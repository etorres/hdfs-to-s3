package es.eriktorr.hdfs.tools

import cats.effect.IO
import es.eriktorr.hdfs.tools.HdfsToS3App.optionsFrom
import es.eriktorr.hdfs.tools.model.{HdfsPath, S3Path}
import io.chrisdavenport.log4cats.SelfAwareStructuredLogger

sealed case class HdfsToS3Params(configFile: Option[String], source: HdfsPath, destination: S3Path)

object HdfsToS3Params {
  def from(args: List[String])(implicit logger: SelfAwareStructuredLogger[IO]): IO[HdfsToS3Params] =
    for {
      options <- optionsFrom(args)(logger)
      configFile = options.get(Symbol("config"))
      source <- paramFrom(options, "source")
      destination <- paramFrom(options, "destination")
    } yield new HdfsToS3Params(configFile, HdfsPath(source), S3Path(destination))

  private def paramFrom(options: OptionMap, paramName: String) =
    options.get(Symbol(paramName)) match {
      case Some(source) => IO.pure(source)
      case None =>
        IO.raiseError(new IllegalArgumentException(s"Missing required parameter: $paramName"))
    }
}
