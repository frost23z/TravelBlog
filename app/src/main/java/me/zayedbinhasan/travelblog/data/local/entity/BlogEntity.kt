package me.zayedbinhasan.travelblog.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.zayedbinhasan.travelblog.domain.model.Author
import me.zayedbinhasan.travelblog.domain.model.Blog

@Entity(tableName = "blogs")
data class BlogEntity(
    @PrimaryKey
    val id: Int,
    @Embedded
    val author: AuthorEntity,
    val title: String,
    val date: String,
    val image: String,
    val description: String,
    val views: Int,
    val rating: Float
)

data class AuthorEntity(
    val name: String,
    val avatar: String
)

fun BlogEntity.toBlog(): Blog {
    return Blog(
        id = id,
        author = Author(
            name = author.name,
            avatar = author.avatar
        ),
        title = title,
        date = date,
        image = image,
        description = description,
        views = views,
        rating = rating
    )
}

fun Blog.toBlogEntity(): BlogEntity {
    return BlogEntity(
        id = id,
        author = AuthorEntity(
            name = author.name,
            avatar = author.avatar
        ),
        title = title,
        date = date,
        image = image,
        description = description,
        views = views,
        rating = rating
    )
}

fun List<BlogEntity>.toDomainList() = map { it.toBlog() }