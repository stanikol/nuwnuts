package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import daos.models.ItemInfo
import org.webjars.play.WebJarsUtil
import play.api.cache.{ AsyncCacheApi, Cached }
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents }
import play.twirl.api.Html
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class ItemController @Inject() (
  components: ControllerComponents,
  cache: AsyncCacheApi,
  cached: Cached,
  silhouette: Silhouette[DefaultEnv]
)(implicit
  ec: ExecutionContext,
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder)
    extends AbstractController(components) with I18nSupport {

  def listAllItems = cached("item.all") {
    Action.async { implicit request =>
      daos.DAOItemInfo.listAllItemInfo.map { items: List[ItemInfo] =>
        Ok(views.html.listAllItems(items))
      }
    }
  }

  def showItem(itemId: Long) = cached(s"item.$itemId") {
    Action.async { implicit request =>
      daos.DAOItemInfo.getItemInfo(itemId).map {
        case Some(item) =>
          Ok(views.html.showItem(item))
        case None =>
          Ok(views.html.frontend(title = "Error")(Html(s"Can't find item #$itemId !")))
      }
    }
  }

}
