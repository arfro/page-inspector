package controllers

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.mvc._
import services.HtmlInspectionService
import services.inspector.JSoupInspectorService

@Singleton
class HtmlInspectionController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  case class HtmlInputForm(str: String)

  def inspect() = Action { implicit request => {
      val input = Form(
        mapping(
          "str" -> text
        )(HtmlInputForm.apply)(HtmlInputForm.unapply)
      )
        val maybeLink = input.bindFromRequest().data.get("pageLink")

      maybeLink match {
        case Some(link) if link.trim.isEmpty => Ok("bad")
        case Some(link) => Ok(handleInspection(link).map(ada => s"$ada"))
        case None => BadRequest
      }

    }

  def handleInspection(link: String) = {
      val ins = new HtmlInspectionService(new JSoupInspectorService)
    for {
      html <- ins.extractHtml(link)
      title = ins.getPageTitle(html)
    } yield title
  }

  }

}
