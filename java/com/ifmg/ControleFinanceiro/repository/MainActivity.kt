package com.ifmg.ControleFinanceiro.repository

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifmg.ControleFinanceiro.dados.DBHelper
import com.ifmg.ControleFinanceiro.databinding.ActivityMainBinding
import com.ifmg.ControleFinanceiro.modelo.Product
import java.text.SimpleDateFormat
import java.util.*
import com.ifmg.ControleFinanceiro.R




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DBHelper

    override fun onStop() {
        super.onStop()

        // Limpar todos os dados do banco ao sair da atividade
        dbHelper.clearAllData()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)  // Inicializa o DBHelper

        // Adicionar ouvinte de alteração para os RadioGroups
        binding.entradaRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.entradaRadioButton) {
                // Se Entrada estiver selecionado, desmarque o botão de Saída
                binding.saidaRadioButton.isChecked = !binding.entradaRadioButton.isChecked
            }
        }

        binding.saidaRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.saidaRadioButton) {
                // Se Saída estiver selecionado, desmarque o botão de Entrada
                binding.entradaRadioButton.isChecked = !binding.saidaRadioButton.isChecked
            }
        }


        // Obter a data e hora atual levando em consideração o fuso horário do dispositivo
        val calendar = Calendar.getInstance()
        val timeZone = TimeZone.getDefault()
        Toast.makeText(this, "Fuso Horário: ${timeZone.displayName}", Toast.LENGTH_SHORT).show()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dataHoraAtual = dateFormat.format(calendar.time)
        binding.DataTxt.setText(dataHoraAtual)

        binding.salvarBtn.setOnClickListener {
            salvarProduto()
        }

        binding.resumoBtn.setOnClickListener {
            // Navegar para a ResumoActivity
            startActivity(ResumoActivity.newIntent(this))
        }
    }

    private fun salvarProduto() {
        val nome = binding.nomeTxt.text.toString()

        // Verificar se o valor é numérico antes de converter para Double
        val valorStr = binding.valorTxt.text.toString()
        val valor = if (valorStr.isNotBlank() && valorStr.toDoubleOrNull() != null) {
            valorStr.toDouble()
        } else {
            // Mostrar mensagem de erro se o valor não for numérico
            Toast.makeText(this, "Informe um valor numérico válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Obter a data e hora atual
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dataHoraAtual = dateFormat.format(calendar.time)

        // Obter se é uma entrada ou saída
        val isEntrada = binding.entradaRadioButton.isChecked

        // Criar um objeto Product com os dados fornecidos
        val produto = Product(0, nome, valor, dataHoraAtual, isEntrada)

        // Adicionar o produto ao banco de dados
        dbHelper.addProduct(produto)

        // Limpar os campos após salvar
        binding.nomeTxt.text.clear()
        binding.valorTxt.text.clear()

        // Notificar o usuário que o produto foi salvo
        Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show()
    }
}
