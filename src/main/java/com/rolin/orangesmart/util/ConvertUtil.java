package com.rolin.orangesmart.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Author: Iverson
 * Date: 2025/1/15
 * Time: 16:01
 * 类转换工具类
 */
@Data
@Slf4j
public class ConvertUtil {

  /**
   * 将源List集合转化为目标List集合
   */
  public static <T> List<T> listsToList(List<List<T>> lists) {
    if (lists == null || lists.isEmpty()) {
      return new ArrayList<>();
    }
    return lists.stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  /**
   * 将源List集合转化为目标List集合
   */
  public static <T, R> List<R> listToList(List<T> entityList, Function<? super T, ? extends R> converter) {
    if (entityList == null || entityList.isEmpty()) {
      return new ArrayList<>();
    }
    return entityList.stream()
        .map(converter)
        .collect(Collectors.toList());
  }

  /**
   * 将源List集合中的元素与目标List集合中的元素进行合并，合并后的结果保存在目标List集合中
   */
  public static <T, S> void mergeList(List<T> targetList, List<S> sourceList, SetterMethod<T, S> setterMethod) {
    if (targetList == null || sourceList == null || targetList.size() != sourceList.size()) {
      log.error("两个集合的大小必须相同");
      return;
    }

    IntStream.range(0, targetList.size()).forEach(i -> setterMethod.set(targetList.get(i), sourceList.get(i)));
  }

  @FunctionalInterface
  public interface SetterMethod<T, S> {
    void set(T target, S source);
  }


  /**
   * 将源List集合转化为目标Set集合
   */
  public static <T, R> Set<R> listToSet(List<T> entityList, Function<? super T, ? extends R> converter) {
    if (entityList == null || entityList.isEmpty()) {
      return new HashSet<>();
    }
    return entityList.stream()
        .map(converter)
        .collect(Collectors.toSet());
  }

  /**
   * 将源Set集合转化为目标Set集合
   */
  public static <T, R> Set<R> setToSet(Set<T> entitySet, Function<? super T, ? extends R> converter) {
    if (entitySet == null || entitySet.isEmpty()) {
      return new HashSet<>();
    }
    return entitySet.stream()
        .map(converter)
        .collect(Collectors.toSet());
  }

  /**
   * 将源Set集合转化为目标List集合
   */
  public static <T, R> List<R> setToList(Set<T> entitySet, Function<? super T, ? extends R> converter) {
    if (entitySet == null || entitySet.isEmpty()) {
      return new ArrayList<>();
    }
    return entitySet.stream()
        .map(converter)
        .collect(Collectors.toList());
  }

  /**
   * 将源List集合转化为String类型的List集合
   */
  public static <T> List<String> listToStringList(List<T> entityList) {
    if (entityList == null || entityList.isEmpty()) {
      return new ArrayList<>();
    }
    return entityList.stream()
        .map(String::valueOf)
        .collect(Collectors.toList());
  }

  /**
   * 将源List集合转化为String, 以逗号分隔
   */
  public static <T> String listToString(List<T> entityList) {
    if (entityList == null || entityList.isEmpty()) {
      return "";
    }
    return String.join(",", entityList.stream()
        .map(String::valueOf)
        .toArray(String[]::new));
  }

  /**
   * 获取List集合的前十个对象,主要用于测试用
   */
  public static <T> List<T> getTenList(List<T> entityList) {
    if (entityList == null || entityList.isEmpty()) {
      return new ArrayList<>();
    }
    List<T> result = new ArrayList<>();
    for (int i = 0; i < 10 && i < entityList.size(); i++) {
      result.add(entityList.get(i));
    }
    return result;
  }

  /**
   * 将源List集合转化为目标List集合并过滤Null值
   */
  public static <T, R> List<R> listToListFilterNull(List<T> entityList, Function<? super T, ? extends R> converter) {
    if (entityList == null || entityList.isEmpty()) {
      return new ArrayList<>();
    }
    return entityList.stream()
        .map(converter)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * List转List
   * @param list 源List
   * @param clazz 转换类
   * @return
   * @param <T>
   * @param <U>
   */
  public static  <T, U> List<U> listToList(List<T> list, Class<U> clazz) {
    if(CollectionUtils.isEmpty(list)){
      return new ArrayList<>();
    }
    return list.stream()
            .map(item -> BeanUtil.copyProperties(item, clazz))
            .collect(Collectors.toList());
  }

  /**
   * 将源List集合转化为目标Map集合
   */
  public static <T, K> Map<K, T> listToMap(List<T> entityList, Function<? super T, ? extends K> keyExtractor) {
    if (entityList == null || entityList.isEmpty()) {
      return Map.of();
    }
    return entityList.stream()
        .collect(Collectors.toMap(keyExtractor, Function.identity()));
  }
}
