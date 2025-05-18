package com.rolin.orangesmart.model.problem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rolin.orangesmart.model.common.po.AbstractPO;
import com.rolin.orangesmart.model.problem.vo.ProblemVO;
import com.rolin.orangesmart.util.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("problem")
public class Problem extends AbstractPO {

    @Schema(description = "问题")
    private String question;

    @Schema(description = "回答")
    private String answer;

    @Schema(description = "优先级,越大优先级越高")
    private Integer showOrder;

    @Schema(description = "0为未启用,1为启用")
    private Integer active;

    public ProblemVO toVo(){
        return BeanUtil.copyProperties(this, ProblemVO.class);
    }

}
