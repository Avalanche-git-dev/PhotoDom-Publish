import base64
import logging
import json
import redis
from kafka import KafkaConsumer, KafkaProducer
from dotenv import load_dotenv
import os
from app.services.compression import compress_image
from app.services.analysis import analyze_image

# Carica variabili d'ambiente
load_dotenv()

# Configura il logger
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("ImageAnalyzer")

# Configurazioni ambiente
KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS")
PHOTO_UPLOADED_TOPIC = os.getenv("PHOTO_UPLOADED_TOPIC")
PHOTO_PROCESSED_TOPIC = os.getenv("PHOTO_PROCESSED_TOPIC")
KAFKA_GROUP_ID = os.getenv("KAFKA_GROUP_ID")
REDIS_HOST = os.getenv("REDIS_HOST")
REDIS_PORT = os.getenv("REDIS_PORT")

# Connessione a Redis
logger.info("Connettendo a Redis...")
redis_client = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)

# Configura il consumer Kafka
logger.info("Configurando Kafka Consumer...")
consumer = KafkaConsumer(
    PHOTO_UPLOADED_TOPIC,
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
    value_deserializer=lambda v: json.loads(v.decode("utf-8")),
    group_id=KAFKA_GROUP_ID,
    auto_offset_reset="earliest"
)

# Configura il producer Kafka
logger.info("Configurando Kafka Producer...")
producer = KafkaProducer(
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
    value_serializer=lambda v: json.dumps(v).encode("utf-8")
)



def process_uploaded_photo(event):
    """
    Elabora una foto caricata: analizza, comprime e aggiorna i dati in Redis.
    """
    photo_id = event["photoId"]
    user_id = event["userId"]
    cache_key = f"image:{photo_id}"
    result_key = f"result:photo:{photo_id}"

    try:
        if redis_client.exists(result_key):
            logger.warning(f"Messaggio già processato per ID {photo_id}. Ignorando.")
            return
        # Recupera la stringa Base64 dalla cache
        base64_image = redis_client.get(cache_key)
        if not base64_image:
            raise ValueError(f"Immagine non trovata in Redis per ID {photo_id}")

        # Decodifica la stringa Base64
        image_bytes = base64.b64decode(base64_image)

        # Analizza l'immagine
        logger.info(f"Analizzando immagine con ID {photo_id}...")
        is_valid = analyze_image(image_bytes)
        if not is_valid:
            raise ValueError("Immagine non valida")

        # Comprime l'immagine
        logger.info(f"Comprimendo immagine con ID {photo_id}...")
        compressed_image_bytes = compress_image(image_bytes)

        # Ricodifica l'immagine compressa in Base64
        compressed_base64_image = base64.b64encode(compressed_image_bytes).decode('utf-8')

        # Salva l'immagine compressa in Redis
        redis_client.set(result_key, compressed_base64_image)
        redis_client.expire(result_key, 600)
        logger.info(f"Immagine compressa salvata in Redis con chiave {result_key}")

        # Pubblica un evento Kafka per notificare che la foto è stata processata
        producer.send(PHOTO_PROCESSED_TOPIC, {
            "photoId": photo_id,
            "userId": user_id,
            "status": "ok"
        })
        logger.info(f"Immagine {photo_id} processata con successo")

    except Exception as e:
        logger.error(f"Errore durante l'elaborazione della foto {photo_id}: {e}")

        # Pubblica un evento Kafka con lo stato di errore
        producer.send(PHOTO_PROCESSED_TOPIC, {
            "photoId": photo_id,
            "userId": user_id,
            "status": "error",
            "reason": str(e)
        })



if __name__ == "__main__":
    logger.info("Image Analyzer Service in esecuzione...")
    for message in consumer:
        process_uploaded_photo(message.value)
