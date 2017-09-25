package com.deleris.tetrix

sealed trait AgentMessage
case class BestMove(s: GameState) extends AgentMessage