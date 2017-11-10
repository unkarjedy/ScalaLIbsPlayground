package _cats

import cats.Functor
import cats.implicits._
import org.scalatest.FunSuite
import org.scalatest.Matchers._

import scala.language.reflectiveCalls

class FunctorTest extends FunSuite {

  test("map") {
    Functor[List].map(List("qwer", "adsfg"))(_.length) should be(List(4, 5))
    Functor[Option].map(Option("Hello"))(_.length) should be(Some(5))
    Functor[Option].map(None: Option[String])(_.length) should be(None)
  }

  test("lift") {
    val lenOption: Option[String] ⇒ Option[Int] = Functor[Option].lift(_.length)
    lenOption(Some("Hello")) should be(Some(5))
  }

  test("fproduct") {
    val source = List("Cats", "is", "awesome")
    val product = Functor[List].fproduct(source)(_.length).toMap

    product.getOrElse("Cats", 0) should be(4)
    product.getOrElse("is", 0) should be(2)
    product.getOrElse("awesome", 0) should be(7)
  }

  test("compose") {
    val listOpt = Functor[List] compose Functor[Option]
    listOpt.map(List(Some(1), None, Some(3)))(_ + 1) should be(List(Some(2), None, Some(4)))

    val wtfType: Functor[({
      type λ[α] = List[Option[α]]
    })#λ] = listOpt
  }
}
