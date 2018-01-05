
package daos

import daos.models.{ Item, ItemInfo }
import org.slf4j.LoggerFactory

import scala.concurrent.Future

object DAOItemInfo {
  import psql._

  import concurrent.ExecutionContext.Implicits.global

  private val log = LoggerFactory.getLogger("blog")

  def listAllItemInfo(): Future[List[ItemInfo]] = {
    val q = quote(query[ItemInfo].filter(_.isPublished).sortBy { i =>
      (i.categorySortOrder, i.sortOrder)
    })
    psql.run(q)
  }

  def getItemInfo(itemId: Long): Future[Option[ItemInfo]] = {
    val q = quote(query[ItemInfo].filter(_.id == lift(itemId)))
    psql.run(q).map(_.headOption)
  }

}
