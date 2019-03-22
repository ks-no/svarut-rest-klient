package no.ks.fiks.svarut.klient;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ks.fiks.svarut.klient.model.Forsendelse;
import no.ks.fiks.svarut.klient.model.Status;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Authentication;
import org.eclipse.jetty.client.api.AuthenticationStore;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.*;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

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
        AuthenticationStore auth = client.getAuthenticationStore();
        URI uri = URI.create(baseUrl);
        auth.addAuthenticationResult(new BasicAuthentication.BasicResult(uri, username, password));
    }

    @Override
    public String sendForsendelse(Forsendelse forsendelse, List<InputStream> filer) {
        try {
            MultiPartContentProvider multipart = new MultiPartContentProvider();
            multipart.addFieldPart("forsendelse", new StringContentProvider(objectMapper.writeValueAsString(forsendelse)), null);
            multipart.addFilePart("filer", "filnavn", new InputStreamContentProvider(filer.get(0)), null);
            multipart.close();

            InputStreamResponseListener listener = new InputStreamResponseListener();
            /*newUploadRequest()
                    .method(HttpMethod.POST)
                    .path(pathHandler.getUploadPath(fiksOrganisasjonId, kontoId))
                    .param("kryptert", String.valueOf(kryptert))
                    .content(multipart)
                    .send(listener);

            Response response = listener.get(1, TimeUnit.HOURS);
            if (isError(response.getStatus())) {
                int status = response.getStatus();
                String content = IOUtils.toString(listener.getInputStream(), StandardCharsets.UTF_8);
                throw new DokumentlagerHttpException(String.format("HTTP-feil under opplasting (%d): %s", status, content), status, content);
            }

            return buildResponse(response, objectMapper.readValue(listener.getInputStream(), DokumentMetadataUploadResult.class));*/
            return "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Status hentStatus(UUID forsendelseId) {
        final Request request = client.newRequest(baseUrl + "/forsendelse/v1/" + forsendelseId + "/status");
        addAuth(request);
        try {
            final ContentResponse send = request.send();
            return Status.fromValue(send.getContentAsString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addAuth(Request request) {
        URI uri = URI.create(baseUrl);
        Authentication.Result authn = new BasicAuthentication.BasicResult(uri, username, password);
        authn.apply(request);
    }
}
