package io.qhquanghuy.btcbillionaire.application.donation

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

import spray.json.JsValue
import org.slf4j.LoggerFactory

import cats.implicits._
import cats.data.EitherT

import io.qhquanghuy.btcbillionaire.domain._
import io.qhquanghuy.btcbillionaire.domain.donation._
import io.qhquanghuy.btcbillionaire.application.donation._
import io.qhquanghuy.btcbillionaire.port.command.CommandHandler



final class DonationService(commandHandler: CommandHandler[DonationCommand]) {
  private val logger = LoggerFactory.getLogger(getClass)

  def donate(dto: DonationDTO)(implicit ctx: ExecutionContext) = {

    logger.info(s"processing: ${dto}")

    val future = Future.successful {
      BTC.mk(dto.amount)
      .map(btc => Donation(dto.datetime, btc))
    }

    EitherT(future)
      .flatMapF { donation =>
        commandHandler.handle(DonationCommand.Donate(donation))
          .map(Right.apply)
      }
  }
}
