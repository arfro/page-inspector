package fixture

import models.Page
import org.jsoup.{Connection, Jsoup}
import org.jsoup.nodes.{Document, Node}
import org.scalatestplus.mockito.MockitoSugar
import services.HtmlInspectionService
import services.inspector.jsoup.{JSoupInspectorService, JSoupWrapper}
import test.scala.UnitTest

class FunctionalTest extends UnitTest with MockitoSugar {

  val examplePageMock = mock[Page]
  val exampleDocMock = mock[Document]
  val childNodesMock = mock[java.util.ArrayList[Node]]

  val JSoupInspectorServiceMock = mock[JSoupInspectorService]
  val htmlInspectionServiceMock = mock[HtmlInspectionService]

  val jsoupWrapperMock = mock[JSoupWrapper]
  val jsoupConnectionMock = mock[Connection]


}

