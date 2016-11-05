name := "jwtyped"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val specs2V = "3.8.6"
  val circeV = "0.5.4"
  val bouncyCastleV = "1.55"

  List(
    "io.circe" %% "circe-core" % circeV % "test",
    "io.circe" %% "circe-generic" % circeV % "test",
    "io.circe" %% "circe-parser" % circeV % "test",
    "org.specs2" %% "specs2-core" % specs2V % "test",
    "org.specs2" %% "specs2-scalacheck" % specs2V % "test",
    "org.bouncycastle" % "bcpkix-jdk15on" % bouncyCastleV % "test"
  )
}
