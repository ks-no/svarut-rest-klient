package no.ks.fiks.svarut.klient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class Forsendelse {

    private Adresse mottaker;
    private Set<DigitalAdresse> eksponertFor;
    private String avgivendeSystem;

    private String tittel;
    private String konteringsKode;
    private boolean kunDigitalLevering;
    private boolean kryptert;
    private UtskriftsKonfigurasjon utskriftsKonfigurasjon;
    private boolean krevNiva4Innlogging;
    private NoarkMetadataFraAvleverendeSaksSystem metadataFraAvleverendeSystem;
    private NoarkMetadataForImport metadataForImport;
    private Adresse svarSendesTil;
    private ForsendelsesId svarPaForsendelse;
    private List<Dokument> dokumenter;
    private List<Lenke> lenker;
    private String forsendelsesType;
    private String eksternReferanse;
    private boolean svarPaForsendelseLink;

    private Date signeringUtloper;

    private SignaturType signaturType;
}
