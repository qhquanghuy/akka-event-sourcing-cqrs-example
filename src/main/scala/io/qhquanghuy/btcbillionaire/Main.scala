package io.qhquanghuy.btcbillionaire

import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.reflect.ClassTag

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.management.scaladsl.AkkaManagement
import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.actor.typed.scaladsl.Behaviors

import slick.jdbc.JdbcBackend._
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

import com.softwaremill.macwire._

import io.qhquanghuy.btcbillionaire.adapter.command.DonationPersistence
import io.qhquanghuy.btcbillionaire.adapter.projection.DonationSummaryProjection
import slick.jdbc.PostgresProfile







object Main {
  //#start-http-server
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    import system.executionContext

    val btcbillionaireInterface = system.settings.config.getString("btc-billionaire.http.interface")
    val btcbillionairePort = system.settings.config.getInt("btc-billionaire.http.port")

    val futureBinding = Http()
      .newServerAt(btcbillionaireInterface, btcbillionairePort)
      .bind(routes)
      .map(_.addToCoordinatedShutdown(3.seconds))
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }


  def init(system: ActorSystem[_]) = {

    AkkaManagement(system).start()
    ClusterBootstrap(system).start()

    DonationPersistence.init(system)
  }

  def main(args: Array[String]): Unit = {

    lazy val system = ActorSystem[Nothing](Behaviors.empty, "BTCBillionaire")
    lazy val dbConfig: DatabaseConfig[PostgresProfile] = DatabaseConfig.forConfig("akka.projection.slick")

    val module = new Module {
      def actorSystem: ActorSystem[_] = system
      def databaseConfig: DatabaseConfig[PostgresProfile] = dbConfig
    }

    init(system)
    startHttpServer(module.routes)(system)
    DonationSummaryProjection.init(system, dbConfig)
  }
}
