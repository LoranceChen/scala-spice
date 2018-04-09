package language

/**
  *
  */
object ScalaImplicit extends App {
  implicit val x = 1

  val clusore = (int: Int) => {
//    implicit val y = 2
    println(implicitly[Int])
  }

  clusore
}
