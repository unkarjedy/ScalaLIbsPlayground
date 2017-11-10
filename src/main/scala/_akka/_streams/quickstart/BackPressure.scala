package _akka._streams.quickstart

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, OverflowStrategy, ThrottleMode}

import scala.concurrent.duration.DurationInt

object BackPressure extends App {
  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 10)

  val source2: Source[Int, NotUsed] = source
    .throttle(1, 10 millis, 1, ThrottleMode.shaping)

  source2
    .buffer(2, OverflowStrategy.dropHead)
    .map(x => {
      (1 to 10000000).map(x => Math.sqrt(x)).sum
      x
    })
    .runForeach(println) // WTF? does not drop?? sink and source share same thread???
}
