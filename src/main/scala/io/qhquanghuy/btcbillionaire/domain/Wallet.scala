package io.qhquanghuy.btcbillionaire.domain

final case class Wallet(address: String, balance: BTC) {
  def add(btc: BTC) = copy(balance = balance.add(btc))
}
object Wallet {
  val Address = "0xE10b158870c029A98836fC38d8AE5c1B8618844a"
}