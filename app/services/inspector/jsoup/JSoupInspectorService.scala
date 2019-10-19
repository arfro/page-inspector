package services.inspector.jsoup

import config.AppConfig
import javax.inject.Inject
import org.jsoup.{Connection, Jsoup}
import org.jsoup.nodes.{Document, DocumentType}
import services.inspector.InspectorService

import scala.util.{Failure, Success, Try}

class JSoupInspectorService @Inject()(JSoupWrapper: JSoupWrapper) extends InspectorService[Document] {

  private def connectAndGetHtmlFunction: String => Document =
    link => JSoupWrapper
      .connect(link)
      .get()

  def extractHtml(link: String): Try[Document] =
    Try(connectAndGetHtmlFunction(link))

  def getPageTitle(doc: Document): Option[String] = Try(doc.title).fold(_ => None, title => Some(title))

  def getHtmlVersion(doc: Document): Option[String] =
    Try(
      doc.childNodes()
      .toArray
      .filter(_.isInstanceOf[DocumentType])
      .map(_.toString)
      .headOption
    ) match { // I could have a map here that does a lookup of translating to human language e.g. <!DOCTYPE html> -> "HTML 5" etc
      case Success(value) => value
      case Failure(_) => None
    }

  def getAllHeadings(doc: Document): Map[String, Int] =
    (1 to 6).map(nr => {
      s"h$nr" -> doc.select(s"h$nr").toArray.length
    }).toMap

  def getAllLinks(doc: Document): List[String] =
    getAttributeForAllTags("a", "href")(doc)

  def containsLoginForm(doc: Document): Boolean =
    getAttributeForAllTags("input", "name")(doc)
      .filter(_.toLowerCase contains ("login")).size > 0

  private def getAttributeForAllTags(tagName: String, attributeKey: String): Document => List[String] = { doc: Document =>
    var allLinks: List[String] = List() // not too happy with using vars, even in a private, local scope.

    doc.getElementsByTag(tagName)
      .stream()
      .forEach(elem => {
        allLinks = elem.attr(attributeKey) :: allLinks
      })

    allLinks.distinct
  }
}
