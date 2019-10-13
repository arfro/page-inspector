package controllers

import javax.inject._
import models.forms.PlayFormMappers
import models.jsoup.{Page, PageInspectionResult}
import play.api.libs.json.Json
import play.api.mvc._
import services.HtmlInspectionService
import services.inspector.JSoupInspectorService

@Singleton
class HtmlInspectionController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val inspectionService = new HtmlInspectionService(new JSoupInspectorService)

  def inspect(): Action[AnyContent] = Action { implicit request => {
        val maybeLink = PlayFormMappers.input.bindFromRequest().data.get("pageLink")

      maybeLink match {
        case Some(link) if link.trim.isEmpty => Ok("bad")
        case Some(link) => {
          handleInspection(link) match {
            case Right(doc) => {
              val getAll2 = getAll(doc)
              Ok(views.html.result
                (getAll2.htmlVersion)
                (getAll2.title)
                (getAll2.headings)
                (getAll2.links)
                (getAll2.isLogin)
              )
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
    val htmlVersion = inspectionService.getHtmlVersion(page)
    val title = inspectionService.getPageTitle(page)
    val headings = inspectionService.getAllHeadings(page)
    val links = inspectionService.getAllLinksGroupedByDomain(page)
    val isLogin = inspectionService.containsLoginForm(page)
    PageInspectionResult(htmlVersion = htmlVersion, title = title, headings = headings, links = links, isLogin = isLogin)
  }

  def handleInspection(link: String) = {

    inspectionService.extractHtml(link) match {
      case Right(doc) => Right(doc)
      case Left(ex) => Left("error")
    }
  }



}
