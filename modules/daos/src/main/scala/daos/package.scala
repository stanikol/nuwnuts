import com.typesafe.config.ConfigFactory
import io.getquill.{ PostgresAsyncContext, SnakeCase }

package object daos {
  private val config = ConfigFactory.load()

  val psql: PostgresAsyncContext[SnakeCase.type] = new PostgresAsyncContext(SnakeCase, config.getConfig("daos"))

}
