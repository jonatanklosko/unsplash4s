name := "unsplash4s"

version := "0.1"

scalaVersion := "2.13.2"

// Dependencies

val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client" %% "core" % "2.1.2",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-future" % "2.1.2",
  "org.slf4j" % "slf4j-nop" % "1.7.30",
  "org.scalactic" %% "scalactic" % "3.1.2"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.2" % "test",
  "org.scalamock" %% "scalamock" % "4.4.0" % "test"
)
