package models.forms

import play.api.data.Forms.mapping
import play.api.data.Form
import play.api.data.Forms._

object PlayFormMappers {

  case class HtmlInputForm(str: String)

  val input = Form(
    mapping(
      "str" -> text
    )(HtmlInputForm.apply)(HtmlInputForm.unapply)
  )

}
