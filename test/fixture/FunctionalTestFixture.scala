package fixture

import models.Page
import org.jsoup.Connection
import org.jsoup.nodes.{Document, DocumentType, Element, Node}
import org.scalatestplus.mockito.MockitoSugar
import services.HtmlInspectionService
import services.inspector.jsoup.{JSoupInspectorService, JSoupWrapper}
import test.scala.UnitTest

class FunctionalTest extends UnitTest with MockitoSugar {

  val examplePageMock = mock[Page]
  val exampleDocMock = mock[Document]


  val nodeMock = mock[DocumentType]
  val childNodesMock = { //TODO: so ugly! :(
    val childNodes = new java.util.ArrayList[Node]
    childNodes.add(nodeMock)
    childNodes
  }

  val elementMock = mock[Element]

  val JSoupInspectorServiceMock = mock[JSoupInspectorService]
  val htmlInspectionServiceMock = mock[HtmlInspectionService]

  val jsoupWrapperMock = mock[JSoupWrapper]
  val jsoupConnectionMock = mock[Connection]


}

