package io.qhquanghuy.btcbillionaire.adapter.command

import scala.concurrent.duration._

import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.actor.typed.{Behavior, SupervisorStrategy}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl._
import akka.pattern.StatusReply
import akka.actor.typed.ActorSystem
import akka.actor.typed.ActorRef

import akka.cluster.sharding.typed.scaladsl.{EntityContext, ClusterSharding, Entity}

import io.qhquanghuy.btcbillionaire.domain.Wallet
import io.qhquanghuy.btcbillionaire.domain.donation.{Event => DonationEvent, Donation}
import org.slf4j.LoggerFactory


sealed trait Command
object Command {
  final case class Donate(donation: Donation, replyTo: ActorRef[StatusReply[Wallet]]) extends Command
}
object DonationPersistence {
  val EntityKey: EntityTypeKey[Command] = EntityTypeKey[Command]("DonationCommand")
  val Tag = "Donation"

  type Event = DonationEvent

  val logger = LoggerFactory.getLogger(this.getClass())

  private def handleCommand(walletAddress: String, state: State, command: Command): ReplyEffect[Event, State] = {
    command match {
      case Command.Donate(donation, replyTo) =>
        logger.info(s"handleCommand ${donation}")
        Effect.persist(DonationEvent.Donated(donation))
          .thenReply(replyTo)(updatedState => StatusReply.success(updatedState.wallet))
    }
  }

  private def handleEvent(state: State, event: Event): State = {
    logger.info(s"handleEvent ${event}")
    event match {
      case DonationEvent.Donated(donation) => state.add(donation.amount)
    }
  }

  def apply(walletAddress: String): Behavior[Command] = {
    EventSourcedBehavior
      .withEnforcedReplies[Command, Event, State](
        persistenceId = PersistenceId(EntityKey.name, walletAddress),
        emptyState = State.empty(walletAddress),
        commandHandler = handleCommand(walletAddress, _, _),
        eventHandler = handleEvent
      )
      .withTagger(_ => Set(Tag))
      .withRetention(
        RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 3)
      )
      .onPersistFailure(
        SupervisorStrategy.restartWithBackoff(200.millis, 5.seconds, 0.1)
      )
  }


  def init(system: ActorSystem[_]): Unit = {
    val behaviorFactory: EntityContext[Command] => Behavior[Command] = {
      entityContext =>
        DonationPersistence(entityContext.entityId)
    }
    ClusterSharding(system).init(Entity(EntityKey)(behaviorFactory))
  }

}

