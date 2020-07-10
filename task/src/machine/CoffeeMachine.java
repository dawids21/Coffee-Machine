package machine;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CoffeeMachine {

    public static void main(String[] args) {
        var coffeeMaker = new CoffeeMaker(400, 540, 120, 9, 550);
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();
                if (input.equals("exit")) {
                    System.exit(0);
                } else {
                    coffeeMaker.handleAction(input);
                }
            }
        }
    }
}
