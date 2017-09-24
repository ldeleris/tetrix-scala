package com.deleris.tetrix

sealed trait StateMessage
case object MoveLeft extends StateMessage
case object MoveRight extends StateMessage
case object RotateCW extends StateMessage
case object Tick extends StateMessage
case object Drop extends StateMessage
case object View extends StateMessage
