package de.cap3.slick

import javax.inject.Inject

import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.util.jdbc.DriverDataSource
import play.api.inject.Module
import play.api.{Configuration, Environment, Logger, Mode}

class ModuleInitializer @Inject()(environment: Environment, configuration: Configuration) {
  getAllDatabaseNames.foreach { dbName =>

    if (environment.mode == Mode.Test && configuration.get[String](s"db.$dbName.drop_on_start").contains("DROP")) {
      val driver = configuration.get[String](s"db.$dbName.driver")
      val url = configuration.get[String](s"db.$dbName.url")
      val user = configuration.get[String](s"db.$dbName.user")
      val password = configuration.get[String](s"db.$dbName.password")

      Logger.warn(s"DELETING DATABASE $dbName")

      val flyway = new Flyway
      val dataSource = new DriverDataSource(
        getClass.getClassLoader,
        driver,
        url,
        user,
        password,
        null
      )
      flyway.setDataSource(dataSource)
      flyway.clean()
    }
  }


  private def getAllDatabaseNames: Seq[String] = (for {
    config <- configuration.getOptional[Configuration]("db").toList
    dbName <- config.subKeys
  } yield {
    dbName
  }).distinct
}

class SlickutilsModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = {
    Seq(
      bind[JsonPostgresProfile].toInstance(JsonPostgresProfile),
      bind(classOf[DatabaseWrapper]).to(classOf[DefaultDatabaseWrapper]),
      bind(classOf[ModuleInitializer]).toSelf.eagerly()
    )
  }
}
