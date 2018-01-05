package daos.models

case class ItemCategory(
  id: Long,
  category: String,
  sortOrder: Option[String]
)
