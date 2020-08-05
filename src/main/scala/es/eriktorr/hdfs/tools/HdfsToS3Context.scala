package es.eriktorr.hdfs.tools

import cats.effect.{IO, Resource}
import es.eriktorr.hdfs.tools.application.HdfsToS3

final class HdfsToS3Context(config: HdfsToS3Config) {
  val hdfsToS3: HdfsToS3 = new HdfsToS3(config.hdfsConfig, config.awsConfig)
}

object HdfsToS3Context {
  def apply(configFile: Option[String]): Resource[IO, HdfsToS3Context] =
    for {
      config <- Resource.liftF(configFile match {
        case Some(configFile) => HdfsToS3ConfigLoader().configFrom(configFile)
        case None => HdfsToS3ConfigLoader().defaultConfig
      })
    } yield new HdfsToS3Context(config)
}
