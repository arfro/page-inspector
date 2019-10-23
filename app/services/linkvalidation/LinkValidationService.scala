package services.linkvalidation

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import config.AppConfig
import javax.inject.Inject
import play.api.libs.ws._

import scala.concurrent.Future
import scala.util.Success


case class ValidationResult(s: String, sr: Int)

class LinkValidationService @Inject() (ws: WSClient)  {

  // TODO: inject execution context

  def validate(listOfUrls: List[String]) = batchProcess(listOfUrls, AppConfig.urlsPerBatch)

  private def batchProcess(listOfUrls: List[String], urlsPerBatch: Int = 20) = {
    // divide into batches
    val batches = listOfUrls.grouped(urlsPerBatch).toList
    println(s"Processing ${batches.length} batch(es)")
    // wrap each batch into a future if not empty then perform process batch
    batches.map(processBatch)
  }

  private def processBatch(batch: List[String]): Future[List[ValidationResult]] = {

    val results: Future[List[ValidationResult]] = Future.successful(List.empty)

    val batchFutures = batch.map(url => {
      ws.url(url).withRequestTimeout(10000.millis).get().map(f => ValidationResult(url, f.status))
    })

    val batchFuture: Future[List[ValidationResult]] = Future.sequence(batchFutures)

    val res2: Future[List[ValidationResult]] = for {
      result <- results
      ba <- batchFuture
    } yield result ++ ba
      res2


  }


}
