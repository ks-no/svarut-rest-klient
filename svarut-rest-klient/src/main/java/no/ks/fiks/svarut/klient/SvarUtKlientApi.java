package no.ks.fiks.svarut.klient;

import no.ks.fiks.svarut.klient.model.*;

import java.util.UUID;

public interface SvarUtKlientApi {

    public ForsendelsesId sendForsendelse(Forsendelse forsendelse);

    public ForsendelseStatus hentStatus(UUID forsendelseId);

    ForsendelsesHistorikk retrieveForsendelsesHistorikk(ForsendelsesId forsendelseId);

    ForsendelsesHistorikk retrieveForsendelsesHistorikk(UUID forsendelseId);

    SigneringsHistorikk retrieveSigneringsHistorikk(ForsendelsesId forsendelseId);

    SigneringsHistorikk retrieveSigneringsHistorikk(UUID forsendelseId);
}
