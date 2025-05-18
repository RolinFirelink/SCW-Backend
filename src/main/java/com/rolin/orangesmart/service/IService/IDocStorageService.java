package com.rolin.orangesmart.service.IService;

import com.rolin.orangesmart.model.attachment.entity.Storage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * 附件接口类
 */
public interface IDocStorageService {

    /**
     * 第一步，上传至暂存目录
     */
    public String uploadStagingDir(MultipartFile multipartFile, boolean staticFlag);

    /**
     * 第二步，移入永久目录
     */
    public String movePermanentDir(String sourcePath, String targetPath, boolean staticFlag);

    public String uploadPermanentDir(File file, String targetPath, boolean staticFlag);

    public void remove(String path);

    /**
     * 下载
     *
     * @param storage
     * @param response
     */
    public void download(Storage storage, HttpServletResponse response);

    /**
     * 在线预览
     *
     * @param storage
     * @param response
     */
    public void onlineView(Storage storage, HttpServletResponse response);

    /**
     * 读取图片
     *
     * @param storage
     * @param response
     */
    public void downloadImage(Storage storage, HttpServletResponse response);

    public void downloadVideo(Storage storage, HttpServletRequest request, HttpServletResponse response);

    public InputStream getStream(Storage storage);


}