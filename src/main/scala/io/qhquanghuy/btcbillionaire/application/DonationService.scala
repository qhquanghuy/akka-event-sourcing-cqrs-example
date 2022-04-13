package io.qhquanghuy.btcbillionaire.application

import scala.concurrent.Future
import scala.concurrent.duration._

import spray.json.JsValue
import org.slf4j.LoggerFactory

import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.scaladsl.ClusterSharding

import cats.implicits._
import cats.data.EitherT

import io.qhquanghuy.btcbillionaire.domain._
import io.qhquanghuy.btcbillionaire.application.donation._


final class DonationService(system: ActorSystem[_]) {
  import system.executionContext
  private val logger = LoggerFactory.getLogger(getClass)
  private val sharding = ClusterSharding(system)

  def donate(dto: DonationDTO) = {

    logger.info(s"processing: ${dto}")

    val future = Future.successful {
      BTC.mk(dto.amount)
      .map(btc => Donation(dto.datetime, btc))
    }

    EitherT(future)
      .flatMapF { donation =>
        val ref = sharding.entityRefFor(DonationCommand.EntityKey, Wallet.Address)
        ref.askWithStatus(Command.Donate(donation, _))(5.seconds)
          .map(Right.apply)
      }
  }
}
