package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.mapper.VisitMapper;
import com.rolin.orangesmart.model.visit.entity.Visit;
import com.rolin.orangesmart.model.visit.vo.VisitDataVO;
import com.rolin.orangesmart.model.visit.vo.VisitEchartsVO;
import com.rolin.orangesmart.model.visit.vo.VisitVO;
import com.rolin.orangesmart.service.common.BaseService;
import com.rolin.orangesmart.util.ConvertUtil;
import com.rolin.orangesmart.util.DateUtil;
import com.rolin.orangesmart.util.QueryWrapperUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Rolin
 * Date: 2025/3/22
 * Time: 12:59
 */
@Service
public class VisitService extends BaseService<VisitMapper, Visit> {

  private final String[] CONTROLLERS = {"attachment","carousel","crawler","download","expert","smart","upload",
      "user","common","post","report","sensitive","problem","visit"};

  public LambdaQueryWrapper<Visit> getLambdaQueryWrapper(){
    return QueryWrapperUtil.getWrapper(Visit.class);
  }

  public boolean saveOrUpdate(String path) {
    boolean save = false;
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

    LambdaQueryWrapper<Visit> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.between(Visit::getCreateDate, startOfDay, endOfDay)
        .last("LIMIT 1"); // 只取一条数据
    Visit visit = getOne(queryWrapper);
    if(visit == null){
      save = true;
      visit = new Visit();
    }
    try {
      for (String controller : CONTROLLERS) {
        if (path.contains(controller)) {
          // 通过反射获取 Visit 类的字段
          Field field = Visit.class.getDeclaredField(controller);
          field.setAccessible(true);
          // 获取当前字段的值并 +1
          Integer currentValue = (Integer) field.get(visit);
          field.set(visit, (currentValue == null ? 0 : currentValue) + 1);
          break;
        }
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      log.error("反射更新访问次数失败", e);
      return false;
    }
    if(save){
      return save(visit);
    }
    return updateById(visit);
  }

  public List<VisitVO> getList() {
    LambdaQueryWrapper<Visit> queryWrapper = getLambdaQueryWrapper();
    List<Visit> visits = list(queryWrapper);
    List<VisitVO> visitVOS = ConvertUtil.listToList(visits, Visit::toVo);
    return visitVOS;
  }

  public VisitVO get() {
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

    LambdaQueryWrapper<Visit> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.between(Visit::getCreateDate, startOfDay, endOfDay)
        .last("LIMIT 1"); // 只取一条数据
    Visit visit = getOne(queryWrapper);
    if(visit == null){
      visit = new Visit();
    }
    return visit.toVo();
  }

  public VisitEchartsVO getSeven() {
    LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(6).atStartOfDay(); // 7天前的00:00:00
    LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX); // 今天的23:59:59

    LambdaQueryWrapper<Visit> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.between(Visit::getCreateDate, sevenDaysAgo, todayEnd)
        .orderByDesc(Visit::getCreateDate); // 按照时间升序排序

    List<Visit> visits = list(queryWrapper);
    List<VisitDataVO> seriesData = new ArrayList<>();
    seriesData.add(new VisitDataVO("附件", ConvertUtil.listToList(visits, Visit::getAttachment)));
    seriesData.add(new VisitDataVO("轮播图", ConvertUtil.listToList(visits, Visit::getCarousel)));
    seriesData.add(new VisitDataVO("爬虫", ConvertUtil.listToList(visits, Visit::getCrawler)));
    seriesData.add(new VisitDataVO("下载", ConvertUtil.listToList(visits, Visit::getDownload)));
    seriesData.add(new VisitDataVO("专家", ConvertUtil.listToList(visits, Visit::getExpert)));
    seriesData.add(new VisitDataVO("智能", ConvertUtil.listToList(visits, Visit::getSmart)));
    seriesData.add(new VisitDataVO("上传", ConvertUtil.listToList(visits, Visit::getUpload)));
    seriesData.add(new VisitDataVO("用户", ConvertUtil.listToList(visits, Visit::getUser)));
    seriesData.add(new VisitDataVO("问答", ConvertUtil.listToList(visits, Visit::getProblem)));
    return new VisitEchartsVO(DateUtil.getDays(visits.size()), seriesData);
  }
}
