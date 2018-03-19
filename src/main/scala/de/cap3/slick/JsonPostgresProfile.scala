package de.cap3.slick

import com.github.tminglei.slickpg.{ExPostgresProfile, PgPlayJsonSupport}
import slick.basic.Capability
import slick.jdbc.JdbcCapabilities

trait JsonPostgresProfile extends ExPostgresProfile with PgPlayJsonSupport {
  def pgjson = "jsonb"

  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  object JsonAPI extends API with JsonImplicits

  override val api = JsonAPI
}

object JsonPostgresProfile extends JsonPostgresProfile
