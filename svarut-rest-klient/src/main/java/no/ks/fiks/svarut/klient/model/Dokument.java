package no.ks.fiks.svarut.klient.model;

import lombok.Builder;
import lombok.Data;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import java.util.TreeSet;

@Data
@Builder
public class Dokument {

    @XmlElement(required = true)
    private String filnavn;
    @XmlElement(required = true)
    private String mimeType;
    @XmlElement(required = true)
    private Boolean skalSigneres;
    private String dokumentType;
    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    private DataHandler data;
    private TreeSet<Integer> giroarkSider;
    private boolean ekskluderesFraUtskrift;
}
