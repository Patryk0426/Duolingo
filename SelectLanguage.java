import java.util.Scanner;

public class SelectLanguage {
    public void selectLanguage() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Wybierz język, którego chcesz się nauczyć:");
        System.out.println("1. Angielski");
        System.out.println("2. Hiszpański");
        System.out.println("3. Japoński");

        int choice = sc.nextInt();


        Language selectedLanguage = null;


        switch (choice) {
            case 1:
                selectedLanguage = new English();
                break;
            case 2:
                selectedLanguage = new Spanish();
                break;
            case 3:
                selectedLanguage = new Japanese();
                break;
            default:
                System.out.println("Nieprawidłowy wybór.");
                return;
        }

        // Wywołanie metody learn() na obiekcie typu Language
        System.out.println("Wybrany język: " + selectedLanguage);
        selectedLanguage.learn();
    }
}
