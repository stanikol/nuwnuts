package controllers.img

import java.net.URLDecoder
import javax.inject.Inject

import controllers.AssetsFinder
import daos.{ DAOImg, Img }
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
    val cacheKey = s"img.$filename"
    cache.getOrElseUpdate[Array[Byte]](cacheKey) {
      logger.trace("Saving `{}` to cache ...", filename)
      DAOImg.get(filename).map(_.map(_.bytes).get)
    }.map { Ok(_).as(BINARY) }
  }
}
