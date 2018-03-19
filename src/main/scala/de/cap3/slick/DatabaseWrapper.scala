package de.cap3.slick
import scala.language.implicitConversions

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend

import scala.concurrent.Future

trait DatabaseWrapper {
  val profile: JsonPostgresProfile

  def db: JdbcBackend#DatabaseDef

  implicit def executeOperation[T](databaseOperation: DBIO[T]): Future[T] = {
    db.run(databaseOperation)
  }
}

@Singleton
class DefaultDatabaseWrapper @Inject()(dbConfigProvider: DatabaseConfigProvider) extends DatabaseWrapper {
  val dbConfig = dbConfigProvider.get[JsonPostgresProfile]
  override val profile = dbConfig.profile

  override def db = dbConfig.db
}
