package com.deleris.tetrix
import akka.actor._

class StageActor(stateActor: ActorRef) extends Actor {
    import Stage._
    import scala.concurrent.{Future, Await}
    import scala.concurrent.duration._
    import akka.pattern.ask

    def receive = {
        case MoveLeft => updateState {moveLeft}
        case MoveRight => updateState {moveRight}
        case RotateCW => updateState {rotateCW}
        case Tick => updateState {tick}
        case Drop => updateState {drop}
    }

    private[this] def updateState(trans: GameState => GameState) {
        val future = (stateActor ? GetState)(60 second).mapTo[GameState] // akka.pattern.ask
        val s1 = Await.result(future, 60 second)
        val s2 = trans(s1)
        stateActor ! SetState(s2)
    }
}