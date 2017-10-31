package me.archdev.purevalidator

class Validator[T](val rules: Seq[T => ValidationResult]) {

  def check(rule: T => ValidationResult): Validator[T] =
    new Validator[T](rules :+ rule)

  def check[A](valueLens: T => A, predicate: A => Boolean, errorCode: String): Validator[T] =
    check(obj =>
      Some(errorCode).filterNot(_ => predicate(valueLens(obj)))
    )

  def validate(t: T): Either[ValidationReport, ValidatedEntity[T]] =
    rules.map(_ (t)).partition(_.isEmpty) match {
      case (_, Nil) =>
        Right(t.asInstanceOf[T with ValidEntity])
      case (_, errors) =>
        Left(errors.map(_.get))
    }

}

object Validator {

  def apply[T] = new Validator[T](Nil)

  def validate[T](t: T)(implicit validator: Validator[T]): Either[ValidationReport, ValidatedEntity[T]] =
    validator.validate(t)

}
