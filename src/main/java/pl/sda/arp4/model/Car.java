package pl.sda.arp4.model;
//Model (samochód):
//- nazwa
//- marka
//- data produkcji (pełna data, nie tylko rocznik, ale bez czasu)
//- typ nadwozia (SEDAN, CABRIO, SUV)
//- ilość pasażerów / miejsc siedzących
//- *typ skrzyni biegów (MANUAL, AUTO)
//- *pojemność silnika (Double)

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String nazwa;
    private String marka;
    private LocalDate dataProdukcji;

    @Enumerated(EnumType.STRING)
    private TypNadwozia nadwozie;
    private Integer miejscaSiedzace;
    private Double pojemnoscSilnika;

    @Enumerated(EnumType.STRING)
    private TypSkrzyni skrzynia;

    @OneToMany (mappedBy = "car", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<CarRental> carRentals;

    public Car(String nazwa, String marka, LocalDate dataProdukcji, TypNadwozia nadwozie, Integer miejscaSiedzace, Double pojemnoscSilnika, TypSkrzyni skrzynia) {
        this.nazwa = nazwa;
        this.marka = marka;
        this.dataProdukcji = dataProdukcji;
        this.nadwozie = nadwozie;
        this.miejscaSiedzace = miejscaSiedzace;
        this.pojemnoscSilnika = pojemnoscSilnika;
        this.skrzynia = skrzynia;
    }
}

