package nl.gn0s1s.bump

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

sealed trait Separator
case object DOT extends Separator
case object HYPHEN extends Separator
case object PLUS extends Separator

object SemVerParser extends RegexParsers {
  val numberRegex: Regex = """(0|[1-9]\d*)""".r

  val preReleaseRegex: Regex = """((((\d*[A-Za-z-][\dA-Za-z-]*)+)|0|[1-9]\d*)\.)*(((\d*[A-Za-z-][\dA-Za-z-]*)+)|0|[1-9]\d*)""".r

  val buildMetadataRegex: Regex = """(([\dA-Za-z-])+\.)*([\dA-Za-z-])+""".r

  private def dot = "." ^^ (_ => DOT)
  private def hyphen = "-" ^^ (_ => HYPHEN)
  private def plus = "+" ^^ (_ => PLUS)
  private def number = numberRegex ^^ { _.toInt }

  private def versionPart: Parser[SemVer] = number ~ dot ~ number ~ dot ~ number ^^ {
    case major ~ _ ~ minor ~ _ ~ patch =>
      SemVer(major, minor, patch)
  }
  private def preReleasePart: Parser[String] = hyphen ~ preReleaseRegex ^^ { case _ ~ preRelease => preRelease.toString }
  private def buildMetadataPart: Parser[String] = plus ~ buildMetadataRegex ^^ { case _ ~ buildMetadata => buildMetadata.toString }

  def parse(input: String): ParseResult[SemVer] = {
    val parser: Parser[SemVer] =
      versionPart ~ preReleasePart.? ~ buildMetadataPart.? ^^ {
        case semVer ~ preReleaseOption ~ buildMetadataOption =>
          semVer.copy(preRelease = preReleaseOption, buildMetadata = buildMetadataOption)
      }

    parse[SemVer](phrase(parser), input)
  }
}
