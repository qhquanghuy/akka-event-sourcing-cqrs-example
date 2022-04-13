package io.qhquanghuy.btcbillionaire.application.donation

import scala.concurrent.duration._

import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.actor.typed.{Behavior, SupervisorStrategy}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl._
import akka.pattern.StatusReply
import akka.actor.typed.ActorSystem

import io.qhquanghuy.btcbillionaire.application.donation.Command.Donate
import io.qhquanghuy.btcbillionaire.application.donation.Event.Donated
import akka.cluster.sharding.typed.scaladsl.EntityContext
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.scaladsl.Entity

object DonationCommand {
  val EntityKey: EntityTypeKey[Command] = EntityTypeKey[Command]("DonationCommand")
  val Tag = "DonationCommand"

  private def handleCommand(walletAddress: String, state: State, command: Command): ReplyEffect[Event, State] = {
    command match {
      case Donate(donation, replyTo) =>
        Effect.persist(Event.Donated(donation))
          .thenReply(replyTo)(updatedState => StatusReply.success(updatedState.wallet))
    }
  }

  private def handleEvent(state: State, event: Event): State = {
    event match {
      case Donated(donation) => state.add(donation.amount)
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
        DonationCommand(entityContext.entityId)
    }
    ClusterSharding(system).init(Entity(EntityKey)(behaviorFactory))
  }

}

