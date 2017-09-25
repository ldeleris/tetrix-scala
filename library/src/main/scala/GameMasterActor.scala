package com.deleris.tetrix
import akka.actor._

class GameMasterActor(stateActor: ActorRef, agentActor: ActorRef) extends Actor {
    import scala.concurrent.{Future, Await}
    import scala.concurrent.duration._
    import akka.pattern.ask

    def receive = {
        case Tick =>
            val s = getState
            if (s.status != GameOver) {
                agentActor ! BestMove(getState)
            }
    }

    private[this] def getState: GameState = {
        val future = (stateActor ? GetState)(1 second).mapTo[GameState]
        Await.result(future, 1 second)
    }
}