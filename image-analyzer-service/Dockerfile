FROM python:3.10

WORKDIR /app

# Copia i file necessari
COPY requirements.txt ./    
COPY . /app  


# Copia il file delle credenziali nel container
COPY credentials/imageanalyzer-443109-c6a036efc6a7.json /app/credentials/imageanalyzer-443109-c6a036efc6a7.json

# Installa le dipendenze
RUN pip install --no-cache-dir -r requirements.txt

# Imposta il PYTHONPATH
ENV PYTHONPATH=/app

# Avvia il servizio
CMD ["python", "app/main.py"]
