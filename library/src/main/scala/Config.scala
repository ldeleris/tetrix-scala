package com.deleris.tetrix

case class Config(minActionTime: Long, maxThinkTime: Long, onDrop: Option[StageMessage])