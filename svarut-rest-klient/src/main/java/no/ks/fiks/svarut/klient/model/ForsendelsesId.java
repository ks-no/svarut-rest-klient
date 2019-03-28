package no.ks.fiks.svarut.klient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class ForsendelsesId {
    private UUID id;

    public String uuidAsString() {
        return Objects.toString(id);
    }


    public static ForsendelsesId toForsendelsesId(UUID id) {
        return ForsendelsesId.builder().id(id).build();
    }

    public static ForsendelsesId generateRandomForsendelsesId() {
        return toForsendelsesId((UUID.randomUUID()));
    }
}
