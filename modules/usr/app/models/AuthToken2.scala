package models

import java.time.LocalDateTime
import java.util.UUID

case class AuthToken2(
  id: UUID,
  userID: UUID,
  //    expiry: DateTime)
  expiry: LocalDateTime)

