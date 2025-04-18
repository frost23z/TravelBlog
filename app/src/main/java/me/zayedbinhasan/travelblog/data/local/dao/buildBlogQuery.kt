package me.zayedbinhasan.travelblog.data.local.dao

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import me.zayedbinhasan.travelblog.ui.screen.list.Sort
import me.zayedbinhasan.travelblog.ui.screen.list.SortOrder

fun buildBlogQuery(
    sortBy: Sort = Sort.DEFAULT,
    order: SortOrder = SortOrder.ASCENDING
): SupportSQLiteQuery {
    val safeSortBy = when (sortBy) {
        Sort.DEFAULT -> "id"
        Sort.TITLE -> "title"
        Sort.DATE -> "date"
    }
    val safeOrder = when (order) {
        SortOrder.ASCENDING -> "ASC"
        SortOrder.DESCENDING -> "DESC"
    }
    val query = "SELECT * FROM blogs ORDER BY $safeSortBy $safeOrder"
    return SimpleSQLiteQuery(query)
}
