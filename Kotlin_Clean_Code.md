# Kotlin Clean Code

## Introduction

Kotlin gives us the capability to write code that it is 
* easy to understand
* short
* expressive
* safe

This doc recommends some best practices and principles of Clean Code when writing code with Kotlin.
The point is to make your code easier to be maintained and used  by changing your approach
when writing code, rather than **following rules**.

### Note: 
There is already a book for this purpose. This book is called _Clean Code and it is written by Robert C. Martin_.
This document borrows some rules/advice from this book and tries to blend/mix them with Kotlin, so as to 
find out how Kotlin can help us write clean code.

## Definition of Clean Code

In the beginning, I think that it should be clear what clean code is.
As said, clean code is the code that is easy to understand. As a result, the code is readable and intuitive.

How do we achieve this?
We can write our code in a way to be
* concise,
* short,
* simple
* expressive.


## Functions

You might have already read/seen somewhere that

> **Rule 1: Functions should be small**

So, you should write small functions, which
* are defined with a descriptive name, and
* separate details from the main logic, and
* they do only one thing.

In this way, your function becomes like a short story. 

**This is a general apporach when writing functions** and has nothing to do with Kotlin.

Take for instance, a Java code sample, for parsing the response from a network call using OkHttp.

```
public CustomType parseCustomType(Response<CustomType> response) {
  if(response == null) throw new CustomException(); 
  
  int responseCode = response.code;
 
  if(code == 200 || code == 201) {
    return mapper.apply(response.body());
  } else if(code >= 400 && code <= 499) {
    throw CustomException("http error");
  } else if(code >= 500 && code <= 599) {
    throw CustomException("server error);
  } else {
    throw CustomException("unkown error"); 
  }  
}
```

This (verbose) code handles all the cases we can meet when we are trying to parse the response.

Let's see the equivalent in Kotlin.

```
fun parseCustomType(response: Response?) = when (response?.code){
   null -> throw CustomException("..")
   200, 201 -> mapper.apply(response.body())
   in 400..499 -> throw CustomException("http error")
   in 500..599 -> throw CustomException("server error")
   else -> throw CustomException("unkown error")
}
```

This sample is **shorter**, **cleaner** && **easier to understand**.
We achieve that by using some Kotlin features, such as:
* `when` statements,
* null checks (`?`)
* condition for multiple values with ranges

thus, making our code smaller and expressive.


## No Side Effects

When writing a function you should limit/reduce the side effects that this method does, meaning
unexpected and hidden changes which are not clear when you firstly look at the function.

### Pure functions

Functional programming introduces a fundamental concept: **pure functions**
It is not just a technique/rule/approach, but a cornerstone of creating
maintainable, consistent and robust software.

What are pure functions?

* Idempotent: Given the same params, the return always the same output
  ```
  fun add(x: Int, y: Int) {
   return x + y;
  }
  
  val result1 = add(3, 5); 
  val result2 = add(3, 5);
  //always the same result 
  ```  
  * No side effects: They don't modify external state, meaning variables or any other type of data outside their scope.
    ```
    //DON'T
    val sum = 0;
    fun addToSum(x: Int) {
      sum += x;
    }
    
    addToSum(5);
    println(sum); // 5

    addToSum(7);
    println(total); // 12
    
    ```
    but this
    ```
    //DO
    val sum = 0;
    fun add(x: Int, y: Int) {
      return x + y;
    }
    val sum = add(7, 5)
    println(sum) // 12
    ```
  * Referential Transparency: Replacing the invocation with the function's body will not change the desired behavior (**_hint: Kotlin's inline keyword_**)
    ```
      //DO
      val sum = 0;
      fun add(x: Int, y: Int) {
        return x + y;
      }
      val sum = add(7, 5)
      println(sum) // 12
    
      val sum2 = 7 + 5
      println(sum2) // 12
    
     inline fun multiply(x: Int, y: Int) {
        return x * y;
     }
     val result = multiply(5, 5)
     //after compilation, it will be in java
     int result = 5 * 5
    ```
* Immutable: They never change the input data, but instead create and return new output, resulting to maintain data integrity
  ```
  fun addToMyList(list: List<Int>, x: Int): List<Int>{
     list.add(x) // compilation error --> immutable list
     val newList = list.toMutableList().add(x)
     return newList;
  }
  
  val originalList = listOf(1,2,3)
  val newList = addToMyList(originalList, 1)
  println(originalList) // --> [1, 2, 3]
  ```

Hope you understood what pure functions are.
So, Kotlin helps us a lot with this approach, since it comes with
* Expressions,
* Immutability,
* Lambdas
In general it has a better support for functional programming in compare with Java.

## Expressions


### Flow Control Syntax

Unlike Java, Kotlin's flow control syntax are expressions and not statements.

We have already seen `when`, but the same applies for `if-else` as well as 
`try-catch`

For instance:
```
val x = 1
val myStr = if(x == 1) x.toString() else "unknown" 
```

In Java, you have to declare the var myStr before the if Statement, and so forth and so on.
```
  public class Test {
    
     public static void main(String[] args) {
        int x = 1; //note, it is not final
        final String myStr;
        if(x == 1) {
          myStr = "1";
        } else {
          myStr = "not initialized";
        }
     }
  }
```
END OF NOISE!

### Single Expression Functions

We can avoid curly braces when defining a function:
```
  fun getData(val id: Int) = repository.getTransaction(id) 
  
  fun getSafeData(val id: Int) = try {
    repository.getData(id)
  } catch(e: Exception) {
    throw Exception("..)
  }
```
### Train Wrecks

Because Kotlin gives us the capability to write single expression methods, 
this does not mean that we should try to squeeze everything into a single expression/
Each developer should write code readable & clean.

```
fun mapToDomain(dto: DTO): Domain = Domain(
  id = dto.id,
  externalId = try {
      dto.externalData?.let {
        it.id
      }
  } catch(e: Exception) {
    -1
  },
  
  content = try {
    Content(
      id = getId(...),
      name = getName(...)
      imageUrl = extractImageUrl(...)
      ....
  } catch(e: Exception){
    EMPTY_CONTENT
  }
```

In case you are in doubt, prefer using subFunctions or temp variables. Our goal is to create
readable code, easy to be debugged.

> **Readability, maintainability** wins over squeezing everything into a single line/portion
> this applies to git commit strategy as well, lol


## Immutability

This comes by default in Kotlin. It encourages as to use immutable variables or data structures and collections.

### Variables
Use `val` to declare `final` variables. In Java, we need to add `final` keyword in order to declare a
variable as immutable (syntactic noise).
```
 val x = 1
 x = 2 //compilation error
 
 var y = 1
 y = 2
```

### Read only Collections

Creating a list, using `listOf()` creates a read only list which is immutable

```
val list = listOf(1,2,3)
list.add(4) // compile error
```

Using a method from the API, returns a new list, which means
```
val filteredList = list.filter { it % 2 == 0 } 
```

Moreover, in compare with Java, you don't have to use `stream()` or `collect(Collectors.toList())`.
Kotlin helps us writing less code.

### Data classes
By using `data class` syntax in Kotlin, you can define a data class.
Data class in Kotlin means a class that:
* has backing support for accessor methods
* has implementation for hashCode(), equals(), toString()
* class is declared as final

Example:
```
data class Human(val name: String, val surname: String, val id: Int)

//with default arguments
data class Human2(val name: String = "", val surname: String = "", val id: Int = 0)
```

Those classes are only consisted of class name and the properties definitions. Such a minimal way!

Usage:
```
val human1 = Human("John", "V", 1)
val empty = Human2() //not versbos
```

Immutable vars:
```
val human1 = Human("John", "V", 1)
human1.surname = "test" //compilation error
```
In case you want to create a new instance:
```
val human3 = human1.copy(id = 3)
```


## Error Handling

### Clean code Suggestions

* Don't use checked exceptions
  * **_What does Kotlin for this?_**
    * Check exceptions don't exist in Kotlin
* Don't return null
  * **_What does Kotlin for this?_**
    * Nothing.
  * Though, you need null checks
    * **_What does Kotlin for this?_**
      * Provides a proper syntax for handling `null`
  * you might forget to handle them resulting to NPE
    * **_What does Kotlin for this?_**
      * Compiler `warns` you about handling those nullable types
* Avoid passing null values and introduce some special objects
  * **_What does Kotlin for this?_**
    * Not something special, it's a language agnostic suggestion
* Separate error handling from logic
  * **_What does Kotlin for this?_**
    * Not something special, it's a language agnostic suggestion
    * Though, we can handle this with sealed classes

## Nullable Types

We can mark a type as Nullable, or not
```
val x: Int = 1
val y: Int = null //compile error


val nullableX: Int? = 1
val nullableY: Int? = null

val x = nullableX // compilation error
```

To achieve that last expression, we need to
```
val lastX = if(nullableX == null) -1 else nullableX // that is ok
```

Though, we can make it even shorter, using Elvis operator `?:`
```
val lastX = nullableX ?: -1
```

## Handling null

Given a class with nested types, which some of them can be null
```
data class Person(
  val info: Info?
  ...
)

data class Info(
  val address: Address?
 ...
)
```

then this won't run, because we don't handle null

```
val street = person.info.address.streetName // compile error
```

This compilation error enforces us to deal with null handling and reduces error.

### Options

1. not null assertion `!!`
   > ```
   > val street = person.info!!.address!!.streetName
   > ```
   > The compiler does not warn you anymore, though it throws a NPE if it finds null 
2.  Java style
   > ```
      > > if(person.info == null || person.info.address == null) {
      > >   return null or throw Exception()
      > > }
      > > ```
3. Safe Call operator `?`
   > ```
   > val street = person.info?.address?.streetName ?: null or throw Exception()
   > ```

Summary, Kotlin language helps us writing code which is cleaner, more readable, safer to use and less error prone.
This happens by using some syntactic sugars.


## Beware & not be obsessed

Because Kotlin offers us some great features, that doesn't mean that you should be obsessed with them and used them in every situation.

Firstly, try to avoid to create train-wrecks (huge expressions) and secondly, handling null types
with not understandable and complicated safe operators and elvis structures.

```
//don't
myType?.aMagicMethod()?.let {
  ....
}

fun MyType.aMagicMethod(): Boolean = ....
```
or don't as well
```
//don't
if(myType?.aMagicMethod() ?: false){
  ...
}
```

Keep it simple:
```
if(myType != null && myType.aMagicMethod()){ 
  //...
  myType!! //using !! it is not a problem here, but we need to use it because compiler does not detect  the null check above
}
```
**Readability and simplicity win over the nice Kotlin syntax**



## Last words

In compare with Java, Kotlin helps us write more
robust, cleaner and expressive code.

In general, we don't need:

* to use `new` keyword to create types
* semicolons `;`
* avoid writing types, they can be inferred
* smart-casts

Other great features:
* infix functions
* inline functions
* operator overloading
* shorter setters/getters methods
* lambda outside the parenthesis
* extension functions
























































  














































































































































