package no.ks.fiks.svarut.klient.model;

import javax.xml.bind.annotation.XmlType;

@XmlType
public enum Status {
    MOTTATT("Mottatt"), AKSEPTERT("Akseptert"), AVVIST("Avvist"), VARSLET("Varslet"), LEST("Lest"), SENDT_PRINT("Sendt print"), PRINTET("Printet"), MANUELT_HANDTERT("Manuelt håndtert"), SENDT_DIGITALT("Sendt digitalt"), SENDT_SDP("Sendt sdp"), LEVERT_SDP("Levert sdp"), KLAR_FOR_MOTTAK("Klar for mottak"), IKKE_LEVERT("Ikke levert");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status fromValue(String v) {
        if (v == null || v.trim().isEmpty())
            return null;
        for (Status c : Status.values()) {
            if (c.value.equalsIgnoreCase(v) || c.toString().equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
