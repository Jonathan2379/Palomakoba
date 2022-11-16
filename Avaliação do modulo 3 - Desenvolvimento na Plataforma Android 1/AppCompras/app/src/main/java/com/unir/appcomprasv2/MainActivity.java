package com.unir.appcomprasv2;

/*
                    ╔═══════════════════════════════════════════════════════════════╗
                    ║                  *** Instituto Eldorado ***                   ║
                    ║   *** Fundação Universidade Federal de Rondônia - UNIR ***    ║
                    ║           *** Bacharelado em Ciência da Computação ***        ║
                    ║                       *** Palomakoba ***                      ║
                    ╚═══════════════════════════════════════════════════════════════╝
            ╔════════════════════════════════════════════════════════════════════════════════╗
            ║         Disciplina: Desenvolvimento na Plataforma Android I                    ║
            ║         Professora: Dra. Liliane da Silva Coelho Jacon                         ║
            ║                                                                                ║
            ║         Data da Tarefa 25/08/2022 a 15/09/2022                                 ║
            ║         Data de criação 29/08/2022                                             ║
            ║         Ultima alteração 02/08/2022                                            ║
            ╚════════════════════════════════════════════════════════════════════════════════╝
            ╔════════════════════════════════════════════════════════════════════════════════╗
            ║         Este projeto foi desenvolvido por Jonathan Oliveira Pinheiro da Costa  ║
            ║              * contato: +55 (69) 3213-4566                                     ║
            ║              * e-mail: contatojonathan1999@gmail.com                           ║
            ╚════════════════════════════════════════════════════════════════════════════════╝
            ╔════════════════════════════════════════════════════════════════════════════════╗
            ║         Componentes do aplicativo:                                             ║
            ║             * Activity, classe java, drawables.                                ║
            ║             * Botões, EditTexts, CheckBox, TexView, RadioGroup, RadioButton.   ║
            ║             * validação de campos, Toast, métotodos.                           ║
            ╚════════════════════════════════════════════════════════════════════════════════╝
            ╔════════════════════════════════════════════════════════════════════════════════╗
            ║         *** Das informações do compilador e sistema ***                        ║
            ║                                                                                ║
            ║                Android Studio Bumblebee | 2021.1.1 Patch 3                     ║
            ║                Build #AI-211.7628.21.2111.8309675, built on March 16, 2022     ║
            ║                Runtime version: 11.0.11+9-b60-7590822 amd64                    ║
            ║                VM: OpenJDK 64-Bit Server VM by Oracle Corporation              ║
            ║                Windows 10 10.0                                                 ║
            ║                GC: G1 Young Generation, G1 Old Generation                      ║
            ║                Memory: 1280M                                                   ║
            ║                Cores: 4                                                        ║
            ║                Registry: external.system.auto.import.disabled=true             ║
            ║                Non-Bundled Plugin s: com.intellij.marketplace (211.7628.36)    ║
            ╚════════════════════════════════════════════════════════════════════════════════╝
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Seguindo as boas regras de programação, os atributos devem ser privadados.

    private CheckBox arroz, feijao, macarrao, pure, bisteca, salada;
    private TextView txt_valor;
    private EditText pagamento1;
    private Button btn_total, btn_pagamento;
    private RadioGroup rb_group;

    private double total;
    private String valor_total_da_compra_ajustado;

    // ! Alguns atributos são locais, haja vista que serão usados apenas localmente.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buscaID();

        // ------- Ação do botão total -------
        btn_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                total = verificaPedidos();

                // ------- Ajustando o valor para conter apenas 2 casas decimais -------
                valor_total_da_compra_ajustado = String.format("%.2f", total);
                txt_valor.setText("Valor R$ " + valor_total_da_compra_ajustado);
            }
        });

        // ------- Ação do botão pagamento -------
        btn_pagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ------- Definição de váriaveis locais -------
                double pagamento = 0;         // iniciando a variável pagamento
                double max_pagamento = 1000;  // valor pressumível que foi um engano, com base nos valores dos itens que podem ser comprados

                try {
                    pagamento = Double.parseDouble(pagamento1.getText().toString());
                } catch (Exception e) {
                    Log.i("Análise de erro", "Erro ao converter para double, provalmente por que está tentando converter um valor null");
                }

                // ------- Verifica se o campo do valor a ser pago foi inserido -------
                if (pagamento1.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, insira o valor a ser pago!", Toast.LENGTH_SHORT).show();

                    // ------- Verifica se algum radio button foi selecionado --------
                } else if (rb_group.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, "Por favor, selecione um desconto!", Toast.LENGTH_SHORT).show();

                    // ------- Para não haver um pagamento de um valor muito alto acidental
                } else if (pagamento >= max_pagamento) {
                    Toast.makeText(MainActivity.this, "Por favor, verifique o valor da sua compra!", Toast.LENGTH_SHORT).show();
                } else {

                    // ------- Cria um Alert Dialog - uma caixa de dialogo já com fundo -------
                    AlertDialog.Builder janela = new AlertDialog.Builder(MainActivity.this, R.style.fundo_dialog);
                    janela.setTitle(R.string.app_name);
                    janela.setIcon(R.drawable.carrinhodecompras);
                    janela.setNeutralButton("ok", null);

                    total = verificaPedidos();

                    // ------- Definição de algumas váriaveis locais -------
                    double totalpamento = 0;
                    int op = rb_group.getCheckedRadioButtonId();
                    String desconto = "";

                    // ------- Verifica se o usuário está fazendo uma compra -------
                    if (total == 0) {
                        Toast.makeText(MainActivity.this, "Por favor, escolha seu pedido!", Toast.LENGTH_SHORT).show();
                    } else {

                        if (op == R.id.rb_semDesconto) {
                            totalpamento = total;
                            desconto = "Sem Desconto";
                        }
                        if (op == R.id.rb_cinco) {
                            totalpamento = total - (total * 0.05);
                            desconto = "5%";
                        }
                        if (op == R.id.rb_dez) {
                            totalpamento = total - (total * 0.1);
                            desconto = "10%";
                        }
                        if (op == R.id.rb_quinze) {
                            totalpamento = total - (total * 0.15);
                            desconto = "15%";
                        }

                        double troco = pagamento - totalpamento;

                        if (pagamento >= totalpamento) {
                            // ------- Ajustando os valores para conter apenas 2 casas decimais -------
                            valor_total_da_compra_ajustado = String.format("%.2f", total);
                            String total_com_desconto_ajustado = String.format("%.2f", totalpamento);
                            String troco_ajustado = String.format("%.2f", troco);

                            janela.setMessage("Valor total da compra R$ " + valor_total_da_compra_ajustado +
                                    "\nDesconto: " + desconto + "\nValor total R$ " + total_com_desconto_ajustado +
                                    "\nValor pago R$ " + pagamento + "\nTroco R$ " + troco_ajustado);

                            // ------- Adiciona a opção de nova compra no alert dialog com evento de click -------
                            janela.setPositiveButton("Nova compra", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    clearComponents();
                                }
                            });
                            janela.show();

                        } else {
                            double valor1 = pagamento - totalpamento;
                            janela.setMessage(String.format("Valor incompatível com a compra!\n\t\tFalta R$ %5.2f", valor1));
                            janela.show();
                        }
                    }
                }
            }
        });
    }

    private void buscaID() {

        // ------- Inicia os componentes buscando os ids -------
        arroz = findViewById(R.id.arroz);
        feijao = findViewById(R.id.feijao);
        macarrao = findViewById(R.id.macarrao);
        pure = findViewById(R.id.pure);
        bisteca = findViewById(R.id.bisteca);
        salada = findViewById(R.id.salada);

        txt_valor = findViewById(R.id.txt_valor);
        rb_group = findViewById(R.id.radioGroup);

        pagamento1 = findViewById(R.id.pagamento);

        btn_total = findViewById(R.id.btn_total);
        btn_pagamento = findViewById(R.id.btn_pagamento);

    }

    private double verificaPedidos() {
        total = 0;

        // ------- Verifica qual/quais checkbuttons estão marcados -------
        if (arroz.isChecked())
            total += 3.8;
        if (feijao.isChecked())
            total += 4.8;
        if (macarrao.isChecked())
            total += 3;
        if (pure.isChecked())
            total += 2;
        if (bisteca.isChecked())
            total += 6;
        if (salada.isChecked())
            total += 1.55;

        return total;
    }

    public void clearComponents() {
        // ------- Desseleciona e limpa todos os componentes -------
        arroz.setChecked(false);
        feijao.setChecked(false);
        macarrao.setChecked(false);
        pure.setChecked(false);
        bisteca.setChecked(false);
        salada.setChecked(false);

        rb_group.clearCheck();

        txt_valor.setText("");
        pagamento1.setText("");
    }
}