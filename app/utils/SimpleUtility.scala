package utils

import org.apache.spark.SparkContext

object SimpleUtility {
  
  def simpleApp {
    val logFile = "public/README.md" // Should be some file on your system
    val sc = new SparkContext("local[4]", "Application", "/home/himanshu/spark-0.9.0-incubating", List("target/scala-2.10/firstsparkapp_2.10-1.0-SNAPSHOT.jar"))
    val logData = sc.textFile(logFile, 4).cache()
    val numSparks = logData.filter(line => line.contains("Spark")).count()
  }

}