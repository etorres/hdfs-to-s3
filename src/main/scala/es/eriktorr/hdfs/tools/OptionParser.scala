package es.eriktorr.hdfs.tools

import scala.annotation.tailrec

trait OptionParser {
  def optionsFrom(args: List[String]): OptionMap = {
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
          optionMap(
            parsedOptions + (OptionParser.unknown -> (parsedOptions
              .get(OptionParser.unknown) match {
              case Some(old) => s"$old, $value"
              case None => value
            })),
            next
          )
        case Nil => parsedOptions
      }
    optionMap(Map(), args)
  }
}

object OptionParser {
  val unknown: Symbol = Symbol("unknown")
}
