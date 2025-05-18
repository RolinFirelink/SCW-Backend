package com.rolin.orangesmart.properties;

import com.google.common.collect.Sets;
import com.rolin.orangesmart.util.DirectoryUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * 文件存储配置
 */
@Getter
@Setter
@Component
public class StoreProperties {

    @PostConstruct
    public void formatDeletePaths() {
        Set<String> temp = Sets.newHashSet();
        if (this.allowedDeletePaths == null) {
            this.allowedDeletePaths = temp;
            return;
        }
        Iterator<String> iterator = allowedDeletePaths.iterator();
        while (iterator.hasNext()) {
            String deletePath = iterator.next();
            if (!StringUtils.hasText(deletePath)) {
                continue;
            }
            deletePath = deletePath.trim();
            //防止path == “/” 或其他特殊目录
            if (deletePath.length() == 1) {
                continue;
            }
            deletePath = DirectoryUtil.splicingPath(this.getPath(), deletePath);
            temp.add(deletePath);
        }
        allowedDeletePaths = temp;
    }

    /**
     * 存储类型 local、nfs
     */
    @Value("${doc.store.type:nfs}")
    private String type;

    /**
     * 存储路径
     */
//    @Value("${doc.store.path:/home/ms/attachment}")
    @Value("${doc.store.path:D:/orange/temp/home/ms/attachment}")
    private String path;

    private Set<String> allowedDeletePaths;

    /**
     * 是否复制标志
     */
    @Value("${doc.store.copyFlag:true}")
    private boolean copyFlag;

    /**
     * 复制存储路径
     */
//    @Value("${doc.store.copyPath:/home/ms/attachment_bak}")
    @Value("${doc.store.copyPath:D:/orange/temp/home/ms/attachment_bak}")
    private String copyPath;

    /**
     * 复制失败日志文件
     */
//    @Value("${doc.store.failureLog:/home/ms/attachment/failureFile.log}")
    @Value("${doc.store.failureLog:D:/orange/temp/home/ms/attachment/failureFile.log}")
    private String failureLog;

//    @Value("${doc.store.tempPath:/tmp}")
    @Value("${doc.store.tempPath:D:/orange/temp/tmp}")
    private String tempPath;
}
