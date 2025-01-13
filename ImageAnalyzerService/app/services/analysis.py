
import base64
from google.cloud import vision
import os

def analyze_image(base64_image):
    """
    Analizza un'immagine in formato base64 per determinarne la validità.
    """
    # Configura il percorso delle credenziali per Google Vision API
    credentials_path = os.getenv("GOOGLE_APPLICATION_CREDENTIALS")
    if not credentials_path or not os.path.exists(credentials_path):
        raise RuntimeError(f"Credenziali non trovate in: {credentials_path}")

    # Inizializza il client Vision API
    client = vision.ImageAnnotatorClient()

    try:
        # Decodifica l'immagine da base64
        #image_bytes = base64.b64decode(base64_image)
        image_bytes = base64_image

        # Prepara l'immagine per Vision API
        image = vision.Image(content=image_bytes)

        # Esegui il controllo di SafeSearch
        response = client.safe_search_detection(image=image)
        safe = response.safe_search_annotation

        # Determina se l'immagine è appropriata
        if safe.adult == vision.Likelihood.VERY_LIKELY or safe.violence == vision.Likelihood.VERY_LIKELY:
            return False  # Immagine non appropriata
        return True
    except Exception as e:
        raise RuntimeError(f"Errore durante l'analisi dell'immagine: {str(e)}")

