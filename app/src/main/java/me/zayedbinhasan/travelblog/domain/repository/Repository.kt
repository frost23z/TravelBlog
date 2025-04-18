package me.zayedbinhasan.travelblog.domain.repository

import kotlinx.coroutines.flow.Flow
import me.zayedbinhasan.travelblog.domain.model.Blog
import me.zayedbinhasan.travelblog.util.Error
import me.zayedbinhasan.travelblog.util.NetworkError
import me.zayedbinhasan.travelblog.util.Result

interface Repository {
    // Remote to local
    suspend fun getBlogsFromRemoteToLocal(): Result<List<Blog>, NetworkError>

    // Load blogs from local
    fun getBlogsFromLocal(): Result<Flow<List<Blog>>, Error>

    fun getBlogsFromLocalById(id: Int): Result<Flow<Blog>, Error>
}