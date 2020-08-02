package es.eriktorr.hdfs.tools

import es.eriktorr.hdfs.tools.model.{HdfsPath, S3Path}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.freespec.AsyncFreeSpec

class HdfsToS3Test extends AsyncFreeSpec with TypeCheckedTripleEquals {
  "HDFS to S3 should" - {
    "copy a single file" in {
      val context = ApplicationContextLoader.defaultApplicationContext
      val hdfsToS3 = new HdfsToS3(context.hdfsConfiguration, context.awsConfiguration)
      val result: Unit = hdfsToS3.copyFile(
        HdfsPath("/dir/file.txt"),
        /* Path: s3://eriktorr.es/testing-hdfs-to-s3/file.txt
         * Object URL: https://s3-eu-west-1.amazonaws.com/eriktorr.es/testing-hdfs-to-s3/file.txt */
        S3Path("s3a://eriktorr.es/testing-hdfs-to-s3/file.txt")
      )
      assert(result === {})
    }

    "sync a directory" in {
      val context = ApplicationContextLoader.defaultApplicationContext
      val hdfsToS3 = new HdfsToS3(context.hdfsConfiguration, context.awsConfiguration)
      val result: Unit = hdfsToS3.copyFile(
        HdfsPath("/dir/"),
        S3Path("s3a://eriktorr.es/testing-hdfs-to-s3/")
      )
      assert(result === {})
    }
  }
}
