package app.domain.util.list

inline fun <T> List<T>.forEachReversed(action: (T) -> Unit) {
    for (i in size - 1 downTo 0) {
        action(get(i))
    }
}

inline fun <T> List<T>.forEachReversedIndexed(action: (Int, T) -> Unit) {
    for (i in size - 1 downTo 0) {
        action(i, get(i))
    }
}

fun <T> List<T>.preLast(): T {
    return if (size > 1) get(lastIndex - 1) else last()
}