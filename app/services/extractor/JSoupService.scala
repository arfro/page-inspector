package services.extractor

import config.AppConfig

import scala.util.{Failure, Success, Try}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, DocumentType}
import org.jsoup.safety.Whitelist

class JSoupService extends {

  def extractHtml(link: String) =
    Try(Jsoup // all java code in Try monad
      .connect(sanitizeInput(link))
      .timeout(AppConfig.jsoupTimeout)
      .get()
    )

  def getPageTitle(doc: Document): Option[String] =
    Try(doc.title).fold(_ => None, title => Some(title))

  def getHtmlVersion(doc: Document): Option[String] =
    Try(
      doc.childNodes()
      .toArray
      .filter(_.isInstanceOf[DocumentType])
      .map(_.toString)
      .headOption
    ) match { // I could have a map here that does a lookup of translating to human language
      case Success(value) => value
      case Failure(_) => None
    }

  def getAllHeadings(doc: Document) = doc.clone()

  def getAllLinks(doc: Document) = doc.select("a[href]")

  private def sanitizeInput(link: String) = Jsoup.clean(link, Whitelist.basic())
}
