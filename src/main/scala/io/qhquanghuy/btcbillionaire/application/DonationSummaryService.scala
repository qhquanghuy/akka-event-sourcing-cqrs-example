package io.qhquanghuy.btcbillionaire.application

import java.time.ZonedDateTime
import org.slf4j.LoggerFactory

final class DonationSummaryService {
  private val logger = LoggerFactory.getLogger(getClass)
  def summary(startTime: ZonedDateTime, endTime: ZonedDateTime) = {
    logger.info(startTime.toString())
    logger.info(endTime.toString())
  }
}
