package processo;

public class Processamento {

    public Processamento() {}

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

    public static void defineTransicao(int[][] matriz, String[] estados, char[] alfabeto, String estadoOrigem, String estadoDestino, char inicio, char fim) {
        for (char elementoTransicao = inicio; elementoTransicao <= fim; elementoTransicao++) {
            matriz[get_string_ref(estados, estadoOrigem)][get_char_ref(alfabeto, elementoTransicao)] = get_string_ref(estados, estadoDestino);
        }
    }

    public static void defineTransicaoUnica(int[][] matriz, String[] estados, char[] alfabeto, String estadoOrigem, String estadoDestino, char transicao) {
        matriz[get_string_ref(estados, estadoOrigem)][get_char_ref(alfabeto, transicao)] = get_string_ref(estados, estadoDestino);
    }

    public static void exibirMatrizTransicao(int[][] matriz, String[] estados, char[] alfabeto) {
        System.out.print("   ");
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


}
