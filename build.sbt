name := "scala-spice"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0-MF"
)

libraryDependencies += "com.lihaoyi" % "ammonite-sshd_2.12.3" % "1.0.2" // cross CrossVersion.full

libraryDependencies ++= Seq(
  //akka
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test,
  "com.typesafe.akka" %% "akka-typed" % "2.5.6",

  //monix - a reactive library
  "io.monix" %% "monix" % "2.3.0",

  //test
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"

)

cancelable in Global := true

mainClass in (Compile, run) := Some("tool.RemoteAmm")
