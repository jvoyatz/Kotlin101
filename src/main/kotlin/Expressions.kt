import java.util.Locale

fun main() {
    println(defineWhatCharIs('j'))
}
 val someChars = arrayOf('I', "N", "J")
 val otherChars = arrayOf('A', "B", "C")

fun defineWhatCharIs(char: Char): String {
    if(char in someChars) {
        return "some chars"
    } else if(char in otherChars) {
        return "other chars"
    } else {
        return "other"
    }
}

fun defineWhatCharisKotlin(char: Char): String = when (char) {
    in someChars -> "some chars"
    in otherChars -> "other chars"
    else -> "other"
}