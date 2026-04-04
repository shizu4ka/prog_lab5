class Response(val message: String?)

class CommandProcessor {

    // Process Help command and return help message
    fun processHelp(): Response {
        return Response("Help message goes here.")
    }

    // Process Info command and return collection info
    fun processInfo(): Response {
        return Response("Collection information goes here.")
    }

    // Process Show command to return all cities sorted
    fun processShow(cities: List<City>): Response {
        val sortedCities = cities.sortedBy { it.name }
        return Response(sortedCities.toString())
    }

    // Process Add command to add a city to collection
    fun processAdd(city: City): Response {
        // Logic to add city to collection
        return Response("City added.")
    }

    // Process Update command to update a city
    fun processUpdate(city: City): Response {
        // Logic to update the city
        return Response("City updated.")
    }

    // Process RemoveById command to remove by id
    fun processRemoveById(id: Int): Response {
        // Logic to remove city by ID
        return Response("City removed by ID.")
    }

    // Process RemoveAt command to remove at index
    fun processRemoveAt(index: Int): Response {
        // Logic to remove city at index
        return Response("City removed at index.")
    }

    // Process RemoveLast command to remove last city
    fun processRemoveLast(): Response {
        // Logic to remove the last city
        return Response("Last city removed.")
    }

    // Process Clear command to clear collection
    fun processClear(): Response {
        // Logic to clear collection
        return Response("Collection cleared.")
    }

    // Process AddIfMax to add if area is maximum using Stream API
    fun processAddIfMax(city: City): Response {
        // Logic to add if area's maximum
        return Response("City added if max.")
    }

    // Process FilterByStandardOfLiving using stream filter
    fun processFilterByStandardOfLiving(standard: Int, cities: List<City>): Response {
        val filteredCities = cities.filter { it.standardOfLiving >= standard }
        return Response(filteredCities.toString())
    }

    // Process FilterStartsWithName to filter by name prefix
    fun processFilterStartsWithName(prefix: String, cities: List<City>): Response {
        val filteredCities = cities.filter { it.name.startsWith(prefix) }
        return Response(filteredCities.toString())
    }

    // Process FilterGreaterThanClimate to filter by climate
    fun processFilterGreaterThanClimate(climate: String, cities: List<City>): Response {
        val filteredCities = cities.filter { it.climate.compareTo(climate) > 0 }
        return Response(filteredCities.toString())
    }
}