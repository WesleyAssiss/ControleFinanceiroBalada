package com.ifmg.ControleFinanceiro.modelo

data class Product(
    val id: Int,
    val name: String,
    val value: Double,
    val date: String,
    val isEntry: Boolean
)