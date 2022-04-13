package io.qhquanghuy.btcbillionaire.adapter.webservice

import java.time.ZonedDateTime

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes

import spray.json._

import _root_.io.qhquanghuy.btcbillionaire.application.donation.DonationService
import _root_.io.qhquanghuy.btcbillionaire.utils.JsonFormats._
import _root_.io.qhquanghuy.btcbillionaire.domain.DonationDTO
import _root_.io.qhquanghuy.btcbillionaire.utils.models._

final class DonationRoute(donationService: DonationService) {
  def routes: Route = (path("donations") & post) { reqCtx =>
    implicit val exc = reqCtx.executionContext
    entity(as[DonationDTO]) { dto =>
      val futureJson = donationService.donate(dto)
        .fold(
          err => StatusCodes.BadRequest -> ErrorResponse(err.code, err.msg).toJson,
          wallet => StatusCodes.OK -> DataResponse(wallet).toJson
        )

      complete(futureJson)
    }(reqCtx)
  }
}