package test.functional

import fixture.FunctionalTest
import models.Page
import models.errors.HtmlExtractionError
import org.mockito.Mockito
import services.HtmlInspectionService

import scala.util.{Failure, Success}

class HtmlInspectionServiceSpec extends FunctionalTest {

  val htmlInspectionServiceUT = new HtmlInspectionService(JSoupInspectorServiceMock)

  "HtmlInspectionService#extractHtml" should {
    "return Right with Page for an valid link" in {
      val someValidLink = "http://thispagetotallyexists.com"
      val hostname = Some("thispagetotallyexists")
      Mockito.when(JSoupInspectorServiceMock.extractHtml(someValidLink)).thenReturn(Success(exampleDocMock))
      val resultPage = Page(someValidLink, hostname, exampleDocMock)

      htmlInspectionServiceUT.extractHtml(someValidLink) mustBe Right(resultPage)
    }
  }

  it should {
    "return Left with HtmlExtractionError for an invalid link" in {
      val someInvalidLink = "I'm totally a valid link"
      Mockito.when(JSoupInspectorServiceMock.extractHtml(someInvalidLink)).thenReturn(Failure(new Exception("I can't even")))
      val expected = HtmlExtractionError("Could not extract HTML page. Is your link correct?")

      htmlInspectionServiceUT.extractHtml(someInvalidLink) mustBe Left(expected)
    }
  }

  "HtmlInspectionService#getTitle" should {
    "return correct data for existing website title" in {
      val title = "Super catchy title"
      Mockito.when(exampleDocMock.title).thenReturn(title)
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getPageTitle(exampleDocMock)).thenReturn(Some(title))
      val result = htmlInspectionServiceUT.getPageTitle(examplePageMock)

      result mustBe title
    }
  }

  it should {
    "return 'unknown' for a missing website title" in {
      Mockito.when(exampleDocMock.title).thenThrow(new RuntimeException("terrible problem and the page title is missing"))
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getPageTitle(exampleDocMock)).thenReturn(None)
      val result = htmlInspectionServiceUT.getPageTitle(examplePageMock)

      result mustBe "unknown"
    }
  }
//
//  "JSoupInspectorService#getHtmlVersion" should {
//    "return correct data for http://www.google.com" in {
//      val page = "http://www.google.com"
//      val doc = htmlInspectionServiceMock.extractHtml(page).right.get
//      htmlInspectionServiceMock.getHtmlVersion(doc) mustBe "<!DOCTYPE html>"
//    }
//  }

}
