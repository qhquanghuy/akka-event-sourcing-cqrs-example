package io.qhquanghuy.btcbillionaire.adapter.command

import io.qhquanghuy.btcbillionaire.domain._
import io.qhquanghuy.btcbillionaire.utils.CborSerializable

final case class State(wallet: Wallet) extends CborSerializable {
  def add(btc: BTC) = State(wallet.add(btc))
}
object State {
  def empty(address: String): State = State(Wallet(address, BTC.zero))
}