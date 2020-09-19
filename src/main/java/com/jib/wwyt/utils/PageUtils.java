package com.jib.wwyt.utils;

import java.util.List;
import java.util.Map;

public class PageUtils{
    private static  int totalPage;  //总页数
    private static int star;//开始数据
    private static int pageSize;//每页显示记录条数
    private static Integer currentPage;//当前页

    public static List getPageList(int pege, int rows, List list) {

        int size = list.size();
        //计算查询结果可以分的页数
        totalPage = size % rows == 0 ? size / rows : size / rows + 1;

        star = (pege - 1) * rows;

        if(pege > totalPage){
            return null;
        }
        List subList = list.subList(star, size - star > rows?star + rows:size);
        return subList;
    }



    public static List getPageList2(List list, Map map) {
        int page = Integer.parseInt(map.get("page").toString());
        int rows =  Integer.parseInt(map.get("rows").toString());

        int size = list.size();
        //计算查询结果可以分的页数
        totalPage = size % rows == 0 ? size / rows : size / rows + 1;

        star = (page - 1) * rows;

        if(page > totalPage){
            return null;
        }
        List subList = list.subList(star, size - star > rows?star + rows:size);
        return subList;
    }
}
