package io.qhquanghuy.btcbillionaire

import scala.util.Failure
import scala.util.Success
import scala.concurrent.duration._

import akka.actor.typed.ActorSystem

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import akka.management.scaladsl.AkkaManagement
import akka.management.cluster.bootstrap.ClusterBootstrap
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.ConfigFactory


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
  //#start-http-server
  def main(args: Array[String]): Unit = {

    lazy val system = ActorSystem[Nothing](Behaviors.empty, "BTCBillionaire")
    val module = new Module()
    module.routes

    AkkaManagement(system).start()
    ClusterBootstrap(system).start()


    startHttpServer(module.routes)(system)
  }
}
