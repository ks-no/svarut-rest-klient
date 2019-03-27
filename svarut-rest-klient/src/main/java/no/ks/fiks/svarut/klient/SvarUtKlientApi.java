package no.ks.fiks.svarut.klient;

import no.ks.fiks.svarut.klient.model.Forsendelse;
import no.ks.fiks.svarut.klient.model.ForsendelseStatus;
import no.ks.fiks.svarut.klient.model.ForsendelsesHistorikk;
import no.ks.fiks.svarut.klient.model.ForsendelsesId;

import java.util.UUID;

public interface SvarUtKlientApi {

    public ForsendelsesId sendForsendelse(Forsendelse forsendelse);

    public ForsendelseStatus hentStatus(UUID forsendelseId);

    ForsendelsesHistorikk retrieveForsendelsesHistorikk(ForsendelsesId forsendelseId);

    ForsendelsesHistorikk retrieveForsendelsesHistorikk(UUID forsendelseId);
}
