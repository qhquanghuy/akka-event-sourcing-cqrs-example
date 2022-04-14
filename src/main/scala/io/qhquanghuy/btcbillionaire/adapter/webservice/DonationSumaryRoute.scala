package io.qhquanghuy.btcbillionaire.adapter.webservice

import java.time.ZonedDateTime
import scala.util.{Try, Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global


import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route

import spray.json._
import spray.json.DefaultJsonProtocol._

import _root_.io.qhquanghuy.btcbillionaire.domain.donation.DonationDTO
import _root_.io.qhquanghuy.btcbillionaire.application.donationsummary.DonationSummaryService
import _root_.io.qhquanghuy.btcbillionaire.utils.models.DataResponse
import _root_.io.qhquanghuy.btcbillionaire.utils.JsonFormats._


final class DonationSumaryRoute(donationSummaryService: DonationSummaryService) {


  val zonedDateTime = Unmarshaller.strict[String, ZonedDateTime](ZonedDateTime.parse)

  def routes: Route = (
    path("donation-summary")
      & get
      & parameters("start_time".as(zonedDateTime), "end_time".as(zonedDateTime))
    ) { (startTime, endTime) =>

      complete {
        donationSummaryService.summary(startTime, endTime)
          .map(summary => DataResponse(summary.donations).toJson)
      }
  }
}