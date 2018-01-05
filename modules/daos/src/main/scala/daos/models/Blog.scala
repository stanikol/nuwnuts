package daos.models

import java.time.LocalDateTime

case class Blog(
  blogId: Long,
  filename: Option[String],
  title: String,
  html: String,
  shortText: String,
  keywords: List[String],
  sortOrder: String,
  lastUpdate: LocalDateTime,
  created: LocalDateTime,
  isPublished: Boolean
)

object Blog {
  def empty = Blog(0, None, "", "", "", List.empty, "", LocalDateTime.now(), LocalDateTime.now(), false)
}