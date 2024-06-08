package com.example.reminderapp.singleresult

class SearchResult : SingleResult<SearchEvents>() {
    internal suspend fun postEvent(result: SearchEvents) {
        postEventInternal(result)
    }
}

sealed class SearchEvents {
    class QueryChanged(val text: String) : SearchEvents()
}