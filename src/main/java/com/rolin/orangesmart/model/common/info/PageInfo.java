package com.rolin.orangesmart.model.common.info;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class PageInfo<T>  {

    //总记录数
    private long total;
    //结果集
    private List<T> list;
    //当前页
    private int pageNum;
    //每页的数量
    private int pageSize;
    //当前页的数量
    private int size;

    //当前页面第一个元素在数据库中的行号
    private int startRow;
    //当前页面最后一个元素在数据库中的行号
    private int endRow;
    //总页数
    private int pages;

    //前一页
    private int prePage;
    //下一页
    private int nextPage;

    //是否为第一页
    private boolean isFirstPage = false;
    //是否为最后一页
    private boolean isLastPage = false;
    //是否有前一页
    private boolean hasPreviousPage = false;
    //是否有下一页
    private boolean hasNextPage = false;
    //导航页码数
    private int navigatePages;
    //所有导航页号
    private int[] navigatepageNums;
    //导航条上的第一页
    private int navigateFirstPage;
    //导航条上的最后一页
    private int navigateLastPage;

    public PageInfo(IPage<T> page) {
        this.pageNum = (int) page.getCurrent();
        this.pageSize = (int) page.getSize();
        this.total = page.getTotal();
        this.list = page.getRecords();

        this.pages = (int) page.getPages();
        this.size = list.size();

        //由于结果是>startRow的，所以实际的需要+1
        if (this.size == 0) {
            this.startRow = 0;
            this.endRow = 0;
        } else {
            this.startRow = (this.pageNum - 1) * this.pageSize + 1;
            //计算实际的endRow（最后一页的时候特殊）
            this.endRow = this.startRow - 1 + this.pageSize;
        }

        this.navigatePages = 8;
        //计算导航页
        calcNavigatepageNums();
        //计算前后页，第一页，最后一页
        calcPage();
        //判断页面边界
        judgePageBoudary();
    }

    private void initEmptyPage() {
        this.total = 0;
        this.pages = 0;
        this.list = Collections.emptyList();
        this.pageNum = 1;
        this.pageSize = 0;
        this.size = 0;
        this.startRow = 0;
        this.endRow = 0;
        this.navigatePages = 8;
        calcNavigatepageNums();
        calcPage();
        judgePageBoudary();
    }

    public PageInfo(int pageNum, int pageSize, List<T> sourceList) {
        // 处理空列表
        if (sourceList == null || sourceList.isEmpty()) {
            initEmptyPage();
            return;
        }

        // 参数修正
        pageSize = Math.max(pageSize, 1);
        pageNum = Math.max(pageNum, 1);

        // 基础分页计算
        this.total = sourceList.size();
        this.pages = (int) Math.ceil((double) total / pageSize);
        pageNum = Math.min(pageNum, pages); // 防止越界

        // 当前页数据切片
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = (int) Math.min(startIndex + pageSize, total);
        this.list = sourceList.subList(startIndex, endIndex);

        // 设置分页参数
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.size = list.size();

        // 行号计算
        if (this.size == 0) {
            this.startRow = 0;
            this.endRow = 0;
        } else {
            this.startRow = startIndex + 1; // 数据库行号从1开始
            this.endRow = (int) Math.min(startIndex + pageSize, total);
        }

        // 导航页相关计算
        this.navigatePages = 8;
        calcNavigatepageNums();
        calcPage();
        judgePageBoudary();
    }

    /**
     * 计算导航页
     */
    private void calcNavigatepageNums() {
        //当总页数小于或等于导航页码数时
        if (pages <= navigatePages) {
            navigatepageNums = new int[pages];
            for (int i = 0; i < pages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = pageNum - navigatePages / 2;
            int endNum = pageNum + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > pages) {
                endNum = pages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
    }

    /**
     * 计算前后页，第一页，最后一页
     */
    private void calcPage() {
        if (navigatepageNums != null && navigatepageNums.length > 0) {
            navigateFirstPage = navigatepageNums[0];
            navigateLastPage = navigatepageNums[navigatepageNums.length - 1];
            if (pageNum > 1) {
                prePage = pageNum - 1;
            }
            if (pageNum < pages) {
                nextPage = pageNum + 1;
            }
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = pageNum == 1;
        isLastPage = pageNum == pages || pages == 0;
        hasPreviousPage = pageNum > 1;
        hasNextPage = pageNum < pages;
    }

    public <R> PageInfo<R> convert(Function<? super T, ? extends R> info) {
        PageInfo<R> pageInfo = new PageInfo<>();
        BeanUtils.copyProperties(this, pageInfo);
        pageInfo.setList(this.getList().stream().map(info).collect(Collectors.toList()));
        return pageInfo;
    }

}
