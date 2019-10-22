package services

import java.net.URL

import javax.inject.Inject
import models.Page
import models.errors.HtmlExtractionError
import org.jsoup.nodes.Document
import services.inspector.InspectorService

import scala.util.Try

class HtmlInspectionService @Inject()(inspectorService: InspectorService[Document]) {

  def extractHtml(link: String): Either[HtmlExtractionError, Page] = {
    inspectorService.extractHtml(link)
      .fold(_ => Left(HtmlExtractionError("Could not extract HTML page. Is your link correct?")),
      doc => {
        val maybeDomainName = getHostName(link)
        val page = Page(rawInputLink = link, hostName = maybeDomainName, doc = doc)
        Right(page)
      })
  }

  def getPageTitle(page: Page): String = inspectorService.getPageTitle(page.doc).getOrElse("unknown")

  def getHtmlVersion(page: Page): String = inspectorService.getHtmlVersion(page.doc).getOrElse("unknown")

  def getAllHeadings(page: Page): Map[String, Int] = inspectorService.getAllHeadings(page.doc)

  def getAllLinksGroupedByDomain(page: Page): Map[String, List[String]] = {
    def domainNamePredicate: String => Boolean = {
      link =>
        page.hostName match { // if hostname is "x.com" then we can be fairly sure any link containing "x." will be an internal link
          case Some(hostName) => link.contains(s"$hostName.") || link.startsWith("/") || link.startsWith("#")
          case None           => link.startsWith("/")
        }
    }

    val allLinksTuple = inspectorService.getAllLinks(page.doc).filter(_ != "")
      .partition(domainNamePredicate)

    Map(
      "internal" -> allLinksTuple._1,
      "external" -> allLinksTuple._2
    )
  }

  def containsLoginForm(page: Page): Boolean = inspectorService.containsLoginForm(page.doc)

  private def getHostName(link: String): Option[String] = {
    Try(new URL(link).getHost.split("\\.")(0)).toOption
  }
}
