package me.zayedbinhasan.travelblog.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.zayedbinhasan.travelblog.data.local.LocalDatabase
import me.zayedbinhasan.travelblog.data.local.dao.buildBlogQuery
import me.zayedbinhasan.travelblog.data.local.entity.toBlog
import me.zayedbinhasan.travelblog.data.local.entity.toBlogEntity
import me.zayedbinhasan.travelblog.data.remote.BLOG_ARTICLES_URL
import me.zayedbinhasan.travelblog.data.remote.RemoteHttpClient
import me.zayedbinhasan.travelblog.domain.model.Blog
import me.zayedbinhasan.travelblog.domain.repository.Repository
import me.zayedbinhasan.travelblog.ui.screen.list.Sort
import me.zayedbinhasan.travelblog.ui.screen.list.SortOrder
import me.zayedbinhasan.travelblog.util.Error
import me.zayedbinhasan.travelblog.util.NetworkError
import me.zayedbinhasan.travelblog.util.Result
import me.zayedbinhasan.travelblog.util.onError
import me.zayedbinhasan.travelblog.util.onSuccess

class RepositoryImpl(
    private val localDatabase: LocalDatabase,
    private val remoteHttpClient: RemoteHttpClient
) : Repository {
    override suspend fun getBlogsFromRemoteToLocal(): Result<List<Blog>, NetworkError> {
        val result = remoteHttpClient.getBlogArticle(BLOG_ARTICLES_URL)
        result.onSuccess { blogs ->
            localDatabase.blogDao().insertBlogs(blogs.map { it.toBlogEntity() })
        }.onError { error ->
            return Result.Error(error)
        }
        return result
    }

    override fun getBlogsFromLocal(
        sort: Sort,
        sortOrder: SortOrder
    ): Result<Flow<List<Blog>>, Error> {

        val result = try {
            localDatabase.blogDao().getAllBlogs(
                query = buildBlogQuery(sort, sortOrder)
            ).map { blogEntities -> blogEntities.map { it.toBlog() } }
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }
        return Result.Success(result)
    }

    override fun getBlogsFromLocalById(id: Int): Result<Flow<Blog>, Error> {
        val result = try {
            localDatabase.blogDao().getBlogById(id)
                .map { blogEntity -> blogEntity?.toBlog() ?: throw Exception("Blog not found") }
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }
        return Result.Success(result)
    }
}