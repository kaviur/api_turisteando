package com.proyecto.turisteando.services;

import com.proyecto.turisteando.exceptions.customExceptions.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FileUploadService {

    List<String> saveImage(List<MultipartFile> multipartFiles) throws FileUploadException;
    List<String> uploadImage(List<MultipartFile> multipartFiles) throws FileUploadException;
    List<String> updateImage(List<String> existingImages, List<MultipartFile> newImages) throws FileUploadException;

}