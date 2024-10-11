package app.domain.util.list

fun <T> MutableList<T>.swap(indexOne: Int, indexTwo: Int) {
    val firstValue = get(indexOne)

    set(indexOne, get(indexTwo))
    set(indexTwo, firstValue)
}

fun <T> MutableList<T>.swapWithLast(index: Int) {
    swap(index, lastIndex)
}