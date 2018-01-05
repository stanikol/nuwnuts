package daos.models

case class ItemInfo(
  id: Long,
  categoryId: Long,
  title: Option[String],
  img: Option[String],
  html: Option[String],
  price: Option[BigDecimal],
  isPublished: Boolean,
  sortOrder: Option[String],
  category: String,
  categorySortOrder: Option[String]
)
