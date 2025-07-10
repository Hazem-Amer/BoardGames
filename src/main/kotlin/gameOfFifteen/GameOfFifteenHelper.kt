package org.example.gameOfFifteen

fun isEven(permutation: List<Int>): Boolean {
    var numberOfInversions = 0
    val size = permutation.size
    for (i in 0..size - 2) {
        for (j in i+1..size - 1) {
            if (permutation[i] < permutation[j]) {
                continue
            } else numberOfInversions++
        }
    }
    return numberOfInversions % 2 == 0
}