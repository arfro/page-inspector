package controllers

import akka.actor.Status.{Failure, Success}
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import javax.inject._
import models.jsoup.{Page, PageInspectionResult}
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc._
import services.HtmlInspectionService
import services.inspector.JSoupInspectorService
import play.api.libs.json._

@Singleton
class HtmlInspectionController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  case class HtmlInputForm(str: String)

  val ins = new HtmlInspectionService(new JSoupInspectorService)

  def inspect() = Action { implicit request => {
    println("inspecting...")
      val input = Form(
        mapping(
          "str" -> text
        )(HtmlInputForm.apply)(HtmlInputForm.unapply)
      )
        val maybeLink = input.bindFromRequest().data.get("pageLink")
    implicit val repositoryMetadataWrites = Json.writes[PageInspectionResult]
      maybeLink match {
        case Some(link) if link.trim.isEmpty => Ok("bad")
        case Some(link) => {
          handleInspection(link) match {
            case Right(doc) => {
              val getAll2 = getAll(doc)
              println(getAll2)
              Ok(Json.toJson(getAll2))
            }
            case Left(ex) => {
              println(ex)
                BadRequest(s"problem $ex")
            }
          }
        }
        case None => BadRequest
      }

    }
  }

  def getAll(page: Page): PageInspectionResult = {
    val htmlVersion = ins.getHtmlVersion(page)
    val title = ins.getPageTitle(page)
    val headings = ins.getAllHeadings(page)
    val links = ins.getAllLinksGroupedByDomain(page)
    val isLogin = ins.containsLoginForm(page)
    PageInspectionResult(htmlVersion = htmlVersion, title = title, headings = headings, links = links, isLogin = isLogin)
  }

  def handleInspection(link: String) = {

    ins.extractHtml(link) match {
      case Right(doc) => Right(doc)
      case Left(ex) => Left("error")
    }
  }



}
