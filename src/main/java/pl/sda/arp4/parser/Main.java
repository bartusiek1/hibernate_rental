package pl.sda.arp4.parser;

import pl.sda.arp4.dao.CarDao;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CarDao carDao = new CarDao();

        CarRentalCommanLineParser parser = new CarRentalCommanLineParser(scanner, carDao);
        parser.obslugaKomend();
    }
}
