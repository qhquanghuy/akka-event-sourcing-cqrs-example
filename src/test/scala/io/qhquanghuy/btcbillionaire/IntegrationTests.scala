package io.qhquanghuy.btcbillionaire

import java.time.ZonedDateTime
import java.time.ZoneId
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.PatienceConfiguration
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.Span
import org.scalatest.wordspec.AnyWordSpec

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import com.typesafe.config.ConfigFactory

import slick.basic.DatabaseConfig
import slick.jdbc.H2Profile

import io.qhquanghuy.btcbillionaire.Main
import io.qhquanghuy.btcbillionaire.domain.donation._
import io.qhquanghuy.btcbillionaire.application.donation.DonationService
import io.qhquanghuy.btcbillionaire.domain.BTCCreationError
import io.qhquanghuy.btcbillionaire.adapter.command.DonationCommandHandler
import io.qhquanghuy.btcbillionaire.port.command.CommandHandler
import io.qhquanghuy.btcbillionaire.domain.donation.DonationCommand
import io.qhquanghuy.btcbillionaire.utils.datetime._
import io.qhquanghuy.btcbillionaire.domain.BTC




class IntegrationTests extends AnyWordSpec
    with Matchers
    with BeforeAndAfterAll
    with ScalaFutures
    with Eventually {

  lazy val testKit = ActorTestKit("IntegrationTests")


  lazy val h2Config: DatabaseConfig[H2Profile] = DatabaseConfig.forConfig("akka.projection.slick", ConfigFactory.load("application-test.conf"))
  lazy val appModule = new AppModule(testKit.system, h2Config)
  override protected def beforeAll(): Unit = {
    super.beforeAll()

    CreateTableTestUtils.dropAndRecreateTables(testKit.system, h2Config)
    Main.init(testKit.system, appModule.routes, h2Config)
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
    testKit.shutdownTestKit()
  }

  lazy val donationService = appModule.donationService

  lazy val donationSummaryService = appModule.donationSummaryService

  def randomAmout(n: Int) = {
    (0 to n - 1)
      .map(_ => scala.util.Random.between(0.0, 9999999999.0))
      .map(value => BigDecimal(value).setScale(8, BigDecimal.RoundingMode.HALF_DOWN))
  }

  lazy val timeSeed = ZonedDateTime.parse("2022-04-13T14:20:22.401643+07:00")
  lazy val hourSeeds = Seq(1, 2, 3, 4)
  lazy val sample100DTOS = {
    hourSeeds.map { hour =>
      val h = timeSeed.withHour(hour)
      val datetimes = (1 to 25).map(_ => h.withMinute(scala.util.Random.between(0, 59)))
      val dtos = datetimes.zip(randomAmout(25)).map {
        case (datetime, amount) => DonationDTO(datetime, amount)
      }

      beginningOfHour(h) -> dtos

    }
    .toMap
  }

  "DonationService" should {
    "successfully persist concurrently 100 events and update current balance if amount is positive" in {

      val dtos = sample100DTOS.values.flatMap(identity).toList
      val result = Future.sequence {
        dtos.take(99).map(dto => {
          donationService.donate(dto).value
        })
      }
      .flatMap(_ => donationService.donate(dtos.last).value)
      .futureValue(timeout(10.minutes))

      val sum = dtos.map(_.amount).sum

      result.isRight should be (true)
      result.toOption.get.balance.value should === (sum)
    }

    "failed persist event and update current balance if amount is negative" in {
      val result = donationService.donate(DonationDTO(ZonedDateTime.now(), BigDecimal(-100)))
        .value
        .futureValue(timeout(10.seconds))
      result.isLeft should be (true)
      result.left.toOption.get should be (BTCCreationError.NegativeAmount)
    }

  }

  "DonationSummaryService" should {
    "sync successfully from write db" in {
      println("wait 3s.....")
      Thread.sleep(3 * 1000)

      val startTime = timeSeed.withHour(hourSeeds.head)
      val endTime = timeSeed.withHour(hourSeeds.last)
      val results = donationSummaryService
        .summary(startTime, endTime)
        .futureValue(timeout(5.minutes))

      val expected = hourSeeds.dropRight(1)
        .map { hour =>
          val datetime = beginningOfHour(timeSeed.withHour(hour))
          val utcHour = ZonedDateTime.ofInstant(datetime.toInstant(), ZoneId.of("UTC"))
          Donation(utcHour, BTC.mk(sample100DTOS(datetime).map(_.amount).sum).toOption.get)
        }
      .sortBy(_.time)

      results.donations.sortBy(_.time) should === (expected)
    }
  }



}
