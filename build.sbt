name := "scala-spice"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0-MF"
)

libraryDependencies += "com.lihaoyi" % "ammonite-sshd_2.12.3" % "1.0.2" // cross CrossVersion.full

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test
)

//unmanagedBase := baseDirectory.value / "lib"

cancelable in Global := true
fork in run := true

mainClass in (Compile, run) := Some("tool.RemoteAmm")

