
import java.util.InputMismatchException;
import java.util.Scanner;

public class SelectLanguage {
    /**.
     *
     */
    public void selectLanguage() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Wybierz język, którego chcesz się nauczyć:");
        System.out.println("1. Angielski");
        System.out.println("2. Hiszpański");
        System.out.println("3. Japoński");
        System.out.println("4. Wróć do menu logowania");

        int choice = 0;
        try {
            choice = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Nieprawidłowy wybór. Podaj liczbę od 1 do 4.");
            sc.nextLine(); // czyści bufor
            selectLanguage();
            return;
        }

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
            case 4:
                SelectUser.select();
                return;
            default:
                System.out.println("Nieprawidłowy wybór.");
                selectLanguage();
                return;
        }

        System.out.println("Wybrany język: " + selectedLanguage);
        selectedLanguage.learn();
    }
}