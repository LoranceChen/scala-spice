package language

/**
  *
  */
object NamedArg extends App {
  def f[T](x: T): T = x

  def varAmbiguityError = {
    var x = 0
    //f(x = 1) // here you get an ambiguity error
    f({x = 1})
  }
}
