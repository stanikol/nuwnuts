package daos.model

import com.mohiva.play.silhouette.api.LoginInfo
import io.getquill.Embedded

case class LoginInfoDb(
  providerId: String,
  providerKey: String
) extends Embedded

object LoginInfoDb {
  def apply(loginInfo: LoginInfo): LoginInfoDb = new LoginInfoDb(loginInfo.providerID, loginInfo.providerKey)
}
