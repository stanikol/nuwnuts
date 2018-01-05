package daos

import daos.models.Blog
import org.slf4j.LoggerFactory

import scala.concurrent.Future

object DAOBlog {
  import psql._

  import concurrent.ExecutionContext.Implicits.global

  private val log = LoggerFactory.getLogger("blog")

  def listAllArticles(): Future[List[Blog]] = {
    val q = quote {
      query[Blog].filter(_.isPublished)
        .sortBy(b => (b.sortOrder, b.blogId))(Ord(Ord.asc, Ord.desc))
    }
    psql.run(q)
  }

  def getBlogPost(blogId: Long): Future[Option[Blog]] = {
    val q = quote(query[Blog].filter(_.blogId == lift(blogId)))
    psql.run(q).map(_.headOption)
  }

}
