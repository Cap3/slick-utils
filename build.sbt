import Dependencies._

val scalaVersion_2_11 = "2.11.11"
val scalaVersion_2_12 = "2.12.4"

val slickUtilsVersion = "0.1-SNAPSHOT"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "de.cap3",
      scalaVersion := scalaVersion_2_12,
      crossScalaVersions := Seq(scalaVersion_2_11, scalaVersion_2_12),
      version := slickUtilsVersion
    )),
    name := "slick-utils",
    scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature"),
    libraryDependencies ++= Seq(
      scalaTest % Test,
      guice,
      "org.postgresql" % "postgresql" % "42.1.4",
      "com.typesafe.slick" %% "slick" % "3.2.1",
      "com.typesafe.slick" %% "slick-codegen" % "3.2.1",
      "com.typesafe.play" %% "play-slick" % "3.0.1",
      "org.flywaydb" %% "flyway-play" % "4.0.0",
      "com.github.tminglei" %% "slick-pg" % "0.15.4",
      "com.github.tminglei" %% "slick-pg_play-json" % "0.15.4"
    ))
  .settings(publishingSettings: _*)

val publishingSettings = Seq(
  pomIncludeRepository := { _ => false },
  publishMavenStyle := true,
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/Cap3/slick-utils")),
  publishArtifact in Test := false,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/Cap3/slick-utils"),
      "scm:git@github.com/Cap3/slick-utils.git"
    )
  ),
  developers := List(
    Developer(
      id    = "cap3",
      name  = "Cap3 GmbH",
      email = "info@cap3.de",
      url   = url("http://www.cap3.de/")
    )
  ),
  credentials += Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    sys.env.getOrElse("SONATYPE_USER", ""),
    sys.env.getOrElse("SONATYPE_PASS", "")
  ),
  useGpg := false,
  usePgpKeyHex("F3DFAA7AC6C7EF24"),
  pgpPublicRing := file("project/.gnupg/pubring.gpg"),
  pgpSecretRing := file("project/.gnupg/secring.gpg"),
  pgpPassphrase := sys.env.get("PGP_PASS").map(_.toArray)
)