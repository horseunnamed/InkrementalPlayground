package wtf.excentric.core


infix fun <A, B, C> ((A) -> B).compose(g: (B) -> C): (A) -> (C) = { g(this(it)) }