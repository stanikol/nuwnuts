package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import controllers.AssetsFinder
import daos.{ DAOBlog, DAOItemInfo }
import org.slf4j.LoggerFactory
import org.webjars.play.WebJarsUtil
import play.api.cache.AsyncCacheApi
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents }
import play.twirl.api.Html
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class HomeController @Inject() (
    components: ControllerComponents,
    cache: AsyncCacheApi,
    silhouette: Silhouette[DefaultEnv]
)(implicit
  ec: ExecutionContext,
    webJarsUtil: WebJarsUtil,
    assets: AssetsFinder) extends AbstractController(components) with I18nSupport {
  private val logger = LoggerFactory.getLogger("img")

  def showHomePage = Action.async { implicit request =>
    for {
      items <- DAOItemInfo.listAllItemInfo()
      articles <- DAOBlog.listAllArticles()
    } yield Ok(views.html.showHome(items, articles))
  }

}
