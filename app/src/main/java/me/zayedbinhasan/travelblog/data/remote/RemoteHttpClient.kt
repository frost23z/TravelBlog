package me.zayedbinhasan.travelblog.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import me.zayedbinhasan.travelblog.domain.model.Blog
import me.zayedbinhasan.travelblog.domain.model.BlogResponse
import me.zayedbinhasan.travelblog.util.NetworkError
import me.zayedbinhasan.travelblog.util.Result

class RemoteHttpClient(
    private val httpClient: HttpClient
) {
    suspend fun getBlogArticle(blogArticlesUrl: String): Result<List<Blog>, NetworkError> {
        val response = try {
            httpClient.get(blogArticlesUrl)
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return when (response.status.value) {
            in 200..299 -> {
                val blogResponse: BlogResponse = response.body()
                Result.Success(blogResponse.data)
            }

            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            403 -> Result.Error(NetworkError.FORBIDDEN)
            404 -> Result.Error(NetworkError.NOT_FOUND)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}