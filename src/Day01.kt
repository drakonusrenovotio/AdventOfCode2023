fun main() {

    val numbers = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun manualSearchSolution(input: List<String>): Map<Int, String> {
        val sol = mutableMapOf<Int, String>()
        var i = 0
        while (i < input.size) {
            val s = input[i]
            var firstIndex = s.length
            var firstValue = -1
            var lastIndex = -1
            var lastValue = -1
            for (number in numbers) {
                if (s.contains(number)) {
                    if (s.indexOf(number) < firstIndex) {
                        firstIndex = s.indexOf(number)
                        firstValue = numbers.indexOf(number)
                    }
                    if (s.lastIndexOf(number) > lastIndex) {
                        lastIndex = s.lastIndexOf(number)
                        lastValue = numbers.indexOf(number)
                    }
                }
            }
            for (char in s.indices) {
                if (s[char].isDigit()) {
                    if (char < firstIndex) {
                        firstIndex = char
                        firstValue = s[char].digitToInt()
                    }
                    if (char > lastIndex) {
                        lastIndex = char
                        lastValue = s[char].digitToInt()
                    }
                }
            }
            sol[i] = "$firstValue$lastValue"
            i++
        }
        return sol
    }

    fun replaceSearchSolution(input: List<String>): Map<Int, String> {
        val solutionMap = mutableMapOf<Int, String>()
        var index = 0
        while (index < input.size) {
            var firstValue = 'a'
            var lastValue = 'a'
            var stringWithReplacements = input[index]
            for (number in numbers) {
                stringWithReplacements =
                    stringWithReplacements.replace(number, "$number${numbers.indexOf(number)}$number")
            }

            for (c in stringWithReplacements) {
                if (c.isDigit()) {
                    firstValue = c
                    break
                }
            }
            for (c in stringWithReplacements.reversed()) {
                if (c.isDigit()) {
                    lastValue = c
                    break
                }
            }
            solutionMap[index] = "$firstValue$lastValue"
            index++
        }
        return solutionMap
    }

    fun justTheDigitsSolution(input: List<String>): Int {
        var total = 0
        for (s in input) {

            var firstValue = 'a'
            var lastValue = 'a'

            for (c in s) {
                if (c.isDigit()) {
                    firstValue = c
                    break
                }
            }
            for (c in s.reversed()) {
                if (c.isDigit()) {
                    lastValue = c
                    break
                }
            }
            total += "$firstValue$lastValue".toInt()
        }
        return total
    }

    fun getTotalFromMap(input: Map<Int, String>): Int {
        var total = 0
        for (value in input.values) {
            total += value.toInt()
        }
        return total
    }

    fun findMismatches(sol1: Map<Int, String>, sol2: Map<Int, String>): Map<Int, String> {
        check(sol1.size == sol2.size)
        val mismatches = mutableMapOf<Int, String>()
        var index = 0
        while (index < sol1.size) {
            if (sol1.getValue(index) != sol2.getValue(index)) {
                mismatches[index] = "${sol1.getValue(index)}!=${sol2.getValue(index)}"
            }
            index++
        }
        return mismatches
    }

    val testInput = readInput("Day01_test")

    check(getTotalFromMap(replaceSearchSolution(testInput)) == (43 + 53 + 65 + 82))
    check(getTotalFromMap(manualSearchSolution(testInput)) == (43 + 53 + 65 + 82))

    val input = readInput("Day01")
    val digitsOnly = justTheDigitsSolution(input)
    val manualSearch = manualSearchSolution(input)
    val replaceSearch = replaceSearchSolution(input)
    check(findMismatches(manualSearch, replaceSearch).isEmpty())

    println(
        "Part1=$digitsOnly Replace=${getTotalFromMap(replaceSearch)} Manual=${getTotalFromMap(manualSearch)}"
    )
}
