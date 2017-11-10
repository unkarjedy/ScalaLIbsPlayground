package _akka._streams.quickstart

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.Source

import scala.concurrent.duration.DurationInt

object TimeBasedProcessing extends App {
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 21)
  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  factorials
    .zipWith(Source(0 to 20))((num, idx) => s"$idx! = $num")
    .throttle(1, 300 millis, 1, ThrottleMode.shaping)
    .runForeach(println)
}
