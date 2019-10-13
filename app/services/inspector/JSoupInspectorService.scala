package services.inspector

import config.AppConfig

import scala.util.{Failure, Success, Try}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, DocumentType}
import org.jsoup.safety.Whitelist

class JSoupInspectorService extends {

  def extractHtml(link: String): Option[Document] =
    Try(Jsoup // all java code in Try monad
      .connect(sanitizeInput(link))
      .timeout(AppConfig.jsoupTimeout)
      .get()
    ).toOption

  def getPageTitle(doc: Document): Option[String] =
    Try(doc.title).fold(_ => None, title => Some(title))

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

  def getAllLinks(doc: Document): List[String] = {
    var allLinks: List[String] = List() // lesser evil to have a var in a local scope only btu not ideal

    doc.getElementsByTag("a")
      .stream()
      .forEach(elem => {
        allLinks = elem.attr("href") :: allLinks
      })

    allLinks
  }

  def containsLoginForm(doc: Document): Boolean = {
    var allInputs: List[String] = List()

    doc.getElementsByTag("input")
      .stream()
      .forEach(elem => {
        allInputs = elem.attr("name") :: allInputs
      })

    allInputs.filter(_.toLowerCase contains ("login")).size > 0
  }

  private def sanitizeInput(link: String): String = Jsoup.clean(link, Whitelist.basic())
}
