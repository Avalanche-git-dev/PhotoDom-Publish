<p align="right">
  <strong>🇮🇹 Italiano</strong> |
  <a href="./README.en.md">🇬🇧 English</a>
</p>

# Front-End - Photo-Dom

Questo progetto rappresenta la parte frontend dell'applicazione **Photo-Dom**, sviluppato con **Angular 22**.

---

## ⚙️ Requisiti Prerequisiti

- **Node.js** installato (consigliata versione LTS 18+).
- **Angular CLI** installato globalmente:
  ```bash
  npm install -g @angular/cli
  ```

---

## 🐳 Nota sul Deploy Docker-Compose

Se si utilizza **Docker-Compose**:

- Assicurarsi di avere **Node.js** installato per la prima configurazione.
- Aprire **Node.js Prompt** o il terminale con Angular CLI.
- Eseguire:
  ```bash
  npm install
  ```
  per installare tutte le dipendenze del progetto.

- Entrare nella directory frontend:
  ```bash
  cd front-end/photo-dom
  ```

- Avviare il progetto in modalità di sviluppo locale:
  ```bash
  ng serve
  ```

- Una volta avviato, il frontend sarà disponibile su:
  - [http://localhost:4200](http://localhost:4200)

> ⚠️ Ricorda di modificare i puntamenti API se necessario, nel file:
> `src/environments/environment.ts`

---

# 🔄 Modalità di Deploy

- **Modalità Sviluppo**: `ng serve` manuale.
- **Modalità Produzione**: build automatizzata tramite Docker-Compose o Kubernetes.

---

# 👩‍💻 Contributore

- [Mohamed Gabr Ashour](https://github.com/Avalanche-git-dev)

---

> **Photo-Dom**: non solo un'applicazione, ma un ecosistema distribuito completo per imparare e migliorarsi.