package daos.model

case class PasswordInfoDb(
  hasher: String,
  password: String,
  salt: Option[String] = None
)
