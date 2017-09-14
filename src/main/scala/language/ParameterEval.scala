package language

/**
  *
  * aaaaaa
    aaa
    bbbbbbb
    bbb
  */
object ParameterEval extends App {

  class A {
    def f1(b: String): A = {
      println(b)
      new A()
    }

    def f2(p: String) = {
      println(p)
    }
  }

  def a = {println("aaaaaa");"aaa"}
  def b = {println("bbbbbbb"); "bbb"}

  /**
    * aaaaaa
      aaa
      bbbbbbb
      bbb
    */
  new A().f1(a).f2(b)
}
