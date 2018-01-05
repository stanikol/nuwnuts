//package daos
//
//import daos.models.Item
//import org.slf4j.LoggerFactory
//
//import scala.concurrent.Future
//
//object DAOItem {
//  import psql._
//
//  import concurrent.ExecutionContext.Implicits.global
//
//  private val log = LoggerFactory.getLogger("blog")
//
//  def listAllItems(): Future[List[Item]] = {
//    val q = quote(query[Item].filter(_.isPublished).sortBy(i => (i.category, i.sortOrder))(Ord(Ord.asc, Ord.asc)))
//    psql.run(q)
//  }
//
//}
