import java.util.Scanner;

public class BatalhaNaval {
	public static void clear() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void line_left() {
		System.out.println("-------------------------------------------------");
	}

	public static void header(){
		clear();
		line_left();
		System.out.printf("--\u001b[1m\u001b[34m%28s%17s\u001B[0m--\n", "BATALHA NAVAL", "");
		line_left();
	}

	public static void winner(Data det) {
		displayBoard(det);
		System.out.printf("-- \u001b[1m\u001b[32mFim de jogo.%32s\u001b[0m--\n", "");
		System.out.printf("-- \u001b[1m\u001b[32m0%d navios afundados, %s disparos no total.\u001b[0m%2s--\n", det.ships.length, det.shots < 10 ? ("0" + det.shots).toString() : ("" + det.shots).toString(), "");
		line_left();
	}

	public static int[] numSort(){
        int[] num = new int[3];
        num[0] = (int) (Math.random() * 5);

        do {
            num[1] = (int) (Math.random() * 5);
        } while (num[0] == num[1]);

        do {
            num[2] = (int) (Math.random() * 5);
        }while (num[0] == num[2] || num[1] == num[2]);

        return num;
    }

	public static void initTb(Data add) {
		//Start the table.
  		for (int i = 0; i < add.board.length; i++)
			for (int j = 0; j < add.board.length; j++)
				add.board[i][j] = 0;

  		//Sort the ships.
        for(int j = 0; j < add.ships.length-1; j++){
            int[] num = numSort();
            for(int i = 0; i < add.ships.length; i++)
            	add.ships[i][j] = num[i];
        }
	}

	public static void displayBoard(Data det) {
		System.out.printf("\n%8s|", "");
		for (int i = 0; i < det.board.length; i++)
			System.out.printf("%4d%3s|", i+1, "");
		System.out.println("\n\n");

		for (int i = 0; i < det.board.length; i++) {
			System.out.printf("|%4d%3s|", i+1, "");
			for (int j = 0; j < det.board.length; j++) {
				String str;
				switch (det.board[i][j]) {
				case 1:
					str = "💥";
					break;
				case 2:
					str = "🚢";
					break;
				default:
					str = "🌊";
					break;
				}
				System.out.printf("%5s%3s", str, "");
			}
			System.out.println("\n\n");
		}
		line_left();
	}

	public static void play(Data add, Scanner input) {
		for (int i = 0; i < add.position.length; i++) {
			switch (i) {
				case 1:
					System.out.print("-- Coluna: ");
					break;

				default:
					System.out.print("-- Linha: ");
					break;
			}

			char x = input.next().toLowerCase().charAt(0);

			if(Character.isDigit(x) && Character.getNumericValue(x) <= 5 && Character.getNumericValue(x) > 0) {
				add.position[i] = Character.getNumericValue(x) - 1;
			}
			else if(Character.isLetter(x)) {
				line_left();
				add.position[i] = (int) (Math.random() * 5);
				System.out.printf("-- \u001B[31mOopss! O caractere '%s' foi informado. %6s\u001B[0m--\n-- \u001B[31mSerá trocado de forma aleatória por %d.%6s\u001B[0m--\n", x, "", add.position[i]+1, "");
				line_left();
			}
			else {
				line_left();
				add.position[i] = (int) (Math.random() * 5);
				System.out.printf("-- \u001B[31mOopss! O valor informado %s.%5s\u001B[0m--\n-- \u001B[31mSerá trocado de forma aleatória por %d.%6s\u001B[0m--\n", Character.getNumericValue(x) > 5 ? "é maior que 5" : "é menor que 1", "", add.position[i]+1, "");
				line_left();
			}
		}
	}

	public static boolean hit(Data det) {
		for (int i = 0; i < det.ships.length; i++)
			if (det.position[0] == det.ships[i][0] && det.position[1] == det.ships[i][1])
				return true;
		return false;
	}

	public static void tips(Data add) {
		int m = 0, n = 0;
		for (int i = 0; i < add.ships.length; i++)
			if (add.position[0] == add.ships[i][0])
				m++;
				else if (add.position[1] == add.ships[i][1])
					n++;
		header();
		System.out.printf("-- %sª - Dica:%33s--\n", add.shots < 10 ? ("0" + add.shots).toString() : ("" + add.shots).toString(), "");
		System.out.printf("-- Linha %d: %d %-33s--\n", add.position[0] + 1, m, m > 1 ? "barcos." : "barco. ", "");
		System.out.printf("-- Coluna %d: %d %-32s--\n", add.position[1] + 1, n, n > 1 ? "barcos." : "barco. ", "");
		line_left();
	}

	public static void hitTarget(Data det) {
		if (hit(det)) {
			det.board[det.position[0]][det.position[1]] = 2;
			System.out.printf("-- \u001B[32mAcertou o disparo na linha %d, coluna %d.%5s\u001B[0m--\n", det.position[0] + 1, det.position[1] + 1, "");
			line_left();
		}
		else
			det.board[det.position[0]][det.position[1]] = 1;
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Data det = new Data();

		header();
		initTb(det);

		do {
			displayBoard(det);
			play(det, input);

			if (hit(det))
				det.target++;
			det.shots++;

			tips(det);

			hitTarget(det);

		} while (det.target != det.ships.length);

		input.close();
		winner(det);
	}
}
