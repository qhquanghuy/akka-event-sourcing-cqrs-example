package io.qhquanghuy.btcbillionaire.application.donation

import java.time.ZonedDateTime
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

import io.qhquanghuy.btcbillionaire.application.donation.DonationService
import io.qhquanghuy.btcbillionaire.CreateTableTestUtils
import io.qhquanghuy.btcbillionaire.Main
import io.qhquanghuy.btcbillionaire.domain.donation._
import io.qhquanghuy.btcbillionaire.domain.BTCCreationError
import io.qhquanghuy.btcbillionaire.adapter.command.DonationCommandHandler
import io.qhquanghuy.btcbillionaire.port.command.CommandHandler
import io.qhquanghuy.btcbillionaire.domain.donation.DonationCommand

class DonationServiceTests extends AnyWordSpec
    with Matchers
    with BeforeAndAfterAll
    with ScalaFutures
    with Eventually {

  lazy val testKit = ActorTestKit("DonationServiceTests")
  override protected def beforeAll(): Unit = {
    super.beforeAll()
    CreateTableTestUtils.dropAndRecreateTables(testKit.system)
    Main.init(testKit.system)
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
    testKit.shutdownTestKit()
  }

  lazy val donationCommanndHandler: CommandHandler[DonationCommand] = new DonationCommandHandler(testKit.system)
  lazy val donationService = new DonationService(donationCommanndHandler)

  implicit val timeoutAfter10 = timeout(10.seconds)

  "DonationService" should {
    "successfully persist concurrently 101 events and update current balance if amount is positive" in {

      val amounts = (0 to 100)
        .map(_ => scala.util.Random.between(0.0, Double.MaxValue))
        .map(value => BigDecimal(value))
      val result = Future.sequence {
        amounts
          .map(amount => donationService.donate(DonationDTO(ZonedDateTime.now(), amount)).value)
      }
      .futureValue(timeout(10.minutes))


      val sum = amounts.sum

      result.forall(_.isRight) should be (true)
      result.last.toOption.get.balance.value should === (sum)
    }

    "failed persist event and update current balance if amount is negative" in {
      val result = donationService.donate(DonationDTO(ZonedDateTime.now(), BigDecimal(-100)))
        .value
        .futureValue(timeoutAfter10)
      result.isLeft should be (true)
      result.left.toOption.get should be (BTCCreationError.NegativeAmount)
    }
  }



}
