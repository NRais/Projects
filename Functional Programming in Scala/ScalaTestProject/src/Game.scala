import scala.io.StdIn._

class Game {

    // Initialize Variables
    val board = Array(
        Array(0, 0, 0),
        Array(0, 0, 0),
        Array(0, 0, 0)
    )
    // ---------------------

    println("(1) Player Name: ")

    val name = readLine().capitalize

    println("(2) Player Name: ")

    val name2 = readLine().capitalize

    println("Hello " + name + " and " + name2 + "! Welcome to the Scala Tic-Tac-Toe Game!")

    printBoard(makeMove(1, board)) // iterative call to play the game


    //  -------  //
    // FUNCTIONS //
    //  -------  //

    def makeMove(player : Integer, oldBoard : Array[Array[Int]]) : Array[Array[Int]] = {
        // check if the game has finished (if so return the board)
        if (gameOver(oldBoard)) {

            oldBoard

        }
        // otherwise ask for them to chuck another piece in
        else {
            printBoard(oldBoard)

            if (player == 2) {
                println(name2 + " (2) would you like to place a piece (ex. 1,3):")
            } else {
                println(name + " (1) would you like to place a piece (ex. 1,3):")
            }

            val input = validateInput(readLine())
            val x = input(0)
            val y = input(1)

            // failed input
            if (x == -1 | y == -1) {

                makeMove(player, oldBoard)
            }
            // successful input
            else {

                //**println("Update + " + x + " " + y)

                //**println("Update2")

                for (i <- 0 until 3) {
                    for (j <- 0 until 3) {

                        // if this is the space you are modifying then do so
                        if (x == i && y == j) {

                            // if there is nothing in this spot currently
                            if (oldBoard(i)(j) == 0) {
                                //**println("Updating " + i + " | " + j + " | b2(i)(j) " + oldBoard(i)(j))
                                oldBoard(i)(j) = player
                            }
                            // if there is then we requery the player
                            else {
                                println("Place your piece in a different place")
                                makeMove(player, oldBoard)
                            }
                        } else {
                            // no change
                        }

                    }
                }

                //**println("Update3")

                makeMove(3 - player, oldBoard)
            }
        }
    }

    def validateInput(input : String) : Array[Integer] = {

        if (input.contains(",")) {
            try {
                //**println("Starting " + input + " | " + input.charAt(0) + " | " + input.charAt(input.length-1))
                val firstInt = Integer.parseInt("" + input.charAt(0))
                val secondInt = Integer.parseInt("" + input.charAt(input.length-1))

                //**println("Success + " + firstInt + " " + secondInt)

                Array(firstInt, secondInt)

            } catch {
                case _: Throwable =>
                    println("Fail, try again");
                    Array(-1,-1);
            }

        } else {
            println("Fail, try again")
            Array(-1, -1)
        }

    }

    def gameOver(board : Array[Array[Int]]): Boolean = {

        // search every space on the board
        for (i <- 0 until 3) {
            for (j <- 0 until 3) {

                if (board(i)(j) == 0) {
                    // return that there is still stuff to do
                    return false
                }

            }
        }

        println("GAME OVER!")
        // if we get here then we must have finished every space
        true
    }


    def printBoard(board : Array[Array[Int]]) = {

        println("     0 1 2")
        println("     _____")


        // search every space on the board
        for (i <- 0 until 3) {
            print(i + " |  ")
            for (j <- 0 until 3) {

                print(board(i)(j) + " ") // print space

            }

            println() // new line
        }
    }
}
