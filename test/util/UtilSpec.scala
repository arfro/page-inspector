import test.scala.UnitTest
import unit.util.Util

class UtilSpec extends UnitTest {

  "Util#sanitizeInput" should {
    "trim a link" in {
      val input = "    http://google.com "
      val expected = "http://google.com"
      Util.sanitizeInput(input) mustBe expected
    }
  }

  it should {
    "remove www. from the link" in {
      val input = "http://www.google.com"
      val expected = "http://google.com"
      Util.sanitizeInput(input) mustBe expected
    }
  }

}