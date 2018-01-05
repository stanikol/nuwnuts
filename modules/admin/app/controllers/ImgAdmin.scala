package controllers.admin

import java.io.File
import java.net.URLDecoder
import java.nio.file.{ Files, Paths }

import play.api.data._
import play.api.data.Forms._
import javax.inject.Inject

import admin.AdminOnly
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import com.mohiva.play.silhouette
import controllers.AssetsFinder
import daos.{ DAOImg, Img }
import play.api.cache.AsyncCacheApi
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, Action, ControllerComponents }
import com.mohiva.play.silhouette.api.Silhouette
import org.slf4j.LoggerFactory
import org.webjars.play.WebJarsUtil
import utils.auth.DefaultEnv
import forms.ImgEditForm._
import views.html.imgEdit

import scala.concurrent.{ ExecutionContext, Future }

class ImgAdmin @Inject() (
    components: ControllerComponents,
    cache: AsyncCacheApi,
    silhouette: Silhouette[DefaultEnv]
)(implicit
  ec: ExecutionContext,
    webJarsUtil: WebJarsUtil,
    assets: AssetsFinder) extends AbstractController(components) with I18nSupport {
  private val logger = LoggerFactory.getLogger("img")

  def editImgGet(filename: Option[String] = None) = silhouette.SecuredAction(AdminOnly()).async { implicit request =>
    //    DAOImg.search(filename).map { foundImages: List[String] =>
    //      foundImages.headOption.map { imgFilename =>
    //        val formData = FormData.empty.copy(filename = Some(imgFilename))
    //        Ok(imgEdit(form.fill(formData), foundImages))
    //      }.getOrElse {
    //        val formData = FormData.empty.copy(filename = filename)
    //        Ok(imgEdit(form.fill(formData), List.empty))
    //      }
    //    }
    val formData = FormData.empty.copy(filename = filename)
    Future.successful(Ok(imgEdit(form.fill(formData), List.empty)))
  }

  def editImgPost() = silhouette.SecuredAction(AdminOnly()).async { implicit request =>
    forms.ImgEditForm.form.bindFromRequest().fold(
      { hasErrors => Future.successful(Ok("Error")) },
      { editImgData =>
        println(editImgData)
        editImgData.action match {
          case Some("select") =>
            val formData = form.fill(editImgData.copy(filename = editImgData.selected))
            Future.successful(Ok(imgEdit(formData, List.empty)))
          case Some("search") =>
            DAOImg.search(editImgData.search).map { foundImages: List[String] =>
              val formData = forms.ImgEditForm.form.fill(editImgData)
              Ok(imgEdit(formData, foundImages))
            }
          case Some("delete-image") =>
            val filename = editImgData.filename.map { filename =>
              DAOImg.remove(filename).map(_ => cache.removeAll())
              filename
            }
            Future.successful(Redirect(controllers.admin.routes.ImgAdmin.editImgGet(filename)))
          case Some("save-image") =>
            request.body.asMultipartFormData.get.file("file").map { newImg =>
              val tmpFile = File.createTempFile("image-upload", ".tmp")
              val bytes = Files.readAllBytes(newImg.ref.path)
              println(s"Read ${bytes.length} bytes. ${newImg.filename}")
              cache.removeAll()
              DAOImg.upsert(Img(editImgData.filename.get, bytes)).map { _ =>
                //                Ok(ImgEdit(editImgData.copy(action = None)).render).as("text/html")
                //                val formData = forms.ImgEditForm.form.fill(editImgData)
                //                Ok(views.html.imgEdit(formData, List.empty))
                Redirect(controllers.admin.routes.ImgAdmin.editImgGet(editImgData.filename))
              }
            }.getOrElse(Future.successful(Ok("Error: No file is selected")))
          case _ =>
            //            Future.successful(Ok(ImgEdit(editImgData).render).as("text/html"))
            Future.successful(Ok(imgEdit(form.fill(editImgData), List.empty)))
        }
      }
    )
  }

  def test = Action.async { implicit req =>
    val d = forms.ImgEditForm.FormData(Some("1"), Some("1"), Some("1"), Some("1"))
    DAOImg.search(None).map { foundImages: List[String] =>
      Ok(views.html.imgEdit(forms.ImgEditForm.form.fill(d), foundImages))
    }
  }

}
