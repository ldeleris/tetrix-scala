package com.deleris.tetrix

class AbstractUI {
  import Stage._
  import java.{util => ju}

  private[this] val initialState = newState(Nil,
    (10, 23), randomStream(new scala.util.Random))
  private[this] var state = initialState
  private[this] val timer = new ju.Timer 
  timer.scheduleAtFixedRate(new ju.TimerTask {
      def run { state = tick(state) }
  }, 0, 1000)

  def left() = updateState {moveLeft}
  def right() = updateState {moveRight}
  def up() = updateState {rotateCW}
  def down() = updateState {tick}
  def space() = updateState {drop}
  def view: GameView = state.view

  private[this] def updateState(trans: GameState => GameState) {
    synchronized {
      state = trans(state)
    }
  }
}