package com.example.finalattestationreddit.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.finalattestationreddit.log.TAG

abstract class BasePagingSource<T : Any> : PagingSource<String, T>() {

    override fun getRefreshKey(state: PagingState<String, T>) = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, T> {
        val page = params.key

        kotlin.runCatching {

            loadData(params)

        }.fold(
            onSuccess = { data: Pair<List<T>, Map<String, String?>> ->
                return LoadResult.Page(
                    data = data.first,
                    prevKey = data.second[CURSOR_BEFORE],
                    nextKey = data.second[CURSOR_AFTER]
                )
            },
            onFailure = { exception ->
                Log.e(
                    TAG, "${::load.name}: onFailure: page = $page, error: ${exception.message}"
                )
                return LoadResult.Error(exception)
            })
    }

    protected abstract suspend fun loadData(params: LoadParams<String>):
            Pair<List<T>, Map<String, String?>>

    internal companion object {
        internal const val CURSOR_BEFORE = "before"
        internal const val CURSOR_AFTER = "after"
        internal const val CURSOR_FIRST_PAGE = ""
    }

}