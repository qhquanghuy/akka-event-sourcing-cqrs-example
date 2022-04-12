package io.qhquanghuy.btcbillionaire.application
import scala.concurrent.Future

import spray.json.JsValue
import org.slf4j.LoggerFactory

final class DonationService {
  private val logger = LoggerFactory.getLogger(getClass)
  def donate(json: JsValue) = {
    logger.info(json.toString())
    Future.successful(json)
  }
}
