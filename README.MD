# PureValidator
[![Build Status](https://travis-ci.org/ArchDev/purevalidator.svg?branch=master)](https://travis-ci.org/ArchDev/purevalidator)

PureValidator is a Scala library implementing typesafe validation mechanism.
With help of it, you will get compile-time guarantee on validness of data.

### Idea
Its a tricky question where in application we should validate data. Some developers prefer defensive development with data checking in every function, while on other side we can check data only in application controllers. Both ways have their pros and cons, but what if we want to get benefits from both? In PureValidator we think that we can write our code in defensive way without tons of boilerplate code and runtime overhead. 

### Example
To demonstrate how PureValidator works, let's create test data model:
```scala
final case class User(firstName: String, lastName: String, age: Int)
```

To define validation rules, create companion object and extend it from `Validatable` trait:
```scala
object User extends Validatable[User] {
    override implicit val validator: Validator[User] =
      Validator[User]
        .check(_.firstName.nonEmpty, "firstName-is-empty")
        .check(_.lastName.nonEmpty, "lastName-is-empty")
        .check(_.age > 0, "age-lower-that-0")      
}
```

To validate your entity, use `Validation` object:
```scala
Validation.validate(User("", "", 0)) // Left(Seq("firstName-is-empty", "lastName-is-empty", "age-lower-that-0"))
Validation.validate(User("A", "K", 1)) // Right(User.Valid("A", "K", 1))
```

As you can see, if validation was successful, user model type was changed. `User.Valid` type is a same `User` object but with special mark. We can use it as just `User` object or ensure that our functions will accept only valid entities:

```scala
def saveUser(user: User.Valid): Future[Id] = ???

Validation.validate(User("A", "K", 1)).map(saveUser) // OK
saveUser(User("A", "K", 1)) // Type missmatch during compilation
```

With help of `.Valid` type, we can be totally sure that nobody will use our codebase with non-validated data. 
