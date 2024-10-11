package app.domain.util.list

fun <T> MutableList<T>.swap(indexOne: Int, indexTwo: Int): MutableList<T> {
    val firstValue = get(indexOne)

    set(indexOne, get(indexTwo))
    set(indexTwo, firstValue)

    return this
}

fun <T> MutableList<T>.swapWithLast(index: Int): MutableList<T> = swap(index, lastIndex)