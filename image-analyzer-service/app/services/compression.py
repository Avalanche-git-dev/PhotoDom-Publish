

from venv import logger
from PIL import Image
import io


def compress_image(image_bytes, quality=70):
    

    try:
        # Crea un BytesIO dall'immagine originale
        image_stream = io.BytesIO(image_bytes)
        image = Image.open(image_stream)

        # Logga il formato e la modalità dell'immagine
        logger.info(f"Immagine caricata con formato: {image.format}, modalità: {image.mode}, dimensioni: {image.size}")

        # Se l'immagine non è in RGB, convertila
        if image.mode not in ("RGB", "L"):
            image = image.convert("RGB")
            logger.info(f"Immagine convertita in modalità RGB.")

        # Crea un BytesIO per l'immagine compressa
        compressed_stream = io.BytesIO()

        # Salva l'immagine compressa nel nuovo stream
        image.save(compressed_stream, format='JPEG', optimize=True, quality=quality)
        compressed_stream.seek(0)

        # Leggi i byte compressi
        compressed_image_bytes = compressed_stream.read()
        logger.info(f"Immagine compressa con successo. Dimensione: {len(compressed_image_bytes)} bytes")

        return compressed_image_bytes

    except Exception as e:
        logger.error(f"Errore durante la compressione: {e}")
        raise e  # Rilancia l'eccezione per essere gestita a livello superiore
