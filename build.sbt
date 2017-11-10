name := "ScalaLibsPlayground"

version := "1.0"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0-RC1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.6",
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

