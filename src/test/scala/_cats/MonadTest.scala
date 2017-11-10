package _cats

import cats.Monad
import cats.data.OptionT
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import cats.implicits._

import scala.language.reflectiveCalls

/**
  * Monad extends the Applicative type class with a new function flatten.
  * Flatten takes a value in a nested context (eg. F[F[A]] where F is the context)
  * and "joins" the contexts together so that we have a single context (ie. F[A]).
  *
  * The name flatten should remind you of the functions of the same name on many classes in the standard library.
  */
class MonadTest extends FunSuite {

  test("standart library flattern") {
    Option(Option(1)).flatten should be(Some(1))
    Option(None).flatten should be(None)
    List(List(1), List(2, 3)).flatten should be(List(1, 2, 3))
  }

  test("option") {
    Monad[Option].pure(42) should be(Some(42))
  }

  test("flatMap") {
    Monad[List].flatMap(List(1, 2, 3))(x ⇒ List(x, x)) should be(List(1, 1, 2, 2, 3, 3))
  }

  test("ifM") {
    Monad[Option].ifM(Option(true))(Option("truthy"), Option("falsy")) should be(Some("truthy"))
    Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4)) should be(List(1, 2, 3, 4, 1, 2))
  }

//  test("composition") {
//    optionTMonad[List].pure(42) should be(OptionT(List(Some(42))))
//  }
//
//  implicit def optionTMonad[F[_]](implicit F: Monad[F]) = {
//    new Monad[OptionT[F, ?]] {
//      def pure[A](a: A): OptionT[F, A] = OptionT(F.pure(Some(a)))
//      def flatMap[A, B](fa: OptionT[F, A])(f: A => OptionT[F, B]): OptionT[F, B] =
//        OptionT {
//          F.flatMap(fa.value) {
//            case None => F.pure(None)
//            case Some(a) => f(a).value
//          }
//        }
//      def tailRecM[A, B](a: A)(f: A => OptionT[F, Either[A, B]]): OptionT[F, B] =
//        defaultTailRecM(a)(f)
//    }
//  }
}
