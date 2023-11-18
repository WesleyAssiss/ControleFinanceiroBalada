package com.ifmg.ControleFinanceiro.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifmg.ControleFinanceiro.databinding.ActivityResumoBinding
import com.ifmg.ControleFinanceiro.dados.DBHelper
import java.text.NumberFormat
import java.util.Locale

class ResumoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var binding: ActivityResumoBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        // Chamar exibirResumo() diretamente ao criar a atividade
        exibirResumo()
    }

    private fun exibirResumo() {
        val produtos = dbHelper.getProducts()
        var totalEntrada = 0.0
        var totalGasto = 0.0

        val listaEntradas = mutableListOf<String>()
        val listaGastos = mutableListOf<String>()

        for (produto in produtos) {
            if (produto.isEntry) {
                totalEntrada += produto.value
                listaEntradas.add("Data: ${produto.date}, Valor: R$ ${produto.value}")
            } else {
                totalGasto += produto.value
                listaGastos.add("Data: ${produto.date}, Valor: R$ ${produto.value}")
            }
        }

        // Calcular o total disponível
        val totalDisponivel = totalEntrada - totalGasto
        // Exibir os resultados
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        binding.caixaTxt.text = currencyFormat.format(totalEntrada)
        binding.gastoTxt.text = currencyFormat.format(totalGasto)
        binding.totalTxt.text = currencyFormat.format(totalDisponivel)

        // Exibir a lista de entradas
        binding.entradasListTxt.text = listaEntradas.joinToString("\n")

        // Exibir a lista de gastos
        binding.gastosListTxt.text = listaGastos.joinToString("\n")
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ResumoActivity::class.java)
        }
    }
}
