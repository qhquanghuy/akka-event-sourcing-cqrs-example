package io.qhquanghuy.btcbillionaire.utils.models

final case class ErrorResponse(code: String, message: String)
final case class DataResponse[T](data: T)