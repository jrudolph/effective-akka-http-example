package example

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object ShowTree {
  def show[T](t: T): T = macro showImpl[T]

  def showImpl[T: c.WeakTypeTag](c: Context)(t: c.Tree): c.Tree = {
    println(t)
    t
  }
}
