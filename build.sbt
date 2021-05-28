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

crossScalaVersions := List("2.11.12", "2.12.14", "2.13.6")
scalaVersion := crossScalaVersions.value.last

fork in Test := scalaVersion.value.startsWith("2.11.") // https://github.com/scala/scala-parser-combinators/issues/197

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.0.0",
  "org.scalacheck" %% "scalacheck" % "1.15.2" % Test
)
