# FROM python:3.10

# WORKDIR /app

# COPY . /app

# RUN pip install --no-cache-dir -r requirements.txt

# COPY credentials/imageanalyzer-443109-934322aa4ffd.json /app/credentials/imageanalyzer-443109-934322aa4ffd.json

# # Esponi la porta dell'app
# EXPOSE 8000

# CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]


FROM python:3.10

WORKDIR /app

# Copia i file necessari
COPY requirements.txt ./
COPY . /app

# Copia il file delle credenziali nel container
COPY credentials/imageanalyzer-443109-c6a036efc6a7.json /app/credentials/imageanalyzer-443109-c6a036efc6a7.json

# Installa le dipendenze
RUN pip install --no-cache-dir -r requirements.txt

# Imposta la variabile d'ambiente
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/credentials/imageanalyzer-443109-c6a036efc6a7.json

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]



