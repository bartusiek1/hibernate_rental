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
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Enumerated(EnumType.STRING)

    private String nazwa;
    private String marka;
    private LocalDate dataProdukcji;
    private TypNadwozia nadwozie;
    private int miejscaSiedzace;
    private Double pojemnoscSilnika;
    private TypSkrzyni skrzynia;
}

