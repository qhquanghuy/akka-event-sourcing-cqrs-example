package io.qhquanghuy.btcbillionaire.application.donationsummary

import java.time.ZonedDateTime

import scala.concurrent.ExecutionContext.Implicits.global

import org.slf4j.LoggerFactory

import io.qhquanghuy.btcbillionaire.domain.donationsummary._
import io.qhquanghuy.btcbillionaire.port.query.DonationSummaryQuery
import io.qhquanghuy.btcbillionaire.utils.datetime._

final class DonationSummaryService(repo: DonationSummaryQuery) {
  private val logger = LoggerFactory.getLogger(getClass)


  def summary(startTime: ZonedDateTime, endTime: ZonedDateTime) = {

    logger.info(s"summary for ${startTime} <= datetime < ${endTime}")

    repo.findByLeftCloseRightOpen(
      beginningOfHour(startTime).toInstant(),
      beginningOfHour(endTime).toInstant()
    )
    .map(DonationSummary.mk)

  }
}
