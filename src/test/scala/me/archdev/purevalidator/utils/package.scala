package me.archdev.purevalidator

package object utils {

  final case class User(firstName: String, lastName: String, age: Int)

  object User extends Validatable[User] {
    override implicit val validator: Validator[User] =
      Validator[User].ruleCheck(passRule)
  }

  final case class Party(owner: User, members: Seq[User])

  object Party extends Validatable[Party] {
    override implicit val validator: Validator[Party] =
      Validator[Party]
        .check(_.owner)
        .ruleCheck(passRule)
  }

  sealed trait MyADT
  final case class Multiply(x: Int, y: Int) extends MyADT
  final case class Divide(x: Int, y: Int) extends MyADT

  object MyADT extends TypeValidatable[MyADT] {
    implicit val divideValidator =
      Validator[Divide]
        .check(_.y > 0, "cannot divide on zero")
  }

  val emptyUser = User("", "", 0)

  def passRule[T](t: T): ValidationResult = None

  def failRule[T](error: String)(t: T): ValidationResult = Some(error)

  def failRule[T](t: T): ValidationResult = failRule("error")(t)

}
