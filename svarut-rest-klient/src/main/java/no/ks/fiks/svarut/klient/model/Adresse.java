package no.ks.fiks.svarut.klient.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Adresse {

    private PostAdresse postAdresse;
    private DigitalAdresse digitalAdresse;
}
