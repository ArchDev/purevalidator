# PureValidator
PureValidator is a Scala library implementing typesafe validation mechanism.
With help of it, you will get compile-time guarantee on validness of data.

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
