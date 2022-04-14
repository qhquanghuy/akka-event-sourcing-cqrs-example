package io.qhquanghuy.btcbillionaire.port.query

import java.time.Instant
import scala.concurrent.Future

import io.qhquanghuy.btcbillionaire.domain.donation.Donation

trait DonationSummaryQuery {
  // startTime <= datetime < endTime
  def findByLeftCloseRightOpen(startTime: Instant, endTime: Instant): Future[Seq[Donation]]
}
