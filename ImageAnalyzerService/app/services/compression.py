# from PIL import Image
# import os

# def compress_image(file_path, max_size_mb=15.5):
#     output_path = f"{file_path}_compressed.jpg"
#     with Image.open(file_path) as img:
#         quality = 85
#         while os.path.getsize(file_path) > max_size_mb * 1024 * 1024 and quality > 10:
#             img.save(output_path, "JPEG", quality=quality)
#             quality -= 5
#     return output_path


from PIL import Image
import os

def compress_image(file_path, max_size_mb=15.5):
    # Percorso per il file compresso
    output_path = f"{file_path}_compressed.jpg"
    
    try:
        with Image.open(file_path) as img:
            quality = 85
            img.save(output_path, "JPEG", quality=quality)  # Salvataggio iniziale
            
            # Controlla la dimensione del file e riduce la qualità se necessario
            while os.path.getsize(output_path) > max_size_mb * 1024 * 1024 and quality > 10:
                quality -= 5
                img.save(output_path, "JPEG", quality=quality)

        # Verifica che il file sia stato creato correttamente
        if not os.path.exists(output_path):
            raise FileNotFoundError(f"Il file compresso non è stato creato: {output_path}")

        return output_path
    except Exception as e:
        raise RuntimeError(f"Errore durante la compressione: {str(e)}")

