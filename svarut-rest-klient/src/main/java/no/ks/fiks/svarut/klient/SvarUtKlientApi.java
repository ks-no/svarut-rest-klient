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

    /**
     * Sender forsendelse til SvarUt, kan ta 15min å fullføre denne rest operasjonen. ReadTimeout er satt til 16min, slik at SvarUt Timer ut først.
     * Hvis du får forsendelseid tilbake er alt i orden, da garanterer SvarUt for leveranse. Ingen grunn til å sjekke om den blir levert.
     * @param forsendelse
     * @return ForsendelseID
     */
    public ForsendelsesId sendForsendelse(Forsendelse forsendelse);

    /**
     * Henter status på en forsendelse. Ikke bruk denne i batch jobber. Brukes til å vise status til saksbehandler eller noen som er interesert i å vite hva som skjedde med forsendelsen.
     * @param forsendelseId
     * @return forsendelse status.
     */
    ForsendelseStatus hentStatus(ForsendelsesId forsendelseId);

    /**
     * @see #hentStatus(ForsendelsesId)
     * @param forsendelseId
     * @return forsendelseStatus
     */
    public ForsendelseStatus hentStatus(UUID forsendelseId);

    /**
     * henter flere statuser
     * @see #hentStatus(ForsendelsesId)
     * @param forsendelseId
     * @return
     */
    List<ForsendelseStatus> hentStatuser(ForsendelsesId... forsendelseId);

    /**
     * henter flere statuser
     * @see #hentStatus(ForsendelsesId)
     * @param forsendelseId
     * @return
     */
    List<ForsendelseStatus> hentStatuser(UUID... forsendelseId);

    /**
     * Henter tekstlig beskrivelse av hva som har skjedd når med en forsendelse. Brukes når dette er interesant å se. Ikke bruk i batch overføringer.
     * @param forsendelseId
     * @return ForsendelseHistorikk
     */
    ForsendelsesHistorikk retrieveForsendelsesHistorikk(ForsendelsesId forsendelseId);

    /**
     * @see #retrieveForsendelsesHistorikk(ForsendelsesId)
     * @param forsendelseId
     * @return
     */
    ForsendelsesHistorikk retrieveForsendelsesHistorikk(UUID forsendelseId);

    /**
     * Henter Dokumentmetadata, brukes for å finne urler til dokumenter i SvarUt.
     * @param forsendelseid
     * @return Liste med dokumentMetadata
     */
    List<DokumentMetadata> retrieveDokumentMetadata(ForsendelsesId forsendelseid);

    /**
     * @see #retrieveDokumentMetadata(ForsendelsesId)
     * @param forsendelseid
     * @return
     */
    List<DokumentMetadata> retrieveDokumentMetadata(UUID forsendelseid);

    /**
     * Henter forsendelsesid med eksternRef. Kan få flere resultat om samme eksternRef er sendt flere ganger.
     * @param eksternRef
     * @return Liste med forsendelsesId
     */
    List<ForsendelsesId> retrieveForsendelsesIderByEksternRef(String eksternRef);

    /**
     * Henter signeringshistorikk
     * @param forsendelseId
     * @return Signeringshistorikk
     */
    SigneringsHistorikk retrieveSigneringsHistorikk(ForsendelsesId forsendelseId);

    /**
     * @see #retrieveSigneringsHistorikk(ForsendelsesId)
     * @param forsendelseId
     * @return
     */
    SigneringsHistorikk retrieveSigneringsHistorikk(UUID forsendelseId);

    /**
     * SettForsendelseLestAvEksterntSystem, setter en forsendelse lest. Brukes når den er lest i annet system og en vil stoppe print/videre prosessering i SvarUt.
     * @param lestAv
     * @param forsendelseId
     */
    void setForsendelseLestAvEksterntSystem(LestAv lestAv, ForsendelsesId forsendelseId);

    /**
     * @see #setForsendelseLestAvEksterntSystem(LestAv,ForsendelsesId)
     * @param lestAv
     * @param forsendelseId
     */
    void setForsendelseLestAvEksterntSystem(LestAv lestAv, UUID forsendelseId);
}
