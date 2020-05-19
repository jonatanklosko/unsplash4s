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
  "com.softwaremill.sttp.client" %% "async-http-client-backend-future" % "2.1.2"
)
