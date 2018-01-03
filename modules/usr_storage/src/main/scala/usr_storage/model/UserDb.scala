package usr_storage.model

import java.util.UUID

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import io.getquill._

case class UserDb(
  userId: UUID,
  loginInfo: LoginInfoDb,
  firstName: Option[String],
  lastName: Option[String],
  fullName: Option[String],
  email: Option[String],
  avatarUrl: Option[String],
  activated: Boolean,
  roles: Option[List[String]]
)

object UserDb {
  implicit val encodeUUID = MappedEncoding[UUID, String](_.toString)

  implicit val decodeUUID = MappedEncoding[String, UUID](UUID.fromString(_))
}

