package com.rolin.orangesmart.util;

import com.rolin.orangesmart.context.ReqEnvContext;
import com.rolin.orangesmart.exception.errorEnum.UserErrorEnum;
import com.rolin.orangesmart.model.user.entity.User;

import java.util.List;

public class CheckUtil {

  /**
   * 检查传入的集合是否不为Null且具有至少一个元素
   */
  public static <T> boolean isNotEmpty(List<T> list) {
    return list != null && !list.isEmpty();
  }

  /**
   * 检查传入的集合是否为Null
   */
  public static <T> boolean isEmpty(List<T> list) {
    return !isNotEmpty(list);
  }

  /**
   * 检查传入的对象不为空且toString至少有一个字符
   */
  public static <T> boolean isNotEmpty(T t) {
    return t != null && !String.valueOf(t).isEmpty();
  }

  /**
   * 检查传入的对象是否为Null
   */
  public static <T> boolean isEmpty(T t) {
    return !isNotEmpty(t);
  }

  public static boolean isPermit(int status) {
    User user = ReqEnvContext.getUser();
    UserErrorEnum.USER_NO_PERMISSION_ERROR.isFalse(user.getUserType().equals(status));
    return true;
  }
}
