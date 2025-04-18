package me.zayedbinhasan.travelblog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.zayedbinhasan.travelblog.data.local.entity.BlogEntity

@Dao
interface BlogDao {
    @Query("SELECT * FROM blogs ORDER BY id ASC")
    fun getAllBlogs(): Flow<List<BlogEntity>>

    @Query("SELECT * FROM blogs WHERE id = :id")
    fun getBlogById(id: Int): Flow<BlogEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlogs(blogs: List<BlogEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlog(blog: BlogEntity)
}