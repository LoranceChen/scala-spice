package language

import scala.util.control.NonFatal

/**
  *
  */
object TryCatchFinally extends App {
  try {
    Thread.sleep(1000)
    println("try")
    throw new Exception("exception")
  } catch {
    case NonFatal(e) =>
      Thread.sleep(1000)
      println("catch")
      Thread.sleep(1000)
      throw e
  } finally {
    Thread.sleep(1000)
    println("finally")
  }
}
