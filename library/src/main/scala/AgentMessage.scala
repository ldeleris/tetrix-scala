package com.deleris.tetrix

sealed trait AgentMessage
case class BestMoves(s: GameState, config: Config) extends AgentMessage