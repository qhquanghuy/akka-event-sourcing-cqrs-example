package io.qhquanghuy.btcbillionaire.adapter.projection

import scala.reflect.ClassTag
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.typed.ActorSystem
import akka.Done
import akka.projection.eventsourced.EventEnvelope
import akka.projection.slick.SlickProjection
import akka.projection.slick.SlickHandler

import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._

import org.slf4j.LoggerFactory

import io.qhquanghuy.btcbillionaire.domain.donation.{Event => DonationEvent, DonationRepository}
import io.qhquanghuy.btcbillionaire.adapter.database.{DonationDAO, DonationRow}
import io.qhquanghuy.btcbillionaire.utils.CborSerialize
import io.qhquanghuy.btcbillionaire.adapter.command.DonationPersistence






final class DonationSummaryProjectionHandler(dao: DonationDAO) extends SlickHandler[EventEnvelope[DonationPersistence.Event]] {

private val logger = LoggerFactory.getLogger(getClass)
  override def process(envelope: EventEnvelope[DonationPersistence.Event]): DBIO[Done] = {
    logger.info(s"process ${envelope.event}")
    envelope.event.value match {
      case DonationEvent.Donated(donation) =>
        (dao.table += DonationRow(None, donation.time.toInstant(), donation.amount.value))
          .map(_ => Done)
    }
  }
}