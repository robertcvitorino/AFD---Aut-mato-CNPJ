/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package buscapadraoweb;

import buscaweb.CapturaRecursosWeb;
import java.util.ArrayList;

/**
 *
 * @author Santiago
 */
public class Main {

    // busca char em vetor e retorna indice
    public static int get_char_ref (char[] vet, char ref ){
        for (int i=0; i<vet.length; i++ ){
            if (vet[i] == ref){
                return i;
            }
        }
        return -1;
    }

    // busca string em vetor e retorna indice
    public static int get_string_ref (String[] vet, String ref ){
        for (int i=0; i<vet.length; i++ ){
            if (vet[i].equals(ref)){
                return i;
            }
        }
        return -1;
    }



    //retorna o próximo estado, dado o estado atual e o símbolo lido
    public static int proximo_estado(char[] alfabeto, int[][] matriz,int estado_atual,char simbolo){
        int simbol_indice = get_char_ref(alfabeto, simbolo);
        if (simbol_indice != -1){
            return matriz[estado_atual][simbol_indice];
        }else{
            return -1;
        }
    }
    public static void exibirMatrizTransicao(int[][] matriz, String[] estados, char[] alfabeto) {
        System.out.print("   "); // Espaço inicial para alinhar cabeçalhos
        for (char simbolo : alfabeto) {
            System.out.printf("%4c", simbolo);
        }
        System.out.println();

        for (int i = 0; i < matriz.length; i++) {
            System.out.printf("%-3s", estados[i]);

            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == -1) {
                    System.out.printf("%4s", "-");
                } else {
                    System.out.printf("%4s", estados[matriz[i][j]]); // Exibir estado destino
                }
            }
            System.out.println();
        }
    }

    public static void configurarTransicao(int[][] matriz, String[] estados, char[] alfabeto, String estadoOrigem, String estadoDestino, char inicio, char fim) {
        for (char elementoTransicao = inicio; elementoTransicao <= fim; elementoTransicao++) {
            matriz[get_string_ref(estados, estadoOrigem)][get_char_ref(alfabeto, elementoTransicao)] = get_string_ref(estados, estadoDestino);
        }
    }

    public static void configurarTransicaoUnica(int[][] matriz, String[] estados, char[] alfabeto, String estadoOrigem, String estadoDestino, char transicao) {
        matriz[get_string_ref(estados, estadoOrigem)][get_char_ref(alfabeto, transicao)] = get_string_ref(estados, estadoDestino);
    }


    public static void main(String[] args) {
        //instancia e usa objeto que captura código-fonte de páginas Web
        CapturaRecursosWeb crw = new CapturaRecursosWeb();
        //crw.getListaRecursos().add("https://www.univali.br/");
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

        int estado = get_string_ref(estados, estado_inicial);
        int estado_anterior = -1;
        ArrayList<String> palavras_reconhecidas = new ArrayList<>();

        // Primeiro e segundo dígito
        configurarTransicao(matriz, estados, alfabeto, "q0", "q1", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q1", "q2", '0', '9');

        // Ponto (.)
        configurarTransicaoUnica(matriz, estados, alfabeto, "q2", "q3", '.');

        // Três dígitos
        configurarTransicao(matriz, estados, alfabeto, "q3", "q4", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q4", "q5", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q5", "q6", '0', '9');

        // Ponto (.)
        configurarTransicaoUnica(matriz, estados, alfabeto, "q6", "q7", '.');

        // Três dígitos
        configurarTransicao(matriz, estados, alfabeto, "q7", "q8", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q8", "q9", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q9", "q10", '0', '9');

        // Barra (/)
        configurarTransicaoUnica(matriz, estados, alfabeto, "q10", "q11", '/');

        // Quatro dígitos
        configurarTransicao(matriz, estados, alfabeto, "q11", "q12", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q12", "q13", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q13", "q14", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q14", "q15", '0', '9');

        // Hífen (-)
        configurarTransicaoUnica(matriz, estados, alfabeto, "q15", "q16", '-');

        // Dois dígitos finais
        configurarTransicao(matriz, estados, alfabeto, "q16", "q17", '0', '9');
        configurarTransicao(matriz, estados, alfabeto, "q17", "q18", '0', '9');


        // Caso haja inserção de entrada inválida, ir direto para estado final (-1)
        for (char elementoTransicao : alfabeto) {
            matriz[get_string_ref(estados, "q18")][get_char_ref(alfabeto, elementoTransicao)] = -1;
        }

        String palavra = "";

       // exibirMatrizTransicao(matriz, estados, alfabeto);
        for (int i = 0; i < listCodigoHTML.length; i++) {
            String codigoHTML = listCodigoHTML[i];

        //varre o código-fonte de um código
        for (int j=0; j<codigoHTML.length(); j++){

            estado_anterior = estado;
            estado = proximo_estado(alfabeto, matriz, estado, codigoHTML.charAt(j));
            //se o não há transição
            if (estado == -1){
                //pega estado inicial
                estado = get_string_ref(estados, estado_inicial);
                // se o estado anterior foi um estado final
                if (get_string_ref(estados_finais, estados[estado_anterior]) != -1){
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
