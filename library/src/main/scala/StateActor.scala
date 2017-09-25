package com.deleris.tetrix
import akka.actor._

class StateActor(s0: GameState) extends Actor {
    private[this] var state: GameState = s0

    def receive = {
        case GetState => sender ! state
        case SetState(s) => state = s 
        case GetView => sender ! state.view 
    }
}