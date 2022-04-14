package io.qhquanghuy.btcbillionaire.adapter.command

import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor.typed.ActorSystem
import akka.actor.typed.ActorRef
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.pattern.StatusReply

import io.qhquanghuy.btcbillionaire.port.command.CommandHandler
import io.qhquanghuy.btcbillionaire.domain.donation._
import io.qhquanghuy.btcbillionaire.domain._


final class DonationCommandHandler(system: ActorSystem[_]) extends CommandHandler[DonationCommand] {
  private val sharding = ClusterSharding(system)
  def handle[Out](command: DonationCommand[Out]): Future[Out] = {
    val ref = sharding.entityRefFor(DonationPersistence.EntityKey, Wallet.Address)
    command match {
      case DonationCommand.Donate(donation) =>
        ref.askWithStatus[Wallet](replyTo => Command.Donate(donation, replyTo))(5.seconds)
    }

  }

}
