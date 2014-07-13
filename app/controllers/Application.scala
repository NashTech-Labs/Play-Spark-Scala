package controllers

import play.api._
import play.api.mvc._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import utils.SparkSQL
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

object Application extends Controller {

  def index = Action {
    Future{SparkSQL.simpleSparkSQLApp}
    Ok(views.html.index(""))
  }

}
