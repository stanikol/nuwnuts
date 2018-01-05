package daos.models

case class Item(
  id: Long,
  categoryId: Long,
  title: Option[String],
  img: Option[String],
  html: Option[String],
  price: Option[BigDecimal],
  isPublished: Boolean,
  sortOrder: Option[String],
  keywords: Option[List[String]]
)
