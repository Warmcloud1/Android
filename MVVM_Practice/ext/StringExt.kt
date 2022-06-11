package ext
//將逗號修剪掉
fun String.trimComma(): String {
    if (contains(',')) {
        return replace(",", "")
    }
    return this
}
