package com.example.finalattestationreddit.data.pagingsource

data class LoadDataResult<T>(
    internal val responseData: List<T>,
    internal val cursorBefore: String?,
    internal val cursorAfter: String?
)