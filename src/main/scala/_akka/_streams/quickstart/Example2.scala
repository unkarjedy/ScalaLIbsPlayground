package _akka._streams.quickstart

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future

object Example2 extends App {
  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val sink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  // connect the Source to the Sink, obtaining a RunnableGraph
  val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

  // materialize the flow and get the value of the FoldSink
  val sum: Future[Int] = runnable.run()

  /**
    * After running (materializing) the RunnableGraph[T] we get back the materialized value of type T.
    * Every stream processing stage can produce a materialized value,
    * and it is the responsibility of the user to combine them to a new type.
    * In the above example we used toMat to indicate that we want to transform the materialized value
    * of the source and sink, and we used the convenience function
    * Keep.right to say that we are only interested in the materialized value of the sink.
    */
}
