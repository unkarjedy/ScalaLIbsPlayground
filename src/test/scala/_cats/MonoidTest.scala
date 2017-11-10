package _cats

import cats.implicits._
import cats.kernel.Monoid
import org.scalatest.FunSuite
import org.scalatest.Matchers._

class MonoidTest extends FunSuite {

  test("string") {
    Monoid[String].empty should be("")
    Monoid[String].combineAll(List("a", "b", "c")) should be("abc")
    Monoid[String].combineAll(List()) should be("")
  }

  test("map") {
    Monoid[Map[String, Int]]
      .combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3))) should be(Map("a" -> 4, "b" -> 2))
    Monoid[Map[String, Int]]
      .combineAll(List()) should be(Map.empty[String, Int])
  }

  test("Foldable") {
    val l = List(1, 2, 3, 4, 5)
    l.foldMap(identity) should be(15)
    l.foldMap(i => i.toString) should be("12345")
  }

  test("tuple") {
    val l = List(1, 2, 3, 4, 5)
    l.foldMap(i ⇒ (i, i.toString)) should be((15, "12345"))
  }

}
