package io.qhquanghuy.btcbillionaire.application.donation

import io.qhquanghuy.btcbillionaire.domain.Donation
import io.qhquanghuy.btcbillionaire.utils.CborSerializable
sealed trait Event extends CborSerializable
object Event {
  final case class Donated(donation: Donation) extends Event
}
