package es.eriktorr.hdfs.tools

import cats.effect.IO
import io.chrisdavenport.log4cats.SelfAwareStructuredLogger

import scala.annotation.tailrec

trait OptionParser {
  def optionsFrom(
    args: List[String]
  )(implicit logger: SelfAwareStructuredLogger[IO]): IO[OptionMap] = IO {
    @tailrec
    def optionMap(parsedOptions: OptionMap, argsList: List[String]): OptionMap =
      argsList match {
        case "-conf" :: value :: tail =>
          optionMap(parsedOptions ++ Map(Symbol("config") -> value), tail)
        case "-src" :: value :: tail =>
          optionMap(parsedOptions ++ Map(Symbol("source") -> value), tail)
        case "-dest" :: value :: tail =>
          optionMap(parsedOptions ++ Map(Symbol("destination") -> value), tail)
        case ::(value, next) =>
          logger.warn(s"Unknown option ignored: $value")
          optionMap(parsedOptions, next)
        case Nil => parsedOptions
      }
    optionMap(Map(), args)
  }
}
