import com.typesafe.config.ConfigFactory
import io.getquill.{ PostgresAsyncContext, SnakeCase }

package object usr_storage {

  private val config = ConfigFactory.load()

  val psql: PostgresAsyncContext[SnakeCase.type] = new PostgresAsyncContext(SnakeCase, config.getConfig("daos"))

}
