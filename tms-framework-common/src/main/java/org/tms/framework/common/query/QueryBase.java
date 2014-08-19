/*
 * Copyright (c) 2006-2010 All Rights Reserved.
 *
 * Author     :zhangsen
 * Version    :1.0
 * Create Date:2010-7-26
 *
 */
package org.tms.framework.common.query;


import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 分页算法封装。 分页须设置: TotalItem（总条数）,缺省为0, 应该在dao中被设置 PageSize（每页条数），应在web层被设置
 * QueryBase 缺省为20，子类可以通过覆盖 getDefaultPageSize() 修改 CurrentPage（当前页）,缺省为1，首页，
 * 应在web层被设置 分页后，可以得到：TotalPage（总页数） FristItem(当前页开始记录位置，从1开始记数)
 * PageLastItem(当前页最后记录位置) 页面上，每页显示条数名字应为： lines ，当前页名字应为： page
 * 
 * @author fish
 * @author shencb 2010-12-3
 */
public class QueryBase implements Serializable {

    /** 序例值 */
    private static final long    serialVersionUID = 7603300820027561064L;

    /** 缺省每分页记录数 */
    private static final Integer defaultPageSize  = new Integer(20);

    /** 缺省首页数 */
    private static final Integer defaultFriatPage = new Integer(1);

    /** 缺省总记录数 */
    private static final Integer defaultTotleItem = new Integer(0);

    /** 总记录数 */
    private Integer              totalItem;

    /** 每分页记录数 */
    private Integer              pageSize;

    /** 当前页数 */
    private Integer              currentPage;

    /**
     * 获取缺省每分页记录数
     * 
     * @return Returns the defaultPageSize.
     */
    protected final Integer getDefaultPageSize() {
        return defaultPageSize;
    }

    /**
     * 判断当前页 是否是首页
     * @return 当前页 是否是首页
     */
    public boolean isFirstPage() {
        return this.getCurrentPage().intValue() == 1;
    }

    /**
     * 获取上一页数
     * @return 上一页数
     */
    public int getPreviousPage() {
        int back = this.getCurrentPage().intValue() - 1;

        if (back <= 0) {
            back = 1;
        }
        return back;
    }

    /**
     * 是否是最后一页
     * @return 是否是最后一页
     */
    public boolean isLastPage() {
        return this.getTotalPage() == this.getCurrentPage().intValue();
    }

    /**
     * 获取下一页数
     * @return 下一页数
     */
    public int getNextPage() {
        int back = this.getCurrentPage().intValue() + 1;

        if (back > this.getTotalPage()) {
            back = this.getTotalPage();
        }

        return back;
    }

    /**
     * 获取当前页
     * @return Returns the currentPage.
     */
    public Integer getCurrentPage() {
        if (currentPage == null) {
            return defaultFriatPage;
        }

        return currentPage;
    }

    /**
     * 设置当前页数
     * @param cPage
     *            The currentPage to set.
     */
    public void setCurrentPage(Integer cPage) {
        if ((cPage == null) || (cPage.intValue() <= 0)) {
            this.currentPage = defaultFriatPage;
        } else {
            this.currentPage = cPage;
        }
    }

    /**
     * 设置当前页数字符串
     * @param s 当前页数字符串
     */
    public void setCurrentPageString(String s) {
        if (StringUtils.isBlank(s)) {
            return;
        }
        try {
            setCurrentPage(Integer.parseInt(s));
        } catch (NumberFormatException ignore) {
            this.setCurrentPage(defaultFriatPage);
        }
    }

    /**
     * 获取每页记录数
     * @return Returns the pageSize.
     */
    public Integer getPageSize() {
        if (pageSize == null) {
            return getDefaultPageSize();
        }

        return pageSize;
    }

    /**
     * 是否设置每页记录数
     * @return 是否设置每页记录数
     */
    public boolean hasSetPageSize() {
        return pageSize != null;
    }

    /**
     * 设置每页记录数
     * @param pSize
     *            The pageSize to set.
     */
    public void setPageSize(Integer pSize) {
        if (pSize == null) {
            throw new IllegalArgumentException("PageSize can't be null.");
        }

        if (pSize.intValue() <= 0) {
            throw new IllegalArgumentException("PageSize must great than zero.");
        }

        this.pageSize = pSize;
    }

    /**
     * 设置每页记录数字符串
     * @param pageSizeString 每页记录数字符串
     */
    public void setPageSizeString(String pageSizeString) {
        if (StringUtils.isBlank(pageSizeString)) {
            return;
        }

        try {
            Integer integer = new Integer(pageSizeString);
            this.setPageSize(integer);
        } catch (NumberFormatException ignore) {
        }
    }

    /**
     * 获取总记录数
     * @return Returns the totalItem.
     */
    public Integer getTotalItem() {
        if (totalItem == null) {
            return defaultTotleItem;
        }
        return totalItem;
    }

    /**
     * 设置总记录数
     * @param tItem
     *            The totalItem to set.
     */
    public void setTotalItem(Integer tItem) {
        if (tItem == null) {
            tItem = new Integer(0);
        }
        this.totalItem = tItem;
        int current = this.getCurrentPage().intValue();
        int lastPage = this.getTotalPage();
        if (current > lastPage) {
            this.setCurrentPage(new Integer(lastPage));
        }
    }

    /**
     * 获取总页数
     * @return 总页数
     */
    public int getTotalPage() {
        int pgSize = this.getPageSize().intValue();
        int total = this.getTotalItem().intValue();
        int result = total / pgSize;
        if ((total % pgSize) != 0) {
            result++;
        }
        return result;
    }

    /**
     * 获取每页开始记录数
     * @return 每页开始记录数
     */
    public int getPageFristItem() {
        int cPage = this.getCurrentPage().intValue();
        if (cPage == 1) {
            return 1; // 第一页开始当然是第 1 条记录
        }
        cPage--;
        int pgSize = this.getPageSize().intValue();

        return (pgSize * cPage) + 1;
    }

    /**
     * @return 如果是mysql，那么该返回的第一条记录应该是0
     */
    public int getMysqlPageFristItem() {
        return getPageFristItem() - 1;
    }

    /**
     * 获取最后一页记录数
     * @return 最后一页记录数
     */
    public int getPageLastItem() {
        int assumeLast = getExpectPageLastItem();
        int totalItem = getTotalItem().intValue();

        if (assumeLast > totalItem) {
            return totalItem;
        } else {
            return assumeLast;
        }
    }

    /**
     * @return 返回最后页内容
     */
    public int getExpectPageLastItem() {
        int cPage = this.getCurrentPage().intValue();
        int pgSize = this.getPageSize().intValue();
        return pgSize * cPage;
    }

}
