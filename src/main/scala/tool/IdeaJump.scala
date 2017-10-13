package tool

/**
  * Cmd + Shift + P find implicit value
  */
object IdeaJump extends App {
  /**
    * jump to implicitly
    */
  class Imp[T] {}
  implicit val imp = new Imp[Int]

  //explicit use `func[String]` if you define multi context implicit value, such as:
//  implicit val imp2 = new Imp[String]

  // same as a[T](implicit t: Imp[T])
  def func[T: Imp]() = {
    // Cmd + Shift + P to find where the instance is
    val i = implicitly[Imp[T]]
    println(i)
  }

  func()
}
