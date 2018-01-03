package usr_storage.model

import com.mohiva.play.silhouette.api.LoginInfo

case class Credential(
  loginInfoDb: LoginInfoDb,
  passwordInfoDb: PasswordInfoDb
)
