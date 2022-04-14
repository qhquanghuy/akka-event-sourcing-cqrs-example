package io.qhquanghuy.btcbillionaire.adapter.projection

import scala.reflect.ClassTag

import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.ShardedDaemonProcessSettings
import akka.cluster.sharding.typed.scaladsl.ShardedDaemonProcess
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.Offset
import akka.projection.eventsourced.EventEnvelope
import akka.projection.eventsourced.scaladsl.EventSourcedProvider
import akka.projection.scaladsl.{ ExactlyOnceProjection, SourceProvider }
import akka.projection.{ ProjectionBehavior, ProjectionId }
import akka.projection.slick.SlickProjection

import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import io.qhquanghuy.btcbillionaire.domain.donation.{Event => DonationEvent}
import io.qhquanghuy.btcbillionaire.adapter.command.DonationPersistence
import io.qhquanghuy.btcbillionaire.adapter.database.DonationDAO




object DonationSummaryProjection {

  private def createProjection(
    system: ActorSystem[_],
    databaseConfig: DatabaseConfig[PostgresProfile]
  ): ExactlyOnceProjection[Offset, EventEnvelope[DonationPersistence.Event]] = {

    val sourceProvider : SourceProvider[Offset, EventEnvelope[DonationPersistence.Event]] =
      EventSourcedProvider.eventsByTag[DonationPersistence.Event](
        system = system,
        readJournalPluginId = JdbcReadJournal.Identifier,
        tag = DonationPersistence.Tag)

    implicit val _system = system

    SlickProjection.exactlyOnce(
      projectionId = ProjectionId("DonationSummaryProjection", DonationPersistence.Tag),
      sourceProvider = sourceProvider,
      databaseConfig = databaseConfig,
      handler = () => new DonationSummaryProjectionHandler(new DonationDAO(databaseConfig))
    )
  }


  def init(system: ActorSystem[_], databaseConfig: DatabaseConfig[PostgresProfile]): Unit = {
    ShardedDaemonProcess(system).init(
      name = "DonationSummaryProjection",
      numberOfInstances = 1,
      _ => ProjectionBehavior(createProjection(system, databaseConfig)),
      ShardedDaemonProcessSettings(system),
      Some(ProjectionBehavior.Stop)
    )
  }

}
