package pl.sda.arp4.parser;

import pl.sda.arp4.dao.GenericDao;
import pl.sda.arp4.model.Car;
import pl.sda.arp4.model.CarRental;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        GenericDao<Car> carGenericDao = new GenericDao();
        GenericDao<CarRental> carRentalGenericDao = new GenericDao<>();

        CarRentalCommandLineParser parser = new CarRentalCommandLineParser(scanner, carGenericDao,carRentalGenericDao);
        parser.obslugaKomend();
    }
}
