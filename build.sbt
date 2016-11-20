name := "jwtyped"

version := "0.1.0"

organization := "com.github.etaty"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0")

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))
homepage := Some(url("https://github.com/etaty/jwtyped"))
scmInfo := Some(ScmInfo(url("https://github.com/etaty/jwtyped"), "scm:git:git@github.com:etaty/jwtyped.git"))
pomExtra := (
  <developers>
    <developer>
      <id>etaty</id>
      <name>Valerian Barbot</name>
      <url>https://github.com/etaty/</url>
    </developer>
  </developers>
  )
publishMavenStyle := true

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
