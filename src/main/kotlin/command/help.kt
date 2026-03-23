package command

import PrintResult

class HelpCommand : Command {
    val info =
        """help : display help for available commands
info : print information about the collection to the standard output (type, initialization date, number of elements, etc.)
show : print all elements of the collection in string representation to the standard output
add {element} : add a new element to the collection
update id {element} : update the value of the collection element whose ID matches the given one
remove_by_id id : remove an element from the collection by its ID
clear : clear the collection
save : save the collection to a file
execute_script file_name : read and execute a script from the specified file. The script contains commands in the same format as used in interactive mode.
exit : terminate the program (without saving to a file)
remove_at index : remove the element at the specified position (index) in the collection
remove_last : remove the last element from the collection
add_if_max {element} : add a new element to the collection if its value exceeds the value of the largest element in the collection
filter_by_standard_of_living standardOfLiving : display elements whose standardOfLiving field value matches the specified one
filter_starts_with_name name : display elements whose name field value starts with the specified substring
filter_greater_than_climate climate : display elements whose climate field value is greater than the specified one
"""
    var result = PrintResult(
        String = info
    )

    override fun execute(): PrintResult {
        return result
    }
}