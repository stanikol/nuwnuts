package daos.models

import java.time.LocalDateTime

case class Blog(
  blogId: Long,
  filename: Option[String],
  title: String,
  html: String,
  keywords: List[String],
  sortOrder: String,
  lastUpdate: LocalDateTime,
  display: Boolean
)

object Blog {
  def empty = Blog(0, None, "", "", List.empty, "", LocalDateTime.now(), false)
}