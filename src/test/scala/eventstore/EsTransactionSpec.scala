package eventstore

import TransactionActor._
import akka.actor.Status.Failure
import akka.util.Timeout
import java.util.concurrent.TimeoutException
import scala.concurrent.duration._
import util.ActorSpec

class EsTransactionSpec extends ActorSpec {

  "EsTransaction.start" should {
    "return timeout exception" in new StartScope {
      start must throwAn[TimeoutException].await
    }

    "return error" in new StartScope {
      val future = start
      lastSender ! Failure(exception)
      future must throwAn(exception).await
    }

    "succeed" in new StartScope {
      val future = start
      lastSender ! TransactionId(transactionId)
      future must beEqualTo(EsTransactionForActor(transactionId, testActor)).await
    }
  }

  "EsTransaction.continue.write" should {
    "return timeout exception" in new ContinueScope {
      write must throwAn[TimeoutException].await
    }

    "return error" in new ContinueScope {
      val future = write
      lastSender ! Failure(exception)
      future must throwAn(exception).await
    }

    "succeed" in new ContinueScope {
      val future = write
      lastSender ! WriteCompleted
      future must beEqualTo(()).await
    }
  }

  "EsTransaction.continue.commit" should {
    "return timeout exception" in new ContinueScope {
      commit must throwAn[TimeoutException].await
    }

    "return error" in new ContinueScope {
      val future = commit
      lastSender ! Failure(exception)
      future must throwAn(exception).await
    }

    "succeed" in new ContinueScope {
      val future = commit
      lastSender ! Commit
      future must beEqualTo(()).await
    }
  }

  trait StartScope extends ActorScope {
    val transactionId = 0L
    implicit val timeout = Timeout(500.millis)
    val exception = EsException(EsError.AccessDenied)

    def start = {
      val future = EsTransaction.start(testActor)
      expectMsg(GetTransactionId)
      future
    }
  }

  trait ContinueScope extends StartScope {
    val transaction = EsTransaction.continue(transactionId, testActor)

    def commit = {
      val future = transaction.commit()
      expectMsg(Commit)
      future
    }

    def write = {
      val events = List(EventData("test"))
      val future = transaction.write(events)
      expectMsg(Write(events))
      future
    }
  }
}
