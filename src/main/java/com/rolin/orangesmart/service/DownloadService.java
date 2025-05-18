package com.rolin.orangesmart.service;

import com.rolin.orangesmart.exception.errorEnum.AttachmentErrorEnum;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.model.attachment.entity.Storage;
import com.rolin.orangesmart.properties.StoreProperties;
import com.rolin.orangesmart.service.IService.IDocStorageService;
import com.rolin.orangesmart.util.DirectoryUtil;
import com.rolin.orangesmart.util.FileIdJasyptUtil;
import com.rolin.orangesmart.util.FileUtil;
import com.rolin.orangesmart.util.IdGarbleUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

/**
 * DownloadService
 */
@Service
public class DownloadService {

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private StoreProperties storeProperties;

    @Resource
    private IDocStorageService docStorageService;

    /**
     * @param encryptedId
     * @param response
     * @return void
     * @throws
     * @Title: download
     */
    public void download(String encryptedId, HttpServletResponse response) {
//        long id = FileIdJasyptUtil.idDecrypt(encryptedId);
//        id = IdGarbleUtil.idDecrypt(id);
        long id = Integer.parseInt(encryptedId);
        this.download(id, response);
    }

    public void download(long id, HttpServletResponse response) {
        Attachment attachment = attachmentService.getById(id);
        this.download(attachment, response);
    }

    public void download(Attachment attachment, HttpServletResponse response) {
        Storage storage = toDocStorage(attachment);
        docStorageService.download(storage, response);
    }

    /**
     * @param encryptedId
     * @param response
     * @return void
     * @throws
     * @Title: onlineView
     */
    public void onlineView(String encryptedId, HttpServletResponse response) {
        long id = FileIdJasyptUtil.idDecrypt(encryptedId);
        id = IdGarbleUtil.idDecrypt(id);
        this.onlineView(id, response);
    }

    public void onlineView(long id, HttpServletResponse response) {
        Attachment attachment = attachmentService.getById(id);
        this.onlineView(attachment, response);
    }

    public void onlineView(Attachment attachment, HttpServletResponse response) {
        Storage storage = toDocStorage(attachment);
        docStorageService.onlineView(storage, response);
    }

    /**
     * @param encryptedId
     * @param response
     * @return void
     * @throws
     * @Title: downloadImage
     */
    public void downloadImage(String encryptedId, HttpServletResponse response) {
        long id = FileIdJasyptUtil.idDecrypt(encryptedId);
        id = IdGarbleUtil.idDecrypt(id);
        this.downloadImage(id, response);
    }

    public void downloadImage(long id, HttpServletResponse response) {
        Attachment attachment = attachmentService.getById(id);
        this.downloadImage(attachment, response);
    }

    public void downloadImage(Attachment attachment, HttpServletResponse response) {
        Storage storage = toDocStorage(attachment);
        docStorageService.downloadImage(storage, response);
    }

    /**
     * @param encryptedId
     * @param request
     * @param response
     * @return void
     * @throws
     * @Title: downloadVideo
     */
    public void downloadVideo(String encryptedId, HttpServletRequest request, HttpServletResponse response) {
        long id = FileIdJasyptUtil.idDecrypt(encryptedId);
        id = IdGarbleUtil.idDecrypt(id);
        this.downloadVideo(id, request, response);
    }

    public void downloadVideo(long id, HttpServletRequest request, HttpServletResponse response) {
        Attachment attachment = attachmentService.getById(id);
        this.downloadVideo(attachment, request, response);
    }

    public void downloadVideo(Attachment attachment, HttpServletRequest request, HttpServletResponse response) {
        Storage storage = toDocStorage(attachment);
        docStorageService.downloadVideo(storage, request, response);
    }

    public InputStream getStream(Long id) {
        Attachment attachment = attachmentService.getById(id);
        return this.getStream(attachment);
    }

    public InputStream getStream(String encryptedId) {
        long id = FileIdJasyptUtil.idDecrypt(encryptedId);
        id = IdGarbleUtil.idDecrypt(id);
        Attachment attachment = attachmentService.getById(id);
        return this.getStream(attachment);
    }

    public InputStream getStream(Attachment attachment) {
        Storage storage = toDocStorage(attachment);
        return docStorageService.getStream(storage);
    }

    /**
     * @param id
     * @return
     * @return File
     * @throws
     * @Title: getFile
     */
    public File getFile(Long id) {
        Attachment attachment = attachmentService.getById(id);
        return this.getFile(attachment);
    }

    public File getFile(Attachment attachment) {
        Storage storage = toDocStorage(attachment);
        InputStream inputStream = docStorageService.getStream(storage);
        String fileFullName = DirectoryUtil.generateTempPath(storeProperties.getTempPath(), attachment.getFileType());
        File file = FileUtil.inputStreamWriteToFile(inputStream, fileFullName);
        return file;
    }

    private Storage toDocStorage(Attachment attachment) {
        AttachmentErrorEnum.BASE_ATTACHMENT_NO_ATTACHMENT_ERROR.isNull(attachment);
        Storage storage = new Storage();
        storage.setFileName(attachment.getFileName());
        storage.setPathName(attachment.getPathName());
        storage.setFileSize(attachment.getFileSize());
        return storage;
    }

}
