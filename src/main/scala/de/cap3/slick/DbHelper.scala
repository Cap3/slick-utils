package de.cap3.slick

import play.api.Logger
import slick.dbio.{DBIOAction, NoStream}
import slick.jdbc.JdbcBackend

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


object DBHelper {

  /**
    * Run the given query and check if the expected number of rows was updated.
    *
    * @param db               database to query
    * @param query            query to execute
    * @param expectedRowCount the expected number of rows
    * @return Success(()) when expected number matches updated rows count, else Failure
    */
  def runQueryCheckUpdatedRows[R](db: JdbcBackend#DatabaseDef,
                                  query: DBIOAction[R, NoStream, Nothing],
                                  expectedRowCount: Int = 1): Future[Try[Unit]] = {
    db.run(query) map {
      case effectedRows if effectedRows == expectedRowCount => Success(())
      case _ => Failure(new RuntimeException("Updating user data effected 0 rows."))
    } recover {
      case ex: Throwable =>
        Logger.error("Error occured while running db querty", ex)
        Failure(ex)
    }
  }
}
