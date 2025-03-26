/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package buscapadraoweb;

import buscaweb.CapturaRecursosWeb;
import processo.Processamento;

import java.util.ArrayList;

/**
 *
 * @author Santiago
 */
public class Main {

    public static void main(String[] args) {
        //instancia e usa objeto que captura código-fonte de páginas Web
        CapturaRecursosWeb crw = new CapturaRecursosWeb();

        crw.getListaRecursos().add("https://www.econodata.com.br/consulta-empresa");
        crw.getListaRecursos().add("https://fateccampinas.com.br/conveniados/public/lista-empresa");
        crw.getListaRecursos().add("https://investidorsardinha.r7.com/geral/todos-os-cnpj-das-empresas-listadas-na-b3-a-bolsa-de-valores-brasileira/");
        ArrayList<String> listaCodigos = crw.carregarRecursos();

        String[] listCodigoHTML = new  String[3];
        listCodigoHTML[0] = listaCodigos.get(0);
        listCodigoHTML[1] = listaCodigos.get(1);
        listCodigoHTML[2] = listaCodigos.get(2);

        //Mapa do alfabeto
        char[] alfabeto = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '/', '-'};

        //mapa de estados
        String[] estados = new String[19];
        for (int i = 0; i < 19; i++) {
            estados[i] = "q" + i;
        }

        // Estado inicial
        String estado_inicial = "q0";

        // Estados finais agora definidos como -1
        String[] estados_finais = new String[1];
        // Q18 recebe -1 sendo estado final
        estados_finais[0] = "q18";

        int[][] matriz = new int[19][alfabeto.length];

        int estado = Processamento.get_string_ref(estados, estado_inicial);
        int estado_anterior = -1;
        ArrayList<String> palavras_reconhecidas = new ArrayList<>();

        // Primeiro e segundo dígito
        Processamento.defineTransicao(matriz, estados, alfabeto, "q0", "q1", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q1", "q2", '0', '9');

        // Ponto (.)
        Processamento.defineTransicaoUnica(matriz, estados, alfabeto, "q2", "q3", '.');

        // Três dígitos
        Processamento.defineTransicao(matriz, estados, alfabeto, "q3", "q4", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q4", "q5", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q5", "q6", '0', '9');

        // Ponto (.)
        Processamento.defineTransicaoUnica(matriz, estados, alfabeto, "q6", "q7", '.');

        // Três dígitos
        Processamento.defineTransicao(matriz, estados, alfabeto, "q7", "q8", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q8", "q9", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q9", "q10", '0', '9');

        // Barra (/)
        Processamento.defineTransicaoUnica(matriz, estados, alfabeto, "q10", "q11", '/');

        // Quatro dígitos
        Processamento.defineTransicao(matriz, estados, alfabeto, "q11", "q12", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q12", "q13", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q13", "q14", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q14", "q15", '0', '9');

        // Hífen (-)
        Processamento.defineTransicaoUnica(matriz, estados, alfabeto, "q15", "q16", '-');

        // Dois dígitos finais
        Processamento.defineTransicao(matriz, estados, alfabeto, "q16", "q17", '0', '9');
        Processamento.defineTransicao(matriz, estados, alfabeto, "q17", "q18", '0', '9');

        // Caso haja inserção de entrada inválida, ir direto para estado final (-1)
        for (char elementoTransicao : alfabeto) {
            matriz[Processamento.get_string_ref(estados, "q18")][Processamento.get_char_ref(alfabeto, elementoTransicao)] = -1;
        }

        String palavra = "";

       // Processamento.exibirMatrizTransicao(matriz, estados, alfabeto);
        for (int i = 0; i < listCodigoHTML.length; i++) {
            String codigoHTML = listCodigoHTML[i];

        //varre o código-fonte de um código
        for (int j=0; j<codigoHTML.length(); j++){

            estado_anterior = estado;
            estado = Processamento.proximo_estado(alfabeto, matriz, estado, codigoHTML.charAt(j));
            //se o não há transição
            if (estado == -1){
                //pega estado inicial
                estado = Processamento.get_string_ref(estados, estado_inicial);
                // se o estado anterior foi um estado final
                if (Processamento.get_string_ref(estados_finais, estados[estado_anterior]) != -1){
                    //se a palavra não é vazia adiciona palavra reconhecida
                    if (!palavra.isEmpty()){
                        palavras_reconhecidas.add(palavra);
                    }
                    // se ao analisar este caracter não houve transição
                    // teste-o novamente, considerando que o estado seja inicial
                    j--;
                }
                //zera palavra
                palavra = "";

            }else{
                //se houver transição válida, adiciona caracter a palavra
                palavra += codigoHTML.charAt(j);
            }
        }
        }
        System.out.println(" ");

        //exibir todas as palavras reconhecidas
        for (String p : palavras_reconhecidas) {
            System.out.println(p);
        }
    }
}
