package com.hutquan.hut.utils;

import com.hutquan.hut.vo.Page;

/**
 * 分页的页码处理
 */
public class  PageUtils {

    public static Page Page(int pageNum, int pageSize){
        Page page = new Page();
        //0~9 9~19
        if(pageNum >= 0 && pageSize >= 0) {
            page.setPageBegin((pageNum - 1) * (pageSize - 1));
            page.setPageEnd((pageSize * pageNum) - 1);
        }else {
            page.setPageBegin(0);
            page.setPageEnd(pageSize - 1);
        }
        return page;
    }

}
