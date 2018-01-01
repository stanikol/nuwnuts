package daos.model

import io.getquill.Embedded

case class LoginInfoDb(
  providerId: String,
  providerKey: String
) extends Embedded
