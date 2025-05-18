package com.rolin.orangesmart.service.fish;


import com.rolin.orangesmart.model.fish.vo.InfoVO;
import com.rolin.orangesmart.model.fish.vo.TotalVO;

import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/12
 * @description
 */
public interface CommonService {

    /**
     * 获取今日信息
     * @return
     */
    InfoVO getToday();

    /**
     * 获取往日信息
     * @return
     * @param current
     * @param pageSize
     */
    List<InfoVO> getHistory(Integer current, Integer pageSize);

    TotalVO getTotal();

}
