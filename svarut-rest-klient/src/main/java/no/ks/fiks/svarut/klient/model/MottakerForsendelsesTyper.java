package no.ks.fiks.svarut.klient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class MottakerForsendelsesTyper {

    private String organisasjonsNummer;
    private String forsendelsesType;
    private String mottakerSystem;
    private String mottakerId;
    private int niva;
}
