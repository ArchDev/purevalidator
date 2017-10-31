package me.archdev.purevalidator

import me.archdev.purevalidator.utils._
import org.scalatest.{Matchers, WordSpec}

class ValidatorTest extends WordSpec with Matchers {

  "Validator" when {

    "check" should {

      "create new validator with appended rule" in {
        val originalValidator = Validator[User]
        val validatorWithRule = originalValidator.check(passRule)

        originalValidator.rules shouldBe Nil
        validatorWithRule.rules.size shouldBe 1
      }

    }

    "alternate check" should {

      "register proper validation conditions" in {
        Validator[User]
          .check(_.firstName.nonEmpty, "firstName-is-empty")
          .check(_.lastName.nonEmpty, "lastName-is-empty")
          .check(_.age > 0, "age-lower-that-0")
          .validate(emptyUser) shouldBe Left(Seq("firstName-is-empty", "lastName-is-empty", "age-lower-that-0"))
      }

    }

    "validate" should {

      "return errors if one of checks is failed" in {
          Validator[User]
            .check(passRule)
            .check(failRule("error-1"))
            .check(failRule("error-2"))
            .check(passRule)
            .validate(emptyUser) shouldBe Left(Seq("error-1", "error-2"))
      }

      "return Valid model if all checks passed" in {
        Validator[User]
          .check(passRule)
          .check(passRule)
          .validate(emptyUser)
          .right.get.isInstanceOf[User.Valid] shouldBe true
      }

    }

  }

}
