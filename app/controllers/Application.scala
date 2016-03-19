package controllers

import play.api.mvc._
import services.WeatherService

import scala.concurrent.{ExecutionContext, Future}

class Application(weatherService: WeatherService)(implicit ec: ExecutionContext) extends Controller {
  def findLinks(query: String) = Action.async {
    Future(Ok(views.html.index("Your new application is ready.")))
  }
}