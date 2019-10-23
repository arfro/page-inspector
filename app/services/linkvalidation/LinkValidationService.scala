package services.linkvalidation


import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import config.AppConfig
import javax.inject.Inject
import play.api.http.Status
import play.api.libs.ws._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


trait ValidationResult
case class ValidationSuccess(url: String, status: Int) extends ValidationResult
case class ValidationFailure(url: String, problem: String) extends ValidationResult

class LinkValidationService @Inject() (ws: WSClient)  {

  // TODO: inject execution context

  def validate(listOfUrls: List[String]) = batchProcess(listOfUrls, AppConfig.urlsPerBatch)

  private def batchProcess(listOfUrls: List[String], urlsPerBatch: Int = 20) = {
    // divide into batches
    val batches = listOfUrls.grouped(urlsPerBatch).toList
    println(s"Processing ${batches.length} batch(es)")
    // wrap each batch into a future if not empty then perform process batch
    val asyncBatch: Future[List[ValidationResult]] =
      batches.foldLeft(Future.successful(List.empty[ValidationResult]))(processBatch)
    // batches.flatMap(batch => processBatch(batch))
    asyncBatch
  }

  private def processBatch(results: Future[List[ValidationResult]], batch: List[String]): Future[List[ValidationResult]] = {

    results.flatMap{ responses =>
    val batchFutures: List[Future[ValidationResult]] = batch.map(url => {
      ws.url(url).withRequestTimeout(2000.millis).get().map {
          case d => ValidationSuccess(url, d.status)
        }.recover{case e: Exception => ValidationFailure(url, e.getMessage)}
    })

    val batchFuture: Future[List[ValidationResult]] = Future.sequence(batchFutures)

    batchFuture.map { batchResponses =>
      println("Finished a batch")
      responses ++ batchResponses
    }}
//
//    val batchFuture: List[Future[ValidationResult]] = batchFutures
//
//    val res2: Future[List[ValidationResult]] = for {
//      result <- results
//      ba <- batchFuture
//    } yield result ++ ba
//      res2


  }


}
