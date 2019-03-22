package no.ks.fiks.svarut.klient;

import no.ks.fiks.svarut.klient.model.Forsendelse;
import no.ks.fiks.svarut.klient.model.Status;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface SvarUtKlientApi {

    public String sendForsendelse(Forsendelse forsendelse, List<InputStream> filer);

    public Status hentStatus(UUID forsendelseId);
}
