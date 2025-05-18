package com.rolin.orangesmart.util;

import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.fish.entity.PageResult;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {

	public static <T> PageResult<T> getPage(List<T> list, int page, int size) {
		if (page == 0 || size == 0 || list == null || list.size() == 0) {
			return new PageResult<T>((long) (list == null ? 0 : list.size()), list);
		}
		List<T> records = new ArrayList<>();
		for (int i = (page - 1) * size; i < page * size && i < list.size(); i++) {
			records.add(list.get(i));
		}
		return new PageResult<T>((long) list.size(), records);
	}

	public static <T> PageInfo<T> buildPageInfo(int pageNum, int pageSize, List<T> list) {
		return buildPageInfo(pageNum, pageSize, list.size(), (pageNum - 1) * pageSize, -1, list);
	}


	public static <T> PageInfo<T> buildPageInfo(int pageNum, int pageSize, int total, int startRow, int endRow, List<T> list) {
		PageInfo<T> pageInfo = new PageInfo<T>();

		// 对传入的 List 进行分页处理
		List<T> pageList = new ArrayList<>();
		int startIndex = (pageNum - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, list.size());
		if (startIndex < list.size()) {
			pageList = list.subList(startIndex, endIndex);
		}

		pageInfo.setList(pageList); // 设置分页后的 List
		pageInfo.setTotal(total);
		pageInfo.setPageNum(pageNum);
		pageInfo.setPageSize(pageSize);
		pageInfo.setSize(pageList.size());
		pageInfo.setPages((int) Math.ceil(((float) total / pageSize)));

		// 由于结果是 > startRow 的，所以实际的需要 +1
		if (pageInfo.getSize() == 0) {
			pageInfo.setStartRow(0);
			pageInfo.setEndRow(0);
		} else {
			pageInfo.setStartRow(startRow + 1);
			// 计算实际的 endRow（最后一页的时候特殊）
			pageInfo.setEndRow(pageInfo.getStartRow() - 1 + pageList.size());
		}

		int navigatePages = 8; // 默认
		pageInfo.setNavigatePages(navigatePages);
		// 计算导航页
		calcNavigatepageNums(pageInfo);
		// 计算前后页，第一页，最后一页
		calcPage(pageInfo);
		// 判断页面边界
		judgePageBoudary(pageInfo);

		return pageInfo;
	}

//	/**
//	 * 功能描述：構建分頁對象
//	 *
//	 * @param <T>
//	 * @param list
//	 * @return
//	 */
//	public static <T> PageInfo<T> buildPageInfo(int pageNum, int pageSize, int total, int startRow, int endRow, List<T> list) {
//		PageInfo<T> pageInfo = new PageInfo<T>();
//		pageInfo.setList(list);
//		pageInfo.setTotal(total);
//		pageInfo.setPageNum(pageNum);
//		pageInfo.setPageSize(pageSize);
//		pageInfo.setSize(list.size());
//		pageInfo.setPages((int) Math.ceil(((float) total / pageSize)));
//		//由于结果是>startRow的，所以实际的需要+1
//		if (pageInfo.getSize() == 0) {
//			pageInfo.setStartRow(0);
//			pageInfo.setEndRow(0);
//		} else {
//			pageInfo.setStartRow(startRow + 1);
//			//计算实际的endRow（最后一页的时候特殊）
//			pageInfo.setEndRow(pageInfo.getStartRow() - 1 + list.size());
//		}
//
//		int navigatePages = 8;//默認
//		pageInfo.setNavigatePages(navigatePages);
//		//计算导航页
//		calcNavigatepageNums(pageInfo);
//		//计算前后页，第一页，最后一页
//		calcPage(pageInfo);
//		//判断页面边界
//		judgePageBoudary(pageInfo);
//
//		return pageInfo;
//	}

	/**
	 * 计算导航页
	 */
	private static <T> void calcNavigatepageNums(PageInfo<T> pageInfo) {
		// 当总页数小于或等于导航页码数时
		if (pageInfo.getPages() <= pageInfo.getNavigatePages()) {
			pageInfo.setNavigatepageNums(new int[pageInfo.getPages()]);
			for (int i = 0; i < pageInfo.getPages(); i++) {
				pageInfo.getNavigatepageNums()[i] = i + 1;
			}
		} else { // 当总页数大于导航页码数时
			pageInfo.setNavigatepageNums(new int[pageInfo.getNavigatePages()]);
			int startNum = pageInfo.getPageNum() - pageInfo.getNavigatePages() / 2;
			int endNum = pageInfo.getPageNum() + pageInfo.getNavigatePages() / 2;

			if (startNum < 1) {
				startNum = 1;
				// (最前navigatePages页
				for (int i = 0; i < pageInfo.getNavigatePages(); i++) {
					pageInfo.getNavigatepageNums()[i] = startNum++;
				}
			} else if (endNum > pageInfo.getPages()) {
				endNum = pageInfo.getPages();
				// 最后navigatePages页
				for (int i = pageInfo.getNavigatePages() - 1; i >= 0; i--) {
					pageInfo.getNavigatepageNums()[i] = endNum--;
				}
			} else {
				// 所有中间页
				for (int i = 0; i < pageInfo.getNavigatePages(); i++) {
					pageInfo.getNavigatepageNums()[i] = startNum++;
				}
			}
		}
	}

	/**
	 * 计算前后页，第一页，最后一页
	 */
	private static <T> void calcPage(PageInfo<T> pageInfo) {
		if (pageInfo.getNavigatepageNums() != null && pageInfo.getNavigatepageNums().length > 0) {
			pageInfo.setNavigateFirstPage(pageInfo.getNavigatepageNums()[0]);
			pageInfo.setNavigateLastPage(pageInfo.getNavigatepageNums()[pageInfo.getNavigatepageNums().length - 1]);
			if (pageInfo.getPageNum() > 1) {
				pageInfo.setPrePage(pageInfo.getPageNum() - 1);
			}
			if (pageInfo.getPageNum() < pageInfo.getPages()) {
				pageInfo.setNextPage(pageInfo.getPageNum() + 1);
			}
		}
	}

	/**
	 * 判定页面边界
	 */
	private static <T> void judgePageBoudary(PageInfo<T> pageInfo) {
		pageInfo.setFirstPage(pageInfo.getPageNum() == 1);
		pageInfo.setLastPage(pageInfo.getPageNum() == pageInfo.getPages() || pageInfo.getPages() == 0);
		pageInfo.setHasPreviousPage(pageInfo.getPageNum() > 1);
		pageInfo.setHasNextPage(pageInfo.getPageNum() < pageInfo.getPages());
	}
}