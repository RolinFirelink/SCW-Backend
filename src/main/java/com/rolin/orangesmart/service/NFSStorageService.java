package com.rolin.orangesmart.service;

import com.rolin.orangesmart.constant.StorageConstant;
import com.rolin.orangesmart.exception.SystemException;
import com.rolin.orangesmart.model.attachment.entity.Storage;
import com.rolin.orangesmart.properties.StoreProperties;
import com.rolin.orangesmart.service.IService.IDocStorageService;
import com.rolin.orangesmart.util.DirectoryUtil;
import com.rolin.orangesmart.util.DownloadUtil;
import com.rolin.orangesmart.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * NFS附件管理
 */
@Slf4j
@Service
public class NFSStorageService implements IDocStorageService, InitializingBean {

    @Autowired
    private StoreProperties storeProperties;

    private volatile boolean existCopyPathFlag;

    @Override
    public String uploadStagingDir(MultipartFile multipartFile, boolean staticFlag) {
        String fileType = FileUtil.getFileType(multipartFile.getOriginalFilename());
        String fullPath = DirectoryUtil.generateFullPath(storeProperties.getPath(), null, fileType, staticFlag, true);
        log.info("upload multipartFile: {}", fullPath);
        FileUtil.multipartFile2File(multipartFile, fullPath);
        copyFileHandle(fullPath);

        fullPath = DirectoryUtil.detachRootPath(storeProperties.getPath(), fullPath);
        return fullPath;
    }

    @Override
    public String uploadPermanentDir(File file, String targetPath, boolean staticFlag) {
        if (file == null) {
            return null;
        }
        String fileType = FileUtil.getFileType(file.getName());
        String fullPath = DirectoryUtil.generateFullPath(storeProperties.getPath(), targetPath, fileType, staticFlag, false);

        log.info("upload file: {}", fullPath);
        File targetFile = new File(fullPath);
        FileUtil.copy(file, targetFile);
        copyFileHandle(fullPath);

        fullPath = DirectoryUtil.detachRootPath(storeProperties.getPath(), fullPath);
        return fullPath;
    }

    @Override
    public String movePermanentDir(String sourcePath, String targetPath, boolean staticFlag) {
        if (sourcePath == null) {
            throw new SystemException("sourcePath 参数不能为空");
        }
        // TODO 到服务器上时才需要打开这个配置
//        sourcePath = sourcePath.replaceAll(StorageConstant.WINDOWS_SEPARATOR, StorageConstant.SEPARATOR);
//        sourcePath = DirectoryUtil.splicingRootPath(storeProperties.getPath(), sourcePath);

        if (staticFlag && !sourcePath.startsWith(DirectoryUtil.splicingPath(storeProperties.getPath(), StorageConstant.STATIC_STORAGE_PATH, StorageConstant.STAGING_STORAGE_PATH))) {
            throw new SystemException("sourcePath=%s 待移动目录不合规", sourcePath);
        }

        if (!staticFlag && !sourcePath.startsWith(DirectoryUtil.splicingPath(storeProperties.getPath(), StorageConstant.STAGING_STORAGE_PATH))) {
            throw new SystemException("sourcePath=%s 待移动目录不合规", sourcePath);
        }

        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new SystemException("sourcePath=%s 文件不存在", sourcePath);
        }
        if (targetPath == null) {
            throw new SystemException("targetPaths 参数不能为空");
        }

        String fileType = FileUtil.getFileType(sourceFile.getName());
        //targetPath中，指定了文件名
        if (targetPath.toLowerCase().endsWith(StorageConstant.PERIOD + fileType.toLowerCase())) {
            fileType = null;
        }
        String fullPath = DirectoryUtil.generateFullPath(storeProperties.getPath(), targetPath, fileType, staticFlag, false);

        log.info("move sourcePath: {}", sourcePath);
        log.info("move targetPath: {}", fullPath);

        FileUtil.move(sourceFile, fullPath);
        if (storeProperties.isCopyFlag()) {
            sourcePath = sourcePath.replace(storeProperties.getPath(), storeProperties.getCopyPath());
            sourceFile = new File(sourcePath);
            fullPath = fullPath.replace(storeProperties.getPath(), storeProperties.getCopyPath());
            FileUtil.move(sourceFile, fullPath);
        }

        fullPath = DirectoryUtil.detachRootPath(storeProperties.getPath(), fullPath);
        return fullPath;
    }

    /**
     * 下载
     */
    @Override
    public void download(Storage storage, HttpServletResponse response) {
        // TODO 到服务器上时才需要打开这个配置
//        String fullPath = DirectoryUtil.splicingRootPath(storeProperties.getPath(), storage.getPathName());
        String fullPath = storage.getPathName();
        File file = new File(fullPath);
        DownloadUtil.downloadFile(file, storage.getFileName(), response);
    }

    @Override
    public void onlineView(Storage storage, HttpServletResponse response) {
        String fullPath = DirectoryUtil.splicingRootPath(storeProperties.getPath(), storage.getPathName());
        File file = new File(fullPath);
        DownloadUtil.onlineViewFile(file, storage.getFileName(), response);
    }

    /**
     * 读取
     */
    @Override
    public void downloadImage(Storage storage, HttpServletResponse response) {
        String fullPath = DirectoryUtil.splicingRootPath(storeProperties.getPath(), storage.getPathName());
        File file = new File(fullPath);
        DownloadUtil.downloadImage(file, storage.getFileName(), response);
    }

    private void copyFileHandle(String pathName) {
        pathName = pathName.replaceAll(StorageConstant.WINDOWS_SEPARATOR, StorageConstant.SEPARATOR);
        if (storeProperties.isCopyFlag()) {
            String basePath = storeProperties.getPath();
            String copyBasePath = storeProperties.getCopyPath();
            String copyPathName = pathName.replaceAll(basePath, copyBasePath);
            try {
                if (existCopyPathFlag) {
                    File destFile = new File(copyPathName);
                    File sourceFile = new File(pathName);
                    FileUtil.copy(sourceFile, destFile);
                } else {
                    log.info("不存在状态文件，复制失败");
                    this.copyFailureHandle(pathName, copyPathName);
                }
            } catch (Exception e) {
                log.info("文件复制失败，{}", e.getMessage());
                this.copyFailureHandle(pathName, copyPathName);
            }
        }
    }

    private void copyFailureHandle(String sourcePath, String destPath) {
        String logFileName = storeProperties.getFailureLog();
        File logFile = new File(logFileName);
        File parentFile = logFile.getParentFile();
        try {
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            log.error("创建错误文件失败，{}", e.getMessage());
        }
        try (FileWriter fileWritter = new FileWriter(logFile, true);) {
            //      cp -a /home/ms/attachment/2019/02/18/aaaa.docx /home/ms/attachment_bak/2019/02/18/aaaa.docx
            fileWritter.write("cp -a " + sourcePath + " " + destPath + "\n");
        } catch (Exception e) {
            log.error("写错误文件失败，{}， \n{}", sourcePath, e);
        }
    }

    @Override
    public void downloadVideo(Storage storage, HttpServletRequest request, HttpServletResponse response) {
        String fullPath = DirectoryUtil.splicingRootPath(storeProperties.getPath(), storage.getPathName());
        File file = new File(fullPath);
        DownloadUtil.downloadVideo(file, request, response);
    }

    @Override
    public InputStream getStream(Storage storage) {
        String fullPath = DirectoryUtil.splicingRootPath(storeProperties.getPath(), storage.getPathName());
        InputStream is = null;
        try {
            is = new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return is;
    }

    @Override
    public void remove(String path) {
        if (!StringUtils.hasText(path)) {
            return;
        }
        path = path.replaceAll(StorageConstant.WINDOWS_SEPARATOR, StorageConstant.SEPARATOR);
        path = path.trim();
        //防止path == “/” 或其他特殊目录
        if (path.length() == 1) {
            return;
        }

        String deletePath = DirectoryUtil.splicingRootPath(storeProperties.getPath(), path);

        boolean allowedDelete = false;
        for (String allowedDeletePath : storeProperties.getAllowedDeletePaths()) {
            if (deletePath.startsWith(allowedDeletePath)) {
                allowedDelete = true;
                break;
            }
        }
        if (!allowedDelete) {
            log.info("文件不允许刪除：{}", deletePath);
            return;
        }

        log.info("删除文件：{}", deletePath);
        File file = new File(deletePath);
        FileUtil.remove(file);

        if (storeProperties.isCopyFlag()) {
            deletePath = deletePath.replace(storeProperties.getPath(), storeProperties.getCopyPath());
            log.info("删除文件：{}", deletePath);
            file = new File(deletePath);
            FileUtil.remove(file);
        }
    }

    class CheckCopyPathThread implements Runnable {
        public void run() {
            File checkFile = new File(DirectoryUtil.splicingPath(storeProperties.getCopyPath(), "orangestatus"));
            while (true) {
                log.info("文件复制模式，监控线程");
                try {
                    if (checkFile.exists()) {
                        existCopyPathFlag = true;
                    } else {
                        existCopyPathFlag = false;
                        log.error("备份目录不存在");
                    }
                } catch (Exception e) {
                    existCopyPathFlag = false;
                    log.error("备份目录不存在， {}", e.getMessage());
                }

                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (storeProperties.isCopyFlag()) {
            Thread thread = new Thread(new CheckCopyPathThread());
            thread.start();
        } else {
            log.info("文件非复制模式");
        }
    }

}
