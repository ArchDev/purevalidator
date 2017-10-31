package me.archdev.purevalidator

package object utils {

  final case class User(firstName: String, lastName: String, age: Int)

  object User extends Validatable[User] {
    override implicit val validator: Validator[User] =
      Validator[User].check(passRule)
  }

  val emptyUser = User("", "", 0)

  def passRule[T](t: T): ValidationResult = None

  def failRule[T](error: String)(t: T): ValidationResult = Some(error)

  def failRule[T](t: T): ValidationResult = failRule("error")(t)

}
