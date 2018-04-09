
import cats._, cats.data._, cats.implicits._

/**
  *
  */
package object fp {
  type Birds = Int
  case class Pole(left: Birds, right: Birds) {
    def landLeft(n: Birds): Option[Pole] =
      if (math.abs((left + n) - right) < 4) copy(left = left + n).some
      else none[Pole]
    def landRight(n: Birds): Option[Pole] =
      if (math.abs(left - (right + n)) < 4) copy(right = right + n).some
      else none[Pole]
  }

  val x = Monad[Option].pure(Pole(0, 0)) >>= {_.landRight(2)} >>=
    {_.landLeft(2)} >>= {_.landRight(2)}

  none[Int] >> 3.some

  (Monad[Option].pure(Pole(0, 0)) >>= {_.landLeft(1)}) >> none[Pole] >>= {_.landRight(1)}

  val px = for {
    start <- Monad[Option].pure(Pole(0, 0))
    first <- start.landLeft(2)
    noneP <- none[Pole]
    second <- noneP.landRight(2)
    third <- second.landLeft(1)
  } yield third

  10 |+| Monoid[Int].empty
}
