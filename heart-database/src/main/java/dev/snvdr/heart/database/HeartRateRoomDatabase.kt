package dev.snvdr.heart.database

import dev.snvdr.heart.database.utils.Converters
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.snvdr.heart.database.dao.HeartRateDAO
import dev.snvdr.heart.database.models.HeartRateDBO


class HeartRateDatabase internal constructor(private val database: HeartRateRoomDatabase){
    val heartRateDAO: HeartRateDAO
        get() = database.heartRateDao()
}
@Database(entities = [HeartRateDBO::class], version = 1)
@TypeConverters(value = [Converters::class])
internal abstract class HeartRateRoomDatabase: RoomDatabase(){

    abstract fun heartRateDao(): HeartRateDAO
}

fun HeartRateDatabase(applicationContext: Context):HeartRateDatabase{
    val heartRateRoomDatabase =  Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        HeartRateRoomDatabase::class.java,
        "heartRate"
    ).build()
    return HeartRateDatabase(heartRateRoomDatabase)
}