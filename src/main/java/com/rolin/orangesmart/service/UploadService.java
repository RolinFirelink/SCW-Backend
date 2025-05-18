package com.rolin.orangesmart.service;

import com.rolin.orangesmart.constant.AttachmentConstant;
import com.rolin.orangesmart.constant.StorageConstant;
import com.rolin.orangesmart.exception.errorEnum.AttachmentErrorEnum;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.service.IService.IDocStorageService;
import com.rolin.orangesmart.util.DirectoryUtil;
import com.rolin.orangesmart.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * UploadService
 */
@Slf4j
@Service
public class UploadService {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private IDocStorageService docStorageService;

    public Attachment upload(File file, String originalFileName, String targetPath) {
        //1、上传至永久目录
        targetPath = docStorageService.uploadPermanentDir(file, targetPath, false);
        //2、写入数据库
        String fileType = FileUtil.getFileType(file.getName());
        long fileSize = file.length();
        Attachment attachment = attachmentService.createAndConfirm(targetPath, fileType, fileSize, originalFileName);
        file.delete();
        return attachment;
    }

    /**
     * @param multipartFile
     * @param staticFlag
     * @return
     * @Title: uploadStagingDir
     * @Description: 后续需调用confirm接口，完成上传全功能
     * @author: sunys
     */
    public Attachment uploadStagingDir(MultipartFile multipartFile, boolean staticFlag) {
        if (multipartFile.getOriginalFilename().length() >= AttachmentConstant.MaxFileNameLength) {
            AttachmentErrorEnum.BASE_ATTACHMENT_FILE_NAME_MAX_LENGTH_ERROR.fail();
        }
        String fileType = FileUtil.getFileType(multipartFile.getOriginalFilename());
        long fileSize = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        String pathName = docStorageService.uploadStagingDir(multipartFile, staticFlag);
        Attachment attachment = attachmentService.create(pathName, fileType, fileSize, originalFilename);
        return attachment;
    }

    /**
     * @param attachmentId
     * @param targetPath
     * @return
     * @Title: submit
     * @Description:
     * @author: sunys
     */
    public Attachment submit(long attachmentId, String targetPath, boolean staticFlag) {
        Attachment attachment = attachmentService.getById(attachmentId);
        if (attachment.isConfirmationFlag()) {
            //已经确认过
            return attachment;
        }
        AttachmentErrorEnum.BASE_ATTACHMENT_NO_ATTACHMENT_ERROR.isNull(attachment);
        String sourcePath = attachment.getPathName();
        if (ObjectUtils.isEmpty(targetPath)) {
            targetPath = DirectoryUtil.splicingPath(StorageConstant.YEAR_MONTH_DAY_PARAM_PATH);
        }
        targetPath = docStorageService.movePermanentDir(sourcePath, targetPath, staticFlag);
        attachmentService.submit(attachment, targetPath);
        return attachment;
    }

    /**
     * @Title: clear
     * @Description:
     * @author: sunys
     */
    public int clear() {
        int success = 0;
        List<Attachment> attachments = attachmentService.findNeedCleared();
        for (Attachment attachment : attachments) {
            try {
                String path = attachment.getPathName();
                docStorageService.remove(path);
                success++;
                log.info("清理 id={}, path={}", attachment.getId(), attachment.getPathName());
            } catch (Exception e) {
                log.error("清理错误, id={}, path={}", attachment.getId(), attachment.getPathName());
                log.error(e.getMessage(), e);
            } finally {
                attachmentService.deleteById(attachment.getId());
            }
        }
        return success;
    }

    public int remove(List<Long> ids) {
        int success = 0;
        for (Long id : ids) {
            Attachment attachment = attachmentService.getById(id);
            try {
                String path = attachment.getPathName();
                docStorageService.remove(path);
                success++;
                log.info("删除 id={}, path={}", attachment.getId(), attachment.getPathName());
            } catch (Exception e) {
                log.error("删除错误, id={}, path={}", attachment.getId(), attachment.getPathName());
                log.error(e.getMessage(), e);
            } finally {
                attachmentService.deleteById(attachment.getId());
            }
        }
        return success;
    }
}
