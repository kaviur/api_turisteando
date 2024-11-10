package com.proyecto.turisteando.services.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.proyecto.turisteando.config.CloudinaryConfig;
import com.proyecto.turisteando.exceptions.customExceptions.FileUploadException;
import com.proyecto.turisteando.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class FileUploadServiceImpl implements FileUploadService {
    private final CloudinaryConfig cloudinaryConfig;

    @Override
    public List<String> saveImage(List<MultipartFile> multipartFiles) throws FileUploadException {
        List<String> listImageName = new ArrayList<>();
        Cloudinary cloudinary = cloudinaryConfig.configuration();

        Map<String, Object> params = getUploadParams();

        for (MultipartFile image : multipartFiles) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), params);
                String imageUrl = uploadResult.get("url").toString();
                listImageName.add(imageUrl);
            } catch (IOException ex) {
                throw new FileUploadException("Error al subir la imagen: " + image.getOriginalFilename(), ex);
            }
        }
        return listImageName;
    }

    /**
     * Método -uploadImage- actúa como un alias de `saveImage` sin agregar funcionalidad extra.
     * Puede ser eliminado si no se requiere una diferenciación entre la lógica de carga y de guardado.
     * Sin embargo, se deja en caso de necesitar una separación de responsabilidades en el futuro
     * (por ejemplo, para manejar configuraciones distintas en cargas temporales o finales).
     */
    @Override
    public List<String> uploadImage(List<MultipartFile> multipartFiles) throws FileUploadException {
        return saveImage(multipartFiles);
    }

    @Override
    public List<String> updateImage(List<String> imagesToKeep, List<String> imagesToDelete, List<MultipartFile> newImages) throws FileUploadException {
        Cloudinary cloudinary = cloudinaryConfig.configuration();

        // Elimina solo las imágenes especificadas en imagesToDelete
        if (!imagesToDelete.isEmpty()) {
            deleteExistingImages(imagesToDelete, cloudinary);
        }

        // Subir las nuevas imágenes y obtener las URLs
        List<String> uploadedImages = newImages.isEmpty() ? Collections.emptyList() : saveImage(newImages);

        // Combina las imágenes a conservar y las nuevas en la lista final
        List<String> finalImages = new ArrayList<>(imagesToKeep);
        finalImages.addAll(uploadedImages);

        return finalImages;  // Devuelve todas las imágenes activas después de la actualización
    }

    // Método auxiliar para obtener los parámetros de subida
    private Map<String, Object> getUploadParams() {
        return ObjectUtils.asMap(
                "use_filename", true,
                "folder", "turisteando",
                "unique_filename", true,
                "overwrite", false
        );
    }

    // Método auxiliar para eliminar imágenes especificadas
    private void deleteExistingImages(List<String> imagesToDelete, Cloudinary cloudinary) throws FileUploadException {
        for (String imageUrl : imagesToDelete) {
            try {
                // Extrae el public_id desde la URL de la imagen
                String publicId = extractPublicId(imageUrl);
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } catch (IOException ex) {
                throw new FileUploadException("Error al eliminar la imagen existente: " + imageUrl, ex);
            }
        }
    }

    // Método auxiliar para extraer el public_id desde la URL de la imagen
    private String extractPublicId(String imageUrl) {
        // Lógica para extraer el public_id del URL (por ejemplo, usando un regex)
        return imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.lastIndexOf('.'));
    }
}
