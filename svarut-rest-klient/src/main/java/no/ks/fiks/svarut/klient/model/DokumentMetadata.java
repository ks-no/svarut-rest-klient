package no.ks.fiks.svarut.klient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class DokumentMetadata {
    private String mimeType;
    private String filnavn;
    private boolean kanSigneres;
    private long sizeInBytes;
    private String sha256hash;
    private String dokumentType;
    private String nedlasningsUrl;
    private String signeringsUrl;

}
