package org.example.game2048


fun <T : Any> List<T?>.moveAndMergeEqual(merge: (T) -> T): List<T> {
    val result = mutableListOf<T>()
    val nonNullables = this.filter { it != null }
    var i = 0
    while (i in 0 until nonNullables.size) {
        nonNullables[i]?.let {
            if (i + 1 < nonNullables.size && nonNullables[i] == nonNullables[i + 1]) {
                result.add(merge(it))
                i += 2
                return@let
            } else {
                result.add(it)
                i++
            }
        }
    }

    return result

}
