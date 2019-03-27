package no.ks.fiks.svarut.klient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class ForsendelseStatus {

    private ForsendelsesId forsendelsesId;
    private Date sisteStatusEndring;
    private Status status;
}
