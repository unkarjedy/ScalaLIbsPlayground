package _cats

import cats.implicits._
import org.scalatest.FunSuite
import org.scalatest.Matchers._

class EitherTest extends FunSuite {

  test("problem") {
    lazy val throwsSomeStuff: Int => Double = ???

    lazy val throwsOtherThings: Double => String = ???

    lazy val moreThrowing: String => List[Char] = ???

    // in Java world hard to compose, try of try of try...
    lazy val magic = throwsSomeStuff.andThen(throwsOtherThings).andThen(moreThrowing)
  }

  test("left & right") {
    val right: Either[String, Int] = Either.right(5)
    right.map(_ + 1) should be(Right(6))

    val left: Either[String, Int] = Either.left("Something went wrong")
    left.map(_ + 1) should be(Left("Something went wrong"))
  }

  test("monad") {
    val right: Either[String, Int] = Either.right(5)
    right.flatMap(x ⇒ Either.right(x + 1)) should be(Right(6))

    val left: Either[String, Int] = Either.left("Something went wrong")
    left.flatMap(x ⇒ Either.right(x + 1)) should be(Left("Something went wrong"))
  }

  test("exception style") {
    def parse(s: String): Int =
      if (s.matches("-?[0-9]+")) s.toInt
      else throw new NumberFormatException(s"$s is not a valid integer.")

    def reciprocal(i: Int): Double =
      if (i == 0) throw new IllegalArgumentException("Cannot take reciprocal of 0.")
      else 1.0 / i

    def stringify(d: Double): String = d.toString
  }

  test("either style") {
    def parse(s: String): Either[NumberFormatException, Int] =
      if (s.matches("-?[0-9]+")) Either.right(s.toInt)
      else Either.left(new NumberFormatException(s"${s} is not a valid integer."))

    def reciprocal(i: Int): Either[IllegalArgumentException, Double] =
      if (i == 0) Either.left(new IllegalArgumentException("Cannot take reciprocal of 0."))
      else Either.right(1.0 / i)

    def stringify(d: Double): String = d.toString

    def magic(s: String): Either[Exception, String] =
      parse(s).flatMap(reciprocal).map(stringify)

    def magicForComr(s: String): Either[Exception, String] =
      for {
        p <- parse(s)
        r <- reciprocal(p)
      } yield stringify(r)

    parse("Not a number").isRight should be(false)
    parse("2").isRight should be(true)

    magic("0").isRight should be(false)
    magic("1").isRight should be(true)
    magic("Not a number").isRight should be(false)

    magicForComr("0").isRight should be(false)
    magicForComr("1").isRight should be(true)
    magicForComr("Not a number").isRight should be(false)

    val result = magic("2") match {
      case Left(_: NumberFormatException) ⇒ "Not a number!"
      case Left(_: IllegalArgumentException) ⇒ "Can't take reciprocal of 0!"
      case Left(_) ⇒ "Unknown error"
      case Right(res) ⇒ s"Got reciprocal: $res"
    }
    result should be("Got reciprocal: 0.5")
  }

  test("either style with adts") {
    sealed abstract class Error
    final case class NotANumber(string: String) extends Error
    final case object NoZeroReciprocal extends Error

    def parse(s: String): Either[Error, Int] =
      if (s.matches("-?[0-9]+")) Either.right(s.toInt)
      else Either.left(NotANumber(s))

    def reciprocal(i: Int): Either[Error, Double] =
      if (i == 0) Either.left(NoZeroReciprocal)
      else Either.right(1.0 / i)

    def stringify(d: Double): String = d.toString

    def magic(s: String): Either[Error, String] =
      parse(s).flatMap(reciprocal).map(stringify)

    val result = magic("2") match {
      case Left(NotANumber(_)) ⇒ "Not a number!"
      case Left(NoZeroReciprocal) ⇒ "Can't take reciprocal of 0!"
      case Right(res) ⇒ s"Got reciprocal: $res"
    }
    result should be("Got reciprocal: 0.5")
  }

  test("either in the small, either in the large PROBLEM") {
    sealed abstract class DatabaseError
    trait DatabaseValue

    object Database {
      def databaseThings(): Either[DatabaseError, DatabaseValue] = ???
    }

    sealed abstract class ServiceError
    trait ServiceValue

    object Service {
      def serviceThings(v: DatabaseValue): Either[ServiceError, ServiceValue] = ???
    }
  }

  test("either in the small, either in the large SOLUTION 1") {
    sealed abstract class AppError
    final case object DatabaseError1 extends AppError
    final case object DatabaseError2 extends AppError
    final case object ServiceError1 extends AppError
    final case object ServiceError2 extends AppError

    trait DatabaseValue
    trait ServiceValue

    object Database {
      def databaseThings(): Either[AppError, DatabaseValue] = ???
    }

    object Service {
      def serviceThings(v: DatabaseValue): Either[AppError, ServiceValue] = ???
    }

    def doApp = Database.databaseThings().flatMap(Service.serviceThings)
  }

  test("either in the small, either in the large SOLUTION 2: ADTS ALL THE WAY DOWN") {
    sealed abstract class DatabaseError
    trait DatabaseValue

    object Database {
      def databaseThings(): Either[DatabaseError, DatabaseValue] = ???
    }

    sealed abstract class ServiceError
    trait ServiceValue

    object Service {
      def serviceThings(v: DatabaseValue): Either[ServiceError, ServiceValue] = ???
    }

    sealed abstract class AppError
    object AppError {
      final case class Database(error: DatabaseError) extends AppError
      final case class Service(error: ServiceError) extends AppError
    }

    def doApp: Either[AppError, ServiceValue] =
      Database.databaseThings().leftMap(AppError.Database)
        .flatMap(dv => Service.serviceThings(dv).leftMap(AppError.Service))

    def awesome =
      doApp match {
        case Left(AppError.Database(_)) => "something in the database went wrong"
        case Left(AppError.Service(_)) => "something in the service went wrong"
        case Right(_) => "everything is alright!"
      }

    val right: Either[String, Int] = Right(41)
    right.map(_ + 1) should be(Right(42))

    val left: Either[String, Int] = Left("Hello")
    left.map(_ + 1) should be(Left("Hello"))
    left.leftMap(_.reverse) should be(Left("olleH"))
  }

  test("catchOnly & catchNonFatal") {
    val either: Either[NumberFormatException, Int] =
      try {
        Either.right("abc".toInt)
      } catch {
        case nfe: NumberFormatException => Either.left(nfe)
      }

    val either2: Either[NumberFormatException, Int] =
      Either.catchOnly[NumberFormatException]("abc".toInt)

    Either.catchOnly[NumberFormatException]("abc".toInt).isRight should be(false)
    Either.catchNonFatal(1 / 0).isLeft should be(true)
  }


  test("extra syntax") {
    val left: Either[String, Int] = "hello 🐈s".asLeft[Int]
    val right: Either[String, Int] = 42.asRight[String]
    
    right should be(Right(42))
  }

}
