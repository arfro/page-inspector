package services.inspector

import org.jsoup.nodes.Document

import scala.util.Try

trait InspectorService {

  def extractHtml(link: String): Try[Document]
  def getPageTitle(doc: Document): Option[String]
  def getHtmlVersion(doc: Document): Option[String]
  def getAllHeadings(doc: Document): Map[String, Int]
  def getAllLinks(doc: Document): List[String]
  def containsLoginForm(doc: Document): Boolean

}
