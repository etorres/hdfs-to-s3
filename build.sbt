import wartremover.{Wart, Warts}

maintainer := "etserrano@gmail.com"
organization := "es.eriktorr"
name := "hdfs-to-s3"
version := "1.0.0"

scalaVersion := "2.13.3"

val HadoopVersion = "3.3.0"

libraryDependencies ++= Seq(
  "com.iheart" %% "ficus" % "1.4.7",
  "org.apache.hadoop" % "hadoop-aws" % HadoopVersion,
  "org.apache.hadoop" % "hadoop-common" % HadoopVersion,
  "org.apache.hadoop" % "hadoop-distcp" % HadoopVersion,
  "org.apache.hadoop" % "hadoop-mapreduce-client-common" % HadoopVersion,
  "org.slf4j" % "slf4j-log4j12" % "1.7.30",
  "org.typelevel" %% "cats-effect" % "2.1.4",
  "io.chrisdavenport" %% "log4cats-slf4j" % "1.1.1",
  "org.scalatest" %% "scalatest" % "3.2.0" % Test,
  "org.scoverage" %% "scalac-scoverage-runtime" % "1.4.1" % Test
)

scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "-Xfatal-warnings",
  "-Xlint",
  "-deprecation",
  "-unchecked"
)

javacOptions ++= Seq(
  "-g:none",
  "-source",
  "11",
  "-target",
  "11",
  "-encoding",
  "UTF-8"
)

scalafmtOnCompile := true

val warts = Warts.allBut(
  Wart.Any,
  Wart.Nothing,
  Wart.Equals,
  Wart.DefaultArguments,
  Wart.Overloading,
  Wart.ToString,
  Wart.ImplicitParameter
)

wartremoverErrors ++= warts
wartremoverWarnings ++= warts

Test / envFileName := ".env_test"
envVars in Test := (envFromFile in Test).value

coverageMinimum := 80
coverageFailOnMinimum := true
coverageEnabled := true
coverageExcludedPackages := "es\\.eriktorr\\.ftp\\.BuildInfo"

enablePlugins(JavaServerAppPackaging)
mappings in Universal in packageBin += file("doc/README_DIST.TXT") -> "README.TXT"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "es.eriktorr.hdfs.tools",
    buildInfoOptions := Seq(BuildInfoOption.BuildTime)
  )
