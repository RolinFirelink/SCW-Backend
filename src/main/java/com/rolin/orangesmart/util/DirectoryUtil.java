package com.rolin.orangesmart.util;

import com.google.common.collect.Lists;
import com.rolin.orangesmart.constant.StorageConstant;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class DirectoryUtil {
    private DirectoryUtil(){

    }

    public static String generateFullPath(String rootPath, String targetPath, String fileType, boolean staticFlag, boolean stagingFlag) {
        if (!StringUtils.hasText(targetPath)) {
            targetPath = StorageConstant.YEAR_MONTH_DAY_PARAM_PATH;
        }
        // 确保以 “/” 开始
        targetPath = splicingPath(targetPath);

        List<String> parts = Lists.newArrayList();
        if(!targetPath.startsWith(rootPath)){
            parts.add(rootPath);
        }
        if (staticFlag && !targetPath.startsWith(StorageConstant.STATIC_STORAGE_PATH)) {
            parts.add(StorageConstant.STATIC_STORAGE_PATH);
        }
        if (stagingFlag && !targetPath.startsWith(StorageConstant.STAGING_STORAGE_PATH)) {
            parts.add(StorageConstant.STAGING_STORAGE_PATH);
        }
        parts.add(targetPath);
        if (StringUtils.hasText(fileType)) {
            parts.add(generateFileName(fileType));
        }

        String fullPath = splicingPath(parts.toArray(String[]::new));
        fullPath = replaceDateParam(fullPath);
        return fullPath;
    }

    public static String splicingRootPath(String rootPath, String path){
        // 确保以 “/” 开始
        path = splicingPath(path);
        if(!path.startsWith(rootPath)){
            path = splicingPath(rootPath, path);
        }
        return path;
    }

    /**
     * 移除目录中的rootPath部分
     */
    public static String detachRootPath(String rootPath, String fullPath){
        if(StringUtils.hasText(fullPath) && fullPath.startsWith(rootPath)){
            fullPath = fullPath.substring(rootPath.length());
        }
        return fullPath;
    }

    public static String generateTempPath(String rootTempPath, String fileType) {
        String targetPath = splicingPath(rootTempPath,
                StorageConstant.YEAR_MONTH_DAY_PARAM_PATH, generateFileName(fileType));
        targetPath = replaceDateParam(targetPath);
        return targetPath;
    }

    private static String generateFileName(String fileType) {
        StringBuilder fileNameBuilder = new StringBuilder();
        if (!fileType.startsWith(StorageConstant.PERIOD)) {
            fileNameBuilder.append(UUID.randomUUID()).append(StorageConstant.PERIOD).append(fileType);
        } else {
            fileNameBuilder.append(UUID.randomUUID()).append(fileType);
        }
        return fileNameBuilder.toString();
    }

    /**
     * 用“/”拼接目录
     */
    public static String splicingPath(String... parts) {
        if (parts == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.startsWith(StorageConstant.SEPARATOR)) {
                sb.append(StorageConstant.SEPARATOR);
            }
            sb.append(part);
        }
        return sb.toString();
    }

    private static String replaceDateParam(String path) {
        LocalDate localDate = LocalDate.now();
        path = path.replace(StorageConstant.YEAR_PARAM, String.valueOf(localDate.getYear()));
        path = path.replace(StorageConstant.MONTH_PARAM, StringUtil.leftPatchZero(2, localDate.getMonthValue()));
        path = path.replace(StorageConstant.DAY_PARAM, StringUtil.leftPatchZero(2, localDate.getDayOfMonth()));
        return path;
    }
}