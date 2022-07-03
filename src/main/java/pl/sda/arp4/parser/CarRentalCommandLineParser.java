package pl.sda.arp4.parser;

import pl.sda.arp4.dao.GenericDao;
import pl.sda.arp4.model.Car;
import pl.sda.arp4.model.CarRental;
import pl.sda.arp4.model.TypNadwozia;
import pl.sda.arp4.model.TypSkrzyni;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CarRentalCommandLineParser {
    private final Scanner scanner;
    private final GenericDao<Car> daoCar;
    private final GenericDao<CarRental> daoCarRental;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CarRentalCommandLineParser(Scanner scanner, GenericDao<Car> daoCar, GenericDao<CarRental> daoCarRental) {
        this.scanner = scanner;
        this.daoCar = daoCar;
        this.daoCarRental = daoCarRental;
    }


    public void obslugaKomend() {
        String command = null;
        do {
            System.out.println("Wybierz jedną z opcji: Dodaj/Usun/Lista/Edytuj/DodajWynajem/ZwrocWynajem/Sprawdz lub Quit");
            command = scanner.next();

            if (command.equalsIgnoreCase("Dodaj")) {
                obsluzDodaj();
            } else if (command.equalsIgnoreCase("Usun")) {
                obsluzUsun();
            } else if (command.equalsIgnoreCase("Lista")) {
                obsluzLista();
            } else if (command.equalsIgnoreCase("Edytuj")) {
                obsluzEdytuj();
            } else if (command.equalsIgnoreCase("DodajWynajem")) {
                obsluzDodajWynajem();
            }else if (command.equalsIgnoreCase("ZwrocWynajem")) {
                obsluzZwrocWynajem();
            } else if (command.equalsIgnoreCase("Sprawdz")) {
                obsluzSprawdz();
            }
        } while (!command.equalsIgnoreCase("Quit"));
    }

    private void obsluzSprawdz() {
        System.out.println("Podaj id samochodu");
        Long podaneId = scanner.nextLong();
        Optional<Car> szukanySamochod = daoCar.znajdzPoId(podaneId, Car.class);

        if (szukanySamochod.isPresent()) {
            Car wybranySamochod = szukanySamochod.get();

            if (sprawdzCzySamochodJestDostepny(wybranySamochod)) {
                System.out.println("Tak, jest dostępny");
            } else {
                System.out.println("Nie, nie jest dostępny");
            }
        } else {
            System.out.println("Samochód nie został znaleziony");
        }
    }

    private boolean sprawdzCzySamochodJestDostepny (Car samochod) {
        Optional<CarRental> optionalCarRental = znajdzAktywnyWynajem(samochod);
        return !optionalCarRental.isPresent();
    }

    private void obsluzZwrocWynajem() {
        System.out.println("Podaj id samochodu");
        Long podaneId = scanner.nextLong();
        Optional<Car> szukanySamochod = daoCar.znajdzPoId(podaneId, Car.class);

        if (szukanySamochod.isPresent()) {
            Car wybranySamochod = szukanySamochod.get();

            Optional<CarRental> optionalCarRental = znajdzAktywnyWynajem(wybranySamochod);
            if (optionalCarRental.isPresent()) {
                CarRental carRental = optionalCarRental.get();

                // ustaw zakończenie najmu na obecną datę i godzinę
                carRental.setReturnDateTime(LocalDateTime.now());
                daoCarRental.aktualizuj(carRental);
            } else {
                System.out.println("Samochód nie ma aktywnego wynajmu");
            }
        } else {
            System.out.println("Samochód nie został znaleziony");
        }
    }

    private Optional<CarRental> znajdzAktywnyWynajem (Car samochod) {
        // jesli nie znalezliśmy żadnych wynajmów na liście, to znaczy że nie znajdziemy aktywnego najmu
        if (samochod.getCarRentals().isEmpty()) {
            return Optional.empty();
        }

        for (CarRental carRental : samochod.getCarRentals()) {
            if (carRental.getReturnDateTime() == null) {   // znajdź aktywny wynajem, bo samochod nie został zwrocony
                return Optional.of(carRental);
            }
        }
        return Optional.empty();
    }

    private void obsluzDodajWynajem() {
        System.out.println("Podaj id samochodu");
        Long podaneId = scanner.nextLong();
        Optional<Car> szukanySamochod = daoCar.znajdzPoId(podaneId, Car.class);

        if (szukanySamochod.isPresent()) {
            Car wybranySamochod = szukanySamochod.get();

            if (sprawdzCzySamochodJestDostepny(wybranySamochod)) {

                System.out.println("Podaj imie");
                String imie = scanner.next();

                System.out.println("Podaj nazwisko");
                String nazwisko = scanner.next();

                LocalDateTime dataCzasWynajmu = LocalDateTime.now();
                System.out.println("Data i godzina wynajmu: " + dataCzasWynajmu);

                CarRental carRental = new CarRental(imie, nazwisko, dataCzasWynajmu, wybranySamochod);
                daoCarRental.dodaj(carRental);

            } else {
                System.out.println("Samochód nie został znaleziony");
            }
        }
    }

    private void obsluzEdytuj () {
            System.out.println("Podaj id samochodu");
            Long podaneId = scanner.nextLong();
            Optional<Car> szukanySamochod = daoCar.znajdzPoId(podaneId,Car.class);

            if (szukanySamochod.isPresent()) {
                Car wybranySamochod = szukanySamochod.get();

                System.out.println("Jaki parametr chcesz wyedytować: nazwa, marka, dataProdukcji, pojemnosc");
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
                daoCar.aktualizuj(wybranySamochod);
                System.out.println("Samochód został zaktualizowany");
            } else {
                System.out.println("Nie odnaleziono wskazanego samochodu");
            }
        }

        private void obsluzLista () {
            List<Car> carList = daoCar.list(Car.class);
            for (Car car : carList) {
                System.out.println(car);
            }
            System.out.println();
        }

        private void obsluzUsun () {
            System.out.println("Podaj id samochodu:");
            Long podaneId = scanner.nextLong();
            Optional<Car> szukanySamochod = daoCar.znajdzPoId(podaneId,Car.class);
            if (szukanySamochod.isPresent()) {
                Car car = szukanySamochod.get();
                daoCar.usun(car);
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

            Car car = new Car(name, marka, dataProdukcji, nadwozie, miejscaSiedzace, pojemnoscSilnika, skrzynia);
            daoCar.dodaj(car);

        }

        private LocalDate loadDataProdukcjiFromUser () {
            LocalDate dataProdukcji = null;
            do {
                try {
                    System.out.println("Podaj datę produkcji:");
                    String dataProdukcjiString = scanner.next();

                    dataProdukcji = LocalDate.parse(dataProdukcjiString, FORMATTER);

                    LocalDate today = LocalDate.now();
                    if (dataProdukcji.isAfter(today)) {
                        throw new IllegalArgumentException("Kłamiesz - to nie może być data praodukcji");
                    }

                } catch (DateTimeException dt) {
                    dataProdukcji = null;
                    System.err.println("Wskazana data produkcji jest nieprawidłowa. Wprowadź datę w formacie: yyyy-MM-dd");
                }catch (IllegalArgumentException iae) {
                    dataProdukcji = null;
                    System.err.println("Kłamiesz - to nie może być data produkcji");
                    System.out.println("Podaj datę produkcji");
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

