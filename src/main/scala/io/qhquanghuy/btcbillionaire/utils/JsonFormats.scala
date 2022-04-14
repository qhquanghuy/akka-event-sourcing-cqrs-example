package io.qhquanghuy.btcbillionaire.utils

import java.time.ZonedDateTime

import scala.util.{Try, Failure, Success}

import spray.json._
import spray.json.DefaultJsonProtocol


import io.qhquanghuy.btcbillionaire.domain._
import io.qhquanghuy.btcbillionaire.domain.donationsummary._
import io.qhquanghuy.btcbillionaire.domain.donation._
import io.qhquanghuy.btcbillionaire.utils.models._

object JsonFormats {
  import DefaultJsonProtocol._
  implicit object ZonedDateTimeFormat extends JsonFormat[ZonedDateTime] {

    override def read(json: JsValue): ZonedDateTime = json match {
      case JsString(str) =>
        Try(ZonedDateTime.parse(str)).recover {
          case throwable => deserializationError(throwable.getMessage(), throwable)
        }
        .get
      case x => deserializationError(s"String expected, got ${x}")
    }

    override def write(zonedDateTime: ZonedDateTime): JsValue = JsString(zonedDateTime.toString())
  }

  implicit object BTCFormat extends JsonFormat[BTC] {
    override def read(json: JsValue): BTC = {
      BTC.mk(json.convertTo[BigDecimal])
        .fold(
          err => deserializationError(err.msg),
          identity
        )
    }

    override def write(btc: BTC): JsValue = btc.value.toJson

  }

  implicit val walletFormat = jsonFormat2(Wallet.apply)

  implicit def dataResponsneFormat[T: JsonFormat] = jsonFormat1(DataResponse.apply[T])

  implicit val errorResponseFormat = jsonFormat2(ErrorResponse)

  implicit val donationRequestFormat = jsonFormat2(DonationDTO)
}
