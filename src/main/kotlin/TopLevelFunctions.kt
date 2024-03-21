

object Utils {
    fun isEmpty(string: String?): Boolean {
        return string.isNullOrBlank()
    }
}


fun String?.isEmpty() = isNullOrEmpty()