package loader


import play.api.libs.ws.ning.NingWSComponents
import router.Routes
import play.api.{BuiltInComponentsFromContext, Application, ApplicationLoader}
import play.api.ApplicationLoader.Context
import services.WeatherService

class CustomApplicationLoader  extends ApplicationLoader {
  override def load(context: Context): Application = {
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context) with NingWSComponents {
  private def getConfigurationValue(field: String): String = {
    context.initialConfiguration.getString(field) match {
      case None => {
        throw new IllegalStateException(s"$field is not found!")
      }
      case Some(value) => {
        value
      }
    }
  }
//  private val filter: CORSFilter = new CORSFilter(CORSConfig.fromConfiguration(configuration))
//override lazy val httpFilters = new CorsFilters(filter).filters

  lazy val weatherHost = getConfigurationValue("weather.host")
  lazy val weatherService = new WeatherService(wsClient, weatherHost)(actorSystem.dispatcher)
  lazy val applicationController = new controllers.WeatherController(weatherService)(actorSystem.dispatcher)
  lazy val assets = new controllers.Assets(httpErrorHandler)
  override lazy val router = new Routes(httpErrorHandler, applicationController, assets)
}
