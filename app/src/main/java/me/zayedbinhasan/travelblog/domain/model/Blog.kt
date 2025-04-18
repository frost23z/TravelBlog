package me.zayedbinhasan.travelblog.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Blog(
    val id: Int,
    val author: Author,
    val title: String,
    val date: String,
    val image: String,
    val description: String,
    val views: Int,
    val rating: Float
)

@Serializable
data class Author(
    val name: String,
    val avatar: String
)

@Serializable
data class BlogResponse(
    val data: List<Blog>
)