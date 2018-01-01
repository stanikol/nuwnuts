package daos

import java.time.LocalDateTime
import java.util.UUID

import daos.model.AuthTokenDb

//import daos.model.AuthTokenDb
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TestJodaDateTime {
  def main(args: Array[String]): Unit = {
    import concurrent.ExecutionContext.Implicits.global
    import daos.psql._

    case class AuthTokenDb(
      id: UUID,
      userId: UUID,
      expiry: LocalDateTime
    )
    val a = AuthTokenDb(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now)

    val q = quote(query[AuthTokenDb].insert(lift(a)))
    println(Await.result(psql.run(q), Duration.Inf))
    val q2 = quote(query[AuthTokenDb])
    println(Await.result(psql.run(q2), Duration.Inf))
  }
}
