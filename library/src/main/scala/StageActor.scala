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
        case Attack => updateState {notifyAttack}
    }

    private[this] def opponent: ActorSelection =
        if (self.path.name == "playerActor1") context.actorSelection("../playerActor2") //context.actorFor("/playerActor2")
        else context.actorSelection("../playerActor1") //context.actorFor("/playerActor1")

    private[this] def updateState(trans: GameState => GameState) {
        val future = (stateActor ? GetState)(1 second).mapTo[GameState]
        val s1 = Await.result(future, 1 second)
        val s2 = trans(s1)
        stateActor ! SetState(s2)
        (0 to s2.lastDeleted - 2) foreach { i =>
            opponent ! Attack 
        }
    }
}