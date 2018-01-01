package daos.model

import com.mohiva.play.silhouette.api.util.PasswordInfo
import io.getquill.Embedded

case class PasswordInfoDb(
  hasher: String,
  password: String,
  salt: Option[String] = None
) extends Embedded

object PasswordInfoDb {
  def apply(passwordInfo: PasswordInfo): PasswordInfoDb =
    new PasswordInfoDb(passwordInfo.hasher, passwordInfo.password, passwordInfo.salt)
}