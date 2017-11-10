package _akka._streams.quickstart

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future

object Example3 extends App {
  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val sink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)
  // materialize the flow, getting the Sinks materialized value
  val sum: Future[Int] = source.runWith(sink)

//  val flow = Flow[Int].map(_ * 2)
//  val sum2: Future[Int] = flow.runWith(source., sink)
}
