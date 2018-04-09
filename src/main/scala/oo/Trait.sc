trait A {
  def f = "a"
}

trait B {
  def f = 1
}

class AB extends A with B {
  def f = 1
}

new AB().f
