package com.deleris.tetrix

sealed trait GameStatus
case object ActiveStatus extends GameStatus
case object GameOver extends GameStatus
case object Victory extends GameStatus