package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.HtmlInspectionService
import services.inspector.JSoupInspectorService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] => {
//    val JSoupService = new JSoupInspectorService
//    val htmlExtractorService = new HtmlInspectionService(JSoupService)
//    htmlExtractorService.extractHtml("httkkspiegel/login.html").map{
//      page =>
//        println(htmlExtractorService.getAllHeadings(page) + "\n\n\n\n")
//        println(htmlExtractorService.getAllLinksGroupedByDomain(page) + "\n\n\n\n")
//        println(htmlExtractorService.getHtmlVersion(page) + "\n\n\n\n")
//        println(htmlExtractorService.containsLoginForm(page) + "\n\n\n\n")
 //   }
    Ok(views.html.index())
  }
  }
}
