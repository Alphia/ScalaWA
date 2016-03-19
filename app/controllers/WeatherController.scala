package controllers

import play.api.Logger
import play.api.mvc._
import services.WeatherService

import scala.concurrent.ExecutionContext

class WeatherController(weatherService: WeatherService)(implicit ec: ExecutionContext) extends Controller {
  def getCurrentWeatherByCityName(cityName: String) = Action.async {
    val futureWeather = weatherService.getCurrentWeather(cityName)
    futureWeather
      .map(weather => Ok(weather))
      .recover{ case anyException =>
        val errorMsg: String = "Failed to get weather from Service"
        Logger.error(errorMsg, anyException)
        InternalServerError(errorMsg)
      }
  }
}