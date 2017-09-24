package com.deleris.tetrix
import java.{util => ju}

class AbstractUI {
  private[this] val initialState = Stage.newState(Nil,
    (10, 23), Stage.randomStream(new scala.util.Random))
  private[this] var state = initialState
  private[this] val timer = new ju.Timer 
  timer.scheduleAtFixedRate(new ju.TimerTask {
      def run { state = Stage.tick(state) }
  }, 0, 1000)

  def left() {
    state = Stage.moveLeft(state)
    
  }
  def right() {
    state = Stage.moveRight(state)
  }
  def up() {
    state = Stage.rotateCW(state)
  }
  def down() {
    state = Stage.tick(state)
  }
  def space() {
    state = Stage.drop(state)
  }
  def view: GameView = state.view
}