package com.deleris.tetrix

sealed trait PieceKind 
case object IKind extends PieceKind { def toInt = 0 }
case object JKind extends PieceKind { def toInt = 1 } 
case object LKind extends PieceKind { def toInt = 2 }
case object OKind extends PieceKind { def toInt = 3 }
case object SKind extends PieceKind { def toInt = 4 }
case object TKind extends PieceKind { def toInt = 5 }
case object ZKind extends PieceKind { def toInt = 6 }

case object PieceKind {
    def apply(x: Int): PieceKind = x match {
        case 0 => IKind
        case 1 => JKind
        case 2 => LKind
        case 3 => OKind
        case 4 => SKind
        case 5 => TKind
        case _ => ZKind
    }
}