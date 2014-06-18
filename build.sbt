name := "PlaySparkApp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.apache.spark"  %% "spark-core" % "0.9.1",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3", 
  "com.typesafe.akka" %% "akka-slf4j" % "2.2.3"
)     

play.Project.playScalaSettings
