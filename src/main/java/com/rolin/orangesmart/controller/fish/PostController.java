package com.rolin.orangesmart.controller.fish;

import com.rolin.orangesmart.model.common.Result;
import com.rolin.orangesmart.model.fish.bo.AddCommentRequest;
import com.rolin.orangesmart.model.fish.bo.AddPostRequest;
import com.rolin.orangesmart.model.fish.bo.UpdatePostRequest;
import com.rolin.orangesmart.model.fish.entity.PageResult;
import com.rolin.orangesmart.model.fish.vo.PostDetailVO;
import com.rolin.orangesmart.model.fish.vo.PostVO;
import com.rolin.orangesmart.service.fish.PostCommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "帖子管理")
@RestController
@CrossOrigin
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostCommonService postCommonService;

    @Operation(description = "点赞或取消点赞帖子", tags = "帖子管理")
    @PostMapping("/toggleLike/{postId}/{action}")
    public Result toggleLike(
            @PathVariable("postId")
            @Parameter(description = "帖子id")
            Integer postId,
            @PathVariable("action")
            @Parameter(description = "1:点赞;0:取消点赞")
            Integer action,
            HttpServletRequest request
    ) {
        if (action == 1) {
            postCommonService.toggleLike(postId, request, true);
            return new Result<>().success().message("点赞成功");
        } else if (action == 0) {
            postCommonService.toggleLike(postId, request, false);
            return new Result<>().success().message("取消点赞成功");
        } else {
            return new Result<>().error().message("无效的操作类型");
        }
    }

    @Operation(description = "上传帖子图片", tags = "帖子管理")
    @PostMapping("/uploadPostImage")
    public Result uploadPostImage(
            @RequestPart
            @Parameter(description = "图片文件（最多 5 张）")
            MultipartFile[] files
    ) {
        if (files == null || files.length == 0) {
            return new Result<>().error().message("请选择至少一张图片");
        }
        if (files.length > 5) {
            return new Result<>().error().message("最多只能上传 5 张图片");
        }
        List<Integer> imageIds = postCommonService.uploadPostImage(files);
        return new Result<>().success().message("上传帖子图片成功").data(imageIds);
    }

    @Operation(description = "创建帖子", tags = "帖子管理")
    @PostMapping("/createPost")
    public Result createPost(
            @RequestBody
            AddPostRequest addPostRequest,
            HttpServletRequest request
    ) {
        postCommonService.createPost(request, addPostRequest);
        return new Result<>().success().message("创建帖子成功");
    }

    @Operation(description = "更新帖子", tags = "帖子管理")
    @PostMapping("/updatePost")
    public Result updatePost(
            @RequestBody
            UpdatePostRequest request
    ) {
        postCommonService.updatePost(request);
        return new Result<>().success().message("更新帖子成功");
    }

    @Operation(description = "删除帖子", tags = "帖子管理")
    @PostMapping("/deletePost")
    public Result deletePost(
            @RequestParam
            Integer postId
    ) {
        postCommonService.deletePost(postId);
        return new Result<>().success().message("删除帖子成功");
    }

    @Operation(description = "获取多条帖子", tags = "帖子管理")
    @GetMapping("/getAllPost")
    public Result getAllPost(
            @RequestParam("current")
            @Parameter(description = "当前页")
            Integer current,
            @RequestParam("pageSize")
            @Parameter(description = "页容量")
            Integer pageSize,
            @RequestParam(value = "keyword", required = false)
            @Parameter(description = "搜索帖子标题关键词")
            String keyword,
            HttpServletRequest request
    ){
        PageResult<PostVO> res = postCommonService.getAllPost(current, pageSize, keyword, request);
        return new Result<>().success().data(res);
    }

    @Operation(description = "获取帖子详情", tags = "帖子管理")
    @GetMapping("/getPost")
    public Result getPost(
            @RequestParam("postId")
            @Parameter(description = "帖子id")
            Integer postId,
            @RequestParam(value = "current", defaultValue = "1")
            @Parameter(description = "当前页")
            Integer current,
            @RequestParam(value = "pageSize", defaultValue = "5")
            @Parameter(description = "页容量")
            Integer pageSize,
            HttpServletRequest request
    ){
        PostDetailVO res = postCommonService.getPost(postId, current, pageSize, request);
        return new Result<>().success().data(res);
    }

    @Operation(description = "获取我发布的帖子", tags = "帖子管理")
    @GetMapping("/getMyPost")
    public Result getMyPost(
            @RequestParam("current")
            @Parameter(description = "当前页")
            Integer current,
            @RequestParam("pageSize")
            @Parameter(description = "页容量")
            Integer pageSize,
            @RequestParam(value = "keyword", required = false)
            @Parameter(description = "搜索帖子标题关键词")
            String keyword,
            HttpServletRequest request
    ){
        PageResult<PostVO> res = postCommonService.getMyPost(current, pageSize, keyword, request);
        return new Result<>().success().data(res);
    }

    @Operation(description = "评论", tags = "帖子管理")
    @PostMapping("/commentPost")
    public Result commentPost(
            @RequestBody
            AddCommentRequest addCommentRequest,
            HttpServletRequest request
    ) {
        postCommonService.commentPost(addCommentRequest, request);
        return new Result<>().success().message("评论成功");
    }

    @Operation(description = "删除评论", tags = "帖子管理")
    @DeleteMapping("/deleteComment")
    public Result deleteComment(
            @RequestParam("commentId")
            @Parameter(description = "评论id")
            Integer commentId
    ){
        postCommonService.deleteComment(commentId);
        return new Result<>().success().message("删除成功");
    }
}
