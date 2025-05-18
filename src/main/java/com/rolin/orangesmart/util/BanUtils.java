package com.rolin.orangesmart.util;

import com.rolin.orangesmart.enums.BanLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.rolin.orangesmart.constant.RedisConstant.*;

@Component
public class BanUtils {

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 处理违规并更新等级
	 */
	public void handleViolation(Integer userId) {
		// 1. 递增违规次数
		String countKey = BAN_COUNT_KEY + userId;
		Integer count = Math.toIntExact(redisTemplate.opsForValue().increment(countKey, 1));
		// 2. 计算当前等级
		BanLevel currentLevel = BanLevel.getByViolationCount(count != null ? count : 1);
		// 3. 存储等级到 Redis
		String levelKey = BAN_LEVEL_KEY + userId;
		redisTemplate.opsForValue().set(levelKey, String.valueOf(currentLevel.getLevel()));

		// 4. 设置禁言结束时间
		if (currentLevel != BanLevel.LEVEL_0) {
			String banEndTimeKey = BAN_END_TIME_KEY + userId;
			long banEndTime = System.currentTimeMillis() / 1000 + currentLevel.getBanDuration();
			redisTemplate.opsForValue().set(banEndTimeKey, String.valueOf(banEndTime));
		}
	}

	/**
	 * 获取用户当前禁言等级
	 */
	public BanLevel getCurrentLevel(Integer userId) {
		String levelKey = BAN_LEVEL_KEY + userId;
		String levelStr = redisTemplate.opsForValue().get(levelKey);
		if (levelStr == null) {
			// 如果等级为空，则根据违规次数重新计算（兼容旧数据或未初始化的情况）
			int count = getViolationCount(userId);
			return BanLevel.getByViolationCount(count);
		}
		Integer level = Integer.valueOf(levelStr);
		return Arrays.stream(BanLevel.values())
				.filter(banLevel -> banLevel.getLevel().equals(level))
				.findFirst()
				.orElse(BanLevel.LEVEL_0);
	}

	/**
	 * 获取用户的违规次数（自动处理空值）
	 */
	public int getViolationCount(Integer userId) {
		String countKey = BAN_COUNT_KEY + userId;
		String countStr = redisTemplate.opsForValue().get(countKey);
		return countStr != null ? Integer.parseInt(countStr) : 0; // 空值视为 0 次违规
	}

	/**
	 * 检查用户是否处于禁言状态
	 */
	public boolean isBanned(Integer userId) {
		String banEndTimeKey = BAN_END_TIME_KEY + userId;
		String banEndTimeStr = redisTemplate.opsForValue().get(banEndTimeKey);
		if (banEndTimeStr != null) {
			long banEndTime = Long.parseLong(banEndTimeStr);
			long currentTime = System.currentTimeMillis() / 1000;
			return currentTime < banEndTime;
		}
		return false;
	}

	/**
	 * 获取用户禁言剩余时间（秒），如果未禁言返回 0
	 */
	public long getRemainingBanTime(Integer userId) {
		String banEndTimeKey = BAN_END_TIME_KEY + userId;
		String banEndTimeStr = redisTemplate.opsForValue().get(banEndTimeKey);
		if (banEndTimeStr != null) {
			long banEndTime = Long.parseLong(banEndTimeStr);
			long currentTime = System.currentTimeMillis() / 1000;
			if (currentTime < banEndTime) {
				return banEndTime - currentTime;
			}
		}
		return 0;
	}
}