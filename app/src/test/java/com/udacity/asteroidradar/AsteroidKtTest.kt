package com.udacity.asteroidradar

import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import org.junit.Assert.*
import org.junit.Test

class AsteroidKtTest {
    private val asteroids: ArrayList<Asteroid> = arrayListOf(
            Asteroid(
                    1,
                    "Asteroid1",
                    "00-00-00",
                    0.4,
                    0.5,
                    0.6,
                    5.6,
                    true
            ),
            Asteroid(
                    1,
                    "Asteroid2",
                    "01-01-01",
                    0.6,
                    0.9,
                    0.1,
                    7.6,
                    false
            )
    )

    private val databaseAsteroids: ArrayList<DatabaseAsteroid> = arrayListOf(
            DatabaseAsteroid(
                    1,
                    "Asteroid1",
                    "00-00-00",
                    0.4,
                    0.5,
                    0.6,
                    5.6,
                    true
            ),
            DatabaseAsteroid(
                    1,
                    "Asteroid2",
                    "01-01-01",
                    0.6,
                    0.9,
                    0.1,
                    7.6,
                    false
            )
    )

    private val databaseAsteroids2: ArrayList<DatabaseAsteroid>? = null

    @Test
    fun testAsteroidListConvertsCorrectly() {
        assertEquals(databaseAsteroids, asteroids.asDatabaseModel())
    }

    @Test
    fun testDatabaseAsteroidListNotNull() {
        assertNotNull(asteroids.asDatabaseModel())
    }

    @Test
    fun testDatabaseAsteroidListConvertsCorrectly() {
        assertEquals(asteroids, databaseAsteroids.asDomainModel())
    }

    @Test
    fun testAsteroidListItemsNotEquals() {
        assertNotEquals(databaseAsteroids.asDomainModel()[0], databaseAsteroids.asDomainModel()[1])
    }

    @Test
    fun testAsteroidListConvertsToNull() {
        assertNull(databaseAsteroids2?.asDomainModel())
    }
}
