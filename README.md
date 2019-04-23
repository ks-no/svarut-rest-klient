# SvarUt-rest-klient [![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.ks.fiks.svarut/svarut-rest-klient/badge.svg)](https://maven-badges.herokuapp.com/maven-central/no.ks.fiks.svarut/svarut-rest-klient)

Klient bibliotek mot SvarUt ForsendelseRestService.

## Getting Started
Legg til maven dependency.

### Prerequisites

  - Java 1.8 or higher

##### Maven
Add dependency no.ks.fiks.svarut:svarut-rest-klient in your POM.

    <dependencies>
       <dependency>
            <groupId>no.ks.fiks.svarut</groupId>
            <artifactId>svarut-rest-klient</artifactId>
            <version>1.0.0-SNAPSHOT</version>
       </dependency>
    </dependencies>


## Usage

```java
Forsendelse forsendelse = //construct;
HashMap<String, InputStream> filer = //construct;
SvarUtKlientApi klient = new SvarUtKlientApiImpl("http://test.svarut.ks.no", avsender, servicePassord);
ForsendelseId id = klient.sendForsendelse(forsendelse, filer);
```


## Dokumentasjon på service:
 
 * [FORSENDELSERESTSERVICEV1](https://ks-no.github.io/svarut/integrasjon/forsendelserestservicev1/)
