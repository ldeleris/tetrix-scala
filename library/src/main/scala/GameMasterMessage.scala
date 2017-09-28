package com.deleris.tetrix

sealed trait GameMasterMessage
case object Start extends GameMasterMessage
case class ReStart(s0: GameState) extends GameMasterMessage