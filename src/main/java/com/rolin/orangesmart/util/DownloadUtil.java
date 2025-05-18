package com.rolin.orangesmart.util;

import com.rolin.orangesmart.exception.SystemException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class DownloadUtil {

    private DownloadUtil(){

    }

    public static void downloadFile(File file, String fileName, HttpServletResponse response) {
        if (!file.exists()) {
            throw new SystemException(file.getAbsolutePath() + " 文件不存在");
        }
        try (OutputStream os = response.getOutputStream();
             InputStream is = new FileInputStream(file)) {
            downloadFile(is, os, fileName, response);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static void downloadFile(InputStream is, String fileName, HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()) {
            downloadFile(is, os, fileName, response);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static void downloadFile(InputStream is,
                                     OutputStream os, String fileName, HttpServletResponse response)
            throws IOException {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String contentStr = "attachment; fileName='%s';filename*=utf-8''%s";
        String contentValue = String.format(contentStr, fileName, fileName.replaceAll("\\+", " "));
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Type", "application/octet-stream");
        response.addHeader("Content-Disposition", contentValue);
        int bytes;
        byte[] bufferOut = new byte[1024];
        while ((bytes = is.read(bufferOut)) != -1) {
            os.write(bufferOut, 0, bytes);
        }
        os.flush();
    }

    public static void onlineViewFile(File file, String fileName, HttpServletResponse response) {
        if (!file.exists()) {
            throw new SystemException(file.getAbsolutePath() + " 文件不存在");
        }
        try (OutputStream os = response.getOutputStream();
             InputStream is = new FileInputStream(file)) {
            onlineViewFile(is, os, fileName, response);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static void onlineViewFile(InputStream is, String fileName, HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()) {
            onlineViewFile(is, os, fileName, response);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static void onlineViewFile(InputStream is,
                                       OutputStream os, String fileName, HttpServletResponse response) throws IOException {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String contentStr = "inline; fileName='%s';filename*=utf-8''%s";
        String contentValue = String.format(contentStr, fileName, fileName.replaceAll("\\+", " "));

        String type = null;
        if ((fileName.indexOf(".xls") > 0) || (fileName.indexOf(".xlsx") > 0) || (fileName.indexOf(".csv") > 0)) {
            type = "application/vnd.ms-excel";
        } else if (fileName.indexOf(".pdf") > 0) {
            type = "application/pdf";
        } else if ((fileName.indexOf(".doc") > 0) || (fileName.indexOf(".docx") > 0)) {
            type = "application/msword";
        } else if (fileName.indexOf(".txt") > 0) {
            type = "text/plain";
        } else if (fileName.indexOf(".ppt") > 0) {
            type = "application/ppt";
        }
        if (!StringUtils.hasText(type)) {
            type = "multipart/form-data";
        }
        response.setHeader("Content-Type", type);
        response.addHeader("Content-Disposition", contentValue);

        int bytes;
        byte[] bufferOut = new byte[1024];
        while ((bytes = is.read(bufferOut)) != -1) {
            os.write(bufferOut, 0, bytes);
        }
        os.flush();
    }

    public static void downloadImage(File file, String fileName, HttpServletResponse response) {
        if (!file.exists()) {
            throw new SystemException(file.getAbsolutePath() + " 文件不存在");
        }
        try (OutputStream os = response.getOutputStream();
             InputStream is = new FileInputStream(file)) {
            downloadImage(is, os, fileName, response);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static void downloadImage(InputStream is, String fileName, HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()) {
            downloadImage(is, os, fileName, response);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static void downloadImage(InputStream is, OutputStream os, String fileName,
                                      HttpServletResponse response) throws IOException {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String contentStr = "attachment; fileName='%s';filename*=utf-8''%s";
        String contentValue = String.format(contentStr, fileName, fileName.replaceAll("\\+", " "));

        String[] tmps = fileName.split("\\.");
        String type = tmps[tmps.length - 1];
        type = type.toLowerCase();

        String contentType = switch (type) {
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> "image/jpeg";
        };
        response.setHeader("Content-Type", contentType);
        response.addHeader("Content-Disposition", contentValue);

        BufferedImage bufImg = ImageIO.read(is);
        BufferedImage bufImg2 = FileUtil.ensureOpaque(bufImg);
        ImageIO.write(bufImg2, type, os);
    }

    public static void downloadVideo(File downloadFile, HttpServletRequest request, HttpServletResponse response) {
        int contentLength = Integer.parseInt(String.valueOf(downloadFile.length()));
        try (RandomAccessFile randomFile = new RandomAccessFile(downloadFile, "r");
             ServletOutputStream out = response.getOutputStream()) {
            String range = request.getHeader("Range");
            int start = 0;
            int end = 0;
            if (range != null && range.startsWith("bytes=")) {
                String[] values = range.split("=")[1].split("-");
                start = Integer.parseInt(values[0]);
                if (values.length > 1) {
                    end = Integer.parseInt(values[1]);
                }
            }
            int requestSize;
            if (end != 0 && end > start) {
                requestSize = end - start + 1;
            } else {
                requestSize = Integer.MAX_VALUE;
            }

            response.setContentType("video/mp4");
            response.setHeader("Accept-Ranges", "bytes");
            // response.setHeader("ETag", fileName);
            // response.setHeader("Last-Modified", new Date().toString());
            // 第一次请求只返回content length来让客户端请求多次实际数据
            if (range == null) {
                response.setHeader("Content-length", String.valueOf(contentLength));
            } else {
                // 以后的多次以断点续传的方式来返回视频数据
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);// 206
                long requestStart = 0;
                long requestEnd = 0;
                String[] ranges = range.split("=");
                if (ranges.length > 1) {
                    String[] rangeDatas = ranges[1].split("-");
                    requestStart = Integer.parseInt(rangeDatas[0]);
                    if (rangeDatas.length > 1) {
                        requestEnd = Integer.parseInt(rangeDatas[1]);
                    }
                }
                long length;
                if (requestEnd > 0) {
                    length = requestEnd - requestStart + 1;
                    response.setHeader("Content-length", String.valueOf(length));
                    response.setHeader("Content-Range",
                            "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
                } else {
                    length = contentLength - requestStart;
                    long maxLength = 10000000;
                    if (length > maxLength) {
                        length = maxLength;
                    }
                    response.setHeader("Content-length", String.valueOf(length));
                    response.setHeader("Content-Range",
                            "bytes " + requestStart + "-" + (requestStart + length - 1) + "/" + contentLength);
                }
            }

            int needSize = requestSize;
            byte[] buffer = new byte[4096];
            randomFile.seek(start);
            while (needSize > 0) {
                int len = randomFile.read(buffer);
                out.write(buffer, 0, len);
                if (len < buffer.length) {
                    break;
                }
                needSize -= buffer.length;
            }
        } catch (IOException e) {
//      throw new SystemException(e);
        }
    }

}
