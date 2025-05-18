package com.rolin.orangesmart.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rolin.orangesmart.cache.ISessionService;
import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.model.common.Result;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.user.bo.UserBO;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.model.user.vo.UserVO;
import com.rolin.orangesmart.service.UserService;
import com.rolin.orangesmart.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private ISessionService sessionService;


    /**
     * 获取所有用户列表
     */
    @GetMapping("/getAll")
    public ResponseDTO<List<User>> getAll() {
        return ResponseDTO.ok(userService.getAll());
    }

    /**
     * 获取当前用户的信息
     */
    @GetMapping("/getSelf")
    public ResponseDTO<UserVO> getSelf() {
        return ResponseDTO.ok(userService.getSelf());
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping("/list")
    public ResponseDTO<List<User>> list(@RequestParam(required = false) String keyword) {
        return ResponseDTO.ok(userService.list(keyword));
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    public ResponseDTO<PageInfo<UserVO>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String keyWord) {
        return ResponseDTO.ok(userService.page(pageNum,pageSize,keyWord));
    }

    @GetMapping("/{id}")
    public ResponseDTO<User> getById(@PathVariable Long id) {
        User byId = userService.getById(id);
        System.out.println(byId);
        return ResponseDTO.ok(byId);
    }

    @GetMapping("/logout")
    public ResponseDTO<Boolean> logout() {
        return ResponseDTO.ok(userService.logout());
    }

    @PostMapping("/register")
    public ResponseDTO<Long> register(@RequestBody @Valid UserBO userBO) {
        return ResponseDTO.ok(userService.save(userBO));
    }

    @PostMapping("/save")
    public ResponseDTO<Long> save(@RequestBody @Valid UserBO userBO) {
        return ResponseDTO.ok(userService.save(userBO));
    }

    @PutMapping("/update")
    public ResponseDTO<Boolean> update(@RequestBody @Valid UserBO userBO) {
        return ResponseDTO.ok(userService.updateById(userBO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable Long id) {
        return ResponseDTO.ok(userService.removeById(id));
    }

    @PostMapping
    public ResponseDTO<String> logout(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.AUTHORIZATION_HEADER);
        try {
            if (StringUtils.hasText(token)) {
                String usernameKey = JwtTokenUtil.getUserNamekey(token);
                sessionService.del(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + usernameKey);
            }
        }catch (Exception e){
            return ResponseDTO.ok("用户已经登出,请勿再次登出");
        }
        return ResponseDTO.ok("登出成功");
    }

    @Operation(description = "获取用户信息", tags = "用户管理")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        UserVO vo = userService.getUserInfo(request);
        return new Result<>().success().message("获取用户信息成功").data(vo);
    }

    @Operation(description = "查看关注的人", tags = "用户管理")
    @GetMapping("/getFollows")
    public Result getFollows(HttpServletRequest request){
        List<UserVO> voList = userService.getFollows(request);
        return new Result<>().success().message("查看成功").data(voList);
    }

    @Operation(description = "查看粉丝", tags = "用户管理")
    @GetMapping("/getFans")
    public Result getFans(HttpServletRequest request){
        List<UserVO> voList = userService.getFans(request);
        return new Result<>().success().message("查看成功").data(voList);
    }

    @Operation(description = "关注/取消关注", tags = "用户管理")
    @PostMapping("/follow/{id}")
    public Result follow(
        @PathVariable("id")
        @Parameter(description = "用户id")
        Integer id,
        HttpServletRequest request
    ){
        userService.follow(request, id);
        return new Result<>().success().message("关注/取消关注成功");
    }
}
