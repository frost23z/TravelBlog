package me.zayedbinhasan.travelblog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.zayedbinhasan.travelblog.data.local.dao.BlogDao
import me.zayedbinhasan.travelblog.data.local.entity.BlogEntity

@Database(entities = [BlogEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun blogDao(): BlogDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "travel_blog_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}