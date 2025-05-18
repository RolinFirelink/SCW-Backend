package com.rolin.orangesmart.model.problem.bo;

import com.rolin.orangesmart.model.problem.entity.Problem;
import com.rolin.orangesmart.util.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProblemBO {

    @Schema(description = "唯一主键")
    private Long id;

    @Schema(description = "问题")
    private String question;

    @Schema(description = "回答")
    private String answer;

    @Schema(description = "优先级,越大优先级越高")
    private Integer showOrder;

    @Schema(description = "0为未启用,1为启用")
    private Integer active;

    public Problem toEntity(){
        return BeanUtil.copyProperties(this, Problem.class);
    }

}
