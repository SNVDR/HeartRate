package dev.snvdr.heart.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.snvdr.heart.database.models.HeartRateDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface HeartRateDAO{

    @Insert
    suspend fun insert(heartRateDBO: HeartRateDBO):Long
    @Query("SELECT * FROM heartRates")
    fun observeAll():Flow<List<HeartRateDBO>>
    @Query("SELECT * FROM heartRates WHERE id = :id LIMIT 1")
    suspend fun get(id:Long):HeartRateDBO?
    @Query("DELETE FROM heartRates")
    suspend fun clean()
}