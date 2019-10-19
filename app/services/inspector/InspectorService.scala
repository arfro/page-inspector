package services.inspector

import com.google.inject.ImplementedBy
import services.inspector.jsoup.JSoupInspectorService

import scala.util.Try

@ImplementedBy(classOf[JSoupInspectorService])
trait InspectorService[A] {

  def extractHtml(link: String): Try[A]
  def getPageTitle(doc: A): Option[String]
  def getHtmlVersion(doc: A): Option[String]
  def getAllHeadings(doc: A): Map[String, Int]
  def getAllLinks(doc: A): List[String]
  def containsLoginForm(doc: A): Boolean

}
