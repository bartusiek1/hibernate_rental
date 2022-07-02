package pl.sda.arp4.parser;

import pl.sda.arp4.dao.CarDao;
import pl.sda.arp4.model.Car;
import pl.sda.arp4.model.TypNadwozia;
import pl.sda.arp4.model.TypSkrzyni;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CarRentalCommanLineParser {
    private final Scanner scanner;
    private final CarDao dao;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyy-MM-dd");

    public CarRentalCommanLineParser(Scanner scanner, CarDao dao) {
        this.scanner = scanner;
        this.dao = dao;
    }

    public void obslugaKomend() {
        String command = null;
        do {
            System.out.println("Wybierz jedną z opcji: Dodaj/Usun/Lista/Edytuj lub Quit");
            command = scanner.next();

            if (command.equalsIgnoreCase("Dodaj")) {
                obsluzDodaj();
            } else if (command.equalsIgnoreCase("Usun")) {
                obsluzUsun();
            } else if (command.equalsIgnoreCase("Lista")) {
                obsluzLista();
            } else if (command.equalsIgnoreCase("Edytuj")) {
                obsluzEdytuj();
            }
        } while (!command.equalsIgnoreCase("Quit"));
    }

        private void obsluzEdytuj () {
            System.out.println("Podaj id samochodu");
            Long podaneId = scanner.nextLong();
            Optional<Car> szukanySamochod = dao.zwrocSamochod(podaneId);

            if (szukanySamochod.isPresent()) {
                Car wybranySamochod = szukanySamochod.get();

                System.out.println("Jaki parametr chcesz wyedytować");
                String output = scanner.next();

                switch (output) {
                    case "nazwa":
                        System.out.println("Wprowadź nową nazwę");
                        String nazwa = scanner.next();
                        wybranySamochod.setNazwa(nazwa);
                        break;
                    case "marka":
                        System.out.println("Wprowadź nową markę");
                        String marka = scanner.next();
                        wybranySamochod.setMarka(marka);
                        break;
                    case "dataProdukcji":
                        LocalDate dataProdukcji = loadDataProdukcjiFromUser();
                        wybranySamochod.setDataProdukcji(dataProdukcji);
                        break;
                    case "pojemnosc":
                        System.out.println("Podaj pojemność silnika");
                        Double pojemnosc = scanner.nextDouble();
                        wybranySamochod.setPojemnoscSilnika(pojemnosc);
                        break;
                    default:
                        System.out.println("Wskazano nieistniejącą opcję");
                }
                dao.updateSamochod(wybranySamochod);
                System.out.println("Samochód został zaktualizowany");
            } else {
                System.out.println("Nie odnaleziono wskazanego samochodu");
            }
        }

        private void obsluzLista () {
            List<Car> carList = dao.zwrocListeSamochodow();
            for (Car car : carList) {
                System.out.println(car);
            }
            System.out.println();
        }

        private void obsluzUsun () {
            System.out.println("Podaj id samochodu:");
            Long podaneId = scanner.nextLong();
            Optional<Car> szukanySamochod = dao.zwrocSamochod(podaneId);
            if (szukanySamochod.isPresent()) {
                Car car = szukanySamochod.get();
                dao.usunSamochod(car);
                System.out.println("Samochód został usunięty");
            } else {
                System.out.println("Nie odnaleziono samochodu");
            }
        }

        private void obsluzDodaj () {
            System.out.println("Podaj nazwę:");
            String name = scanner.next();

            System.out.println("Podaj markę:");
            String marka = scanner.next();

            LocalDate dataProdukcji = loadDataProdukcjiFromUser();

            TypNadwozia nadwozie = zaladujNadwozie();

            System.out.println("Podaj liczbę miejsc siedzących:");
            Integer miejscaSiedzace = scanner.nextInt();

            System.out.println("Podaj pojemność silnika:");
            Double pojemnoscSilnika = scanner.nextDouble();

            TypSkrzyni skrzynia = zaladujSkrzynie();

            Car car = new Car(null, name, marka, dataProdukcji, nadwozie, miejscaSiedzace, pojemnoscSilnika, skrzynia);
            dao.dodajSamochod(car);

        }

        private LocalDate loadDataProdukcjiFromUser () {
            LocalDate dataProdukcji = null;
            do {
                try {
                    System.out.println("Podaj datę produkcji:");
                    String dataProdukcjiString = scanner.next();

                    dataProdukcji = LocalDate.parse(dataProdukcjiString, FORMATTER);

                    LocalDate today = LocalDate.now();
                    if (dataProdukcji.isBefore(today)) {
                        throw new IllegalArgumentException("Kłamiesz - to nie może być data produkcji");
                    }

                } catch (IllegalArgumentException | DateTimeException iae) {
                    dataProdukcji = null;
                    System.err.println("Wskazana data produkcji jest nieprawidłowa. Wprowadź datę w formacie: yyy-MM-dd");
                }
            } while (dataProdukcji == null);
            return dataProdukcji;
        }

        private TypSkrzyni zaladujSkrzynie () {
            TypSkrzyni skrzynia = null;
            do {
                try {
                    System.out.println("Podaj rodzaj skrzyni biegów");
                    String skrzyniaString = scanner.next();

                    skrzynia = TypSkrzyni.valueOf(skrzyniaString.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    System.err.println("Podano błędne dane ...");
                }
            } while (skrzynia == null);
            return skrzynia;
        }

        private TypNadwozia zaladujNadwozie () {
            TypNadwozia nadwozie = null;
            do {
                try {
                    System.out.println("Podaj typ nadwozia:");
                    String nadwozieString = scanner.next();

                    nadwozie = TypNadwozia.valueOf(nadwozieString.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    System.err.println("Podano błędne dane ...");
                }
            } while (nadwozie == null);
            return nadwozie;
        }
    }

