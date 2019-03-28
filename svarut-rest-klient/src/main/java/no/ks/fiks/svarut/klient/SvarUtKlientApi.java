package no.ks.fiks.svarut.klient;

import no.ks.fiks.svarut.klient.model.*;

import java.util.List;
import java.util.UUID;

public interface SvarUtKlientApi {

    @Deprecated
    /**
     * "Bør bruke sendForsendelse om en ikke absolutt må få iden før en sender forsendelse."
     */
    ForsendelsesId startNyForsendelse();

    @Deprecated
    /**
     * "Bør bruke sendForsendelse om en ikke absolutt må få iden før en sender forsendelse."
     */
    ForsendelsesId sendForsendelseMedId(Forsendelse forsendelse, ForsendelsesId forsendelsesId);

    public ForsendelsesId sendForsendelse(Forsendelse forsendelse);

    ForsendelseStatus hentStatus(ForsendelsesId forsendelseId);

    public ForsendelseStatus hentStatus(UUID forsendelseId);

    List<ForsendelseStatus> hentStatuser(ForsendelsesId... forsendelseId);

    List<ForsendelseStatus> hentStatuser(UUID... forsendelseId);

    ForsendelsesHistorikk retrieveForsendelsesHistorikk(ForsendelsesId forsendelseId);

    ForsendelsesHistorikk retrieveForsendelsesHistorikk(UUID forsendelseId);

    List<DokumentMetadata> retrieveDokumentMetadata(ForsendelsesId forsendelseid);

    List<DokumentMetadata> retrieveDokumentMetadata(UUID forsendelseid);

    List<ForsendelsesId> retrieveForsendelsesIderByEksternRef(String eksternRef);

    SigneringsHistorikk retrieveSigneringsHistorikk(ForsendelsesId forsendelseId);

    SigneringsHistorikk retrieveSigneringsHistorikk(UUID forsendelseId);

    void setForsendelseLestAvEksterntSystem(LestAv lestAv, ForsendelsesId forsendelseId);

    void setForsendelseLestAvEksterntSystem(LestAv lestAv, UUID forsendelseId);
}
