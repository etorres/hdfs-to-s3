package es.eriktorr.hdfs.tools

import java.io.File

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

final class HdfsToS3ConfigLoader private () {
  private[this] val hdfs = "hdfs"
  private[this] val aws = "aws"

  def defaultConfig: IO[HdfsToS3Config] = IO {
    val config: Config = ConfigFactory.load()

    val hdfsConfig: HdfsConfig = config.as[HdfsConfig](hdfs)
    val awsConfig: AwsConfig = config.as[AwsConfig](aws)

    HdfsToS3Config(hdfsConfig, awsConfig)
  }

  def configFrom(configFile: String): IO[HdfsToS3Config] = IO {
    val baseConfig = ConfigFactory.load()
    val customConfig = ConfigFactory.parseFile(new File(configFile))

    val hdfsConfig: HdfsConfig = customConfig.withFallback(baseConfig).as[HdfsConfig](hdfs)
    val awsConfig: AwsConfig = customConfig.withFallback(baseConfig).as[AwsConfig](aws)

    HdfsToS3Config(hdfsConfig, awsConfig)
  }
}

object HdfsToS3ConfigLoader {
  def apply(): HdfsToS3ConfigLoader = new HdfsToS3ConfigLoader()
}
