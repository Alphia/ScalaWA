package loader


import router.Routes
import play.api.{BuiltInComponentsFromContext, Application, ApplicationLoader}
import play.api.ApplicationLoader.Context
import services.{LinkService, LogService}

class CustomApplicationLoader  extends ApplicationLoader {
  override def load(context: Context): Application = {
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context) {
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
  lazy val logService = new LogService
  lazy val linkService = new LinkService(logService)

  lazy val applicationController = new controllers.Application(linkService)(actorSystem.dispatcher)
  lazy val assets = new controllers.Assets(httpErrorHandler)
  override lazy val router = new Routes(httpErrorHandler, applicationController, assets)
}
