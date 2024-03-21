


data class Human(val name: String, val surname: String, val id: Int)
data class Human2(val name: Name, val surname: Surname, val id: Id)

@JvmInline
value class Name(private val value: String) {
    init {
        validateName(value)
    }
    private fun validateName(value: String){
        if(value.isEmpty())
            error("not accepted name")
    }
}
@JvmInline
value class Id(private val value: Int) {
    init {
        if(value < 0)
            error("negative, not allowed")
    }
}

@JvmInline
value class Surname(private val value: String) {
    init {
        validateName(value)
    }
    private fun validateName(value: String){
        if(value.isEmpty() || value.length > 20)
            error("not accepted surname")
    }
}


fun main() {
//    Human("john", "surname", 1) // ok
//    Human("surname", "john", 1) //wrong values given
//    Human("surname", "john", -1) //contains a second error as well
//    Human(
//        surname = "surname",
//        name = "john",
//        id = -1) //better

    try {
        val name = Name("")
        val surname = Surname("surname")
        val human = Human2(name, surname, Id(-1))
        println(human) //throws exception
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}