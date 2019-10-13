package test.functional

import fixture.FunctionalTest

class HtmlInspectionServiceSpec extends FunctionalTest {

  "HtmlInspectionService#getTitle" should {
    "return correct data for google.com" in {
      val page = "http://google.com"
      val doc = htmlInspectionService.extractHtml(page).right.get
      htmlInspectionService.getPageTitle(doc) mustBe "Google"
    }
  }

  "JSoupInspectorService#getHtmlVersion" should {
    "return correct data for http://www.google.com" in {
      val page = "http://www.google.com"
      val doc = htmlInspectionService.extractHtml(page).right.get
      htmlInspectionService.getHtmlVersion(doc) mustBe "<!DOCTYPE html>"
    }
  }

}
