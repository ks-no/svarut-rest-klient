package no.ks.fiks.svarut.klient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

import java.net.URI;
import java.util.UUID;
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
        /*AuthenticationStore auth = client.getAuthenticationStore();
        URI uri = URI.create(baseUrl);
        auth.addAuthenticationResult(new BasicAuthentication.BasicResult(uri, username, password));*/
    }

    @Override
    public ForsendelsesId sendForsendelse(Forsendelse forsendelse) {
        try {
            MultiPartContentProvider multipart = new MultiPartContentProvider();
            multipart.addFieldPart("forsendelse", new StringContentProvider(objectMapper.writeValueAsString(forsendelse)), null);
            for (Dokument dokument : forsendelse.getDokumenter()) {
                multipart.addFilePart("filer", dokument.getFilnavn(), new InputStreamContentProvider(dokument.getData().getInputStream()), null);
            }

            multipart.close();

            final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/sendForsendelse");
            addAuth(request);
            request.content(multipart);
            request.method(HttpMethod.POST);
            request.idleTimeout(16, TimeUnit.MINUTES);
            final ContentResponse send = request.send();

            if (send.getStatus() != 200) {
                throw new RuntimeException("Send failed " +  send.getStatus() + " : " + send.getContentAsString());
            } else {
                return objectMapper.readValue(send.getContentAsString(), ForsendelsesId.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ForsendelseStatus hentStatus(UUID forsendelseId) {
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/status");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if(send.getStatus() == 404) return null;
            if(send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelseStatus.class);
            else {
                log.error("Failed to get status {}: {} ", send.getStatus(), send.getContentAsString());
                throw new RuntimeException("Error getting status resposne code " + send.getStatus());
            }
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
    public ForsendelsesHistorikk retrieveForsendelsesHistorikk(ForsendelsesId forsendelseId){
        return retrieveForsendelsesHistorikk(forsendelseId.getId());
    }

    @Override
    public ForsendelsesHistorikk retrieveForsendelsesHistorikk(UUID forsendelseId){
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/historikk");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if(send.getStatus() == 404) return null;
            if(send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), ForsendelsesHistorikk.class);
            else {
                log.error("Failed to get historikk {}: {} ", send.getStatus(), send.getContentAsString());
                throw new RuntimeException("Error getting historikk resposne code " + send.getStatus());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SigneringsHistorikk retrieveSigneringsHistorikk(ForsendelsesId forsendelseId){
        return retrieveSigneringsHistorikk(forsendelseId.getId());
    }

    @Override
    public SigneringsHistorikk retrieveSigneringsHistorikk(UUID forsendelseId){
        final Request request = client.newRequest(baseUrl + "/tjenester/api/forsendelse/v1/" + forsendelseId + "/signeringhistorikk");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            if(send.getStatus() == 404) return null;
            if(send.getStatus() == 200)
                return objectMapper.readValue(send.getContentAsString(), SigneringsHistorikk.class);
            else {
                log.error("Failed to get signeringhistorikk {}: {} ", send.getStatus(), send.getContentAsString());
                throw new RuntimeException("Error getting signeringhistorikk response code " + send.getStatus());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
