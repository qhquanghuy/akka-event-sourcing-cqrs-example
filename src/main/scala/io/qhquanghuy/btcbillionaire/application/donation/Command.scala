package io.qhquanghuy.btcbillionaire.application.donation

import java.time.ZonedDateTime

import akka.actor.typed.ActorRef
import akka.pattern.StatusReply

import io.qhquanghuy.btcbillionaire.domain._

sealed trait Command
object Command {
  final case class Donate(donation: Donation, replyTo: ActorRef[StatusReply[Wallet]]) extends Command
}
