import org.jsoup.Jsoup
import scala.util.Try
import scala.util.{Success, Failure}
import org.jsoup.nodes.DocumentType
import org.jsoup.select.Elements
import org.jsoup.nodes.Element

def extractHtml(link: String) =
  Try(Jsoup
    .connect(link)
    .timeout(34224)
    .get()
  )

val doc = extractHtml("http://github.com").get


doc.select("a")