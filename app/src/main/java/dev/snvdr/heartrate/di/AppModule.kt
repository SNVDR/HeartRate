package dev.snvdr.heartrate.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.snvdr.data.HeartRateRepository
import dev.snvdr.heart.database.HeartRateDatabase
import dev.snvdr.heartrate.features.navigation.navigator.AppNavigator
import dev.snvdr.heartrate.features.navigation.navigator.AppNavigatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesHeartRateDatabase(@ApplicationContext context: Context):HeartRateDatabase{
        return HeartRateDatabase(context)
    }

    @Provides
    @Singleton
    fun providesAppNavigator():AppNavigator{
        return AppNavigatorImpl()
    }

}