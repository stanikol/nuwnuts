package usr_storage.model

import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

import io.getquill.context.async.SqlTypes
import org.joda.time.DateTime
import io.getquill._

case class AuthTokenDb(
  id: UUID,
  userId: UUID,
  //  expiry: DateTime
  expiry: LocalDateTime
)

object AuthTokenDb {

  //  implicit val encodeUUID = MappedEncoding[UUID, String](_.toString)
  //
  //  implicit val decodeUUID = MappedEncoding[String, UUID](UUID.fromString(_))

  //  implicit val decodeDateTime = MappedEncoding[Date, DateTime](new DateTime(_))
  //  implicit val encodeDateTime = MappedEncoding[DateTime, Date](_.toDate)

  //  implicit val decodeDateTime = MappedEncoding[String, DateTime](_ => DateTime.now())
  //  implicit val encodeDateTime = MappedEncoding[DateTime, String](_ => "2019-01-01T03:12:50.871+02:00")

  //  implicit val jodaDateTimeDecoder: Decoder[org.joda.time.DateTime] = decoder[DateTime]({
  //    case dateTime: DateTime => dateTime
  //  }, SqlTypes.TIMESTAMP)
  //
  //  implicit val jodaDateTimeEncoder: Encoder[org.joda.time.DateTime] = encoder[DateTime](SqlTypes.TIMESTAMP)
}
