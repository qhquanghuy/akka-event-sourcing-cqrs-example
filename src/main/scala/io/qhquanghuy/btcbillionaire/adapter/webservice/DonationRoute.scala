package io.qhquanghuy.btcbillionaire.adapter.webservice

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import spray.json._

import _root_.io.qhquanghuy.btcbillionaire.application.DonationService


final class DonationRoute(donationService: DonationService) {
  def routes: Route = (path("donations") & post) {
    entity(as[JsValue]) { json =>
      complete(donationService.donate(json))
    }
  }
}