package _cats

import cats.implicits._
import cats.{Foldable, Later, Now}
import org.scalatest.FunSuite
import org.scalatest.Matchers._

import scala.language.reflectiveCalls


class FoldableTest extends FunSuite {

  test("all in one") {
    // foldLeft
    Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) should be(6)
    Foldable[List].foldLeft(List("a", "b", "c"), "")(_ + _) should be("abc")

    // foldRigt Lazy
    val lazyResult = Foldable[List].foldRight(List(1, 2, 3), Now(0))((x, rest) => Later(x + rest.value))
    lazyResult.value should be(6)

    // fold
    Foldable[List].fold(List("a", "b", "c")) should be("abc")
    Foldable[List].fold(List(1, 2, 3)) should be(6)
    Foldable[List].fold(List.empty[Int]) should be(0)

    // foldMap
    Foldable[List].foldMap(List("a", "b", "c"))(_.length) should be(3)
    Foldable[List].foldMap(List(1, 2, 3))(_.toString) should be("123")

    // foldK
    Foldable[List].foldK(List(List(1, 2), List(3, 4, 5))) should be(List(1, 2, 3, 4, 5))
    Foldable[List].foldK(List(None, Option("two"), Option("three"))) should be(Some("two"))

    // find
    Foldable[List].find(List(1, 2, 3))(_ > 2) should be(Some(3))
    Foldable[List].find(List(1, 2, 3))(_ > 5) should be(None)

    // exists
    Foldable[List].exists(List(1, 2, 3))(_ > 2) should be(true)
    Foldable[List].exists(List(1, 2, 3))(_ > 5) should be(false)

    // forall
    Foldable[List].forall(List(1, 2, 3))(_ <= 3) should be(true)
    Foldable[List].forall(List(1, 2, 3))(_ < 3) should be(false)

    // toList
    Foldable[List].toList(List(1, 2, 3)) should be(List(1, 2, 3))
    Foldable[Option].toList(Option(42)) should be(List(42))
    Foldable[Option].toList(None) should be(List())

    // filter
    Foldable[List].filter_(List(1, 2, 3))(_ < 3) should be(List(1, 2))
    Foldable[Option].filter_(Option(42))(_ != 42) should be(List())

    // traverse_
    def parseInt(s: String): Option[Int] = Either.catchOnly[NumberFormatException](s.toInt).toOption

    Foldable[List].traverse_(List("1", "2", "3"))(parseInt) should be(Some(Unit))
    Foldable[List].traverse_(List("a", "b", "c"))(parseInt) should be(None)

    // compose
    val FoldableListOption = Foldable[List].compose[Option]
    FoldableListOption.fold(List(Option(1), Option(2), Option(3), Option(4))) should be(10)
    FoldableListOption.fold(List(Option("1"), Option("2"), None, Option("3"))) should be("123")

    // other
    Foldable[List].isEmpty(List(1, 2, 3)) should be(false)
    Foldable[List].dropWhile_(List(1, 2, 3))(_ < 2) should be(List(2, 3))
    Foldable[List].takeWhile_(List(1, 2, 3))(_ < 2) should be(List(1))
  }

}
