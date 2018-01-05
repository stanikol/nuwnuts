package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import org.slf4j.LoggerFactory
import org.webjars.play.WebJarsUtil
import play.api.cache.{ AsyncCacheApi, Cached }
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents }
import play.twirl.api
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class ArticleController @Inject() (
  components: ControllerComponents,
  cache: AsyncCacheApi,
  cached: Cached,
  silhouette: Silhouette[DefaultEnv]
)(implicit
  ec: ExecutionContext,
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder)
    extends AbstractController(components) with I18nSupport {
  private val logger = LoggerFactory.getLogger("img")

  def listAllArticles = cached("article.all") {
    Action.async { implicit request =>
      for {
        articles <- daos.DAOBlog.listAllArticles()
      } yield {
        Ok(views.html.listAllArticles(articles))
      }
    }
  }

  def showArticle(blogId: Long) = cached(s"article.$blogId") {
    Action.async { implicit request =>
      daos.DAOBlog.getBlogPost(blogId).map { blogPostOpt =>
        blogPostOpt.map { blog =>
          Ok(views.html.showArticle(blog))
        }.getOrElse(Ok(views.html.frontend("Ошибка !")(api.Html(s"Статья ${blogId} не найдена!"))))
      }
    }
  }
}
