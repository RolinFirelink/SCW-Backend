package com.rolin.orangesmart.controller.fish;

import com.rolin.orangesmart.model.common.Result;
import com.rolin.orangesmart.model.fish.vo.InfoVO;
import com.rolin.orangesmart.model.fish.vo.TotalVO;
import com.rolin.orangesmart.service.fish.CommonService;
import com.rolin.orangesmart.util.BanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "通用管理")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final BanUtils banUtils;
    private final CommonService commonService;

    @Operation(description = "获取今日信息", tags = "通用管理")
    @GetMapping("/getToday")
    public Result getToday() {
        InfoVO vo = commonService.getToday();
        return new Result<>().success().message("获取成功").data(vo);
    }

    @Operation(description = "获取往日信息", tags = "通用管理")
    @GetMapping("/getHistory")
    public Result getHistory(
            @RequestParam("current")
            @Parameter(description = "当前页")
                    Integer current,
            @RequestParam("pageSize")
            @Parameter(description = "页容量")
                    Integer pageSize
    ) {
        List<InfoVO> voList = commonService.getHistory(current, pageSize);
        return new Result<>().success().message("获取成功").data(voList);
    }

    @Operation(description = "获取累计信息", tags = "通用管理")
    @GetMapping("/getTotal")
    public Result getTotal() {
        TotalVO vo = commonService.getTotal();
        return new Result<>().success().message("获取成功").data(vo);
    }
}
