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
        println(s"$maybeDomainName --------------------")
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
        page.hostName match { // if hostname is "x.com" then we can be fairly sure any link containing "x." will be an internal link
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

  private def getHostName(link: String): Option[String] = {
    val sanitizedLink = link.replace("www.", "")
    Try(new URL(sanitizedLink).getHost.split("\\.")(0)).toOption
  }
}
