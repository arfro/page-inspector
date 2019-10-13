package models.forms

import play.api.data.Forms.mapping
import play.api.data.Form
import play.api.data.Forms._

case class HtmlInputForm(str: String)

object PlayFormMappers {

  val input = Form(
    mapping(
      "textFromInputForm" -> text
    )(HtmlInputForm.apply)(HtmlInputForm.unapply)
  )

}
