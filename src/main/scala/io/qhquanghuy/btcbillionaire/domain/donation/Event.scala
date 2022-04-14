package io.qhquanghuy.btcbillionaire.domain.donation

import io.qhquanghuy.btcbillionaire.domain.donation.Donation
import io.qhquanghuy.btcbillionaire.domain.CborSerializable

sealed trait Event extends CborSerializable
object Event {
  final case class Donated(donation: Donation) extends Event
}
