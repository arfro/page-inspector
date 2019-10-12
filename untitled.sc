import org.jsoup.Jsoup
import scala.util.Try
import scala.util.{Success, Failure}
import org.jsoup.nodes.DocumentType
import org.jsoup.select.Elements
import org.jsoup.select.NodeVisitor
import org.jsoup.nodes.Element

def extractHtml(link: String) =
  Try(Jsoup
    .connect(link)
    .timeout(34224)
    .get()
  )

val doc = extractHtml("https://www.spiegel.de/meinspiegel/login.html").get

var allInputs: List[String] = List()

doc.getElementsByTag("input")
  .stream()
  .forEach(elem => {
    allInputs = elem.attr("name") :: allInputs
  })

allInputs

allInputs.filter(_.toLowerCase contains ("login"))