import java.lang.Math.*

data class Query(val ownerId: String?, val lat: Double?, val long: Double?, val maxDistance: Double?, val limit: Int?)

data class Address(val country: String? = null, val zipcode: String? = null, val city: String? = null, val street: String? = null, val lat: Double? = null, val long: Double? = null)
data class Location(val id: String, val ownerId: String, val address: Address)

data class Point(val lat: Double, val long: Double)

data class DistanceAndLocation(val location: Location, val distance: Double?)

fun search(query: Query, locations: List<Location>): List<DistanceAndLocation> {
    return locations
            .filter { it.ownerId == query.ownerId }
            .map { DistanceAndLocation(it, distance(point(it.address.lat, it.address.long), point(query.lat, query.long))) }
            .filter {
                if (query.maxDistance != null && it.distance != null) it.distance < query.maxDistance
                else query.maxDistance == null
            }
            .sortedBy { it.distance }
            .take(query.limit ?: locations.size)
}

fun point(lat: Double?, long: Double?): Point? {
    if (lat == null) return null
    if (long == null) return null

    return Point(lat, long)
}

fun distance(pointA: Point?, pointB: Point?): Double? {
    if (pointA == null) return null
    if (pointB == null) return null

    val deltaLat = toRadians(pointB.lat - pointA.lat)
    val deltaLong = toRadians(pointB.long - pointA.long)
    val a = pow(sin(deltaLat / 2), 2.0) + cos(toRadians(pointA.lat)) * cos(toRadians(pointB.lat)) * pow(sin(deltaLong / 2), 2.0)
    val greatCircleDistance = 2 * atan2(sqrt(a), sqrt(1 - a))
    return 6378.137 * greatCircleDistance
}


fun main(args: Array<String>) {
    search(
            Query(ownerId = "MMDE", long = 100.0, lat = 20.0, maxDistance = 800.0, limit = 3),
            listOf(
                    Location(id = "MMMUC01", ownerId =  "MMDE", address = Address(long = 100.0, lat = 20.0)),
                    Location(id = "MMMUC02", ownerId =  "MMDE", address = Address(long = 105.0, lat = 25.0)),
                    Location(id = "MMMUC03", ownerId =  "MMDE", address = Address(long = 130.0, lat = 50.0)),
                    Location(id = "SEMUC01", ownerId =  "SEDE", address = Address(long = 100.0, lat = 20.0)),
                    Location(id = "SEMUC02", ownerId =  "SEDE", address = Address(long = 100.0, lat = 20.0)),
                    Location(id = "MMBER01", ownerId =  "MMDE", address = Address(long = 400.0, lat = 200.0))
            )
    ).forEach { println(it) }
}