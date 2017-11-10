package _cats

import cats.implicits._
import cats.{Applicative, Monad}
import org.scalatest.FunSuite
import org.scalatest.Matchers._

import scala.language.reflectiveCalls

/**
  * Applicative extends Apply by adding a single method, pure:
  * `def pure[A](x: A): F[A]`
  *
  * This method takes any value and returns the value in the context of the functor.
  * For many familiar functors, how to do this is obvious.
  * For Option, the pure operation wraps the value in Some.
  * For List, the pure operation returns a single element List:
  */
class ApplicativeTest extends FunSuite {

  test("simple") {
    Applicative[Option].pure(1) should be(Some(1))
    Applicative[List].pure(1) should be(List(1))
  }

  test("compose") {
    val composed = Applicative[List] compose Applicative[Option]
    composed.pure(1) should be(List(Some(1)))
  }

  /**
    * Applicative is a generalization of Monad,
    * allowing expression of effectful computations in a pure functional way.
    * Applicative is generally preferred to Monad when the structure of a computation is fixed a priori.
    * That makes it possible to perform certain kinds of static analysis on applicative values.
    */
  test("... monad") {
    Monad[Option].pure(1) should be(Some(1))
    Applicative[Option].pure(1) should be(Some(1))
  }

}
