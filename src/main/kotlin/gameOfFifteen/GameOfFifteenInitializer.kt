package org.example.gameOfFifteen



interface GameOfFifteenInitializer {

    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {

    override val initialPermutation by lazy {
        val randomList = generateRandomList().toMutableList()
        //[3,1,8,5,2] is !Even inversions are : 2+0+2+1 5(odd)
        // if we swaped last two elements, this will eliminate the last inversion which makes the total 2+0+2+0 = 4(even)
        if (!isEven(randomList)) {
            val lastIndex = randomList.lastIndex
            val temp = randomList[lastIndex-1]
            randomList[lastIndex-1] = randomList[lastIndex]
            randomList[lastIndex] = temp
        }
        return@lazy randomList
    }


    private fun generateRandomList(): List<Int> {
        return generateSequence(1) {
            it + 1
        }.take(15).toList().shuffled()
    }
}


