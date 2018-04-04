package nl.gn0s1s.bump

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

object SemVerParser extends RegexParsers {
  private val number = """0|[1-9]\d*""".r ^^ { _.toInt }

  private val dot = "."

  private val versionPart: Parser[SemVer] = number ~ dot ~ number ~ dot ~ number ^^ {
    case major ~ _ ~ minor ~ _ ~ patch =>
      SemVer(major, minor, patch)
  }

  private val hyphen = "-"
  private val atLeastOneNonDigit: Regex = """(\d*[A-Za-z-][\dA-Za-z-]*)+""".r
  private val preReleaseIdentifier = (atLeastOneNonDigit | number) ^^ { _.toString }
  private val preReleaseParser = repsep(preReleaseIdentifier, dot) ^^ { _.mkString(dot) }
  private val preReleasePart = hyphen ~ preReleaseParser ^^ { case _ ~ preRelease => preRelease }

  private val plus = "+"
  private val buildMetadataIdentifier = """([\dA-Za-z-])+""".r ^^ { _.toString }
  private val buildMetadataParser = repsep(buildMetadataIdentifier, dot) ^^ { _.mkString(dot) }
  private val buildMetadataPart = plus ~ buildMetadataParser ^^ { case _ ~ buildMetadata => buildMetadata }

  def parse(input: String): ParseResult[SemVer] = {
    val parser: Parser[SemVer] =
      versionPart ~ preReleasePart.? ~ buildMetadataPart.? ^^ {
        case semVer ~ preReleaseOption ~ buildMetadataOption =>
          semVer.copy(preRelease = preReleaseOption, buildMetadata = buildMetadataOption)
      }

    parse[SemVer](phrase(parser), input)
  }

  def validPreRelease(input: String): Boolean =
    parse(phrase(preReleaseParser), input).successful

  def validBuildMetadata(input: String): Boolean =
    parse(phrase(buildMetadataParser), input).successful
}
