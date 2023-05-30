name := "titlebot-server"
organization := "com.jikim"
version := "1.0.0"

scalaVersion := "2.12.6"

val akkaVersion = "2.8.0"
val akkaHttpVersion = "10.5.0"
val circeVersion = "0.14.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,

  "org.jsoup" % "jsoup" % "1.15.4",
  "ch.megard" %% "akka-http-cors" % "1.1.1",

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",

  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" %% "mockito-scala-scalatest" % "1.16.32" % Test
)