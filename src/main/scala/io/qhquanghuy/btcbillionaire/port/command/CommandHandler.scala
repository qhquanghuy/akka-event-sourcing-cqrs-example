package io.qhquanghuy.btcbillionaire.port.command

import scala.concurrent.Future

trait CommandHandler[C[_]] {
  def handle[Out](command: C[Out]): Future[Out]
}
