package com.rolin.orangesmart.model.common.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public abstract class AbstractPO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    // 创建者id
    @TableField(fill = FieldFill.INSERT)
    private Long createdUserId;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;
    // 修改者id
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedUserId;
    // 修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
    // 是否有效
    private Integer status = 1;

    /**
     * <p>Title: 数据状态</p>
     * <p>Description: 0：禁用无效，1：正常有效</p>
     */
    public enum Status {
        DISABLE(0, "无效"), ENABLE(1, "有效");

        private Integer value;

        private String comment;

        Status(Integer value, String comment) {
            this.value = value;
            this.comment = comment;
        }

        public Integer value() {
            return this.value;
        }

        public String comment() {
            return this.comment;
        }
    }

}
