package com.rolin.orangesmart.service.fish;

import com.rolin.orangesmart.model.fish.bo.AddCommentRequest;
import com.rolin.orangesmart.model.fish.bo.AddPostRequest;
import com.rolin.orangesmart.model.fish.bo.UpdatePostRequest;
import com.rolin.orangesmart.model.fish.entity.PageResult;
import com.rolin.orangesmart.model.fish.vo.CommentVO;
import com.rolin.orangesmart.model.fish.vo.PostDetailVO;
import com.rolin.orangesmart.model.fish.vo.PostVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/24
 * @description
 */
public interface PostCommonService {
    void toggleLike(Integer postId, HttpServletRequest request, boolean b);

    void createPost(HttpServletRequest request, AddPostRequest addPostRequest);

    void updatePost(UpdatePostRequest request);

    void deletePost(Integer postId);

    PageResult<PostVO> getAllPost(Integer current, Integer pageSize, String keyword, HttpServletRequest request);

    PostDetailVO getPost(Integer postId, Integer current, Integer pageSize, HttpServletRequest request);

    void commentPost(AddCommentRequest addCommentRequest, HttpServletRequest request);

    void deleteComment(Integer commentId);

    List<Integer> uploadPostImage(MultipartFile[] file);

    String uploadPostImageReturnUrl(MultipartFile[] files);

    String uploadPostImageReturnUrl(File[] files);

    PageResult<PostVO> getMyPost(Integer current, Integer pageSize, String keyword, HttpServletRequest request);

    CommentVO getCommentInfo(Integer objectId);

    PostVO getPostInfo(Integer objectId);

	  Long getUserPostLikeCount(Integer userId);
}
