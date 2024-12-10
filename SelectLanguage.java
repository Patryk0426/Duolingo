import java.util.Scanner;

public class SelectLanguage {
    public void selectLanguage() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Wybierz jezyk, którego chcesz się nauczyć:");
        System.out.println("1. Angielski");
        System.out.println("2. Hiszpański");
        System.out.println("3. Japoński");
        int choice = sc.nextInt();
        Languages pick = null;
        switch (choice) {
            case 1:
                pick = Languages.ENGLISH;
                break;
            case 2:
                pick = Languages.SPANISH;
                break;
            case 3:
                pick = Languages.JAPANESE;
                break;
            default:
                System.out.println("Nieprawidłowy wybór.");
                return;
        }

    }
}
