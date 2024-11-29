# from google.cloud import vision
# import os

# def analyze_image(file_path):
#     client = vision.ImageAnnotatorClient()
#     with open(file_path, "rb") as image_file:
#         content = image_file.read()
#     image = vision.Image(content=content)
#     response = client.safe_search_detection(image=image)
#     safe = response.safe_search_annotation

#     if safe.adult == vision.Likelihood.VERY_LIKELY or safe.violence == vision.Likelihood.VERY_LIKELY:
#         return False  # Immagine non appropriata
#     return True





# from google.cloud import vision
# import os

# def analyze_image(file_path):
#     client = vision.ImageAnnotatorClient()
#     try:
#         with open(file_path, "rb") as image_file:
#             content = image_file.read()
#         image = vision.Image(content=content)
#         response = client.safe_search_detection(image=image)
#         safe = response.safe_search_annotation

#         if safe.adult == vision.Likelihood.VERY_LIKELY or safe.violence == vision.Likelihood.VERY_LIKELY:
#             return False  # Immagine non appropriata
#         return True
#     except Exception as e:
#         raise RuntimeError(f"Errore durante l'analisi dell'immagine: {str(e)}")





from google.cloud import vision
import os

def analyze_image(file_path):
    # Verifica che le credenziali siano configurate correttamente
    credentials_path = os.getenv("GOOGLE_APPLICATION_CREDENTIALS")
    if not credentials_path or not os.path.exists(credentials_path):
        raise RuntimeError(f"Credenziali non trovate in: {credentials_path}")
    
    # Inizializza il client Vision API
    client = vision.ImageAnnotatorClient()
    
    try:
        # Leggi il contenuto dell'immagine
        with open(file_path, "rb") as image_file:
            content = image_file.read()
        image = vision.Image(content=content)
        
        # Richiesta SafeSearch Detection
        response = client.safe_search_detection(image=image)
        safe = response.safe_search_annotation
        
        # Analizza i risultati
        if safe.adult == vision.Likelihood.VERY_LIKELY or safe.violence == vision.Likelihood.VERY_LIKELY:
            return False  # Immagine non appropriata
        return True
    except Exception as e:
        raise RuntimeError(f"Errore durante l'analisi dell'immagine: {str(e)}")