package nl.gn0s1s.bump

import scala.annotation.tailrec

case class SemVer(major: Int, minor: Int, patch: Int, preRelease: Option[String] = None, buildMetadata: Option[String] = None)
  extends Ordered[SemVer] {
  require(major >= 0)
  require(minor >= 0)
  require(patch >= 0)

  // https://semver.org/#spec-item-9
  require(preRelease.isEmpty ||
    preRelease.exists(_ match {
      case SemVerParser.preReleaseRegex(_*) => true
      case _ => false
    }))

  // https://semver.org/#spec-item-10
  require(buildMetadata.isEmpty ||
    buildMetadata.exists(_ match {
      case SemVerParser.buildMetadataRegex(_*) => true
      case _ => false
    }))

  override def toString: String =
    s"${major}.${minor}.${patch}" + preRelease.map("-" + _).getOrElse("") + buildMetadata.map("+" + _).getOrElse("")

  // https://semver.org/#spec-item-11
  def compare(that: SemVer): Int = SemVer.compare(this, that)

  // https://semver.org/#spec-item-6
  def nextPatch: SemVer = this.withPatch(patch + 1)
  def bumpPatch: SemVer = nextPatch

  // https://semver.org/#spec-item-7
  def nextMinor: SemVer = this.withMinor(minor + 1).withPatch(0)
  def bumpMinor: SemVer = nextMinor

  // https://semver.org/#spec-item-8
  def nextMajor: SemVer = this.withMajor(major + 1).withMinor(0).withPatch(0)
  def bumpMajor: SemVer = nextMajor

  def withMajor(major: Int): SemVer = this.copy(major = major)

  def withMinor(minor: Int): SemVer = this.copy(minor = minor)

  def withPatch(patch: Int): SemVer = this.copy(patch = patch)

  def withPreRelease(preRelease: String): SemVer = this.copy(preRelease = Some(preRelease))

  def withoutPreRelease: SemVer = this.copy(preRelease = None)

  def withBuildMetadata(buildMetadata: String): SemVer = this.copy(buildMetadata = Some(buildMetadata))

  def withoutBuildMetadata: SemVer = this.copy(buildMetadata = None)
}

object SemVer {
  def apply(s: String): Option[SemVer] = SemVerParser.parse(s).map(Some(_)).getOrElse(None)

  def compare(l: SemVer, r: SemVer): Int = {
    if (l.withoutBuildMetadata == r.withoutBuildMetadata) 0
    else if (l.major < r.major) -1
    else if (l.major > r.major) 1
    else if (l.minor < r.minor) -1
    else if (l.minor > r.minor) 1
    else if (l.patch < r.patch) -1
    else if (l.patch > r.patch) 1
    else SemVer.comparePreReleases(l, r)
  }

  private def comparePreReleases(l: SemVer, r: SemVer): Int = {
    @tailrec def compareBytes(l: Array[Byte], r: Array[Byte]): Int = {
      (l, r) match {
        case (l, r) if l.isEmpty & r.isEmpty => 0
        case (l, r) if l.isEmpty => -1
        case (l, r) if r.isEmpty => 1
        case (l, r) if l.head < r.head => -1
        case (l, r) if l.head > r.head => 1
        case (l, r) => compareBytes(l.tail, r.tail)
      }
    }

    @tailrec def compareValues(l: List[String], r: List[String]): Int = {
      (l, r) match {
        case (l, r) if l == r => 0
        case (l, r) if l.isEmpty => -1
        case (l, r) if r.isEmpty => 1
        case (l, r) if l.head == r.head => compareValues(l.tail, r.tail)
        case (l, r) if l.head.forall(_.isDigit) && r.head.forall(_.isDigit) =>
          if (l.head.toInt < r.head.toInt) -1 else 1
        case (l, r) if l.head.forall(_.isDigit) => -1
        case (l, r) if r.head.forall(_.isDigit) => 1
        case (l, r) =>
          compareBytes(l.head.getBytes, r.head.getBytes)
      }
    }

    (l.preRelease, r.preRelease) match {
      case (l, r) if l == r => 0
      case (Some(l), Some(r)) => compareValues(l.split('.').toList, r.split('.').toList)
      case (Some(_), None) => -1
      case (None, Some(_)) => 1
    }
  }
}
