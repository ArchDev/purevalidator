name := "purevalidator"

version := "0.0.1"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.12.4", "2.11.11", "2.13.0-M2")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)