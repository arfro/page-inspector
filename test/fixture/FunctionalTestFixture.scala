package fixture

import org.scalatestplus.mockito.MockitoSugar
import services.HtmlInspectionService
import services.inspector.JSoupInspectorService
import test.scala.UnitTest

class FunctionalTest extends UnitTest with MockitoSugar {

  val JSoupInspectorService = new JSoupInspectorService
  val htmlInspectionService = new HtmlInspectionService(JSoupInspectorService)
}

