package nl.gn0s1s.bump

import org.scalacheck._
import org.scalacheck.Prop._

object SemVerSpec extends Properties("SemVer") {
  val precedenceList = List(
    "1.0.0-alpha", "1.0.0-alpha.1", "1.0.0-alpha.2", "1.0.0-alpha.beta", "1.0.0-beta",
    "1.0.0-beta.2", "1.0.0-beta.11", "1.0.0-rc.1", "1.0.0-rc.2", "1.0.0",
    "2.0.0", "2.1.0", "2.1.1", "2.1.2-alpha1+342", "2.1.2-alpha2",
    "2.1.3-0.1", "2.1.3-0.2", "2.1.4+8383")

  property("calculates version precedence properly") = {
    val zipped = precedenceList.zip(precedenceList.tail)
    zipped.forall { case (l, r) => SemVer(l) < SemVer(r) && SemVer(r) > SemVer(l) }
  }

  property("a pre-release version has lower precedence than a normal version") = {
    precedenceList.map(SemVer(_)).forall { elem =>
      if (elem.exists(_.preRelease.nonEmpty)) {
        elem < elem.map(_.withoutPreRelease)
      } else {
        elem.map(_.withPreRelease("alpha")) < elem
      }
    }
  }

  property("ignores build meta data when determining version precedence") = {
    SemVer(1, 0, 0).withBuildMetadata("a").compareTo(SemVer(1, 0, 0).withBuildMetadata("b")) == 0
  }

  property("toString and the parsed string are equal") = {
    precedenceList.forall(elem => SemVer(elem).exists(_.toString == elem))
  }

  property("nextMajor/bumpMajor increases major and resets minor and patch") = {
    SemVer(1, 1, 1).nextMajor == SemVer(2, 0, 0)
  }

  property("nextMinor/bumpMinor increases major and resets patch") = {
    SemVer(1, 1, 1).nextMinor == SemVer(1, 2, 0)
  }

  property("nextPatch/bumpPatch increases patch") = {
    SemVer(1, 1, 1).nextPatch == SemVer(1, 1, 2)
  }

  property("nextMajor/bumpMajor has higher precedence than the original") = {
    precedenceList.map(SemVer(_)).forall { elem =>
      elem.exists(semVer => semVer.bumpMajor > semVer)
    }
  }

  property("nextMinor/bumpMinor has higher precedence than the original") = {
    precedenceList.map(SemVer(_)).forall { elem =>
      elem.exists(semVer => semVer.bumpMinor > semVer)
    }
  }

  property("nextPatch/bumpPatch has higher precedence than the original") = {
    precedenceList.map(SemVer(_)).forall { elem =>
      elem.exists(semVer => semVer.bumpPatch > semVer)
    }
  }

  property("checks for illegal characters in pre-release") = {
    throws(classOf[IllegalArgumentException])(SemVer(1, 0, 0).withPreRelease("+"))
  }

  property("checks for illegal characters in build metadata") = {
    throws(classOf[IllegalArgumentException])(SemVer(1, 0, 0).withBuildMetadata("+"))
  }
}
