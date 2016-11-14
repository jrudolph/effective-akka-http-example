scalaVersion := "2.11.8"

ScalariformSupport.formatSettings

libraryDependencies ++= Seq(
  // base dependency needed for all the examples
  "com.typesafe.akka" %% "akka-http" % "10.0.0-RC2",

  // needed for showing JSON integration
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.0-RC2",
  "io.spray" %% "spray-json" % "1.3.2",

  // needed for the ShowTree macro
  "org.scala-lang" % "scala-reflect" % "2.11.8"
)