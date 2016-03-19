package services

import java.util.concurrent.TimeoutException

import play.api.Logger
import play.api.libs.ws.{WSResponse, WSClient}

import scala.concurrent.{Future, ExecutionContext}


class WeatherService(ws: WSClient, baseUrl: String)(implicit ec: ExecutionContext) {

  def getCurrentWeather(name: String): Future[String] = {
    ws.url(baseUrl + "/data/2.5/weather")
      .withHeaders("Content-Type" -> "application/json; charset=utf-8")
      .withQueryString("q" -> name, "appid" -> "b1b15e88fa797225412429c1c50c122a")
      .withRequestTimeout(3000)
      .get
      .map(deserialize)
      .recover {
        case ex: TimeoutException =>
          Logger.error(s"Get Weather data of city $name timeout, $ex")
          ""
      }
  }

  private def deserialize(response: WSResponse) = response.body
}
