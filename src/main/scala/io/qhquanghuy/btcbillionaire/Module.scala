package io.qhquanghuy.btcbillionaire

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.server.Route

import com.softwaremill.macwire._

import _root_.io.qhquanghuy.btcbillionaire.adapter.webservice._
import _root_.io.qhquanghuy.btcbillionaire.application._

final class Module() {
  lazy val donationService: DonationService = wire[DonationService]
  lazy val donationSummaryService: DonationSummaryService = wire[DonationSummaryService]

  lazy val routes: Route = AppRoute.routes(
    wire[DonationSumaryRoute].routes,
    wire[DonationRoute].routes
  )
}
