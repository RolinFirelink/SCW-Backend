package com.rolin.orangesmart.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 图片参数配置
 */
@Getter
@Setter
@Component
public class ImageProperties {


    // 输入图片大小，进行处理的图片不能大于这个值，默认10M
    private long maxInputSize = 10485760;

    // 输出图片大小，压缩后的图片不能大于这个值，默认2M
    private long maxOutputSize = 2097152;

    /**
     * 是否压缩开关
     */
    private boolean compressFlag = false;

    /**
     * 压缩比例
     */
    private double compressScale = 1;

    /**
     * 压缩质量，可以看作图片清晰度，不能改太小，图片会失真
     */
    private double compressQuality = 0.8;

    /**
     * 压缩后大小，单位字节 1024*1024=1M
     */
    private long compressOutSize = 1048576L;

}