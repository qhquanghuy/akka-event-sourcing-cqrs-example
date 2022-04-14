package io.qhquanghuy.btcbillionaire

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.server.Route

import com.softwaremill.macwire._

import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import _root_.io.qhquanghuy.btcbillionaire.adapter.webservice._
import _root_.io.qhquanghuy.btcbillionaire.application.donation._
import _root_.io.qhquanghuy.btcbillionaire.application.donationsummary._
import _root_.io.qhquanghuy.btcbillionaire.adapter.command.DonationCommandHandler
import _root_.io.qhquanghuy.btcbillionaire.port.command.CommandHandler
import _root_.io.qhquanghuy.btcbillionaire.domain.donation.DonationCommand
import _root_.io.qhquanghuy.btcbillionaire.port.query.DonationSummaryQuery
import _root_.io.qhquanghuy.btcbillionaire.adapter.query.DonationSummaryQueryImpl
import _root_.io.qhquanghuy.btcbillionaire.adapter.database.DonationDAO


trait Module {
  def actorSystem: ActorSystem[_]
  def databaseConfig: DatabaseConfig[PostgresProfile]
  lazy val donationCommandHandler: CommandHandler[DonationCommand] = wire[DonationCommandHandler]
  lazy val donationService: DonationService = wire[DonationService]
  lazy val donationDAO: DonationDAO = wire[DonationDAO]
  lazy val DonationSummaryQuery: DonationSummaryQuery = wire[DonationSummaryQueryImpl]
  lazy val donationSummaryService: DonationSummaryService = wire[DonationSummaryService]

  lazy val routes: Route = AppRoute.routes(
    wire[DonationSumaryRoute].routes,
    wire[DonationRoute].routes
  )
}