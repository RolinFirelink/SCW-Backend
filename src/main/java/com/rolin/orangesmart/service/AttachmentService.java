package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.rolin.orangesmart.exception.errorEnum.AttachmentErrorEnum;
import com.rolin.orangesmart.mapper.AttachmentMapper;
import com.rolin.orangesmart.model.attachment.bo.AttachmentBo;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.IdGarbleUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AttachmentService extends BaseService<AttachmentMapper, Attachment> {

    @Autowired
    private AttachmentMapper attachmentMapper;

    private static final Pattern pattern = Pattern.compile("^([a-z0-9_\\-\\u4e00-\\u9fa5]+)(\\.)([a-z]{2,5})$");

    public void create(Attachment attachment) {
        boolean flag = pattern.matcher(attachment.getFileName()).matches();
        AttachmentErrorEnum.BASE_ATTACHMENT_FILE_NAME_REGEXP_ERROR.isTrue(flag);
        this.insert(attachment);
    }

    public Attachment getById(Long id) {
        if (id == null) {
            return null;
        }
        QueryWrapper<Attachment> queryWrapper = new QueryWrapper<>();
        id = IdGarbleUtil.idDecrypt(id);
        queryWrapper.lambda().eq(Attachment::getId, id);
        queryWrapper.lambda().eq(Attachment::getStatus, 1);
        queryWrapper.lambda().and(wrapper -> {
            wrapper.isNull(Attachment::getExpireDate);
            wrapper.or();
            wrapper.ge(Attachment::getExpireDate, new Date());
        });
        return this.getOne(queryWrapper);
    }

    public void update(AttachmentBo attachmentBo) {
        Attachment attachment = new Attachment();
        BeanUtils.copyProperties(attachmentBo, attachment);
        this.updateById(attachment);
    }

    public Attachment getByPath(String path) {
        if (!StringUtils.hasText(path)) {
            return null;
        }
        QueryWrapper<Attachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Attachment::getPathName, path);
        queryWrapper.lambda().eq(Attachment::getStatus, 1);
        queryWrapper.lambda().and(wrapper -> {
            wrapper.isNull(Attachment::getExpireDate);
            wrapper.or();
            wrapper.ge(Attachment::getExpireDate, new Date());
        });
        return this.getOne(queryWrapper);
    }

    public List<Attachment> getByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Lists.newArrayList();
        }
        QueryWrapper<Attachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Attachment::getId, ids);
        queryWrapper.lambda().eq(Attachment::getStatus, 1);
        queryWrapper.lambda().and(wrapper -> {
            wrapper.isNull(Attachment::getExpireDate);
            wrapper.or();
            wrapper.ge(Attachment::getExpireDate, new Date());
        });
        return this.list(queryWrapper);
    }

    public Attachment create(String pathName, String fileType, long fileSize, String originalFileName) {
        return this.create(pathName, fileType, fileSize, originalFileName, false);
    }

    public Attachment createAndConfirm(String pathName, String fileType, long fileSize, String originalFileName) {
        return this.create(pathName, fileType, fileSize, originalFileName, true);
    }

    private Attachment create(String pathName, String fileType, long fileSize, String originalFileName, boolean confirmationFlag) {
        Attachment attachment = new Attachment();
        attachment.setPathName(pathName);

        //文件名超长处理
        if(StringUtils.hasText(originalFileName)) {
            originalFileName = originalFileName.length() > 128 ? originalFileName.substring(0, 128) : originalFileName;
        }
        attachment.setFileName(originalFileName);

        attachment.setFileSize(fileSize);
        attachment.setFileType(fileType);
        attachment.setConfirmationFlag(confirmationFlag);
        this.insert(attachment);
        return attachment;
    }

    public void submit(Attachment attachment, String pathName) {
        attachment.setConfirmationFlag(true);
        attachment.setPathName(pathName);
        this.updateById(attachment);
    }

    /**
     * 查询30天后还未确认（即无效）的附件
     * TODO 该方法目前有Invalid bound statement (not found)问题待解决
     */
    public List<Attachment> findNeedCleared() {
        return attachmentMapper.findNeedCleared();
    }

}