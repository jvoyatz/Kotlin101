
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

 class Person2() {
    var name: String = ""
    var surname: String = ""
    var age: Int = 0
    var nickName: String = ""
}
fun main() {

    Person2().apply {
        name = "name"
        surname = "surname"
        age = 20
        nickName = "nickname"
    }
    Person.builder()
        .setName("name")
        .setSurname("surname")
        .setAge(20)
        .build()
}