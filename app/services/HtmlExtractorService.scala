package services

import java.net.URL

import errors.jsoup.HtmlExtractionError
import models.jsoup.Page
import services.extractor.JSoupService

import scala.util.Try

class HtmlExtractorService(JsoupService: JSoupService) {

  def extractHtml(link: String): Either[HtmlExtractionError, Page] = {
    JsoupService.extractHtml(link) match {
      case Some(doc) => {
        val maybeDomainName = getHostName(link)
        val page = Page(rawInputLink = link, hostName = maybeDomainName, doc = doc)
        Right(page)
      }
      case None => Left(HtmlExtractionError("Could not extract HTML page")) // better error handling here?
    }

  }

  def getPageTitle(page: Page): Option[String] = JsoupService.getPageTitle(page.doc)

  def getHtmlVersion(page: Page): Option[String] = JsoupService.getHtmlVersion(page.doc)

  def getAllHeadings(page: Page): List[Map[String, Int]] = JsoupService.getAllHeadings(page.doc)

  def getAllLinksGroupedByDomain(page: Page): Map[String, List[String]] = {
    def domainNamePredicate: String => Boolean = {
      link =>
        page.hostName match {
          case Some(hostName) => link.contains(s"$hostName.") || link.startsWith("/")
          case None           => link.startsWith("/")
        }
    }

    val allLinksTuple = JsoupService.getAllLinks(page.doc)
      .partition(domainNamePredicate)

    Map(
      "internal" -> allLinksTuple._1,
      "external" -> allLinksTuple._2
    )
  }

  def containsLoginForm(page: Page): Boolean = JsoupService.containsLoginForm(page.doc)

  private def getHostName(link: String): Option[String] = Try(new URL(link).getHost.split(".")(0)).toOption
}
