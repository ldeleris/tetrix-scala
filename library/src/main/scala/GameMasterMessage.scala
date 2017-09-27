package com.deleris.tetrix

sealed trait GameMasterMessage
case object Start extends GameMasterMessage