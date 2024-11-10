package com.proyecto.turisteando.utils;

import com.proyecto.turisteando.exceptions.customExceptions.FileValidationException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Component
public class FileValidator {

    public static boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public static void validateFiles(List<MultipartFile> files) throws FileValidationException {
        // Validar que la lista de archivos no esté vacía
        if (files == null || files.isEmpty() || files.get(0).isEmpty()) {
            throw new FileValidationException("Debes cargar al menos una imagen");
        }
        // Validar el tamaño de la lista de archivos
        if (files.size() > 5) {
            throw new FileValidationException("El número máximo de imágenes permitidas es 5");
        }

        // Validar que los archivos sean imágenes y que no superen los 10 MB
        for (MultipartFile file : files) {
            if (!isImage(file)) {
                throw new FileValidationException("El archivo " + file.getOriginalFilename() + " no es una imagen");
            } else if (file.getSize() > 10 * 1024 * 1024) {
                throw new FileValidationException("El tamaño máximo de cada imagen debe ser de 10 MB");
            }
        }
    }


}