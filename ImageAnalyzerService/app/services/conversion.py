# from PIL import Image

# def convert_to_webp(file_path):
#     output_path = f"{file_path}.webp"
#     with Image.open(file_path) as img:
#         img.save(output_path, "WEBP", quality=80)
#     return output_path

from PIL import Image

def convert_to_webp(file_path):
    output_path = f"{file_path}.webp"
    try:
        with Image.open(file_path) as img:
            img.save(output_path, "WEBP", quality=80)
    except Exception as e:
        raise RuntimeError(f"Errore durante la conversione in WebP: {str(e)}")
    return output_path
