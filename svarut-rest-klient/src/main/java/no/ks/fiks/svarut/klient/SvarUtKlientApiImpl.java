package no.ks.fiks.svarut.klient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.ks.fiks.svarut.klient.exceptions.HttpException;
import no.ks.fiks.svarut.klient.model.Error;
import no.ks.fiks.svarut.klient.model.*;
import org.eclipse.jetty.client.*;
import org.eclipse.jetty.client.transport.HttpClientTransportDynamic;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.MultiPart;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SvarUtKlientApiImpl implements SvarUtKlientApi {

    private static final String FORSENDELSE_PART = "forsendelse";
    private static final String FILER_PART = "filer";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl;

    private final HttpClient client;
    private final String username;
    private final String password;

    public SvarUtKlientApiImpl(String baseUrl, String username, String password) {
        ClientConnector clientConnector = new ClientConnector();
        clientConnector.setSslContextFactory(new SslContextFactory.Client());
        this.baseUrl = baseUrl;
        this.client = new HttpClient(new HttpClientTransportDynamic(clientConnector));
        this.username = username;
        this.password = password;
        try {
            client.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public SvarUtKlientApiImpl(String baseUrl, HttpClient client, String username, String password) {
        this.baseUrl = baseUrl;
        this.client = client;
        this.username = username;
        this.password = password;
    }

    @Override
    @Deprecated
    public ForsendelsesId startNyForsendelse() {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/startNyForsendelse");
        addAuth(request);
        request.method(HttpMethod.POST);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelsesId.class);
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Deprecated
    public ForsendelsesId sendForsendelseMedId(Forsendelse forsendelse, ForsendelsesId forsendelsesId, Map<String, InputStream> data) {
        if (forsendelse == null) throw new IllegalArgumentException("Forsendelse kan ikke være null");
        validateForNull(forsendelsesId);
        if (data == null) throw new IllegalArgumentException("Data kan ikke være null");
        try {
            final Request request = client
                    .newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelsesId.getId().toString() + "/sendForsendelse")
                    .method(HttpMethod.POST)
                    .body(buildMultipartContent(forsendelse, data))
                    .idleTimeout(16, TimeUnit.MINUTES);
            addAuth(request);
            final ContentResponse send = request.send();

            if (send.getStatus() != 200) {
                throw getHttpException(send);
            } else {
                return objectMapper.readValue(send.getContentAsString(), ForsendelsesId.class);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void slettForsendelseDokumenter(ForsendelsesId... forsendelseId) {
        if (forsendelseId == null || forsendelseId.length == 0 || Arrays.stream(forsendelseId).anyMatch(Objects::isNull)) throw new IllegalArgumentException("ForsendelseId kan ikke være null");
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/slettFiler")
                .method(HttpMethod.POST)
                .body(buildJsonStringContent(forsendelseId));
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() != 204) {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ForsendelsesId sendForsendelse(Forsendelse forsendelse, Map<String, InputStream> data) {
        if (forsendelse == null) throw new IllegalArgumentException("Forsendelse kan ikke være null");
        if (data == null) throw new IllegalArgumentException("Data kan ikke være null");
        try {
            final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/sendForsendelse")
                    .method(HttpMethod.POST)
                    .body(buildMultipartContent(forsendelse, data))
                    .idleTimeout(16, TimeUnit.MINUTES);
            addAuth(request);
            final ContentResponse send = request.send();

            if (send.getStatus() != 200) {
                throw getHttpException(send);
            } else {
                return objectMapper.readValue(send.getContentAsString(), ForsendelsesId.class);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ForsendelseStatus hentStatus(ForsendelsesId forsendelseId) {
        validateForNull(forsendelseId);

        return hentStatus(forsendelseId.getId());
    }

    @Override
    public ForsendelseStatus hentStatus(UUID forsendelseId) {
        validateForNull(forsendelseId);

        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/status");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelseStatus.class);
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ForsendelseStatus> hentStatuser(ForsendelsesId... forsendelseId) {
        if (forsendelseId == null || forsendelseId.length == 0 || Arrays.stream(forsendelseId).anyMatch((v) -> v == null || v.getId() == null)) throw new IllegalArgumentException("ForsendelseId kan ikke være null");
        return hentStatuser(Arrays.stream(forsendelseId).map(ForsendelsesId::getId).toArray(UUID[]::new));
    }

    @Override
    public List<ForsendelseStatus> hentStatuser(UUID... forsendelseId) {
        if (forsendelseId == null || forsendelseId.length == 0 || Arrays.stream(forsendelseId).anyMatch(Objects::isNull)) throw new IllegalArgumentException("ForsendelseId kan ikke være null");
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/statuser")
                .method(HttpMethod.POST)
                .body(buildJsonStringContent(forsendelseId));
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelseStatusResult.class).getStatuser();
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addAuth(Request request) {
        URI uri = URI.create(baseUrl);
        Authentication.Result authn = new BasicAuthentication.BasicResult(uri, username, password);
        authn.apply(request);
    }

    @Override
    public ForsendelsesHistorikk retrieveForsendelsesHistorikk(ForsendelsesId forsendelseId) {
        validateForNull(forsendelseId);
        return retrieveForsendelsesHistorikk(forsendelseId.getId());
    }

    @Override
    public ForsendelsesHistorikk retrieveForsendelsesHistorikk(UUID forsendelseId) {
        validateForNull(forsendelseId);
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/historikk");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;

            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelsesHistorikk.class);
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DokumentMetadata> retrieveDokumentMetadata(ForsendelsesId forsendelseid) {
        validateForNull(forsendelseid);
        return retrieveDokumentMetadata(forsendelseid.getId());
    }

    @Override
    public List<DokumentMetadata> retrieveDokumentMetadata(UUID forsendelseid) {
        validateForNull(forsendelseid);
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseid + "/dokumentMetadata");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), DokumentMetadataResult.class).getDokumentMetadata();
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> forsendelseTyper() {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/forsendelseTyper");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelseTyperResult.class).getForsendelseTyper();
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MottakerForsendelsesTyper> retrieveMottakersystem(String organisasjonsNummer, String forsendelsesType, Integer niva) {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/mottakersystem");

        if (organisasjonsNummer != null) {
            if (!organisasjonsNummer.matches("^[0-9]{9}$")) {
                throw new IllegalArgumentException("Orgnr må bestå av nøyaktig ni tall: " + organisasjonsNummer);
            }
            request.param("organisasjonsNummer", organisasjonsNummer);
        }

        if (forsendelsesType != null) {
            if (!forsendelsesType.matches("^[a-zA-ZæøåÆØÅ0-9]+(\\.[a-zA-ZæøåÆØÅ0-9]+)*$")) {
                throw new IllegalArgumentException("Forsendelsestype har ugyldig format: " + forsendelsesType);
            }
            request.param("forsendelsesType", forsendelsesType);
        }

        if (niva != null) {
            if (niva != 3 && niva != 4) {
                throw new IllegalArgumentException("Ugyldig nivå: " + niva);
            }
            request.param("niva", String.valueOf(niva));
        }

        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 200) {
                return objectMapper.readValue(send.getContentAsString(), MottakerForsendelsesTyperResult.class).getTreff();
            } else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<MottakerForsendelsesTyper> retrieveMottakersystemForOrgnr(String orgnr) {
        if (orgnr == null || orgnr.length() != 9) throw new IllegalArgumentException("Orgnr må være 9 tall");
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/mottakersystem/" + orgnr);
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), MottakerForsendelsesTyperResult.class).getTreff();
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ForsendelsesId> retrieveForsendelsesIderByEksternRef(String eksternRef) {
        if (eksternRef == null || eksternRef.isEmpty()) throw new IllegalArgumentException("Eksternref kan ikke være null eller tom");
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/eksternref/" + eksternRef);
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelsesIdResult.class).getForsendelseIder();
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SigneringsHistorikk retrieveSigneringsHistorikk(ForsendelsesId forsendelseId) {
        validateForNull(forsendelseId);
        return retrieveSigneringsHistorikk(forsendelseId.getId());
    }

    private void validateForNull(ForsendelsesId forsendelseId) {
        if (forsendelseId == null || forsendelseId.getId() == null) throw new IllegalArgumentException("ForsendelseId kan ikke være null");
    }

    @Override
    public SigneringsHistorikk retrieveSigneringsHistorikk(UUID forsendelseId) {
        validateForNull(forsendelseId);
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/signeringhistorikk");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), SigneringsHistorikk.class);
            else {
                throw getHttpException(send);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpException getHttpException(ContentResponse send) {
        try {
            final Error error = objectMapper.readValue(send.getContentAsString(), Error.class);
            return new HttpException(send.getStatus(), error.getMessage(), error.getErrorcode());
        } catch (Exception e) {
            return new HttpException(send.getStatus(), send.getContentAsString());
        }
    }

    @Override
    public void setForsendelseLestAvEksterntSystem(LestAv lestAv, ForsendelsesId forsendelseId) {
        if (lestAv == null) throw new IllegalArgumentException("Lest av kan ikke være null");
        validateForNull(forsendelseId);
        setForsendelseLestAvEksterntSystem(lestAv, forsendelseId.getId());
    }

    @Override
    public void setForsendelseLestAvEksterntSystem(LestAv lestAv, UUID forsendelseId) {
        if (lestAv == null) throw new IllegalArgumentException("Lest av kan ikke være null");
        validateForNull(forsendelseId);
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/settLest")
                .method(HttpMethod.POST)
                .body(buildJsonStringContent(lestAv));
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() != 200)
                throw getHttpException(send);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateForNull(UUID forsendelseId) {
        if (forsendelseId == null) throw new IllegalArgumentException("ForsendelseId kan ikke være null");
    }

    private MultiPartRequestContent buildMultipartContent(Forsendelse forsendelse, Map<String, InputStream> data) {
        MultiPartRequestContent multipart = new MultiPartRequestContent();
        multipart.addPart(buildForsendelsePart(forsendelse));
        forsendelse.getDokumenter().stream()
                .map(dokument -> buildFilerPart(dokument, data))
                .forEach(multipart::addPart);
        multipart.close();
        return multipart;
    }

    private MultiPart.Part buildForsendelsePart(Forsendelse forsendelse) {
        try {
            return new MultiPart.ContentSourcePart(
                    FORSENDELSE_PART,
                    null,
                    null,
                    new StringRequestContent(
                            "application/json",
                            objectMapper.writeValueAsString(forsendelse)
                    )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Feil under serialisering av forsendelse-metadata", e);
        }
    }

    private MultiPart.Part buildFilerPart(Dokument dokument, Map<String, InputStream> data) {
        return new MultiPart.ContentSourcePart(
                FILER_PART,
                dokument.getFilnavn(),
                null,
                new InputStreamRequestContent(data.get(dokument.getFilnavn()))
        );
    }

    private StringRequestContent buildJsonStringContent(Object toBeSerialized) {
        try {
            return new StringRequestContent(
                    "application/json",
                    objectMapper.writeValueAsString(toBeSerialized)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Feil under serialisering av objekt", e);
        }
    }

}
