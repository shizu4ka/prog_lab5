sealed class Command {
    sealed class Type {
        object Help : Type()
        object Info : Type()
        object Show : Type()
        object Add : Type()
        object Update : Type()
        object RemoveById : Type()
        object RemoveAt : Type()
        object RemoveLast : Type()
        object Clear : Type()
        object AddIfMax : Type()
        object FilterByStandardOfLiving : Type()
        object FilterStartsWithName : Type()
        object FilterGreaterThanClimate : Type()
        object ExecuteScript : Type()
        object Exit : Type()
        object Save : Type()
    }
}