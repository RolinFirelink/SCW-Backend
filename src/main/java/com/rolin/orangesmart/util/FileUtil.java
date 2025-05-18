package com.rolin.orangesmart.util;

import com.google.common.io.Files;
import com.rolin.orangesmart.constant.StorageConstant;
import com.rolin.orangesmart.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;

@Slf4j
public final class FileUtil {

    private FileUtil() {
    }


    /**
     * <p>
     * Title: 读取文件内容
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param filePath
     * @return
     */
    public static byte[] fileToBytes(String filePath) {
        byte[] buffer = null;
        File file = new File(filePath);
        try (InputStream ips = new FileInputStream(file);
             ByteArrayOutputStream ops = new ByteArrayOutputStream()) {
            readAndWrite(ips, ops);
            buffer = ops.toByteArray();
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return buffer;
    }

    public static File inputStreamWriteToFile(InputStream ips, String fileFullName) {
        File file = new File(fileFullName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try (OutputStream ops = new FileOutputStream(file);) {
            readAndWrite(ips, ops);
        } catch (Exception e) {
        } finally {
            try {
                ips.close();
            } catch (IOException e) {
            }
        }
        return file;
    }

    private static void readAndWrite(InputStream ips, OutputStream ops) throws IOException {
        int byteCount = 0;
        byte[] bytes = new byte[1024];
        while ((byteCount = ips.read(bytes)) != -1) {
            ops.write(bytes, 0, byteCount);
        }
    }

    public static File writeToFile(String content, String fileFullName) {
        File file = new File(fileFullName);
        FileUtil.writeToFile(content, file);
        return file;
    }

    public static void writeToFile(String content, File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(content);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static File writeToFile(byte[] datas, String fileFullName) {
        File file = new File(fileFullName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (BufferedOutputStream bfos = new BufferedOutputStream(new FileOutputStream(file))) {
            bfos.write(datas);
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return file;
    }

    public static BufferedImage ensureOpaque(BufferedImage bi) {
        if (bi.getTransparency() == BufferedImage.OPAQUE)
            return bi;
        int w = bi.getWidth();
        int h = bi.getHeight();
        int[] pixels = new int[w * h];
        bi.getRGB(0, 0, w, h, pixels, 0, w);
        BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        bi2.setRGB(0, 0, w, h, pixels, 0, w);
        return bi2;
    }

    public static File multipartFile2File(MultipartFile multipartFile, String targetPath) {
        File targetFile = new File(targetPath);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(targetFile)) {
            out.write(multipartFile.getBytes());
        } catch (Exception e) {
            log.info("multipartFile2File失败", e.getMessage());
            throw new SystemException(e);
        }
        return targetFile;
    }

    public static String getSubPath(String path, String parentPath) {
        String subPath = path.replaceFirst(parentPath, "");
        return subPath;
    }

    public static File copy(String sourceFileName, String targetFileName) {
        File sourceFile = new File(sourceFileName);
        File targetFile = new File(targetFileName);
        copy(sourceFile, targetFile);
        return targetFile;
    }

    public static void copy(File sourceFile, File targetFile) {
        try {
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            Files.copy(sourceFile, targetFile);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static void move(File sourceFile, String targetPath) {
        try {
            File targetFile = new File(targetPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            Files.move(sourceFile, targetFile);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static void remove(File file) {
        if (file.isFile()) {
            if (file.exists()) {
                file.delete();
            }
        }
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                FileUtil.remove(subFile);
            }
            file.delete();
        }
    }

    public static String getFileType(String fileName) {
        String fileType = fileName.substring(fileName.lastIndexOf(StorageConstant.PERIOD) + 1);
        return fileType;
    }
}