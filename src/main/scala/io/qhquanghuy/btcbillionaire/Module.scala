package io.qhquanghuy.btcbillionaire

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.server.Route

import com.softwaremill.macwire._

import slick.basic.DatabaseConfig
import slick.driver.JdbcProfile

import _root_.io.qhquanghuy.btcbillionaire.adapter.webservice._
import _root_.io.qhquanghuy.btcbillionaire.application.donation._
import _root_.io.qhquanghuy.btcbillionaire.application.donationsummary._
import _root_.io.qhquanghuy.btcbillionaire.adapter.command.DonationCommandHandler
import _root_.io.qhquanghuy.btcbillionaire.port.command.CommandHandler
import _root_.io.qhquanghuy.btcbillionaire.domain.donation.DonationCommand
import _root_.io.qhquanghuy.btcbillionaire.port.query.DonationSummaryQuery
import _root_.io.qhquanghuy.btcbillionaire.adapter.query.DonationSummaryQueryImpl
import _root_.io.qhquanghuy.btcbillionaire.adapter.database.DonationDAO



trait Module[P <: JdbcProfile] {
  def donationCommandHandler: CommandHandler[DonationCommand]
  def donationService: DonationService
  def donationDAO: DonationDAO[P]
  def DonationSummaryQuery: DonationSummaryQuery
  def donationSummaryService: DonationSummaryService

  def routes: Route = AppRoute.routes(
    wire[DonationSumaryRoute].routes,
    wire[DonationRoute].routes
  )
}

final class AppModule[P <: JdbcProfile](actorSystem: ActorSystem[_], databaseConfig: DatabaseConfig[P]) extends Module[P] {

  override lazy val donationCommandHandler: CommandHandler[DonationCommand] = wire[DonationCommandHandler]
  override lazy val donationService: DonationService = wire[DonationService]
  override lazy val donationDAO = wire[DonationDAO[P]]
  override lazy val DonationSummaryQuery: DonationSummaryQuery = wire[DonationSummaryQueryImpl[P]]
  override lazy val donationSummaryService: DonationSummaryService = wire[DonationSummaryService]

  override lazy val routes: Route = AppRoute.routes(
    wire[DonationSumaryRoute].routes,
    wire[DonationRoute].routes
  )
}