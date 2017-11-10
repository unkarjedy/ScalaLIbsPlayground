package _cats

import cats._
import org.scalatest.FunSuite
import org.scalatest.Matchers._

class IdentityTest extends FunSuite {

  test("test") {
    val x: Id[Int] = 1
    val y: Int = x

    val anId: Id[Int] = 42
    anId should be(42)

    val one: Int = 1
    Functor[Id].map(one)(_ + 1)
    Applicative[Id].pure(42) should be(42)
  }
}
