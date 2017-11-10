package _cats

import cats.Apply
import cats.implicits._
import org.scalatest.FunSuite
import org.scalatest.Matchers._

import scala.language.reflectiveCalls

/**
  * Apply extends the Functor type class (which features the familiar map function) with a new function ap.
  * The ap function is similar to map in that we are transforming a value in a context
  * (a context being the F in F[A]; a context can be Option, List or Future for example).
  * However, the difference between ap and map is that for ap the function that takes care of the
  * transformation is of type F[A => B], whereas for map it is A => B:
  * Here are the implementations of Apply for the Option and List types:
  */
class ApplyTest extends FunSuite {

  val intToString: Int ⇒ String = _.toString
  val double: Int ⇒ Int = _ * 2
  val addTwo: Int ⇒ Int = _ + 2

  test("map") {

    Apply[Option].map(Some(1))(intToString) should be(Some("1"))
    Apply[Option].map(Some(1))(double) should be(Some(2))
    Apply[Option].map(None)(addTwo) should be(None)
  }

  test("compose") {
    val listOpt = Apply[List] compose Apply[Option]
    val plusOne = (x: Int) ⇒ x + 1
    listOpt.ap(List(Some(plusOne)))(List(Some(1), None, Some(3))) should be(List(Some(2), None, Some(4)))
  }

  test("ap") {
    Apply[Option].ap(Some(intToString))(Some(1)) should be(Some("1"))
    Apply[Option].ap(Some(double))(Some(1)) should be(Some(2))
    Apply[Option].ap(Some(double))(None) should be(None)
    Apply[Option].ap(None)(Some(1)) should be(None)
    Apply[Option].ap(None)(None) should be(None)

    Apply[List].ap(List(double, addTwo))(List(1, 2, 3)) should be(List(2, 4, 6, 3, 4, 5))
  }

  test("ap2, ap3 ... ap22") {
    val addArity2 = (a: Int, b: Int) => a + b
    val addArity3 = (a: Int, b: Int, c: Int) => a + b + c

    Apply[Option].ap2(Some(addArity2))(Some(1), Some(2)) should be(Some(3))
    Apply[Option].ap2(Some(addArity2))(Some(1), None) should be(None)
    Apply[Option].ap3(Some(addArity3))(Some(1), Some(2), Some(3)) should be(Some(6))
  }

  test("map2, map3 ... map22") {
    val addArity2 = (a: Int, b: Int) => a + b
    val addArity3 = (a: Int, b: Int, c: Int) => a + b + c

    Apply[Option].map2(Some(1), Some(2))(addArity2) should be(Some(3))
    Apply[Option].map3(Some(1), Some(2), Some(3))(addArity3) should be(Some(6))
  }

  test("tuple2 ... tuple22") {
    Apply[Option].tuple2(Some(1), Some(2)) should be(Some((1, 2)))
    Apply[Option].tuple3(Some(1), Some(2), Some(3)) should be(Some((1, 2, 3)))
  }

  test("apply builder syntax") {
    val addArity2 = (a: Int, b: Int) => a + b
    val addArity3 = (a: Int, b: Int, c: Int) => a + b + c

    val option2 = Option(1) |@| Option(2)
    val option3 = option2 |@| Option.empty[Int]

    option2 map addArity2 should be(Some(3))
    option3 map addArity3 should be(None)

    option2 apWith Some(addArity2) should be(Some(3))
    option3 apWith Some(addArity3) should be(None)

    option2.tupled should be(Some((1, 2)))
    option3.tupled should be(None)
  }

}
