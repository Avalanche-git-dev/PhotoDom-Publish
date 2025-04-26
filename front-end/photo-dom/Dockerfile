# Fase 1: build dell'applicazione
FROM node:22 AS build

# Imposta la directory di lavoro
WORKDIR /app

# Copia i file di configurazione di npm
COPY package*.json ./

# Installa tutte le dipendenze
RUN npm install

# Copia tutto il resto dei file
COPY . .

# Costruisci il progetto Angular-dev
RUN npm run build




# Costruisci il progetto Angular-prod-k8s
# RUN npm run build-no-budget


# Fase 2: creazione immagine Nginx
FROM nginx:alpine

# Copia i file buildati nella directory di Nginx
COPY --from=build /app/dist/photo-dom-2/browser /usr/share/nginx/html

# Espone la porta 80
EXPOSE 80

# Avvia Nginx
CMD ["nginx", "-g", "daemon off;"]
