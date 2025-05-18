package com.rolin.orangesmart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {

    List<Attachment> findNeedCleared();

}