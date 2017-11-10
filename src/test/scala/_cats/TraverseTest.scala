package _cats

import cats.data.{Validated, ValidatedNel}
import cats.implicits._
import org.scalatest.FunSuite

import scala.concurrent.Future

/**
  * TODO: investigate Traverse
  *
  * @see https://www.scala-exercises.org/cats/traverse
  */
class TraverseTest extends FunSuite {

  test("just examples") {
    def parseInt(s: String): Option[Int] = ???

    trait SecurityError
    trait Credentials

    def validateLogin(cred: Credentials): Either[SecurityError, Unit] = ???

    trait Profile
    trait User

    def userInfo(user: User): Future[Profile] = ???
  }

  def parseIntEither(s: String): Either[NumberFormatException, Int] =
    Either.catchOnly[NumberFormatException](s.toInt)

  def parseIntValidated(s: String): ValidatedNel[NumberFormatException, Int] =
    Validated.catchOnly[NumberFormatException](s.toInt).toValidatedNel

  test("just examples 2") {
    //    List("1", "2", "3").traverseU(parseIntEither) should be(Right(List(1, 2, 3)))
    //    List("1", "abc", "3").traverseU(parseIntEither).isLeft should be(true)
    //
    //    implicit def nelSemigroup[A]: Semigroup[NonEmptyList[A]] =
    //      OneAnd.oneAndSemigroupK[List].algebra[A]
    //
    //    List("1", "2", "3").traverseU(parseIntValidated).isValid should be(
    //    )

    // ??????????????????????????
  }

}
