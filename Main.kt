package bullscows

import java.util.*
import kotlin.system.exitProcess

var secret_code = ""

fun generateCode() {
    val scanner = Scanner(System.`in`)
    println("Input the length of the secret code:\n")
    var length = 0
    try {
        length  = scanner.nextInt()
        if ((length == 0) || (length > 36)){
            println("Error: 0 isn't a valid number.")
            exitProcess(0)
        }
    } catch (e: InputMismatchException){
        println("Error: it isn't a valid number.")
        exitProcess(0)
    }

    println("Input the number of possible symbols in the code:\n")
    val symbols: Int = scanner.nextInt()
    if (symbols < length) {
        System.out.println("Error: it's not possible to generate a code with a length of $length with $symbols unique symbols.");
        return
    } else if (symbols > 36) {
        println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z)");
        return
    } else {
        val range = "0123456789abcdefghijklmnopqrstuvwxyz"
        val rangeList = range.split("").toMutableList()
        rangeList.removeAt(0)
        rangeList.removeAt(rangeList.size - 1)
        shrink(rangeList, symbols)
        val finalRangeList: MutableList<String> = MutableList(length) { "" }
        for (i in finalRangeList.size - 1 downTo 0) {
            rangeList.shuffle()
            finalRangeList[i] = rangeList[i]
            rangeList.removeAt(i)
        }
        println("The secret is prepared: %s ".format("*".repeat(length)))
        if (symbols <= 10) {
            println("(0-%d).\n".format(symbols - 1))
        } else {
            println("(0-9,a-%s).\n".format("abcdefghijklmnopqrstuvwxyz"[symbols - 11]))
        }
        secret_code = finalRangeList.joinToString("")
    }
}

fun shrink(rangeList: MutableList<String>, newSize: Int){
    val size = rangeList.size
    if (newSize >= size) return
    for (i in newSize until size) {
        rangeList.removeAt(rangeList.size - 1)
    }
}

fun game() {
    println("Okay, let's start a game!\n")
    var turn = 1
    var cow = 0
    var bull = 0

    while (bull != secret_code.length) {
        println("Turn $turn:")
        turn++
        val guess: String = readln()

        for (i in secret_code.indices) {
            if (secret_code[i] == guess[i]) {
                bull++
            } else if (guess.contains(secret_code[i])) {
                cow++
            }
        }
        print("Grade: ")
        print(
            when {
                bull > 0 && cow > 0 -> "$bull bull(s) and $cow cow(s).\n "
                bull > 0 -> "$bull bull(s).\n "
                cow > 0 -> "$cow cow(s).\n "
                else -> "None.\n "
            }
        )
        if (bull == secret_code.length) {
            break
        }
        bull = 0
        cow = 0
    }
    println("Congratulations! You guessed the secret code.")
}

fun main() {
    generateCode()
    game()
}