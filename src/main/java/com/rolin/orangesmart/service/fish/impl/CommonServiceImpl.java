package com.rolin.orangesmart.service.fish.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.constant.RedisConstant;
import com.rolin.orangesmart.model.fish.entity.Post;
import com.rolin.orangesmart.model.fish.vo.InfoVO;
import com.rolin.orangesmart.model.fish.vo.TotalVO;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.service.UserService;
import com.rolin.orangesmart.service.fish.CommonService;
import com.rolin.orangesmart.service.fish.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author hzzzzzy
 * @date 2025/1/12
 * @description
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Override
    public InfoVO getToday() {
        InfoVO vo = new InfoVO();
        // 获取今日新增用户数
        vo.setNewUserCount((int)userService.count(new LambdaQueryWrapper<User>().gt(User::getCreateDate, LocalDateTime.now().minusDays(1))));
        // 获取今日新增帖子数量
        long count = postService.count(new LambdaQueryWrapper<Post>().gt(Post::getCreateTime, LocalDateTime.now().minusDays(1)));
        vo.setNewPostCount((int)count);
        // 获取活跃用户数量
        String key = RedisConstant.ACTIVE_USER + LocalDate.now();
        Long activeUserCount = redisTemplate.opsForList().size(key);
        vo.setActiveUserCount(activeUserCount == null ? 0 : activeUserCount.intValue());
        // 获取日期
        vo.setDate(LocalDate.now().toString());
        return vo;
    }

    @Override
    public List<InfoVO> getHistory(Integer current, Integer pageSize) {
        List<InfoVO> historyList = new ArrayList<>();
        // 计算起始日期和结束日期
        LocalDate endDate = LocalDate.now().minusDays((current - 1) * pageSize);
        LocalDate startDate = endDate.minusDays(pageSize - 1);

        for (LocalDate date = endDate;!date.isBefore(startDate); date = date.minusDays(1)) {
            InfoVO vo = new InfoVO();
            String dateStr = date.toString();

            // 设置日期
            vo.setDate(dateStr);

            // 获取当日活跃用户数量
            String activeUserKey = RedisConstant.ACTIVE_USER + dateStr;
            Long activeUserCount = redisTemplate.opsForList().size(activeUserKey);
            vo.setActiveUserCount(activeUserCount == null ? 0 : activeUserCount.intValue());

            // 获取当日新增用户数
            int newUserCount = (int)userService.count(new LambdaQueryWrapper<User>()
                    .ge(User::getCreateDate, date.atStartOfDay())
                    .lt(User::getCreateDate, date.plusDays(1).atStartOfDay()));
            vo.setNewUserCount(newUserCount);

            // 获取当日新增帖子数量
            int newPostCount = (int)postService.count(new LambdaQueryWrapper<Post>()
                    .ge(Post::getCreateTime, date.atStartOfDay())
                    .lt(Post::getCreateTime, date.plusDays(1).atStartOfDay()));
            vo.setNewPostCount(newPostCount);

            historyList.add(vo);
        }
        return historyList;
    }

    @Override
    public TotalVO getTotal() {
        TotalVO vo = new TotalVO();
        vo.setTotalUser((int)userService.count(new LambdaQueryWrapper<User>().eq(User::getUserType, 1)));
        vo.setTotalPost((int)postService.count());
        String value = redisTemplate.opsForValue().get(RedisConstant.TOTAL_LIKE_COUNT_KEY);
        if (value != null) {
            vo.setTotalLike(Long.parseLong(value));
        }else {
            vo.setTotalLike(0L);
        }
        return vo;
    }
}
