package pl.sda.arp4.dao;

import pl.sda.arp4.model.Car;

import java.util.List;
import java.util.Optional;

public class CarDao implements ICarDao{
    @Override
    public void dodajSamochod(Car car) {

    }

    @Override
    public void usunSamochod(Car car) {

    }

    @Override
    public Optional<Car> zwrocSamochod(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Car> zwrocListeSamochodow() {
        return null;
    }

    @Override
    public void updateSamochod(Car car) {

    }
}
