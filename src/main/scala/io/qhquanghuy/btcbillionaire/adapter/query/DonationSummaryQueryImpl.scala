package io.qhquanghuy.btcbillionaire.adapter.query

import java.time._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import slick.jdbc.PostgresProfile.api._

import io.qhquanghuy.btcbillionaire.port.query.DonationSummaryQuery
import io.qhquanghuy.btcbillionaire.domain.donation.Donation
import io.qhquanghuy.btcbillionaire.domain.BTC
import io.qhquanghuy.btcbillionaire.adapter.database.DonationDAO


final class DonationSummaryQueryImpl(dao: DonationDAO) extends DonationSummaryQuery {
  override def findByLeftCloseRightOpen(startTime: Instant, endTime: Instant): Future[Seq[Donation]] = {
    dao.config.db.run {
      dao.table
        .filter(row => (row.datetime >= startTime) && (row.datetime < endTime))
        .result
    }
    .map(_.map {row =>
      //unsafe get, data from db must be well format
      Donation(ZonedDateTime.ofInstant(row.datetime, ZoneId.of("UTC")), BTC.mk(row.amount).toOption.get)
    })
  }
}
