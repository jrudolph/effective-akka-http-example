scalaVersion := "2.11.8"

ScalariformSupport.formatSettings

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.0-RC2",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.0-RC2",
  "io.spray" %% "spray-json" % "1.3.2"
)