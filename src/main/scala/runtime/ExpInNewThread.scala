package runtime

import java.util.concurrent.ExecutionException

/**
  *
  */
object ExpInNewThread extends App {
  def func = {
    try{
      errorFunc()
      Thread.sleep(1000 * 5)
    } catch {
      case e: Throwable =>
        println("catch exception")
        println("func - " + e.getStackTrace.mkString("\n"))
    } finally {
      println("finally....")
    }
  }

  def errorF2: Unit = {
    throw new RuntimeException("errorF2....")
  }

  def errorFunc(): Unit = {
    new Thread(() => {
      try {
        println("error func")
        try {
          errorF2
        } catch {
          case e => throw new Exception("errorFunc ....",e )
        }
      } catch {
        case e: Throwable =>
          println("aaaaa - " + e.getStackTrace.mkString("\n"))
          println("ccccc - " + e.getCause.getStackTrace.mkString("\n"))

          throw new ExecutionException(e)
      }
    }).start()
  }

  func

}
