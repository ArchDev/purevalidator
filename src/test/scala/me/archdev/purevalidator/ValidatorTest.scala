package me.archdev.purevalidator

import me.archdev.purevalidator.utils._
import org.scalatest.{Matchers, WordSpec}

class ValidatorTest extends WordSpec with Matchers {

  "Validator" when {

    "check" should {

      "create new validator with appended rule" in {
        val originalValidator = Validator[User]
        val validatorWithRule = originalValidator.ruleCheck(passRule)

        originalValidator.rules shouldBe Nil
        validatorWithRule.rules.size shouldBe 1
      }

      "register proper validation conditions" in {
        Validator[User]
          .check(_.firstName.nonEmpty, "firstName-is-empty")
          .check(_.lastName.nonEmpty, "lastName-is-empty")
          .check(_.age > 0, "age-lower-that-0")
          .validate(emptyUser) shouldBe Left(Seq("firstName-is-empty", "lastName-is-empty", "age-lower-that-0"))
      }

      "register validation for sub object" in {
        implicit val validator: Validator[User] =
          Validator[User]
            .ruleCheck(failRule("fail"))

        Validator[Party]
          .check(_.owner)
          .validate(Party(emptyUser, Nil)) shouldBe Left(Seq("fail"))
      }

    }

    "validate" should {

      "working with AST" in {
        import me.archdev.purevalidator.syntax._

        Divide(1, 0).validate shouldBe Left(Seq("cannot divide on zero"))
        Divide(1, 1).validate.right.get.isInstanceOf[MyADT.Valid] shouldBe true
        Multiply(1, 1).asInstanceOf[MyADT].validate.right.get.isInstanceOf[MyADT.Valid] shouldBe true
      }

      "return errors if one of checks is failed" in {
        Validator[User]
          .ruleCheck(passRule)
          .ruleCheck(failRule("error-1"))
          .ruleCheck(failRule("error-2"))
          .ruleCheck(passRule)
          .validate(emptyUser) shouldBe Left(Seq("error-1", "error-2"))
      }

      "return Valid model if all checks passed" in {
        Validator[User]
          .ruleCheck(passRule)
          .ruleCheck(passRule)
          .validate(emptyUser)
          .right.get.isInstanceOf[User.Valid] shouldBe true
      }

    }

    "alternative syntax used" should {

      "compile and work" in {
        implicit val validator = Validator[User]
        import me.archdev.purevalidator.syntax._

        User("", "", 0).validate shouldBe Right(User("", "", 0))
      }

    }

  }

}
