# Kotlin - Best Practices

Use Kotlin in order to avoid writing things as we did in Java. We
are able to write things in a better way.

**Note: Don't overuse Kotlin's syntax**

### Java vs Kotlin Table

In this table you will the major differences between Java and Kotlin

| Java                                                                                          | Kotlin                                            |
|-----------------------------------------------------------------------------------------------|---------------------------------------------------|
| Accessors / Getters / Setters                                                                 | Properties                                        |
| Immutability (final fields)                                                                   | `data class` with immutable fields, then use copy |
| Setters used to fill with values an object                                                    | Named or default arguments, using `apply`         |       
| Singleton Pattern                                                                             | `object`                                          |
| Lazy init (double check locking etc)                                                          | `lazy`                                            |
| Static methods / Util classes                                                                 | top level functions                               |
| Nullability? Optional                                                                         | Nullable types `?`                                |
| [Value object classes](https://hackernoon.com/value-object-design-pattern-in-hibernatespring) | `inline class`                                    |
| Delegation pattern                                                                            | use `by` keyword                                  |
| Method chaining                                                                               | named arguments                                   |

### Functional Programming

Kotlin helps us making our code

* cleaner
* easier to understand and maintain
* easier to test
* less error prone

New things that you can see in Kotlin:

* `if-else`, `when` and `try-catch` are expressions
* lambda expressions, function types
* immutability via `val` properties

### Expressions

```
// not this
fun defineWhatCharIs(char: Char): String {
    if(char in someChars) {
        return "some chars"
    } else if(char in otherChars) {
        return "other chars"
    } else {
        return "other"
    }
}
```

```
//this

fun defineWhatCharisKotlin(char: Char): String = when (char) {
    in someChars -> "some chars"
    in otherChars -> "other chars"
    else -> "other"
}
```

Rule: __use when instead of if__

```
val test = try {
    throw NullPointerException()
} catch(e: Exception) {
  "error"
}
```


#### Single expressions
lets say, when mapping from a type A to Type B

for instance

```
//not this
fun mapToTypeB(typeA: TypeA): TypeB {
    val typeB = TypeB(
        field1 = typeA.field1
        field2 = typeA.field2
    )
    return typeB
}
```

```
//this
fun mapToTypeB(typeA: TypeA) = TypeB(
        field1 = typeA.field1
        field2 = typeA.field2
    )
```

makes your code more concise!

### Top level functions for Utils

In Java:

```
public class Utils {
  public static final boolean isEmpty(x: String) {
    return x != null && x.isEmpty() && x.equals("")
  } 
}
```

In Kotlin:

```
// not this
object Utils {
    fun isEmpty(string: String?): Boolean {
        return string.isNullOrBlank()
    }
}
```

```
//this
fun String?.isEmpty() = isNullOrEmpty()
```

### Named Arguments

Person class
```
data class Person(
    val name: String,
    val surname: String,
    val age: Int,
    val nickName: String
) {
    
    class Builder () {
        private var name: String = ""
        private var age: Int = 0
        private var surname: String = ""
        private var nickName: String = ""
        
        
        fun setName(name: String): Builder {
            this.name = name
            return this
        }
        fun setSurname(name: String): Builder {
            this.surname = name
            return this
        }
        fun setAge(age: Int): Builder {
            this.age = age
            return this
        }
        fun setNickName(name: String): Builder {
            this.nickName = name
            return this
        }
        fun build() = Person(name, surname, age, nickName)
    }
    
    companion object {
        fun builder() = Builder()
    }
}

```

```
//not this
val person =  Person.builder()
        .setName("name")
        .setSurname("surname")
        .setAge(20)
        .build()
```


```
val person =  Person(
    name = "name"
    surname = "surname"
    age = 20
    nickname = "nickName"
)
```

### Initialization using apply()

Person2 class definition

```
data class Person2() {
    var name: String = ""
    var surname: String = ""
    var age: Int = 0
    var nickName: String = ""
}
```


apply() helps to configure an object
```
// not this
val person = Person2()
person.name = "name"
person.age = 18
person.surname = "surname"
```

```
//this
val person = Person().apply {
    name = "name"
    age = 18
    surname = "surname"
}
```

### Avoid Unnecessary Overloading

```
//not this

fun get(name: String){
    get(name, true)
}
fun get(name: String, refresh: Boolean){
}
```

```
//this
fun get(name: String,  refresh = true) {
}
```


### Nullability

Given
```
data class Someone(
    val personalInfo: PersonalInfo?,
)

data class PersonalInfo(
    val name: String?,
    val surname: String?
)

val someone = Someone(PesonalInfo(null, null))
```

```
//not this
val test = if(someone != null && 
    someone.personalInfo != null && 
    someone.personalInfo.name != null) {
    someone.personalInfo.name
} else {
    ""
}
```

```
//this
val test = dummyObj?.personalInfo?.name ?: ""
```


#### Use let

```
val someone = Someone(PesonalInfo(null, null))
someone?.personalInfo?.let {
   if(name != null)
    ....
}
```

### Instance checks

Given a certain type, let's say
```
interface CertainType
```

then...
```
//not this
if(x is CertainType){
   ...
} else {
 throw IllegalStateException(..)
}
```


```
//this
x as? CertainType ?: throw IllegalStateException(..)
```

### Avoid null assertions
```
//not this
someone!!.personal!!.name!!
```

You can always find a better approach while writing your code.


### Value Classes

Classes that contain only one immutable property, which can be _inlined_ at compile time
which means that removes the class type and only the wrapped property is used. and declared as,
```
@JvmInline //use this 
value class MyClass(...)
```

Read [this](https://kotlinlang.org/docs/inline-classes.html) for more details.

_Objects that hold only one property an also be replaced with the value set in this property_

Just define a class in this way:
```
@JvmInline
value class Name(private val value: String)
```

Remember: _As said, during compilation, this class will replaced_ as well as methods
defined in this class will be transformed as static methods in Java.<br>

Benefits from using them:
* protection from misuse
* readability
* avoid creating unnecessary objects ?

A first example
```
data class Human(val name: String, val surname: String, val id: Int)

Human("john", "surname", 1) // ok
Human("surname", "john", 1) //wrong values given
Human("surname", "john", -1) //contains a second error as well
Human(
        surname = "surname", 
        name = "john", 
        id = -1) //better
```

Using value classes 

```

@JvmInline
value class Name(private val value: String) {
    init {
        validateName(value)
    }
    private fun validateName(value: String){
        if(value.isEmpty() ||  value.lenth > 30)
            error("not accepted name")
    }
}

@JvmInline
value class Surname(private val value: String) {
    init {
        validateName(value)
    }
    private fun validateName(value: String){
        if(value.isEmpty() || value.lenth > 20)
            error("not accepted surname")
    }
}
@JvmInline
value class Id(private val value: Int) {
    init {
        if(value < 0)
            error("negative, not allowed")
    }
}

val name = Name("")
val surname = Surname("surname")
val human = Human2(name, surname, Id(-1))
println(human) //throws exception

```

The corresponding (decompiled) Java code 
```
try {
     String name = Name.constructor-impl("");
     String surname = Surname.constructor-impl("surname");
     Human2 human = new Human2(name, surname, Id.constructor-impl(-1), (DefaultConstructorMarker)null);
     System.out.println(human);
  } catch (Throwable var3) {
     var3.printStackTrace();
  }
  
  
  public final class Name {
    private final String value;
    
    // $FF: synthetic method
    private Name(String value) {
      Intrinsics.checkNotNullParameter(value, "value");
      super();
      this.value = value;
    }
    
    private static final void validateName_impl/* $FF was: validateName-impl*/(String $this, String value) {
      CharSequence var2 = (CharSequence)value;
      if (var2.length() == 0) {
         String var3 = "not accepted name";
         throw new IllegalStateException(var3.toString());
      }
    }

    @NotNull
    public static String constructor_impl/* $FF was: constructor-impl*/(@NotNull String value) {
      Intrinsics.checkNotNullParameter(value, "value");
      validateName-impl(value, value);
      return value;
    }
}
```

### State Handling Using Sealed classes/interfaces

Instead of 
```
interface Repo {
    fun getData(): CustomType
}
//not this
try {
    customType = repo.getData()
} catch(e: Exception) {
    ...
}
```

do

```
//this

interface Repo {
    fun getData(): Result<CustomType>
}
sealed interface Result {
  data class Success(..): Result
  data class Error(..): Result 
}

val result = repo.getData()

//if (result is Result.Success)
//result as? Result.Success
```

### Destructuring



```
data class Someone(val age: Int, val name: String) //ok, but expensive creation
fun returnSomeone() : Someone = Someone( age = 20, name = "john")

val someone = returnSomeone()
val (age, name) = returnSomeone()

//tip: 
val myMap = mapOf("key1" to 1) //to is an infix function

//then you could write
for((key, value) in myMap){} 
```

# Json Construction
Not necessary the best approach, but good to know

```
val someone = mapOf(
    "name" to "John"
    "surname" to "Hobbbyist"
    "age" to "32"
    "hobbies" to listOf("bass", "running", "hobbyist")
    "personalInfo" to mapOf(
        "address" to "..."
        "city" to "Livadeia"
    )
)
```

# Avoid Unnecessary Init blocks

```
//not this
class Config(ttl: Int, url: String) {
   private val socket: Socket
   
   init {
    socket = Socket(ttl, url)
   }
}
```
```
//this
class Config(ttl: Int, url: String) {
   private val socket: Socket = Socket(ttl, url) // ---> single expression
}
```

