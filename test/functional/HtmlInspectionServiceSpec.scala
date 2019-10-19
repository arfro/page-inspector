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

  "HtmlInspectionService#getHtmlVersion" should {
    "return data for existing html version" in {
      val htmlVersion = "<!DOCTYPE html>"
      Mockito.when(exampleDocMock.childNodes()).thenReturn(childNodesMock)
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getHtmlVersion(exampleDocMock)).thenReturn(Some(htmlVersion))
      val result = htmlInspectionServiceUT.getHtmlVersion(examplePageMock)

      result mustBe htmlVersion
    }
  }

  it should {
    "return unknown for a missing html version" in {
      Mockito.when(exampleDocMock.childNodes()).thenReturn(childNodesMock)
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getHtmlVersion(exampleDocMock)).thenReturn(None)
      val result = htmlInspectionServiceUT.getHtmlVersion(examplePageMock)

      result mustBe "unknown"
    }
  }

  "HtmlInspectionService#getAllHeadings" should {
    "return data for all headings" in {
      val allHeadings = Map("h1" -> 0, "h2" -> 0, "h3" -> 0, "h4" -> 0, "h5" -> 0, "h6" -> 0 )
      Mockito.when(exampleDocMock.childNodes()).thenReturn(childNodesMock)
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getAllHeadings(exampleDocMock)).thenReturn(allHeadings)
      val result = htmlInspectionServiceUT.getAllHeadings(examplePageMock)

      result mustBe allHeadings
    }
  }

  "HtmlInspectionService#getAllLinksGroupedByDomain" should {
    "group links starting in / or # or containing hostname. as internal" in {
      val allLinks = List("/internalLink", "#internalLink", "www.hostName.com/info")
      val hostName = "hostName"
      Mockito.when(examplePageMock.hostName).thenReturn(Some(hostName))
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getAllLinks(exampleDocMock)).thenReturn(allLinks)

      val result = htmlInspectionServiceUT.getAllLinksGroupedByDomain(examplePageMock)

      result.get("internal").get mustBe allLinks
      result.get("external").get.length mustBe 0
    }
  }

  it should {
    "group external links correctly" in {
      val allLinks = List("www.linkedin.com", "www.google.com")
      val hostName = "hostName"
      Mockito.when(examplePageMock.hostName).thenReturn(Some(hostName))
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getAllLinks(exampleDocMock)).thenReturn(allLinks)

      val result = htmlInspectionServiceUT.getAllLinksGroupedByDomain(examplePageMock)

      result.get("internal").get.length mustBe 0
      result.get("external").get mustBe allLinks
    }
  }

  it should {
    "group external and internal links correctly" in {
      val allLinks = List("www.linkedin.com", "www.google.com", "/internalLink", "#internalLink", "www.hostName.com/infor", "hostName.com/about")
      val hostName = "hostName"
      Mockito.when(examplePageMock.hostName).thenReturn(Some(hostName))
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getAllLinks(exampleDocMock)).thenReturn(allLinks)

      val result = htmlInspectionServiceUT.getAllLinksGroupedByDomain(examplePageMock)

      result.get("internal").get.length mustBe 4
      result.get("external").get.length mustBe 2
    }
  }

  it should {
    "handle no links in the page correctly" in {
      val allLinks = List()
      val hostName = "hostName"
      Mockito.when(examplePageMock.hostName).thenReturn(Some(hostName))
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.getAllLinks(exampleDocMock)).thenReturn(allLinks)

      val result = htmlInspectionServiceUT.getAllLinksGroupedByDomain(examplePageMock)

      result.get("internal").get.length mustBe 0
      result.get("external").get.length mustBe 0
    }
  }

  "HtmlInspectionService#containsLoginForm" should {
    "handle login form detection if login form exists" in {
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.containsLoginForm(exampleDocMock)).thenReturn(true)

      val result = htmlInspectionServiceUT.containsLoginForm(examplePageMock)

      result mustBe true
    }
  }

  it should {
    "handle login form detection if login form does not exist" in {
      Mockito.when(examplePageMock.doc).thenReturn(exampleDocMock)
      Mockito.when(JSoupInspectorServiceMock.containsLoginForm(exampleDocMock)).thenReturn(false)

      val result = htmlInspectionServiceUT.containsLoginForm(examplePageMock)

      result mustBe false
    }
  }


}
