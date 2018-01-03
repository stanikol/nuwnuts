package controllers.img

import java.net.URLDecoder
import javax.inject.Inject

import controllers.AssetsFinder
import daos.DAOImg
import daos.models.Img
import org.slf4j.LoggerFactory
import play.api.cache.{ AsyncCacheApi, Cached }
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, Action, ControllerComponents }

import scala.concurrent.ExecutionContext

class ImgController @Inject() (
    components: ControllerComponents,
    cache: AsyncCacheApi
)(
    implicit
    ec: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  private val logger = LoggerFactory.getLogger("img")

  def getImg(f: String) = Action.async { implicit request =>
    val filename = URLDecoder.decode(f, "utf-8")
    cache.getOrElseUpdate[Option[Img]](s"img.$filename") {
      logger.debug("Saving `{}` to cache ...", filename)
      DAOImg.get(filename)
    }.map { imgOpt: Option[Img] =>
      imgOpt.map(i => Ok(i.bytes).as(BINARY)).getOrElse {
        println(s"NO IMAGE FOUND! $filename")
        Ok(s"Not found `$filename` !")
      }
    }
  }
}
