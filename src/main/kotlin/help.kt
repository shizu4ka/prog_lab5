class HelpCommand : Command {
    override fun execute() {
        println(
            "help : display help for available commands\n" +
                    "info : output collection information to standard output (type, initialization date, number of elements, etc.)\n" +
                    "show : output all collection elements to standard output in string representation\n" +
                    "add {element} : add a new element to the collection\n" +
                    "update id {element} : update the value of the collection element with the specified id\n" +
                    "remove_by_id id : remove an element from the collection by its id\n" +
                    "clear : clear the collection\n" +
                    "save : save the collection to a file\n" +
                    "execute_script file_name : read and execute a script from the specified file. The script contains commands in the same format as entered by the user in interactive mode.\n" +
                    "exit : terminate the program (without saving to file)\n" +
                    "insert_at index {element} : insert a new element at the specified position\n" +
                    "remove_first : remove the first element from the collection\n" +
                    "history : display the last 8 commands (without their arguments)\n" +
                    "group_counting_by_heart_count : group collection elements by heartCount field value, output the number of elements in each group\n" +
                    "filter_less_than_melee_weapon meleeWeapon : output elements whose meleeWeapon field value is less than the specified one\n" +
                    "print_field_descending_chapter : output chapter field values of all elements in descending order"
        )
    }
}