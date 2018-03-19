import Dependencies._

val scalaVersion_2_11 = "2.11.11"
val scalaVersion_2_12 = "2.12.4"

val slickUtilsVersion = "1.0-SMAPSHOT"

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
      "com.github.tminglei" %% "slick-pg_play-json" % "0.15.4",
    )
  )
