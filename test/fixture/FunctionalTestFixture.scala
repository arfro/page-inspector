package fixture

import models.Page
import org.jsoup.nodes.Document
import org.scalatestplus.mockito.MockitoSugar
import services.HtmlInspectionService
import services.inspector.JSoupInspectorService
import test.scala.UnitTest

class FunctionalTest extends UnitTest with MockitoSugar {

  val examplePageMock = mock[Page]
  val exampleDocMock = mock[Document]
  val JSoupInspectorServiceMock = mock[JSoupInspectorService]
  val htmlInspectionServiceMock = mock[HtmlInspectionService]
}

