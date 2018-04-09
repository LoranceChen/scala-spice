package typesys


/**
  * +A : Foo[SeniorTeacher] is subtype of Foo[Teacher].
  * eg:
  * a method: `def f(foo: Foo[Teacher]) = `
  * foo could be Foo[Teacher] or Foo[SeniorTeacher]
  * - B : if define B >: A, B could be super of A, we can use Teacher, Human.
  * - B : if define B `<:` A, B could be Teacher and SeniorTeacher.
  *
  */
trait Foo[+A] { def bar[B >: A](a: B): Unit}
trait Foo4[+A] { def bar[B](a: B): Unit}

class Foo2[-A] { def bar(a: A): Unit = {}}
class Foo3[A] { def bar[B >: A](a: B): A = ???}

object GenericType2 {
  def main(arg: Array[String]): Unit = {
    val humans = List[ScalaSeniorTeacher]()
    humans.::(new ScalaTeacher())

    //    foo(new ScalaTeacher() , new Foo3[ScalaTeacher])

    println(intPlus1Cache.get)
    Thread.sleep(7 * 1000)
    println(intPlus1Cache.get)
    Thread.sleep(7 * 1000)
    println(intPlus1Cache.get)

  }

  val intPlus1Cache: Cache[Int] = {
    var x = 0
    Cache[Int](() => {x += 1; x})
  }

  val humanCache: Cache[ScalaHuman] = {
    var x = 0
    Cache(() => new ScalaTeacher())
  }

//  def foo[A <: ScalaHuman](a: A, fhuman: Foo3[A]) = {fhuman.bar(a)}

  def doCache(cache: List[Cache[_ <: Human]]) = {}

}

trait ScalaHuman
class ScalaTeacher() extends ScalaHuman
class ScalaSeniorTeacher() extends ScalaTeacher
class ScalaStudent() extends ScalaHuman

/**
  * @param calcu
  * @param recalculateSpareTime
  * @tparam T can't be +T, because `value` is var which means T could be put
  */
class Cache[T]( calcu: () => T,
                recalculateSpareTime: Long = 10 * 1000 // 10s
              ) {
  private var value: T = calcu()

  new Thread(() => {
    while(true) {
      Thread.sleep(recalculateSpareTime)
      value = calcu()
    }
  }).start()

  def map[B](f: T => B): Cache[B] = {
    Cache(() => f(value))
  }

  def flatMap[B](f: T => Cache[B]): Cache[B] = {
    f(value)
  }

  def get = value
}

object Cache {
  def apply[T](t: () => T): Cache[T] = new Cache(t)
}
