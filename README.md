# SvarUt-rest-klient 
![GitHub License](https://img.shields.io/github/license/ks-no/svarut-rest-klient)
[![Maven Central](https://img.shields.io/maven-central/v/no.ks.fiks.svarut/svarut-rest-klient)](https://search.maven.org/artifact/no.ks.fiks.svarut/svarut-rest-klient)
![GitHub Release Date](https://img.shields.io/github/release-date/ks-no/svarut-rest-klient.svg)
![GitHub Last Commit](https://img.shields.io/github/last-commit/ks-no/svarut-rest-klient.svg)

### Denne klienten fungerer kun mot versjon 1 av REST-APIet. For nyere versjoner, se https://github.com/ks-no/fiks-svarut-klient

Klient bibliotek mot SvarUt ForsendelseRestService. 

### Prerequisites

| Client version | JDK version | Jetty client version |
|----------------|-------------|----------------------|
| 1.x.x          | 11          | 9                    |
| 2.x.x          | 11          | 11                   |
| 3.x.x          | 17          | 11                   |
| 4.x.x          | 17          | 12                   |

##### Maven
Add dependency no.ks.fiks.svarut:svarut-rest-klient in your POM.

    <dependencies>
       <dependency>
            <groupId>no.ks.fiks.svarut</groupId>
            <artifactId>svarut-rest-klient</artifactId>
            <version>x.x.x</version>
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
