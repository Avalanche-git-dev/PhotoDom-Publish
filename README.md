# Photo-Dom - Documentazione Tecnica

**Photo-Dom** è un'applicazione social sviluppata da zero considerabile pertanto di tipo gestionale, da consegnare a un customer, progettata per esplorare e implementare:
1. **Logiche di architettura a microservizi**: affrontare i concetti chiave, come la decomposizione del dominio, la comunicazione tra servizi e la resilienza.
2. **Conoscenze tecnologiche**: ampliare le competenze introducendo una varietà di tecnologie, stack e pattern di progettazione.


### Obiettivi principali:

- **Studio delle architetture a microservizi**: ciascun servizio è progettato seguendo il principio della singola responsabilità.
- **Sperimentazione tecnologica**: utilizzo di tecnologie come Java, Spring Boot, Angular, MongoDB, Redis e kafka per costruire un'applicazione scalabile e performante.
- **Learn by doing, affrontando le logiche di una architettura distrubuita, ottimizzandola in risposta alle esigenze di una cusomter che la rileverà al rilascio.

## **Requisiti**

Per modellare la realtà al meglio i requisiti del customer sono:
- Creare una applicazione flessibile, che abbia una struttura tale da poter accettare in futuro aggiunte di intere features senza dover modificare il codice preesistente.
- L'applicazione deve essere altamente scalabile, sopratutto orizzontalmente. Il cliente esige infatti che l'applicazione mantenga i costi contenuti in termini di risorse visto l'incognita dell'utenza.
- Allo stesso modo del punto precedente, l'aumento dei costi dovrà essere correlazionato all'aumento di utenza e gestito in maniera dinamica.
- Il cliente richiede l'implementazione di una logica tale da permettere all'applicazione di essere resiliente, e di ridurre al minimo le dipendenze fra servizi.



## **Features**

**Photo-Dom** è un social network dove gli utenti possono:
- Autenticazione protetta, e distinzione fra ruoli admin/user.
- Creare, caricare e gestire foto.
- Interagire sia con i profili di altri utenti che con le foto di altri utenti tramite like e commenti.
- Gli amministratori possono gestire gli utenti (es. bannare, promuovere, monitorare, rimuovere post non consoni).
- Supporto a notifiche in tempo reale per like/commenti tramite WebSocket. 

L'applicazione è composta da più servizi indipendenti, ognuno con responsabilità ben definite, e utilizza tecnologie moderne per garantire scalabilità, monitoraggio e manutenzione.

---

### **General Networking**
La rete è organizzata per consentire comunicazioni interne sicure tra i microservizi e connessioni esterne per i client.

#### **Topologia di rete**
1. **Client (Frontend)**: Angular, comunicazione con il backend tramite HTTP/REST e WebSocket.
2. **Gateway API**: Punto di ingresso centrale per il routing delle richieste verso i microservizi.
3. **Microservizi**: Comunicazione tra servizi tramite REST o messaggi asincroni (Apache Kafka).
4. **Database**: Ogni servizio gestisce il proprio database (ad esempio, MongoDB per foto e Commenti, mentre per utenti MySQL).


#### **Diagramma della rete**
![Diagramma UML](PhotoDom-General.png)





## **Concetti Generali**

Questo progetto è stato sviluppato con l'obiettivo di:

### **Applicare principi di progettazione avanzata**
- **Single Responsibility Principle (SRP)**: ogni servizio è responsabile di una specifica funzione, riducendo le dipendenze e migliorando la manutenibilità.
- **Event-Driven Architecture**: utilizzo di Apache Kafka per eventi asincroni tra i microservizi.

### **Garantire resilienza e scalabilità**
- **Resilience4J**: Circuit breaker per gestire i guasti dei servizi.
- **API Gateway**: Bilanciamento del carico e gestione centralizzata delle richieste.

### **Implementare una sicurezza avanzata**
- **Keycloak** come Identity Provider (IDP) per OAuth2 e JWT.
- Protezione degli endpoint con **Spring Security**.

---

## **Tecnologie utilizzate**

### **Backend**
- **Java 17+** con **Spring Boot 3.3.7**: Framework principale per lo sviluppo dei microservizi.
- **Spring Security**: Implementazione di OAuth2 e JWT.
- **MongoDB**: Archiviazione delle foto e dei commenti.
- **MySQL**: Gestione degli utenti.
- **Redis**: Cache distribuita per migliorare le prestazioni.
- **Apache Kafka**: Messaggistica asincrona per comunicazioni event-driven.
- **Loki**: Logging centralizzato per monitoraggio e debug.

### **Frontend**
- **Angular 22**: Framework per lo sviluppo del frontend.
- **Angular Material**: Libreria per un design moderno e responsivo.
- **RxJS**: Gestione reattiva e asincrona.
- **WebSocket**: Notifiche in tempo reale per aggiornamenti di like e commenti.

---

## **Caratteristiche principali**

### **Gestione utenti**
- Registrazione sicura con OAuth2 e Keycloak.
- Ruoli distinti: **Admin** e **User**.
- Login e gestione social profilo come funzionalità di base.
- Gestione utenti e contenuto del social, come funzionalità avanzata per gli ADMIN.

### **Gestione delle foto**
- Caricamento, modifica ed eliminazione delle foto.
- Gestione tramite event driven architecture e caching con Redis.
- Photo-Service gestisce le operazioni crud e le funzionalità riguardanti le foto.
- Image-Analyzer-Service, è un piccolo servizio containerizzato on permise, scritto in python, che si occupa di:
 - Analizzare l'immagine tramite CloudVisionApi.
 - Ottimizzarne formato e dimensione per la corretta conservazione dei contenuti mediatici.
- Salvataggio scalabile su **MongoDB** tramite **GridFS** del contenuto dell'immagine, dei suoi metadati su PostgreSQL.

### **Interazioni social**
- Aggiunta e rimozione di commenti e repliche agli stessi.
- Sistema di like per le foto.
- Notifiche in tempo reale.

### **Amministrazione avanzata**
- Moderazione utenti (ban, promozione).
- Monitoraggio dei post e gestione contenuti non conformi.

### **Resilienza e monitoraggio**
- Logging centralizzato con **Loki**.
- Circuit breaker per mantenere la disponibilità del sistema.

---

## **Avvio dell'applicazione**

### **Backend**
1. Configura i database (**MySQL** per gli utenti, **MongoDB** per le foto e i commenti).
2. Assicurati che **Kafka** e **Redis** siano in esecuzione.
3. Avvia ogni servizio dalla directory corrispondente:
   ```bash
   mvn spring-boot:run

### **Frontend**
1. Installa le dipendenze: npm install 
2. Avvia il server di sviluppo, utilizzando lo script: ng serve
3. Visita l'applicazione su: http://localhost:4200

## **Documentazione API**

Ogni microservizio è documentato tramite **Swagger/OpenAPI**. Puoi esplorare la documentazione direttamente tramite Swagger UI o accedere alle specifiche in formato JSON per ulteriori integrazioni.

- **User Service**:
  - **Swagger UI**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
  - **OpenAPI JSON**: [docs/api/user-api.json](docs/api//user-api.json)

- **Photo Service**:
  - **Swagger UI**: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
  - **OpenAPI JSON**: [docs/api/photo-api.json](docs/api/photo-api.json)

- **Comment Service**:
  - **Swagger UI**: [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)
  - **OpenAPI JSON**: [docs/api/comment-api.json](docs/api/comment-api.json)

---

## **Test delle API**

Le collection Postman per testare le API sono disponibili nei seguenti file:

- **User Controller**: [Scarica](docs/test/PhotoDom-MicroServices-User.postman_collection.json)
- **Admin Controller**: [Scarica](docs/test/PhotoDom-MicroServices-Admin.postman_collection.json)
- **Keycloak Controller**: [Scarica](docs/test/PhotoDom-MicroServices-Keycloak.postman_collection.json)
- **Photo Controller**: [Scarica](docs/test/PhotoDom-MicroServices-Photo.postman_collection.json)
- **Comment Controller**: [Scarica](docs/test/PhotoDom-MicroServices-Comment.postman_collection.json)
- **ApiGateway**: [Scarica](docs/test/PhotoDom-MicroServices-APIGATEWAY.postman_collection.json)
- **WebFlow Endpoint**: [Scarica](docs/test/PhotoDom-MicroServices-WebFlow-TEST.postman_collection.json)


Puoi importare questi file in Postman:
1. Apri Postman.
2. Vai su **File > Import**.
3. Carica il file JSON.




## **Learning Objectives**

Questo progetto offre un'ottima opportunità per apprendere:

1. Progettazione e implementazione di **architetture a microservizi**.
2. Configurazione e utilizzo di strumenti avanzati come **Redis**, **Kafka** e **MongoDB GridFS**.
3. Applicazione e studio delle principali componenti di spring cloud e dei pattern per ottimizzarne la flessibilità e la scalabilità.
4. Sicurezza delle applicazioni tramite **OAuth2**, **JWT** e **Keycloak**.
5. Realizzazione di un frontend con **Angular**, ottimizzato per:
   - Propagazione corretta delle risposte dal backend, grazie alla formattazione uniforme delle risposte e ai **Global Exception Handler** per ogni servizio.
   - Riduzione dei tempi di risposta tramite **caching** backend con **Redis**.
   - Notifiche in tempo reale sfruttando **WebSocket**.
6. Monitoraggio delle applicazioni con **Loki** e logging centralizzato per debug e tracciamento.

---

## **Contributori**

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev) - **Sviluppatore full-stack**
