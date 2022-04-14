package io.qhquanghuy.btcbillionaire.domain.donation

import java.time.ZonedDateTime

import io.qhquanghuy.btcbillionaire.domain.Wallet


sealed trait DonationCommand[Out]
object DonationCommand {
  final case class Donate(donation: Donation) extends DonationCommand[Wallet]
}