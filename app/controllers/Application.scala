package controllers

import play.api._
import play.api.mvc._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object Application extends Controller {

  def index = Action {
    val logFile = "public/data/README.md" // Should be some file on your system
    val sc = new SparkContext("local", "Application", "/path/to/spark-0.9.0-incubating", List("target/scala-2.10/playsparkapp_2.10-1.0-SNAPSHOT.jar"))
    val logData = sc.textFile(logFile, 2).cache()
    val numThes = logData.filter(line => line.contains("Spark")).count()
    Ok(views.html.index("Lines with Spark: " + numThes))
  }

}
