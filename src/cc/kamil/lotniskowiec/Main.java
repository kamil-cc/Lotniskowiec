package cc.kamil.lotniskowiec;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int iloscSamolotow; //Samoloty
        int priorytet; //Zmiana priorytetu ze sartu na lądowanie przy x samolotach
        int pojemnoscLotniskowca; //Max samolotów na lotniskowcu
        System.out.println("Podaj ilość samolotów: ");
        iloscSamolotow = scanner.nextInt();
        System.out.println("Podaj pojemność lotniskowca: ");
        pojemnoscLotniskowca = scanner.nextInt();
        System.out.println("Podaj preferowaną liczbę samolotów na lotniskowcu "
                + "(od 0 do " + pojemnoscLotniskowca + "): ");
        priorytet = scanner.nextInt();
        scanner.close();
        if (priorytet < 0 || priorytet > pojemnoscLotniskowca) {
            System.out.println("Podałeś nieprawidłą preferowaną liczbę ");
            return;
        }
        Symulacja symulacja = new Symulacja(iloscSamolotow, pojemnoscLotniskowca,
                priorytet);
        symulacja.rozpocznij();
    }
}
