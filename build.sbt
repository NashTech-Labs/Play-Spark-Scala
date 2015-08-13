name := "firstSparkApp"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

val sparkVersion = "1.4.0"
val playVersion = "2.3.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.apache.spark"  %% "spark-core"              % sparkVersion,
  "com.typesafe.akka" %% "akka-actor"              % playVersion,
  "com.typesafe.akka" %% "akka-slf4j"              % playVersion,
  "org.apache.spark"  %% "spark-streaming-twitter" % sparkVersion,
  "org.apache.spark"  %% "spark-sql"               % sparkVersion,
  "org.apache.spark"  %% "spark-mllib"             % sparkVersion
  )     


