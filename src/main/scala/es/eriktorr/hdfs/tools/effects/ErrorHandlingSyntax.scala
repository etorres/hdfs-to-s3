package es.eriktorr.hdfs.tools.effects

import cats.effect.IO
import cats.implicits._
import io.chrisdavenport.log4cats.SelfAwareStructuredLogger

private object ErrorHandling {
  def logErrors[A](io: IO[A])(logger: SelfAwareStructuredLogger[IO]): IO[A] = io.onError {
    case unhandledError => logger.error(unhandledError)("Unhandled error found")
  }
}

trait ErrorHandlingSyntax {
  implicit class IoOps[A](io: IO[A]) {
    def logErrors(logger: SelfAwareStructuredLogger[IO]): IO[A] =
      ErrorHandling.logErrors(io)(logger)
  }
}

object ErrorHandlingSyntax extends ErrorHandlingSyntax
