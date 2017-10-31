package me.archdev

package object purevalidator {

  trait ValidEntity

  type FieldName = String
  type ErrorCode = String

  type ValidationResult = Option[ErrorCode]
  type ValidationReport = Seq[ErrorCode]

  type ValidatedEntity[T] = T with ValidEntity

  trait Validatable[T] {
    type Valid = ValidatedEntity[T]
    implicit val validator: Validator[T]
  }

}
