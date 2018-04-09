name := "scala-spice"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0-MF"
)

libraryDependencies += "com.lihaoyi" % "ammonite-sshd_2.12.4" % "1.0.3" // cross CrossVersion.full

libraryDependencies ++= Seq(
  //akka
//  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
//  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test,
  "com.typesafe.akka" %% "akka-typed" % "2.5.6",

  //monix - a reactive library
  "io.monix" %% "monix" % "3.0.0-M3",

  //test
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",

  //lift json
"net.liftweb" % "lift-json_2.12" % "3.2.0-M3"
)

cancelable in Global := true

mainClass in (Compile, run) := Some("tool.RemoteAmm")
