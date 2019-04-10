package no.ks.fiks.svarut.klient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.ks.fiks.svarut.klient.exceptions.HttpException;
import no.ks.fiks.svarut.klient.model.*;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Authentication;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BasicAuthentication;
import org.eclipse.jetty.client.util.InputStreamContentProvider;
import org.eclipse.jetty.client.util.MultiPartContentProvider;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SvarUtKlientApiImpl implements SvarUtKlientApi {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrl;

    private final HttpClient client;
    private final String username;
    private final String password;

    public SvarUtKlientApiImpl(String baseUrl, String username, String password) {
        this(baseUrl, new HttpClient(), username, password);
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
                throw new HttpException(send.getStatus(), send.getContentAsString());
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
        try {
            MultiPartContentProvider multipart = new MultiPartContentProvider();
            multipart.addFieldPart("forsendelse", new StringContentProvider(objectMapper.writeValueAsString(forsendelse)), null);
            for (Dokument dokument : forsendelse.getDokumenter()) {
                multipart.addFilePart("filer", dokument.getFilnavn(), new InputStreamContentProvider(data.get(dokument.getFilnavn())), null);
            }

            multipart.close();

            final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelsesId.getId().toString() + "/sendForsendelse");
            addAuth(request);
            request.content(multipart);
            request.method(HttpMethod.POST);
            request.idleTimeout(16, TimeUnit.MINUTES);
            final ContentResponse send = request.send();

            if (send.getStatus() != 200) {
                throw new HttpException(send.getStatus(), send.getContentAsString());
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
    public ForsendelsesId sendForsendelse(Forsendelse forsendelse, Map<String, InputStream> data) {
        try {
            MultiPartContentProvider multipart = new MultiPartContentProvider();
            multipart.addFieldPart("forsendelse", new StringContentProvider(objectMapper.writeValueAsString(forsendelse)), null);
            for (Dokument dokument : forsendelse.getDokumenter()) {
                multipart.addFilePart("filer", dokument.getFilnavn(), new InputStreamContentProvider(data.get(dokument.getFilnavn())), null);
            }

            multipart.close();

            final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/sendForsendelse");
            addAuth(request);
            request.content(multipart);
            request.method(HttpMethod.POST);
            request.idleTimeout(16, TimeUnit.MINUTES);
            final ContentResponse send = request.send();

            if (send.getStatus() != 200) {
                throw new HttpException(send.getStatus(), send.getContentAsString());
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
        return hentStatus(forsendelseId.getId());
    }
    @Override
    public ForsendelseStatus hentStatus(UUID forsendelseId) {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/status");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelseStatus.class);
            else {
                throw new HttpException(send.getStatus(), send.getContentAsString());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ForsendelseStatus> hentStatuser(ForsendelsesId... forsendelseId) {
        return hentStatuser(Arrays.stream(forsendelseId).map(v -> v.getId()).toArray(UUID[]::new));
    }

    @Override
    public List<ForsendelseStatus> hentStatuser(UUID... forsendelseId) {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/statuser");
        request.method(HttpMethod.POST);
        addAuth(request);
        try {
            final String content = objectMapper.writeValueAsString(forsendelseId);

            request.content(new StringContentProvider("application/json", content, Charset.forName("UTF-8")));
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return Arrays.asList(objectMapper.readValue(send.getContentAsString(), ForsendelseStatus[].class));
            else {
                throw new HttpException(send.getStatus(), send.getContentAsString());
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
        return retrieveForsendelsesHistorikk(forsendelseId.getId());
    }

    @Override
    public ForsendelsesHistorikk retrieveForsendelsesHistorikk(UUID forsendelseId) {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/historikk");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;

            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelsesHistorikk.class);
            else {
                throw new HttpException(send.getStatus(), send.getContentAsString());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DokumentMetadata> retrieveDokumentMetadata(ForsendelsesId forsendelseid){
        return retrieveDokumentMetadata(forsendelseid.getId());
    }

    @Override
    public List<DokumentMetadata> retrieveDokumentMetadata(UUID forsendelseid){
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseid + "/dokumentMetadata");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return Arrays.asList(objectMapper.readValue(send.getContentAsString(), DokumentMetadata[].class));
            else {
                throw new HttpException(send.getStatus(), send.getContentAsString());
            }
        }catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> forsendelseTyper(){
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/forsendelseTyper");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return Arrays.asList(objectMapper.readValue(send.getContentAsString(), String[].class));
            else {
                throw new HttpException(send.getStatus(), send.getContentAsString());
            }
        }catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<MottakerForsendelsesTyper> retrieveMottakersystemForOrgnr(String orgnr){
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/mottakersystem/" + orgnr);
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return Arrays.asList(objectMapper.readValue(send.getContentAsString(), MottakerForsendelsesTyper[].class));
            else {
                throw new HttpException(send.getStatus(), send.getContentAsString());
            }
        }catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ForsendelsesId> retrieveForsendelsesIderByEksternRef(String eksternRef){
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/eksternref/" + eksternRef);
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return Arrays.asList(objectMapper.readValue(send.getContentAsString(), ForsendelsesId[].class));
            else {
                throw new HttpException(send.getStatus(), send.getContentAsString());
            }
        }catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SigneringsHistorikk retrieveSigneringsHistorikk(ForsendelsesId forsendelseId) {
        return retrieveSigneringsHistorikk(forsendelseId.getId());
    }

    @Override
    public SigneringsHistorikk retrieveSigneringsHistorikk(UUID forsendelseId) {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/signeringhistorikk");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if (send.getStatus() == 404) return null;
            if (send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), SigneringsHistorikk.class);
            else {
                throw new HttpException(send.getStatus(), send.getContentAsString());
            }
        }catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setForsendelseLestAvEksterntSystem(LestAv lestAv, ForsendelsesId forsendelseId) {
        setForsendelseLestAvEksterntSystem(lestAv, forsendelseId.getId());
    }
    @Override
    public void setForsendelseLestAvEksterntSystem(LestAv lestAv, UUID forsendelseId) {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/settLest");
        request.method(HttpMethod.POST);

        addAuth(request);
        try {
            request.content(new StringContentProvider("application/json", objectMapper.writeValueAsString(lestAv), Charset.forName("UTF-8")));
            final ContentResponse send = request.send();
            if(send.getStatus()!= 200)
                throw new HttpException(send.getStatus(), send.getContentAsString());
        }catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
