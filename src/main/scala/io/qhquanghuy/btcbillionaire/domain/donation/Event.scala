package io.qhquanghuy.btcbillionaire.domain.donation

import io.qhquanghuy.btcbillionaire.domain.donation.Donation

sealed trait Event
object Event {
  final case class Donated(donation: Donation) extends Event
}
