package pl.sda.arp4.parser;

import pl.sda.arp4.dao.CarDao;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CarRentalCommanLineParser {
    private final Scanner;
    private final CarDao;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyy-MM-dd");


}
