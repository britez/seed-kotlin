package com.kiwi.seed.domain

data class Pokemon(
    val id:Long,
    val name:String,
    val type:String,
    val abilities: List<Ability>)