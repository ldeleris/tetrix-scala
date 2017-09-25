package com.deleris.tetrix

sealed trait StateMessage
case object GetState extends StateMessage
case class SetState(s: GameState) extends StateMessage
case object GetView extends StateMessage