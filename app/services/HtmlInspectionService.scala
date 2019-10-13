package services

import java.net.URL

import errors.HtmlExtractionError
import javax.inject.Inject
import models.Page
import services.inspector.JSoupInspectorService

import scala.util.Try

class HtmlInspectionService @Inject()(JsoupService: JSoupInspectorService) {

  def extractHtml(link: String): Either[HtmlExtractionError, Page] = {
    JsoupService.extractHtml(link)
      .fold(_ => Left(HtmlExtractionError("Could not extract HTML page. Is your link correct?")),
      doc => {
        val maybeDomainName = getHostName(link)
        val page = Page(rawInputLink = link, hostName = maybeDomainName, doc = doc)
        Right(page)
      })
  }

  def getPageTitle(page: Page): String = JsoupService.getPageTitle(page.doc).getOrElse("unknown")

  def getHtmlVersion(page: Page): String = JsoupService.getHtmlVersion(page.doc).getOrElse("unknown")

  def getAllHeadings(page: Page): Map[String, Int] = JsoupService.getAllHeadings(page.doc)

  def getAllLinksGroupedByDomain(page: Page): Map[String, List[String]] = {
    def domainNamePredicate: String => Boolean = {
      link =>
        page.hostName match { // if hostname is "x.com" then we can be fairly sure any link containing "x." will be an internal link
          case Some(hostName) => link.contains(s"$hostName.") || link.startsWith("/") || link.startsWith("#")
          case None           => link.startsWith("/")
        }
    }

    val allLinksTuple = JsoupService.getAllLinks(page.doc).filter(_ != "")
      .partition(domainNamePredicate)

    Map(
      "internal" -> allLinksTuple._1,
      "external" -> allLinksTuple._2
    )
  }

  def containsLoginForm(page: Page): Boolean = JsoupService.containsLoginForm(page.doc)

  private def getHostName(link: String): Option[String] = {
    Try(new URL(link).getHost.split("\\.")(0)).toOption
  }
}
