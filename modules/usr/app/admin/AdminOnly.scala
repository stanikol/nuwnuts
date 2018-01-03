package admin

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User
import play.api.mvc.Request

import scala.concurrent.Future

case class AdminOnly() extends Authorization[User, CookieAuthenticator] {

  def isAuthorized[B](user: User, authenticator: CookieAuthenticator)(implicit request: Request[B]) = {
    Future.successful(user.roles.map(_.contains("admin")).getOrElse(false))
    //    Future.successful(true)
  }

}
