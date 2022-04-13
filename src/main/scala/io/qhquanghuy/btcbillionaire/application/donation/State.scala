package io.qhquanghuy.btcbillionaire.application.donation

import io.qhquanghuy.btcbillionaire.domain._

final case class State(wallet: Wallet) {
  def add(btc: BTC) = State(wallet.add(btc))
}
object State {
  def empty(address: String): State = State(Wallet(address, BTC.zero))
}