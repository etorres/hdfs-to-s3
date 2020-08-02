package es.eriktorr.hdfs.tools

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

sealed case class AwsConfig(accessKeyId: String, secretAccessKey: String, region: String)
sealed case class HdfsConfig(baseUri: String, superUser: String, superGroup: String)

sealed case class ApplicationContext(
  hdfsConfiguration: HdfsConfig,
  awsConfiguration: AwsConfig
)

object ApplicationContextLoader {
  private[this] val hdfs = "hdfs"
  private[this] val aws = "aws"

  def defaultApplicationContext: ApplicationContext = {
    val config: Config = ConfigFactory.load()

    val hdfsConfig: HdfsConfig = config.as[HdfsConfig](hdfs)
    val awsConfig: AwsConfig = config.as[AwsConfig](aws)

    ApplicationContext(hdfsConfig, awsConfig)
  }
}
