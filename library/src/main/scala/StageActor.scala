package com.deleris.tetrix
import akka.actor._

class StageActor(s0: GameState) extends Actor {
    import Stage._

    private[this] var state: GameState = s0

    def receive = {
        case MoveLeft => state = moveLeft(state)
        case MoveRight => state = moveRight(state)
        case RotateCW => state = rotateCW(state)
        case Tick => state = tick(state)
        case Drop => state = drop(state)
        case View => sender ! state.view
    }
}