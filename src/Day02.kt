import java.util.stream.Collectors

fun main() {

    fun getGamesFromInput(input: List<String>): List<Game> {
        return input
            .stream()
            .map { Game(it) }
            .collect(Collectors.toList())
    }

    fun sumPossibleGameIds(games: List<Game>, redCount: Int, greenCount: Int, blueCount: Int): Int {
        return games
            .stream()
            .filter { it.gameIsPossible(redCount, greenCount, blueCount) }
            .map(Game::gameId)
            .collect(Collectors.toList())
            .sum()
    }

    fun sumGamePowers(games: List<Game>): Int {
        return games
            .stream()
            .map { it.getGamePower() }
            .collect(Collectors.toList())
            .sum()
    }

    fun checkRound(round: GameRound, red: Int, green: Int, blue: Int): Boolean {
        return round.redCount == red && round.greenCount == green && round.blueCount == blue
    }

    fun checkMinViable(minMap: Map<String, Int>, minRed: Int, minGreen: Int, minBlue: Int): Boolean {
        return minMap.getValue("red") == minRed && minMap.getValue("green") == minGreen && minMap.getValue("blue") == minBlue
    }

    val testInput = readInput("Day02_test")
    val testGames = getGamesFromInput(testInput)
    val testPossibleRed = 12
    val testPossibleGreen = 13
    val testPossibleBlue = 14

    check(testGames.count() == 5)
    check(sumPossibleGameIds(testGames, testPossibleRed, testPossibleGreen, testPossibleBlue) == 8)
    check(sumGamePowers(testGames) == 2286)
    val game1 = testGames[0]
    check(game1.gameId == 1)
    check(checkRound(game1.gameRounds[0], 4, 0, 3))
    check(checkRound(game1.gameRounds[1], 1, 2, 6))
    check(checkRound(game1.gameRounds[2], 0, 2, 0))
    check(game1.gameIsPossible(testPossibleRed, testPossibleGreen, testPossibleBlue))
    check(checkMinViable(game1.getMinViableCounts(), 4, 2, 6))
    check(game1.getGamePower() == 48)
    val game2 = testGames[1]
    check(game2.gameId == 2)
    check(checkRound(game2.gameRounds[0], 0, 2, 1))
    check(checkRound(game2.gameRounds[1], 1, 3, 4))
    check(checkRound(game2.gameRounds[2], 0, 1, 1))
    check(game2.gameIsPossible(testPossibleRed, testPossibleGreen, testPossibleBlue))
    check(checkMinViable(game2.getMinViableCounts(), 1, 3, 4))
    check(game2.getGamePower() == 12)
    val game3 = testGames[2]
    check(game3.gameId == 3)
    check(checkRound(game3.gameRounds[0], 20, 8, 6))
    check(checkRound(game3.gameRounds[1], 4, 13, 5))
    check(checkRound(game3.gameRounds[2], 1, 5, 0))
    check(!game3.gameIsPossible(testPossibleRed, testPossibleGreen, testPossibleBlue))
    check(checkMinViable(game3.getMinViableCounts(), 20, 13, 6))
    check(game3.getGamePower() == 1560)
    val game4 = testGames[3]
    check(game4.gameId == 4)
    check(checkRound(game4.gameRounds[0], 3, 1, 6))
    check(checkRound(game4.gameRounds[1], 6, 3, 0))
    check(checkRound(game4.gameRounds[2], 14, 3, 15))
    check(!game4.gameIsPossible(testPossibleRed, testPossibleGreen, testPossibleBlue))
    check(checkMinViable(game4.getMinViableCounts(), 14, 3, 15))
    check(game4.getGamePower() == 630)
    val game5 = testGames[4]
    check(game5.gameId == 5)
    check(checkRound(game5.gameRounds[0], 6, 3, 1))
    check(checkRound(game5.gameRounds[1], 1, 2, 2))
    check(game5.gameIsPossible(testPossibleRed, testPossibleGreen, testPossibleBlue))
    check(checkMinViable(game5.getMinViableCounts(), 6, 3, 2))
    check(game5.getGamePower() == 36)

    val possibleRed = 12
    val possibleGreen = 13
    val possibleBlue = 14
    val sumOfPossibleGames = 2551
    val sumOfGamePowers = 62811

    val input = readInput("Day02")
    val games = getGamesFromInput(input)
    val computedSumOfPossiblesGames = sumPossibleGameIds(games, possibleRed, possibleGreen, possibleBlue)
    val computedSumOfGamePowers = sumGamePowers(games)
    check(computedSumOfPossiblesGames == sumOfPossibleGames)
    println(computedSumOfPossiblesGames)
    check(computedSumOfGamePowers == sumOfGamePowers)
    println(computedSumOfGamePowers)
}

class Game(s: String) {
    var gameId: Int = -1
    var gameRounds: List<GameRound>

    init {
        val split = s.split(':')
        gameId = split[0].trim().split(' ')[1].toInt()
        gameRounds = getRoundsFromLine(split[1].trim())
    }

    private fun getRoundsFromLine(s: String): List<GameRound> {
        return s.trim().split(";")
            .stream()
            .map { GameRound(it.trim()) }
            .collect(Collectors.toList())
    }

    fun gameIsPossible(redCount: Int, greenCount: Int, blueCount: Int): Boolean {
        return gameRounds
            .stream()
            .allMatch { it.roundIsPossible(redCount, greenCount, blueCount) }
    }

    fun getMinViableCounts(): Map<String, Int> {
        return mutableMapOf<String, Int>(
            "red" to gameRounds.stream().map(GameRound::redCount).collect(Collectors.toList()).max(),
            "green" to gameRounds.stream().map(GameRound::greenCount).collect(Collectors.toList()).max(),
            "blue" to gameRounds.stream().map(GameRound::blueCount).collect(Collectors.toList()).max()
        )
    }

    fun getGamePower(): Int {
        return getMinViableCounts().values.reduce { acc, i -> acc * i }
    }
}

class GameRound(string: String) {
    var redCount: Int = 0
    var greenCount: Int = 0
    var blueCount: Int = 0

    init {
        val split = string.split(',')
        for (s in split) {
            val count = s.trim().split(' ')
            if (count[1] == "red") {
                redCount = count[0].toInt()
            }
            if (count[1] == "green") {
                greenCount = count[0].toInt()
            }
            if (count[1] == "blue") {
                blueCount = count[0].toInt()
            }
        }
    }

    fun roundIsPossible(possibleRed: Int, possibleGreen: Int, possibleBlue: Int): Boolean {
        return (redCount <= possibleRed && greenCount <= possibleGreen && blueCount <= possibleBlue)
    }
}