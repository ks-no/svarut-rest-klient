package no.ks.fiks.svarut.klient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class NoarkMetadataFraAvleverendeSaksSystem {

    private int saksSekvensNummer;
    private int saksAar;
    private int journalAar;
    private int journalSekvensNummer;
    private int journalPostNummer;
    private String journalPostType;
    private String journalStatus;
    private Date journalDato;
    private Date dokumentetsDato;
    private String tittel;
    private String saksBehandler;
    private List<Entry> ekstraMetadata;
}
