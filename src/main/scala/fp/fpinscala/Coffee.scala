package fp.fpinscala

case class CreditCard()
case class Charge(cc: CreditCard, price: Double)
case class Coffee(price: Double)
case class Payments() {
  def charge(cc: CreditCard, cup: Coffee) = ???
}

class Cofe1 {
  def buyCoffee(cc: CreditCard, p: Payments): Coffee = {
    val cup = Coffee(10)
    p.charge(cc, cup)
    cup
  }
}
/**
  *
  */
class Cafe2 {
  /**
    * 分离费用的创建的和执行过程
    * @param cc
    * @return
    */
  def buyCoffee(cc: CreditCard): (Coffee, Charge) = {
    val cup = Coffee(10)
    (cup, Charge(cc, cup.price))
  }


}
