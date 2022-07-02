package pl.sda.arp4.dao;

import pl.sda.arp4.model.Car;

import java.util.List;
import java.util.Optional;

public interface ICarDao {

    // Create
    public void dodajSamochod(Car car);

    // Delete
    public void usunSamochod(Car car);

    // Read
    public Optional<Car> zwrocSamochod(Long id);

    public List<Car> zwrocListeSamochodow();

    // Update
    public void updateSamochod(Car car);
}
