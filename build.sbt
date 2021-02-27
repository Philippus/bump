name := "bump"
organization := "nl.gn0s1s"
version := "0.1.3"
startYear := Some(2018)
homepage := Some(url("https://github.com/philippus/bump"))
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

crossScalaVersions := List("2.13.5", "2.12.12", "2.13.4")
scalaVersion := crossScalaVersions.value.last
bintrayOrganization := Some("gn0s1s")
bintrayRepository := "releases"

fork in Test := scalaVersion.value.startsWith("2.11.") // https://github.com/scala/scala-parser-combinators/issues/197

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "org.scalacheck" %% "scalacheck" % "1.15.2" % Test
)

pomExtra :=
  <scm>
    <url>git@github.com:Philippus/bump.git</url>
    <connection>scm:git@github.com:Philippus/bump.git</connection>
  </scm>
    <developers>
      <developer>
        <id>philippus</id>
        <name>Philippus Baalman</name>
        <url>https://github.com/philippus</url>
      </developer>
    </developers>
