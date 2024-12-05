

import asyncio
import logging
from fastapi import FastAPI, UploadFile, HTTPException
from app.services.compression import compress_image
from app.services.analysis import analyze_image
from app.services.conversion import convert_to_webp
from app.utils.file_manager import save_temp_file, delete_temp_file
from kafka import KafkaConsumer, KafkaProducer
import redis
import json
from dotenv import load_dotenv
import os

load_dotenv() 

credentials_path = os.getenv("GOOGLE_APPLICATION_CREDENTIALS")
if not credentials_path or not os.path.exists(credentials_path):
    raise RuntimeError(f"File credenziali non trovato o variabile non impostata: {credentials_path}")

app = FastAPI()




@app.post("/analyze-image/")
async def analyze_image_endpoint(file: UploadFile):
    if not file.filename.lower().endswith(('.png', '.jpg', '.jpeg', '.webp')):
        raise HTTPException(status_code=400, detail="File non supportato. Carica un'immagine valida.")

    temp_file_path = save_temp_file(file)
    print(f"File temporaneo salvato: {temp_file_path}")

    try:
        # Analizza l'immagine
        print("Analisi dell'immagine in corso...")
        is_appropriate = analyze_image(temp_file_path)
        if not is_appropriate:
            raise HTTPException(status_code=400, detail="Immagine non appropriata")
        print("Analisi completata. L'immagine è appropriata.")

        # Comprime l'immagine
        print("Compressione dell'immagine in corso...")
        compressed_file_path = compress_image(temp_file_path, max_size_mb=15.5)
        print(f"File compresso salvato: {compressed_file_path}")

        # Converte in WebP
        print("Conversione in WebP in corso...")
        final_file_path = convert_to_webp(compressed_file_path)
        print(f"File convertito in WebP salvato: {final_file_path}")

        return {"message": "Immagine elaborata con successo", "file_path": final_file_path}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Errore durante l'elaborazione: {str(e)}")
    finally:
        # Pulizia dei file temporanei
        print(f"Eliminazione del file temporaneo: {temp_file_path}")
        delete_temp_file(temp_file_path)









# # Configura il logger
# log_level = os.getenv("LOG_LEVEL", "INFO")
# logging.basicConfig(level=log_level)
# logger = logging.getLogger("ImageAnalyzer")

# # Configura le variabili d'ambiente
# KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS")
# PHOTO_UPLOADED_TOPIC = os.getenv("PHOTO_UPLOADED_TOPIC")
# PHOTO_PROCESSED_TOPIC = os.getenv("PHOTO_PROCESSED_TOPIC")
# KAFKA_GROUP_ID = os.getenv("KAFKA_GROUP_ID")
# REDIS_HOST = os.getenv("REDIS_HOST")
# REDIS_PORT = os.getenv("REDIS_PORT")



# logger.info(f"KAFKA_BOOTSTRAP_SERVERS: {os.getenv('KAFKA_BOOTSTRAP_SERVERS')}")
# logger.info(f"PHOTO_UPLOADED_TOPIC: {os.getenv('PHOTO_UPLOADED_TOPIC')}")
# logger.info(f"REDIS_HOST: {os.getenv('REDIS_HOST')}")

# # Connessione a Redis
# logger.info("Connettendo a Redis...")
# redis_client = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)

# # Configura il consumatore Kafka
# logger.info("Configurando Kafka Consumer...")
# consumer = KafkaConsumer(
#     PHOTO_UPLOADED_TOPIC,
#     bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
#     value_deserializer=lambda v: json.loads(v.decode("utf-8")),
#     group_id=KAFKA_GROUP_ID,
#     auto_offset_reset="earliest"
# )

# # Configura il produttore Kafka
# logger.info("Configurando Kafka Producer...")
# producer = KafkaProducer(
#     bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
#     value_serializer=lambda v: json.dumps(v).encode("utf-8")
# )

# # Funzione per processare un'immagine
# def process_uploaded_photo(event):
#     logger.debug(f"Messaggio ricevuto: {event}")
#     photo_id = event["photoId"]
#     user_id = event["userId"]
#     cache_key = f"file:temp:{photo_id}"

#     # Recupera i dati dalla cache
#     cached_data = redis_client.get(cache_key)
#     if not cached_data:
#         logger.error(f"Errore: Nessun file trovato per ID {photo_id}")
#         return

#     # Decodifica i dati
#     photo_data = json.loads(cached_data)

#     try:
#         # Analizza l'immagine
#         logger.info(f"Analizzando immagine con ID {photo_id}...")
#         is_valid = analyze_image(photo_data["imageBytes"])
#         if not is_valid:
#             logger.warning(f"Immagine {photo_id} non valida. Rimuovendo dalla cache...")
#             redis_client.delete(cache_key)
#             return

#         # Comprime l'immagine
#         logger.info(f"Comprimendo immagine con ID {photo_id}...")
#         compressed_image = compress_image(photo_data["imageBytes"])
#         photo_data["imageBytes"] = compressed_image

#         # Salva l'immagine compressa nella cache
#         redis_client.set(cache_key, json.dumps(photo_data))
#         redis_client.expire(cache_key, 600)
#         logger.info(f"Immagine {photo_id} salvata nella cache dopo la compressione.")

#         # Pubblica un messaggio su `photo-processed`
#         processed_event = {
#             "photoId": photo_id,
#             "userId": user_id,
#             "timestamp": event["timestamp"],
#             "status": "processed"
#         }
#         producer.send(PHOTO_PROCESSED_TOPIC, processed_event)
#         logger.info(f"Messaggio pubblicato su {PHOTO_PROCESSED_TOPIC} per ID {photo_id}.")

#     except Exception as e:
#         logger.error(f"Errore durante l'elaborazione della foto {photo_id}: {e}")

# # Ciclo principale
# if __name__ == "__main__":
#     logger.info("Image Analyzer in esecuzione...")
#     for message in consumer:
#         process_uploaded_photo(message.value)





# import logging
# import json
# from kafka import KafkaConsumer, KafkaProducer
# import redis
# from dotenv import load_dotenv
# import os
# from app.services.compression import compress_image
# from app.services.analysis import analyze_image

# # Carica le variabili d'ambiente
# load_dotenv()

# # Configura il logger
# log_level = os.getenv("LOG_LEVEL", "INFO")
# logging.basicConfig(level=log_level)
# logger = logging.getLogger("ImageAnalyzer")

# # Configura le variabili d'ambiente
# KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS")
# PHOTO_UPLOADED_TOPIC = os.getenv("PHOTO_UPLOADED_TOPIC")
# PHOTO_PROCESSED_TOPIC = os.getenv("PHOTO_PROCESSED_TOPIC")
# KAFKA_GROUP_ID = os.getenv("KAFKA_GROUP_ID")
# REDIS_HOST = os.getenv("REDIS_HOST")
# REDIS_PORT = os.getenv("REDIS_PORT")

# logger.info(f"KAFKA_BOOTSTRAP_SERVERS: {KAFKA_BOOTSTRAP_SERVERS}")
# logger.info(f"PHOTO_UPLOADED_TOPIC: {PHOTO_UPLOADED_TOPIC}")
# logger.info(f"REDIS_HOST: {REDIS_HOST}")

# # Connessione a Redis
# try:
#     redis_client = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)
#     redis_client.ping()
#     logger.info("Connessione a Redis riuscita.")
# except redis.exceptions.ConnectionError as e:
#     logger.error(f"Errore nella connessione a Redis: {e}")
#     exit(1)

# # Configura il consumatore Kafka
# try:
#     consumer = KafkaConsumer(
#         PHOTO_UPLOADED_TOPIC,
#         bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
#         value_deserializer=lambda v: json.loads(v.decode("utf-8")),
#         group_id=KAFKA_GROUP_ID,
#         auto_offset_reset="earliest"
#     )
#     logger.info("Kafka Consumer configurato correttamente.")
# except Exception as e:
#     logger.error(f"Errore nella configurazione del Kafka Consumer: {e}")
#     exit(1)

# # Configura il produttore Kafka
# try:
#     producer = KafkaProducer(
#         bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
#         value_serializer=lambda v: json.dumps(v).encode("utf-8")
#     )
#     logger.info("Kafka Producer configurato correttamente.")
# except Exception as e:
#     logger.error(f"Errore nella configurazione del Kafka Producer: {e}")
#     exit(1)

# # Funzione per processare un'immagine
# def process_uploaded_photo(event):
#     logger.debug(f"Messaggio ricevuto: {event}")
#     photo_id = event.get("photoId")
#     user_id = event.get("userId")
#     if not photo_id or not user_id:
#         logger.error("Il messaggio ricevuto manca di `photoId` o `userId`.")
#         return

#     cache_key = f"file:temp:{photo_id}"

#     # Recupera i dati dalla cache
#     cached_data = redis_client.get(cache_key)
#     if not cached_data:
#         logger.error(f"Errore: Nessun file trovato per ID {photo_id}")
#         return

#     # Decodifica i dati
#     photo_data = json.loads(cached_data)

#     try:
#         # Analizza l'immagine
#         logger.info(f"Analizzando immagine con ID {photo_id}...")
#         is_valid = analyze_image(photo_data["imageBytes"])
#         if not is_valid:
#             logger.warning(f"Immagine {photo_id} non valida. Rimuovendo dalla cache...")
#             redis_client.delete(cache_key)
#             return

#         # Comprime l'immagine
#         logger.info(f"Comprimendo immagine con ID {photo_id}...")
#         compressed_image = compress_image(photo_data["imageBytes"])
#         photo_data["imageBytes"] = compressed_image

#         # Salva l'immagine compressa nella cache
#         redis_client.set(cache_key, json.dumps(photo_data))
#         redis_client.expire(cache_key, 600)
#         logger.info(f"Immagine {photo_id} salvata nella cache dopo la compressione.")

#         # Pubblica un messaggio su `photo-processed`
#         processed_event = {
#             "photoId": photo_id,
#             "userId": user_id,
#             "timestamp": event.get("timestamp"),
#             "status": "processed"
#         }
#         producer.send(PHOTO_PROCESSED_TOPIC, processed_event)
#         logger.info(f"Messaggio pubblicato su {PHOTO_PROCESSED_TOPIC} per ID {photo_id}.")

#     except Exception as e:
#         logger.error(f"Errore durante l'elaborazione della foto {photo_id}: {e}")

# # Ciclo principale
# def main():
#     logger.info("Image Analyzer in esecuzione...")
#     try:
#         for message in consumer:
#             logger.info(f"Messaggio consumato: {message.value}")
#             process_uploaded_photo(message.value)
#     except KeyboardInterrupt:
#         logger.info("Interruzione ricevuta, chiusura consumer e producer...")
#     finally:
#         consumer.close()
#         producer.close()
#         logger.info("Consumer e producer chiusi.")

# if __name__ == "__main__":
#     main()
