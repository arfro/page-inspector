package controllers

import javax.inject._
import models.{Page, PageInspectionResult}
import models.forms.PlayFormMappers
import play.api.mvc._
import services.HtmlInspectionService
import unit.util.Util

@Singleton
class HtmlInspectionController @Inject()(cc: ControllerComponents)
                                        (inspectionService: HtmlInspectionService) extends AbstractController(cc) {

  def inspect(): Action[AnyContent] = Action { implicit request => handleRequest }

  private def handleRequest(implicit request: Request[AnyContent]) = {
    val maybeLink = PlayFormMappers.input.bindFromRequest().data.get("pageLink")

    maybeLink match {
      case Some(link) if link.trim.isEmpty => BadRequest("Empty input")
      case Some(link) => (sendForHtmlInspection andThen handleHtmlInspectionResponse)(Util.sanitizeInput(link))
      case None => BadRequest("Problem with a request")
    }
  }

  private def sendForHtmlInspection: String => Either[String, Page] = { link =>
    inspectionService.extractHtml(link) match {
      case Right(doc) => Right(doc)
      case Left(ex) => Left(s"Could not extract HTML. Error: $ex")
    }
  }

  private def handleHtmlInspectionResponse: Either[String, Page] => Result = { response =>
    response match {
      case Right(doc) => {
        val pageInspectionResult = pageToPageInspectionResult(doc)

        Ok(views.html.result
          (pageInspectionResult.htmlVersion)
          (pageInspectionResult.title)
          (pageInspectionResult.headings)
          (pageInspectionResult.links)
          (pageInspectionResult.isLogin)
        )
      }
      case Left(ex) => BadRequest(ex)
    }
  }

  private def pageToPageInspectionResult(page: Page): PageInspectionResult = {
    val htmlVersion = inspectionService.getHtmlVersion(page)
    val title = inspectionService.getPageTitle(page)
    val headings = inspectionService.getAllHeadings(page)
    val links = inspectionService.getAllLinksGroupedByDomain(page)
    val isLogin = inspectionService.containsLoginForm(page)
    PageInspectionResult(htmlVersion = htmlVersion, title = title, headings = headings, links = links, isLogin = isLogin)
  }


}
