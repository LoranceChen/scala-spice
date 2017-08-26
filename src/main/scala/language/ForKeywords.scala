package language

/**
  *
  */
object ForKeywords extends App {

  /**
    * fully support For-expression after implement `map`, `flatMap`, `withFilter` and `foreach`
    * @param data
    * @tparam T
    */
  class A[T](val data: T) {
    def map[B](f: T => B): A[B] = {
      println("do map")
      new A(f(data))
    }

    def flatMap[B](f: T => A[B]): A[B] = {
      println("do flatMap")
      f(data)
    }

    def withFilter(p: T => Boolean): A[T] = {
      if(p(data)) new A(data) else new A(data)
    }

    /**
      * for a For-expressions but not yield keywords
      * @param p
      */
    def foreach(p: T => Unit): Unit = {
      p(data)
    }
  }

  val a = new A[String]("I'm A")
  val x = for {
    data <- a
    newData = 1
    newData2 = Unit
    _ = println("Inner for - " + newData2)
    data2 <- a if a.data.contains("A")
    data3 <- new A[String]("I'm B")
  } yield {
    data + data3 + newData
  }

  println(x)

}
