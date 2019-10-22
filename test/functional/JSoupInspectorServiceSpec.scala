package functional

import fixture.FunctionalTest
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
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

  "JSoupInspectorService#getHtmlVersion" should {
    "return if Html version detected" in {
      val htmlVersion = "<!DOCTYPE html>"
      Mockito.when(exampleDocMock.childNodes()).thenReturn(childNodesMock)
      Mockito.when(nodeMock.toString).thenReturn(htmlVersion)
      val result = jsoupInspectorServiceUT.getHtmlVersion(exampleDocMock)

      result mustBe Some(htmlVersion)
    }
  }

  it should {
    "return None for a missing html version" in {
      Mockito.when(exampleDocMock.childNodes()).thenReturn(new java.util.ArrayList[Node]())
      val result = jsoupInspectorServiceUT.getHtmlVersion(exampleDocMock)

      result mustBe None
    }
  }


  "JSoupInspectorService#getAllHeadings" should {
    "return correct amount of headers" in {
      Mockito.when(exampleDocMock.childNodes()).thenReturn(childNodesMock)
      Mockito.when(exampleDocMock.select("h1")).thenReturn(new Elements(elementMock, elementMock))
      Mockito.when(exampleDocMock.select("h2")).thenReturn(new Elements())
      Mockito.when(exampleDocMock.select("h3")).thenReturn(new Elements())
      Mockito.when(exampleDocMock.select("h4")).thenReturn(new Elements())
      Mockito.when(exampleDocMock.select("h5")).thenReturn(new Elements(elementMock))
      Mockito.when(exampleDocMock.select("h5")).thenReturn(new Elements(elementMock))
      Mockito.when(exampleDocMock.select("h6")).thenReturn(new Elements(elementMock, elementMock, elementMock))
      val expected = Map("h1" -> 2, "h2" -> 0, "h3" -> 0, "h4" -> 0, "h5" -> 1, "h6" -> 3)

      jsoupInspectorServiceUT.getAllHeadings(exampleDocMock) mustBe expected
    }
  }

}
