

import shutil

def save_temp_file(upload_file):
    temp_path = f"temp_{upload_file.filename}"
    try:
        with open(temp_path, "wb") as buffer:
            shutil.copyfileobj(upload_file.file, buffer)
        print(f"File salvato temporaneamente in: {temp_path}")
        return temp_path
    except Exception as e:
        raise RuntimeError(f"Errore durante il salvataggio del file temporaneo: {str(e)}")



import os

def delete_temp_file(file_path):
    if os.path.exists(file_path):
        os.remove(file_path)
        print(f"File temporaneo eliminato: {file_path}")
    else:
        print(f"File non trovato per l'eliminazione: {file_path}")
