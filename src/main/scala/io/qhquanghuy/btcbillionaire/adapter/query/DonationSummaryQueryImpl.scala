package io.qhquanghuy.btcbillionaire.adapter.query

import java.time._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import slick.jdbc.JdbcProfile

import io.qhquanghuy.btcbillionaire.port.query.DonationSummaryQuery
import io.qhquanghuy.btcbillionaire.domain.donation.Donation
import io.qhquanghuy.btcbillionaire.domain.BTC
import io.qhquanghuy.btcbillionaire.adapter.database.DonationDAO


final class DonationSummaryQueryImpl[P <: JdbcProfile](dao: DonationDAO[P]) extends DonationSummaryQuery {
  override def findByLeftCloseRightOpen(startTime: Instant, endTime: Instant): Future[Seq[Donation]] = {
    dao.findByLeftCloseRightOpen(startTime, endTime)
    .map(_.map { row =>
      //unsafe get, data from db must be well format
      Donation(ZonedDateTime.ofInstant(Instant.ofEpochMilli(row.datetime), ZoneId.of("UTC")), BTC.mk(row.amount).toOption.get)
    })
  }
}
