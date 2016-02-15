package dao

import scala.concurrent.Future

import javax.inject.Inject
import models.Dog
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.db.NamedDatabase
import slick.driver.JdbcProfile

class DogDAO @Inject()(@NamedDatabase("mydb") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {


	import driver.api._

	private val Dogs = TableQuery[DogsTable]

	def all(): Future[Seq[Dog]] = db.run(Dogs.result)

	def insert(dog: Dog): Future[Unit] = db.run(Dogs += dog).map { _ => () }

	private class DogsTable(tag: Tag) extends Table[Dog](tag, "dog") {

		def name = column[String]("name", O.PrimaryKey)
		def color = column[String]("color")
		def * = (name, color) <> (Dog.tupled, Dog.unapply _)

	}


}