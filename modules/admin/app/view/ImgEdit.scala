//package view
//
//import play.api.mvc.{ AnyContent, Request }
//
//import scalatags.Text.all._
//import play.filters.csrf.CSRF
//import play.filters.csrf.CSRF.Token
//
//object ImgEdit {
//
//  def apply(data: forms.ImgEditForm.FormData, foundImages: List[String] = List.empty[String])(
//    implicit
//    request: Request[_]
//  ) = {
//    val tokenCSRF = CSRF.getToken.get
//    AdminMain(div(
//      id := "edit-img",
//      `class` := "container-fluid",
//      form(
//        action := controllers.admin.routes.ImgAdmin.editImgPost().url,
//        method := "POST",
//        enctype := "multipart/form-data",
//        input(`type` := "hidden", name := tokenCSRF.name, value := tokenCSRF.value),
//        div(
//          `class` := "row",
//          div(
//            `class` := "col-sm-4",
//            div(
//              `class` := "form-group",
//              label(`for` := "filename", "Filename"),
//              input(id := "filename", value := data.filename.getOrElse(""), name := "filename")
//            ),
//            div(
//              `class` := "form-group",
//              //              label(`for` := "file", "Image"),
//              input(id := "file", `type` := "file", name := "file"),
//              button(`type` := "submit", name := "action", value := "save-image", "SAVE IMAGE")
//            )
//          ),
//          div(
//            `class` := "col-sm-4",
//            data.filename.map(f => img(src := controllers.img.routes.ImgController.getImg(f).url)).getOrElse(div())
//          )
//        ),
//        div(
//          `class` := "form-group",
//          label(`for` := "search", "Search image"),
//          input(id := "search", value := data.search.getOrElse(""), name := "search"),
//          button(`type` := "submit", name := "action", value := "search", "SEARCH")
//        ),
//        button(`type` := "submit", name := "action", value := "select", "SELECT"),
//        div(
//          `class` := "row form-group",
//          foundImages.map { imgFilename =>
//            div(
//              `class` := "col-sm-4",
//              input(`type` := "radio", name := "selected-img", value := imgFilename,
//                imgFilename, br(), img(src := controllers.img.routes.ImgController.getImg(imgFilename).url, `class` := "img-fluid"))
//            )
//          }
//        )
//      )
//    ))
//  }
//
//}
