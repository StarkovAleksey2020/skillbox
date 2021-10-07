package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.book.file.BookFileEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.BookFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class ResourceStorage {

    @Value("${upload.path}")
    String uploadPath;

    @Value("${download.path}")
    String downloadPath;

    private BookFileRepository bookFileRepository;

    @Autowired
    public ResourceStorage(BookFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }

    public String saveNewBookImage(MultipartFile file, String slug) throws IOException {
        String resourceURI = null;

        if (!file.isEmpty()) {
            if (!new File(uploadPath).exists()) {
                Files.createDirectories(Paths.get(uploadPath));
                Logger.getLogger(this.getClass().getSimpleName()).info("created image folder in " + uploadPath);
            }

            String fileName = slug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = "/book-covers/" + fileName;
            file.transferTo(path);
            Logger.getLogger(this.getClass().getSimpleName()).info(fileName + " uploaded OK!");
        }
        return resourceURI;
    }

    public Path getBookFilePath(String hash) throws BookstoreAPiWrongParameterException {
        if (hash != null && !hash.equals("")) {
            BookFileEntity bookFileEntity = bookFileRepository.findBookFileEntityByHash(hash);
            return Paths.get(bookFileEntity.getPath());
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public MediaType getBookFileMime(String hash) throws BookstoreAPiWrongParameterException {
        if (hash != null && !hash.equals("")) {
            BookFileEntity bookFileEntity = bookFileRepository.findBookFileEntityByHash(hash);
            String mimeType = getMimeType(bookFileEntity);
            if (mimeType != null) {
                return MediaType.parseMediaType(mimeType);
            } else {
                return MediaType.APPLICATION_OCTET_STREAM;
            }
        } else {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException, BookstoreAPiWrongParameterException {
        validateHash(hash);
        BookFileEntity bookFileEntity = bookFileRepository.findBookFileEntityByHash(hash);
        Path path = Paths.get(downloadPath, bookFileEntity.getPath());
        return Files.readAllBytes(path);

    }

    public String getMimeType(BookFileEntity bookFileEntity) {
        return URLConnection.guessContentTypeFromName(Paths.get(bookFileEntity.getPath()).getFileName().toString());
    }

    private void validateHash(String hash) throws BookstoreAPiWrongParameterException {
        if (hash == null || hash.equals("")) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }
}
