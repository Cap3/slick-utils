package de.cap3.slick

import slick.codegen.SourceCodeGenerator
import slick.sql.SqlProfile.ColumnOption
import slick.{model => m}

class JsonSourceSourceCodeGenerator(model: m.Model) extends SourceCodeGenerator(model) {

  override def Table = new Table(_) {
    table =>

    private val eventualKeyType = columns.find(_.model.name == "id").map(_.model.tpe)

    override def EntityType = new EntityTypeDef {

      override def parents: Seq[String] = super.parents ++ eventualKeyType.map(tpe => s"Entity[$name,$tpe]").toList

      override def code: String =
        super.code + eventualKeyType.map(tpe => s"  { override def withId(id: $tpe): UsersRow = copy(id = Some(id)) }").getOrElse("")
    }

    override def TableClass = new TableClassDef {

      override def parents: Seq[String] = super.parents ++ eventualKeyType.toList.map(tpe => s"Keyed[$tpe]")

      override def code: String =
        super.code + repositoryCode

      def repositoryCode: String =
        eventualKeyType.map { keyType =>
          s"""
             |class ${name}Repository extends Repository[$elementType, String](profile) {
             |  import profile.api._
             |  val pkType = implicitly[BaseTypedType[String]]
             |  val tableQuery = $name
             |  type TableType = $name
             |}
        """.stripMargin
        }.getOrElse("")

    }

    override def Column = new Column(_) {
      column =>

      override def asOption: Boolean = super.asOption || column.name == "id"

      override def rawType: String = model.tpe match {
        case "String" => model.options.find(_.isInstanceOf[ColumnOption.SqlType]).map(_.asInstanceOf[ColumnOption.SqlType].typeName).map({
          case "jsonb" => "JsValue"
          case _ => "String"
        }).getOrElse("String")
        case _ => super.rawType
      }
    }
  }

  // ensure to use our customized postgres driver at `import profile.simple._`
  override def packageCode(profile: String, pkg: String, container: String, parentType: Option[String]): String = {
    s"""
package ${pkg}
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object ${container} extends {
  val profile = ${profile}
} with ${container}
/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait ${container}${parentType.map(t => s" extends $t").getOrElse("")} {
  val profile: $profile
  import profile.api._
  import com.byteslounge.slickrepo.meta.{Entity, Keyed}
  import com.byteslounge.slickrepo.repository.Repository
  import slick.ast.BaseTypedType

  ${indent(code)}
}
      """.trim()
  }
}


