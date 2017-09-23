package com.deleris.tetrix

class AbstractUI {
  private[this] val initialState = Stage.newState(Seq[Block](new Block((0, 0),TKind)))
  private[this] var state = initialState

  def left() {
    state = Stage.moveLeft(state)
    
  }
  def right() {
    state = Stage.moveRight(state)
  }
  def up() {
    
  }
  def down() {
    
  }
  def space() {
    
  }
  def view: GameView = state.view
}