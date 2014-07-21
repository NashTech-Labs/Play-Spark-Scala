package controllers

import play.api._
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

import utils.SparkMLLibUtility

object Application extends Controller {

  def index = Action {
    Future{SparkMLLibUtility.SparkMLLibExample}
    Ok(views.html.index(""))
  }

}
