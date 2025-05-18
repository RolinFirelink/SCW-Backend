package com.rolin.orangesmart.enums;

import lombok.Getter;

/**
 * @author hzzzzzy
 * @date 2025/2/9
 * @description
 */
@Getter
public enum BanLevel {

	LEVEL_3(3, "禁言一个月", 5, 30 * 24 * 60 * 60L), // 一个月，单位：秒
	LEVEL_2(2, "禁言12小时", 3, 12 * 60 * 60L), // 12小时，单位：秒
	LEVEL_1(1, "禁言6小时", 1, 6 * 60 * 60L), // 6小时，单位：秒
	LEVEL_0(0, "正常", 0, 0L);

	private final Integer level;   // 禁言等级
	private final String desc;     // 描述
	private final Integer threshold; // 触发该等级的最小违规次数
	private final Long banDuration; // 禁言时长，单位：秒

	BanLevel(Integer level, String desc, Integer threshold, Long banDuration) {
		this.level = level;
		this.desc = desc;
		this.threshold = threshold;
		this.banDuration = banDuration;
	}

	/**
	 * 根据违规次数获取对应禁言等级
	 */
	public static BanLevel getByViolationCount(Integer count) {
		for (BanLevel level : new BanLevel[]{LEVEL_3, LEVEL_2, LEVEL_1}) {
			if (count >= level.threshold) {
				return level;
			}
		}
		return LEVEL_0;
	}

	@Override
	public String toString() {
		return "BanLevel{" +
				"level=" + level +
				", desc='" + desc + '\'' +
				", threshold=" + threshold +
				", banDuration=" + banDuration +
				'}';
	}
}