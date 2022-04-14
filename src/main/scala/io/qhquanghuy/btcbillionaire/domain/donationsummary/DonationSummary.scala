package io.qhquanghuy.btcbillionaire.domain.donationsummary

import java.time.ZonedDateTime

import io.qhquanghuy.btcbillionaire.domain.donation.Donation
import io.qhquanghuy.btcbillionaire.utils.datetime._

final case class DonationSummary private (donations: Seq[Donation]) {
  private def copy() = ???
}

object DonationSummary {
  def mk(donations: Seq[Donation]) = {
    DonationSummary {
      donations
        .groupMapReduce(donation => beginningOfHour(donation.time))(_.amount)((a, b) => a.add(b))
        .map {
          case (datetime, amount) => Donation(datetime, amount)
        }
        .toSeq
        .sortBy(_.time)(implicitly[Ordering[ZonedDateTime]].reverse)
    }
  }
}
