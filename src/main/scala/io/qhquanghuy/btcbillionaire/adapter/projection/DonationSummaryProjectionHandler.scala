package io.qhquanghuy.btcbillionaire.adapter.projection

import scala.concurrent.ExecutionContext.Implicits.global

import akka.Done
import akka.actor.typed.ActorSystem
import akka.projection.eventsourced.EventEnvelope
import akka.projection.slick.SlickProjection
import akka.projection.slick.SlickHandler

import slick.jdbc.JdbcProfile

import org.slf4j.LoggerFactory

import io.qhquanghuy.btcbillionaire.domain.donation.{Event => DonationEvent, DonationRepository}
import io.qhquanghuy.btcbillionaire.adapter.database.{DonationDAO, DonationRow}
import io.qhquanghuy.btcbillionaire.adapter.command.DonationPersistence






final class DonationSummaryProjectionHandler[P <: JdbcProfile](dao: DonationDAO[P]) extends SlickHandler[EventEnvelope[DonationPersistence.Event]] {
  private val logger = LoggerFactory.getLogger(getClass)
  override def process(envelope: EventEnvelope[DonationPersistence.Event]): slick.dbio.DBIO[Done] = {
    logger.info(s"process ${envelope.event}")
    envelope.event match {
      case DonationEvent.Donated(donation) =>
        dao.insertAction(DonationRow(None, donation.time.toInstant.toEpochMilli(), donation.amount.value))
          .map(_ => Done)
    }
  }
}