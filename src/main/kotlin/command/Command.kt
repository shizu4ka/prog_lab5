package command

import PrintResult

interface Command {
    fun execute(): PrintResult?
}