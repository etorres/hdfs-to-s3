package es.eriktorr.hdfs

import cats.effect.IO
import cats.implicits._
import io.chrisdavenport.log4cats.SelfAwareStructuredLogger

package object tools {
  type OptionMap = Map[Symbol, String]

  implicit class IoOps[A](io: IO[A]) {
    @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
    def ignoreImpureResult(): Unit = ()

    def logErrors(logger: SelfAwareStructuredLogger[IO]): IO[A] = io.onError {
      case unhandledError => logger.error(unhandledError)("Unhandled error found")
    }
  }
}
