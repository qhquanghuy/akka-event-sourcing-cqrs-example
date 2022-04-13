package io.qhquanghuy.btcbillionaire.domain

final case class BTC private (value: BigDecimal) {
  private def copy() = ???

  def add(btc: BTC) = BTC(value + btc.value)
}

object BTC {
  sealed trait Error {
    def msg: String
  }
  object Error {
    final object NegativeAmount extends Error {
      override def msg: String = "Negative amount is invalid"
    }
  }

  def mk(amount: BigDecimal): Either[BTCCreationError, BTC] = {
    if (amount.signum == -1) Left(BTCCreationError.NegativeAmount)
    else Right(BTC(amount))
  }

  def zero = BTC(BigDecimal(0))
}

sealed trait BTCCreationError extends DomainError
object BTCCreationError {
  final object NegativeAmount extends BTCCreationError {
    override def msg: String = "Negative amount is invalid"
  }
}
