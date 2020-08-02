package es.eriktorr.hdfs.tools

import es.eriktorr.hdfs.tools.model.{HdfsPath, S3Path}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.s3a.Constants
import org.apache.hadoop.fs.{CommonConfigurationKeysPublic, Path}
import org.apache.hadoop.tools.{DistCp, DistCpOptions}
import org.apache.hadoop.util.ToolRunner

import scala.jdk.CollectionConverters._

final class HdfsToS3(hdfsConfig: HdfsConfig, awsConfig: AwsConfig) {
  def copyFile(source: HdfsPath, destination: S3Path): Unit =
    copy(source, destination, syncFolder = false)

  def syncFolder(source: HdfsPath, destination: S3Path): Unit =
    copy(source, destination, syncFolder = true)

  private[this] def copy(source: HdfsPath, destination: S3Path, syncFolder: Boolean): Unit = {
    val configuration = new Configuration
    /* See more details about Hadoop configuration in:
     * https://hadoop.apache.org/docs/r2.6.5/hadoop-project-dist/hadoop-common/core-default.xml */
    configuration.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, hdfsConfig.baseUri)
    configuration.set("hadoop.job.ugi", s"${hdfsConfig.superUser},${hdfsConfig.superGroup}")
    /* See more details about S3 authentication properties in:
     * https://hadoop.apache.org/docs/current/hadoop-aws/tools/hadoop-aws/index.html */
    configuration.set(Constants.ACCESS_KEY, awsConfig.accessKeyId)
    configuration.set(Constants.SECRET_KEY, awsConfig.secretAccessKey)
    configuration.set(Constants.ENDPOINT, s"s3-${awsConfig.region}.amazonaws.com")
    configuration.set(Constants.PATH_STYLE_ACCESS, "true")

    val distCpOptions =
      new DistCpOptions.Builder(Seq(new Path(source.value)).asJava, new Path(destination.value))
        .withDeleteMissing(syncFolder)
        .withDirectWrite(true)
        .withOverwrite(true)
        .withSyncFolder(syncFolder)
        .build()

    val distCp = new DistCp(configuration, distCpOptions)

    val exitCode = ToolRunner.run(distCp, Array(source.value, destination.value))
    require(exitCode == 0, s"Operation failed with exit code ${exitCode.toString}")
  }
}
