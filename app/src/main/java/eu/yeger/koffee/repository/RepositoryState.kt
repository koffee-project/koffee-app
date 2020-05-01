package eu.yeger.koffee.repository

sealed class RepositoryState {
    object Idle : RepositoryState()
    object Refreshing : RepositoryState()
    object Done : RepositoryState()
    class Error(val exception: Exception) : RepositoryState()
}
