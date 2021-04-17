// See README.md for license details.

ThisBuild / scalaVersion     := "2.12.13"
ThisBuild / version          := "1.0.0"
ThisBuild / transitiveClassifiers := Seq(Artifact.SourceClassifier)

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

lazy val root = (project in file("."))
  .settings(
    name := "chisel-learn",
    libraryDependencies ++= Seq(
      "org.easysoc" %% "layered-firrtl" % "1.1.+",
      "edu.berkeley.cs" %% "chisel-iotesters" % "1.5.+",
      "edu.berkeley.cs" %% "chiseltest" % "0.3.+"
    ),
    scalacOptions ++= Seq(
      "-Xsource:2.11",
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit"
    ),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.4.+" cross CrossVersion.full),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
  )

