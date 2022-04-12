package io.qhquanghuy.btcbillionaire.adapter.webservice

import java.time.ZonedDateTime
import scala.util.{Try, Success, Failure}

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.settings.RoutingSettings
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpResponse

import spray.json._

import _root_.io.qhquanghuy.btcbillionaire.application.DonationSummaryService
import akka.http.scaladsl.server.RejectionHandler
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes

final class DonationSumaryRoute(donationSummaryService: DonationSummaryService) {


  val zonedDateTime = Unmarshaller.strict[String, ZonedDateTime](ZonedDateTime.parse)

  def routes: Route = (
    path("donation-summary")
      & get
      & parameters("start_time".as(zonedDateTime), "end_time".as(zonedDateTime))
    ) { (startTime, endTime) =>
      donationSummaryService.summary(startTime, endTime)
      complete(StatusCodes.OK)
  }
}