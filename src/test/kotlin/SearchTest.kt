import kotlin.test.assertEquals
import org.junit.Test as test

class SearchTest {
    @test fun `Does not filter if maxDistance is not given`() {
        assertEquals(
                search(
                        Query(ownerId = "MMDE", long = 100.0, lat = 20.0, maxDistance = null, limit = 3),
                        listOf(
                                Location(id = "MMMUC01", ownerId = "MMDE", address = Address(long = 100.0, lat = null))
                        )
                ),
                listOf(
                        DistanceAndLocation(
                                Location(id = "MMMUC01", ownerId = "MMDE", address = Address(long = 100.0, lat = null)),
                                null
                        )
                )
        )
    }

    @test fun `Filters locations with incomplete coordinates if maxDistance is given`() {
        assertEquals(
                search(
                        Query(ownerId = "MMDE", long = 100.0, lat = 20.0, maxDistance = 800.0, limit = 3),
                        listOf(
                                Location(id = "MMMUC01", ownerId =  "MMDE", address = Address(long = 100.0, lat = null))
                        )
                ),
                listOf()
        )
    }

    @test fun `Filters locations by maxDistance and ownerId`() {
        assertEquals(
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
                ).map { it.location },
                listOf(
                        Location(id = "MMMUC01", ownerId =  "MMDE", address = Address(long = 100.0, lat = 20.0)),
                        Location(id = "MMMUC02", ownerId =  "MMDE", address = Address(long = 105.0, lat = 25.0))
                )
        )
    }
}