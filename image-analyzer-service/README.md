<p align="right">
  <strong>🇮🇹 Italiano</strong> |
  <a href="./README.en.md">🇬🇧 English</a>
</p>

# Image-Analyzer-Service

Microservizio parte del progetto **Photo-Dom**, responsabile dell'**analisi automatica delle immagini** caricate dagli utenti.
Utilizza intelligenza artificiale per il safe-search e ottimizza le immagini tramite compressione.

---

## ✨ Funzionalità principali

- Consumo eventi asincroni da **Kafka** (topic `photo-uploaded`).
- Analisi contenuti immagini tramite **Google Cloud Vision API** (Safe Search Detection).
- Compressione e ottimizzazione immagini con **Pillow** (senza perdita qualitativa rilevante).
- Pubblicazione eventi elaborati su **Kafka** (topic `photo-processed`).
- Utilizzo intensivo di **Redis** come cache temporanea per immagini e metadati.

---

## 💾 Struttura Principale

| Cartella/File         | Descrizione                                         |
| :-------------------- | :-------------------------------------------------- |
| `services/`            | Modulo AI di analisi immagini e compressione       |
| `utils/`               | Funzioni di supporto                               |
| `main.py`              | Corpo principale del modulo                        |

---

## ⚙️ Tecnologie e Librerie Principali

| Tecnologia/Stack            | Ruolo                                     |
| :--------------------------- | :--------------------------------------- |
| **Python 3.10+**             | Linguaggio principale                    |
| **kafka-python**             | Consumer e Producer Kafka                |
| **redis-py**                 | Accesso ottimizzato alla cache Redis     |
| **pillow**                   | Compressione immagini senza perdita      |
| **google-cloud-vision**      | Safe Content Detection tramite AI        |
| **dotenv**                   | Gestione variabili ambiente             |

---

## 🔗 Comunicazioni Esterne

| Sistema                 | Protocollo | Descrizione                                   |
| :---------------------- | :--------- | :------------------------------------------- |
| **Photo-Service**        | Kafka      | Ricezione eventi upload immagini             |
| **Photo-Service**        | Kafka      | Restituzione eventi immagine analizzata       |
| **Google Cloud Vision**  | HTTPS API  | Analisi SafeSearch immagini                   |
| **Redis**                | TCP        | Caching temporaneo immagini e risultati      |

---

## 🔐 Sicurezza

- Comunicazioni sicure su rete interna Kubernetes.
- Accesso a Google Cloud Vision autenticato tramite file JSON.
- Kafka utilizzato solo su network privato.

---

## 🛠️ Setup sviluppo (locale)

1. Installare dipendenze Python:

```bash
pip install -r requirements.txt
```

2. Modificare le variabili ambiente se necessario 


3. Avviare il microservizio:

```bash
python app.py
```

> **⚠️** Assicurarsi che Redis, Kafka e Photo-Service siano attivi!

---

## 📃 Requisiti principali (`requirements.txt`)

```text
kafka-python
redis
python-dotenv
pillow
google-cloud-vision
```

---

## 📊 Event-Driven Flow

- Consumo evento `photo-uploaded` da Kafka.
- Recupero immagine da **Redis**.
- Analisi tramite **Google Cloud Vision API**.
- Compressione immagine con **Pillow**.
- Pubblicazione evento `photo-processed` su Kafka.
- Aggiornamento metadati su **Redis** per Photo-Service.

![alt text](image.png)

---

## 📊 Ottimizzazioni principali

- Utilizzo di **Pillow** per:
  - Compressare immagini senza perdita qualitativa evidente.
  - Ridurre dimensioni e tempi di elaborazione backend.
- Gestione caching **batch-oriented** su Redis:
  - Serializzazione uniforme in JSON.
  - Minimizzazione load server Redis.

---

## 👩‍💻 Contributore

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

# 🌟 Stato

**Completato** — Pronto per ambienti di sviluppo e produzione.
