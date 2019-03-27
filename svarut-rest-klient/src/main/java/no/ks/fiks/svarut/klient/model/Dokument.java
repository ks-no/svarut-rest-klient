package no.ks.fiks.svarut.klient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import java.util.TreeSet;

@Data
@Builder
public class Dokument {

    private String filnavn;
    private String mimeType;
    private Boolean skalSigneres;
    private String dokumentType;
    @JsonIgnore
    private DataHandler data;
    private TreeSet<Integer> giroarkSider;
    private boolean ekskluderesFraUtskrift;
}
