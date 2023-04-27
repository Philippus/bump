name := "bump"
organization := "nl.gn0s1s"
startYear := Some(2018)
homepage := Some(url("https://github.com/philippus/bump"))
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

developers := List(
  Developer(
    id = "philippus",
    name = "Philippus Baalman",
    email = "",
    url = url("https://github.com/philippus")
  )
)

ThisBuild / versionScheme := Some("semver-spec")
ThisBuild / versionPolicyIntention := Compatibility.BinaryCompatible

Compile / packageBin / packageOptions += Package.ManifestAttributes("Automatic-Module-Name" -> "nl.gn0s1s.bump")

crossScalaVersions := List("2.13.10")
scalaVersion := crossScalaVersions.value.last

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.3.0",
  "org.scalacheck" %% "scalacheck" % "1.17.0" % Test
)
