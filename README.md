# Spring-configurator

## Descrizione

Questa applicazione Spring Boot è progettata per configurare e avviare un'applicazione utilizzando file di configurazione YAML specificati dall'utente. L'applicazione legge variabili d'ambiente da un file `.env` e le sostituisce nei file YAML di configurazione.

## Funzionalità

- **Caricamento delle variabili d'ambiente**: Legge le variabili d'ambiente da un file `.env`.
- **Sostituzione delle variabili nei file YAML**: Sostituisce le variabili d'ambiente nei file YAML di configurazione specificati.
- **Supporto per più file YAML**: Supporta la configurazione di uno o due file YAML.

## Requisiti

- Java 1.8 o superiore
- Maven 3.6.0 o superiore

## Compilazione

Per compilare il progetto, eseguire il seguente comando Maven:

mvn clean install


Per eseguire l'applicazione, utilizzare il seguente comando:

````
java -jar target/spring-configurator-1.0-SNAPSHOT.jar <envFilePath> <ymlFilePath1> [<ymlFilePath2>]
````


- `<envFilePath>`: Percorso al file `.env` contenente le variabili d'ambiente.
- `<ymlFilePath1>`: Percorso al primo file YAML di configurazione.
- `[<ymlFilePath2>]`: (Opzionale) Percorso al secondo file YAML di configurazione.

## Esempio

````
java -jar target/spring-configurator-1.0-SNAPSHOT.jar src/main/resources/.env src/main/resources/application-prod.yml src/main/resources/application-dev.yml
`````

## Struttura del Progetto

- **src/main/java**: Contiene il codice sorgente Java.
- **src/main/resources**: Contiene i file di configurazione e le risorse statiche.
- **pom.xml**: File di configurazione di Maven.

## Autore

- Nome: [Il Tuo Nome]
- Email: [la.tua.email@example.com]

## Licenza

Questo progetto è distribuito sotto la licenza MIT. Vedi il file [LICENSE](LICENSE) per maggiori dettagli.
