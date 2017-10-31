package me.archdev.purevalidator

package object syntax {

  implicit class ValidationSyntax[T](t: T)(implicit validator: Validator[T]) {
    def validate =
      validator.validate(t)
  }

}
