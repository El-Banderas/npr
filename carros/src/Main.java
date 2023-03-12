public class Main {

	public static void imprime_numeros(int max){
		for (int i = 0; i < max; i++){
			System.out.println(i);
		}
		for (int i = max; i > 0; i--){
			System.out.println(i);
		}
	}
	public static void main(String[] args) {
		int n_repeticoes = 3;
		int max = 5;
	for (int vez_atual = 0; vez_atual < n_repeticoes; vez_atual++){
		imprime_numeros(max);
	}
	}
}