package es.eriktorr.hdfs.tools

import cats.effect.IO
import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

sealed case class AwsConfig(accessKeyId: String, secretAccessKey: String, region: String)
sealed case class HdfsConfig(baseUri: String, superUser: String, superGroup: String)

sealed case class HdfsToS3Config(
  hdfsConfig: HdfsConfig,
  awsConfig: AwsConfig
)

final class HdfsToS3ConfigLoader {
  def defaultConfig: IO[HdfsToS3Config] = IO {
    val config: Config = ConfigFactory.load()

    val hdfsConfig: HdfsConfig = config.as[HdfsConfig]("hdfs")
    val awsConfig: AwsConfig = config.as[AwsConfig]("aws")

    HdfsToS3Config(hdfsConfig, awsConfig)
  }
}
