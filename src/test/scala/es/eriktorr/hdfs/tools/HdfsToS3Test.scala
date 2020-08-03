package es.eriktorr.hdfs.tools

import cats.effect.{Blocker, ContextShift, IO, Resource}
import es.eriktorr.hdfs.tools.model.{HdfsPath, S3Path}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.freespec.AsyncFreeSpec

import scala.concurrent.ExecutionContext

class HdfsToS3Test extends AsyncFreeSpec with TypeCheckedTripleEquals {
  implicit private[this] val contextShift: ContextShift[IO] =
    IO.contextShift(ExecutionContext.global)

  "HDFS to S3 should" - {
    "copy a single file" in {
      assert(
        withDefaultContext(
          _.hdfsToS3.copyFile(
            HdfsPath("/dir/file.txt"),
            /* Path: s3://eriktorr.es/testing-hdfs-to-s3/file.txt
             * Object URL: https://s3-eu-west-1.amazonaws.com/eriktorr.es/testing-hdfs-to-s3/file.txt */
            S3Path("s3a://eriktorr.es/testing-hdfs-to-s3/file.txt")
          )
        ).unsafeRunSync() === {}
      )
    }

    "sync a directory" in {
      assert(
        withDefaultContext(
          _.hdfsToS3.syncFolder(
            HdfsPath("/dir/"),
            S3Path("s3a://eriktorr.es/testing-hdfs-to-s3/")
          )
        ).unsafeRunSync() === {}
      )
    }

    def withDefaultContext(f: HdfsToS3Context => IO[Unit]): IO[Unit] = {
      val programResource = for {
        context <- HdfsToS3Context()
      } yield context
      runTest(programResource)(f)
    }

    def runTest(context: Resource[IO, HdfsToS3Context])(f: HdfsToS3Context => IO[Unit]): IO[Unit] =
      Blocker[IO].use { blocker =>
        for {
          result <- blocker.blockOn {
            context.use(f)
          }
        } yield result
      }
  }
}
