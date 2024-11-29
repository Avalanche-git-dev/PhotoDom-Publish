# from fastapi import FastAPI, UploadFile, HTTPException
# from app.services.compression import compress_image
# from app.services.analysis import analyze_image
# from app.services.conversion import convert_to_webp
# from app.utils.file_manager import save_temp_file, delete_temp_file
# from dotenv import load_dotenv
# import os


# load_dotenv()  

# app = FastAPI()

# @app.post("/analyze-image/")
# async def analyze_image_endpoint(file: UploadFile):
#     # Salva temporaneamente il file
#     temp_file_path = save_temp_file(file)

#     try:
#         # Analizza l'immagine (API esterna)
#         is_appropriate = analyze_image(temp_file_path)
#         if not is_appropriate:
#             raise HTTPException(status_code=400, detail="Immagine non appropriata")

#         # Comprime l'immagine
#         compressed_file_path = compress_image(temp_file_path, max_size_mb=15.5)

#         # Converte in WebP
#         final_file_path = convert_to_webp(compressed_file_path)

#         # Restituisce il file processato
#         return {"message": "Immagine elaborata con successo", "file_path": final_file_path}
#     finally:
#         # Pulizia dei file temporanei
#         delete_temp_file(temp_file_path)





# from fastapi import FastAPI, UploadFile, HTTPException
# from app.services.compression import compress_image
# from app.services.analysis import analyze_image
# from app.services.conversion import convert_to_webp
# from app.utils.file_manager import save_temp_file, delete_temp_file
# from dotenv import load_dotenv
# import os

# load_dotenv() 

# credentials_path = os.getenv("GOOGLE_APPLICATION_CREDENTIALS")
# if not credentials_path or not os.path.exists(credentials_path):
#     raise RuntimeError(f"File credenziali non trovato o variabile non impostata: {credentials_path}")

# app = FastAPI()

# @app.post("/analyze-image/")
# async def analyze_image_endpoint(file: UploadFile):
#     if not file.filename.lower().endswith(('.png', '.jpg', '.jpeg', '.webp')):
#         raise HTTPException(status_code=400, detail="File non supportato. Carica un'immagine valida.")

#     temp_file_path = save_temp_file(file)

#     try:
#         # Analizza l'immagine (API esterna)
#         is_appropriate = analyze_image(temp_file_path)
#         if not is_appropriate:
#             raise HTTPException(status_code=400, detail="Immagine non appropriata")

#         # Comprime l'immagine
#         compressed_file_path = compress_image(temp_file_path, max_size_mb=15.5)

#         # Converte in WebP
#         final_file_path = convert_to_webp(compressed_file_path)

#         # Restituisce il file processato
#         return {"message": "Immagine elaborata con successo", "file_path": final_file_path}
#     except Exception as e:
#         raise HTTPException(status_code=500, detail=f"Errore durante l'elaborazione: {str(e)}")
#     finally:
#         # Pulizia dei file temporanei
#         delete_temp_file(temp_file_path)





from fastapi import FastAPI, UploadFile, HTTPException
from app.services.compression import compress_image
from app.services.analysis import analyze_image
from app.services.conversion import convert_to_webp
from app.utils.file_manager import save_temp_file, delete_temp_file
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
