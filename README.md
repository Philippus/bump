# Bump - Semantic Versioning

[![Build Status](https://travis-ci.org/Philippus/bump.svg?branch=master)](https://travis-ci.org/Philippus/bump)
[![codecov](https://codecov.io/gh/Philippus/bump/branch/master/graph/badge.svg)](https://codecov.io/gh/Philippus/bump)
![Current Version](https://img.shields.io/badge/version-0.1.0-brightgreen.svg?style=flat "0.1.0")
[![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat "MIT")](LICENSE.md)

Bump is a library for working with semantic versioning following the [Semantic Versioning 2.0.0](https://semver.org/)
specification. It supports validation, precedence comparison, and increasing version numbers.

A `SemVer` object representing the version can be created by supplying arguments for the `version`, `preRelease` and
`buildMetaData` parts to the constructor or by supplying a string which will be parsed by the `SemVerParser` using
[parser combinators](https://github.com/scala/scala-parser-combinators).

## Installation

Bump is published for Scala 2.11 and 2.12. To start using it add the following to your `build.sbt`:

```
resolvers += Resolver.bintrayRepo("gn0s1s", "releases")

libraryDependencies += "nl.gn0s1s" %% "bump" % "0.1.0"
```

## Example usage

```scala
import nl.gn0s1s.bump._

val version = SemVer(1, 0, 1, Some("alpha"), Some("20180329")) // version: nl.gn0s1s.bump.SemVer = 1.0.1-alpha+20180329

version.nextMinor.withoutPreRelease.withoutBuildMetadata // res0: nl.gn0s1s.bump.SemVer = 1.1.0

val version2 = SemVer("2.0.0").get // version2: nl.gn0s1s.bump.SemVer = 2.0.0

version < version2 // res1: Boolean = true

version2.nextPatch // res2: nl.gn0s1s.bump.SemVer = 2.0.1

val invalidVersion = SemVer("3.0") // invalidVersion: Option[nl.gn0s1s.bump.SemVer] = None
```

## Methods
The following methods are available on a `SemVer` object:

* `toString` - returns the semantic versioning 2.0.0 string
* `compare` - compares the precedence to the supplied SemVer
* `nextPatch` - returns a new SemVer with an incremented `patch` version number
* `nextMinor` - returns a new SemVer with an incremented `minor` and a reset (0) `patch` version number
* `nextMajor` - returns a new SemVer with an incremented `major` and reset (0) `minor` and `patch` version numbers
* `withMajor` - returns a new SemVer with the supplied `major` version number
* `withMinor` - returns a new SemVer with the supplied `minor` version number
* `withPatch` - returns a new SemVer with the supplied `patch` version number
* `withPreRelease` - returns a new SemVer with the supplied `preRelease` string
* `withoutPreRelease` - returns a new SemVer without pre-release information
* `withBuildMetadata` - returns a new SemVer with the supplied `buildMetadata` string
* `withoutBuildMetadata` - returns a new SemVer without build metadata

## Links
- [Semantic Versioning 2.0.0](https://semver.org/)

## License
The code is available under the [MIT license](LICENSE.md).
