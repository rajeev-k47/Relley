package net.runner.relley.fn

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    val id: Long?=null,
    val name: String?=null,
    val full_name: String?=null,
    val private: Boolean?=null,
    val owner: Owner?=null,
    val htmlUrl: String?=null,
    val description: String?,
    val fork: Boolean?=null
)

@Serializable
data class Owner(
    val login: String?=null,
    val id: Long?=null,
    val avatar_url: String?=null
)
