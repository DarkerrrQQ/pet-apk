package com.petspick.app

import android.content.Context
import com.petspick.app.api.FirebaseRepository
import com.petspick.app.data.storage.MainDatabase
import com.petspick.app.data.storage.dao.Dao
import com.petspick.app.data.storage.repository.BDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {

    @Provides
    fun provideFirebaseRepository(): FirebaseRepository {
        return FirebaseRepository()
    }

    @Provides
    fun provideBDRepository(@ApplicationContext context: Context): BDRepository {
        return BDRepository(MainDatabase.getDatabase(context).dao())
    }

}