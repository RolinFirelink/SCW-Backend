package com.rolin.orangesmart.constant;

/**
 * @author hzzzzzy
 * @create 2025/1/6
 * @description 业务枚举类
 */
public interface RedisConstant {

  /**
   * 用户登录token
   */
  String USER_LOGIN_TOKEN="login:token:";

  /**
   * 用户登录token过期时间
   */
  Integer USER_LOGIN_TOKEN_EXPIRE = 60 * 60 * 24 * 7;

  /**
   * 聊天历史记录key前缀
   */
  String CHAT_HISTORY_PREFIX = "chat:history:";

  /**
   * 活跃用户key前缀
   */
  String ACTIVE_USER = "active:user:";

  /**
   * 帖子点赞key前缀
   */
  String POST_LIKE_KEY_PREFIX = "post:like:";

  /**
   * 帖子评论key前缀
   */
  String POST_COMMENT_KEY_PREFIX = "post:comment:";

  /**
   * 总点赞数key
   */
  String TOTAL_LIKE_COUNT_KEY = "total:like:count";

  /**
   * 帖子点赞去重key前缀
   */
  String POST_DEDUPLICATION_KEY_PREFIX = "post:deduplication:";

  /**
   * 分类名称key前缀
   */
  String CATEGORY_NAME_KEY_PREFIX = "category:name:";

  /**
   * 违规次数 Key
   */
  String BAN_COUNT_KEY = "user:ban:count:";

  /**
   * 禁言等级 Key
   */
  String BAN_LEVEL_KEY = "user:ban:level:";

  /**
   * 禁言时间
   */
  String BAN_END_TIME_KEY = "ban:end_time:";
}