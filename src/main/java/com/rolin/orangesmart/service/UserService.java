package com.rolin.orangesmart.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rolin.orangesmart.cache.redis.RedisCacheService;
import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.constant.RedisConstant;
import com.rolin.orangesmart.context.ReqEnvContext;
import com.rolin.orangesmart.enums.BanLevel;
import com.rolin.orangesmart.enums.BusinessFailCode;
import com.rolin.orangesmart.exception.GlobalException;
import com.rolin.orangesmart.exception.errorEnum.UserErrorEnum;
import com.rolin.orangesmart.mapper.UserMapper;
import com.rolin.orangesmart.model.common.Result;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.fish.entity.Follows;
import com.rolin.orangesmart.model.user.bo.UserBO;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.model.user.vo.UserVO;
import com.rolin.orangesmart.security.encoder.IPasswordEncoder;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.service.fish.FollowsService;
import com.rolin.orangesmart.service.fish.PostCommonService;
import com.rolin.orangesmart.util.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rolin.orangesmart.constant.CommonConstant.HEADER_TOKEN;

@Service
public class UserService extends BaseService<UserMapper, User> {

    @Resource
    public IPasswordEncoder passwordEncoder;
    @Resource
    public UserMapper userMapper;
    @Resource
    private RedisCacheService redisCacheService;
    private static final String SALT = "hzzzzzy";

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private FollowsService followsService;
    @Autowired
    private PostCommonService postCommonService;
    @Autowired
    private BanUtils banUtils;

    public PageInfo<UserVO> page(Integer pageNum, Integer pageSize, String keyWord) {
        LambdaQueryWrapper<User> queryWrapper = getLambdaQueryWrapper();
        if (CheckUtil.isNotEmpty(keyWord)) {
            queryWrapper.like(User::getUserName, keyWord).or().like(User::getAccount, keyWord);
        }
        PageInfo<User> page = page(pageNum, pageSize, queryWrapper);
        return page.convert(User::toVo);
    }

    public Long save(UserBO userBO) {
        User user = userBO.toEntity();
        LambdaQueryWrapper<User> queryWrapper = getLambdaQueryWrapper();
        queryWrapper.eq(User::getAccount, user.getAccount());
        UserErrorEnum.USER_EXIST_ERROR.isTrue(exists(queryWrapper));
        EntityUtil.convertToSave(user);
        user.setPassword(generateEncryptPassword(user.getPassword()));
        save(user);
        return user.getId();
    }

    public boolean updateById(UserBO userBO) {
        User byId = getById(userBO.getId());
        UserErrorEnum.USER_NOT_EXIST_ERROR.isNull(byId);
        User user = BeanUtil.copyProperties(userBO.toEntity(), User.class);
        user.setPassword(generateEncryptPassword(user.getPassword()));
        return updateById(user);
    }

    public LambdaQueryWrapper<User> getLambdaQueryWrapper() {
        return QueryWrapperUtil.getWrapper(User.class);
    }

    public List<User> list(String keyword) {
        LambdaQueryWrapper<User> queryWrapper = getLambdaQueryWrapper();
        if (CheckUtil.isNotEmpty(keyword)) {
            queryWrapper.like(User::getUserName, keyword).or().like(User::getAccount, keyword);
        }
        return list(queryWrapper);
    }

    public User getUserByAccountAndUserType(String account, Integer userType) {
        LambdaQueryWrapper<User> queryWrapper = getLambdaQueryWrapper();
        queryWrapper.eq(User::getAccount, account);
        queryWrapper.eq(User::getUserType, userType);
        return getOne(queryWrapper);
    }

    private String generateEncryptPassword(String password) {
        if (!StringUtils.hasText(password)) {
            password = "a123456";
        }
        UserErrorEnum.USER_NO_SET_PASSWORD_ERROR.isNull(!StringUtils.hasText(password));

        password = passwordEncoder.frontendEncode(password);
        password = passwordEncoder.backendEncode(password);
        return password;
    }

    public List<User> getAll() {
        return userMapper.getAll();
    }

    public boolean logout() {
        String key = CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + ReqEnvContext.getUser().getAccount();
        redisCacheService.del(key);
        return true;
    }

    public UserVO getSelf() {
        return ReqEnvContext.getUser().toVo();
    }


    // fish
    public UserVO getUserInfo(HttpServletRequest request) {
        User user = getUser(request);
        if (user == null) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.PARAMETER_ERROR).message("token为空，获取用户信息失败"));
        }
        Integer userId = user.getId().intValue();
        User newUser = this.getById(userId);
        UserVO userVO = cn.hutool.core.bean.BeanUtil.copyProperties(newUser, UserVO.class);
        // 获取关注的人数
        int followCount = (int)followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowerId, userId));
        // 获取被关注的人数
        int fansCount = (int)followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowedId, userId));
        // 获取帖子点赞的总数量
        long postLikeCount = postCommonService.getUserPostLikeCount(userId);
        userVO.setFollowCount(followCount);
        userVO.setFansCount(fansCount);
        userVO.setPostLikeCount(postLikeCount);
        BanLevel banLevel = banUtils.getCurrentLevel(userId);
        if (banLevel == null || banLevel.getLevel() == BanLevel.LEVEL_0.getLevel()) {
            userVO.setBanDesc(BanLevel.LEVEL_0.getDesc());
        } else {
            userVO.setBanDesc("已被" + banLevel.getDesc());
        }
        return userVO;
    }

    public UserVO getUserInfo(Integer userId) {
        User newUser = this.getById(userId);
        UserVO userVO = cn.hutool.core.bean.BeanUtil.copyProperties(newUser, UserVO.class);
        // 获取关注的人数
        int followCount = (int)followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowerId, userId));
        // 获取被关注的人数
        int fansCount = (int)followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowedId, userId));
        long postLikeCount = postCommonService.getUserPostLikeCount(userId);
        userVO.setFollowCount(followCount);
        userVO.setFansCount(fansCount);
        userVO.setPostLikeCount(postLikeCount);
        BanLevel banLevel = banUtils.getCurrentLevel(userId);
        if (banLevel == null) {
            userVO.setBanDesc(BanLevel.LEVEL_0.getDesc());
        }
        userVO.setBanDesc("已被 " + banLevel.getDesc());
        return userVO;
    }

    public List<UserVO> getFollows(HttpServletRequest request) {
        User user = getUser(request);
        List<Follows> followsList = followsService.list(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowerId, user.getId()));
        if (followsList.isEmpty()) {
            return new ArrayList<>();
        }
        return followsList.stream().map(follows -> {
            User followUser = this.getById(follows.getFollowedId());
            UserVO userVO = cn.hutool.core.bean.BeanUtil.copyProperties(followUser, UserVO.class);
            // 获取关注的人数
            int followCount = (int)followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowerId, user.getId()));
            // 获取被关注的人数
            int fansCount = (int)followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowedId, user.getId()));
            userVO.setFollowCount(followCount);
            userVO.setFansCount(fansCount);
            return userVO;
        }).collect(Collectors.toList());
    }

    public List<UserVO> getFans(HttpServletRequest request) {
        User user = getUser(request);
        List<Follows> followsList = followsService.list(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowedId, user.getId()));
        if (followsList.isEmpty()) {
            return new ArrayList<>();
        }
        return followsList.stream().map(follows -> {
            User followUser = this.getById(follows.getFollowerId());
            UserVO userVO = cn.hutool.core.bean.BeanUtil.copyProperties(followUser, UserVO.class);
            // 获取关注的人数
            int followCount = (int)followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowerId, user.getId()));
            // 获取被关注的人数
            int fansCount = (int)followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowedId, user.getId()));
            userVO.setFollowCount(followCount);
            userVO.setFansCount(fansCount);
            return userVO;
        }).collect(Collectors.toList());
    }

    public void follow(HttpServletRequest request, Integer id) {
        // 1. 先判断是否已经关注了该用户
        // 2. 如果已经关注了，则取消关注
        // 3. 如果没有关注，则添加关注
        User user = getUser(request);
        if (followsService.count(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowerId, user.getId()).eq(Follows::getFollowedId, id)) > 0) {
            followsService.remove(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowerId, user.getId()).eq(Follows::getFollowedId, id));
        } else {
            Follows follows = new Follows();
            follows.setFollowerId(user.getId().intValue());
            follows.setFollowedId(id);
            followsService.save(follows);
        }
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = stringRedisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
        User user = JSONUtil.toBean(jsonUser, User.class);
        return user;
    }



}
