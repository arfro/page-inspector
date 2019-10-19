package functional

import fixture.FunctionalTest
import org.mockito.Mockito
import services.inspector.jsoup.JSoupInspectorService

import scala.util.{Failure, Success}


class JSoupInspectorServiceSpec extends FunctionalTest {

  val jsoupInspectorServiceUT = new JSoupInspectorService(jsoupWrapperMock)

  "JSoupInspectorService#extractHtml" should {
    "return success for a valid link" in {
      val link = "valid link"
      Mockito.when(jsoupWrapperMock.connect(link)).thenReturn(jsoupConnectionMock)
      Mockito.when(jsoupConnectionMock.timeout(400)).thenReturn(jsoupConnectionMock)
      Mockito.when(jsoupConnectionMock.get()).thenReturn(exampleDocMock)

      val result = jsoupInspectorServiceUT.extractHtml(link)

      result mustBe Success(exampleDocMock)
    }
  }

  it should {
    "return failure for an invalid link" in {
      val link = "invalid link"
      val exception = new java.net.MalformedURLException("problem with the link")
      Mockito.when(jsoupWrapperMock.connect(link)).thenReturn(jsoupConnectionMock)
      Mockito.when(jsoupConnectionMock.timeout(400)).thenReturn(jsoupConnectionMock)
      Mockito.when(jsoupConnectionMock.get()).thenThrow(exception)

      val result = jsoupInspectorServiceUT.extractHtml(link)

      result mustBe Failure(exception)
    }
  }

  "JSoupInspectorService#getPagetitle" should {
    "return empty string for a page title empty string" in {
      Mockito.when(exampleDocMock.title).thenReturn("")

      val result = jsoupInspectorServiceUT.getPageTitle(exampleDocMock)

      result mustBe Some("")
    }
  }

  it should {
    "return None for a page title throwing an Exception" in {
      Mockito.when(exampleDocMock.title).thenThrow(new RuntimeException("something went terribly wrong"))

      val result = jsoupInspectorServiceUT.getPageTitle(exampleDocMock)

      result mustBe None
    }
  }




}
