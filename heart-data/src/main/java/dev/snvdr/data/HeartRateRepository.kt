package dev.snvdr.data

import dev.snvdr.data.models.HeartRateDTO
import dev.snvdr.data.utils.toHeartRateDTO
import dev.snvdr.data.utils.toHeartRateDBO
import dev.snvdr.heart.database.HeartRateDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HeartRateRepository @Inject constructor(
    private val database: HeartRateDatabase
) {

    suspend fun addHeartRate(heartRateDTO: HeartRateDTO):Long {
        return database.heartRateDAO.insert(heartRateDTO.toHeartRateDBO())
    }

    fun observeAllHeartRates(): Flow<List<HeartRateDTO>> {
        return database.heartRateDAO.observeAll()
            .map { dbos -> dbos.map { dbo -> dbo.toHeartRateDTO() } }
    }

    suspend fun getHeartRateById(id:Long):HeartRateDTO?{
        return database.heartRateDAO.get(id)?.toHeartRateDTO()
    }

    suspend fun clearHearRates(){
        database.heartRateDAO.clean()
    }

}