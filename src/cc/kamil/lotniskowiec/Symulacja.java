package cc.kamil.lotniskowiec;

import java.util.Random;
/*import java.util.logging.Level;
 import java.util.logging.Logger;*/

public class Symulacja {

    private final Samolot[] samoloty;
    private final int iloscSamolotow;
    private final int pojemnoscLotniskowca;
    private final int priorytet;
    private final Lotniskowiec lotniskowiec;
    private final Random losowa;

    private class Lotniskowiec {

        private int naLotniskowcu;

        public Lotniskowiec() {
            if (iloscSamolotow < pojemnoscLotniskowca) {
                naLotniskowcu = iloscSamolotow;
            } else {
                naLotniskowcu = pojemnoscLotniskowca;
            }
            System.out.println("Pojemność lotniskowca: " + pojemnoscLotniskowca
                    + ".\nIlość samolotów na na lotniskowcu w chwili rozpoczęcia "
                    + "symulacji: " + naLotniskowcu + ".");
        }

        public void laduj() {
            synchronized (this) { //Tylko jeden samolot ląduje w tym samym czasie
                while (naLotniskowcu >= priorytet) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println("Błąd! Nie można czekać w \"ląduj\".");
                    }
                }
                naLotniskowcu++; //Sukces, samolot wylądował
                notifyAll();
            }
        }

        public void startuj() {
            synchronized (this) { //Tylko jeden samolot startuje w tym samym czasie
                while (naLotniskowcu < priorytet) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println("Błąd! Nie można czekać w \"starruj\".");
                    }
                }
                naLotniskowcu--; //Sukces, samolot wystartował
                notifyAll();
            }
        }
    }

    private class Samolot extends Thread {

        private final int numer;
        private StanSamolotu stanSamolotu; //Enum -  wartość określająca czynność samolotu  

        public Samolot(int numer) { //Tworzy samolot o konkretnym numerze
            this.numer = numer;
            stanSamolotu = StanSamolotu.STOI;
        }

        @Override
        public void run() {
            //super.run();
            /*System.out.println("Utworzono samolot: " + numer + ". Stan samolotu: "
                    + stanSamolotu.name() + ".");*/
            while (true) {// Główna pętla samolotu
                if (stanSamolotu == StanSamolotu.STOI) {
                    System.out.println("Samolot numer: " + numer
                            + " stoi na lotniskowcu.");
                    try {
                        Samolot.sleep(losowa.nextInt(1000));
                    } catch (InterruptedException ex) {
                        /*Logger.getLogger(Symulacja.class.getName()).log(Level.SEVERE,
                         null, ex);*/
                        System.out.println("Błąd! Nie można uśpić wątku: "
                                + numer + ". Stan samolotu: " 
                                + stanSamolotu.name() + ".");
                    }
                    stanSamolotu = StanSamolotu.CZEKA_NA_START;
                }
                if (stanSamolotu == StanSamolotu.CZEKA_NA_START) {
                    System.out.println("Samolot numer: " + numer
                            + " czeka na możliwość startu.");
                    lotniskowiec.startuj();
                    stanSamolotu = StanSamolotu.LATA;
                    System.out.println("Samolot numer: " + numer
                            + " wystartował.");
                }
                if (stanSamolotu == StanSamolotu.LATA) {
                    System.out.println("Samolot numer: " + numer
                            + " jest w powietrzu.");
                    try {
                        Samolot.sleep(losowa.nextInt(1000));
                    } catch (InterruptedException ex) {
                        /*Logger.getLogger(Symulacja.class.getName()).log(Level.SEVERE,
                         null, ex);*/
                        System.out.println("Błąd! Nie można uśpić wątku: "
                                + numer + ". Stan samolotu: " 
                                + stanSamolotu.name() + ".");
                    }
                    stanSamolotu = StanSamolotu.CZEKA_NA_LADOWANIE;
                }
                if (stanSamolotu == StanSamolotu.CZEKA_NA_LADOWANIE) {
                    System.out.println("Samolot numer: " + numer
                            + " czeka na możliwość lądowania.");
                    lotniskowiec.laduj();
                    stanSamolotu = StanSamolotu.STOI;
                    System.out.println("Samolot numer: " + numer + " wylądował");
                }
            }
        }

        public void ustawStanNaLotniskowcu() {
            stanSamolotu = StanSamolotu.STOI;
        }

        public void ustawStanWPowietrzu() {
            stanSamolotu = StanSamolotu.LATA;
        }
    }

    /**
     * Rozpoczęcie symulacji z zadanymi parametrami.
     *
     * @param ilosc
     * @param pojemnosc
     * @param priorytet
     */
    public Symulacja(int ilosc, int pojemnosc, int priorytet) {
        this.iloscSamolotow = ilosc;
        this.pojemnoscLotniskowca = pojemnosc;
        this.priorytet = priorytet;
        //
        samoloty = new Samolot[ilosc];
        for (int i = 0; i < ilosc; i++) {
            samoloty[i] = new Samolot(i);
            if (i < pojemnosc) {
                samoloty[i].ustawStanNaLotniskowcu();
            } else {
                samoloty[i].ustawStanWPowietrzu();
            }
        }
        lotniskowiec = new Lotniskowiec();
        losowa = new Random();
    }

    public void rozpocznij() { //Startują wątki
        for (Samolot samolot : samoloty) {
            samolot.start();
        }
    }

}
