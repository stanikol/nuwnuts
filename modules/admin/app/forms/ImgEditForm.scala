package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, optional, text}


object ImgEditForm{
  
  val form = Form(
    mapping(
      "action" -> optional(text),
      "search" -> optional(text),
      "filename" -> optional(text),
      "selected-img" -> optional(text),
    )(FormData.apply)(FormData.unapply)
  )

  case class FormData(
      action: Option[String],
      search: Option[String],
      filename: Option[String],
      selected: Option[String]
  ) {
    override def toString: String = s"ImgEditForm.FormData(action=$action, search=$search, filename=$filename, selected=$selected)"
  }

  object FormData{
    def empty = FormData(None, None, None, None)
  }
}
