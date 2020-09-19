package com.jib.wwyt.utils;

import com.cczu.sys.comm.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    /**
     * 获取request对象
     *
     * @param request
     * @return page对象
     */
    public static Map<String, Object> getPageMap(HttpServletRequest request) {
        int pageNo = 0; // 当前页码
        int pageSize = 0; // 每页行数
        String orderBy = ""; // 排序字段
        String order = ""; // 排序顺序
        if (StringUtils.isNotEmpty(request.getParameter("page")))
            pageNo = Integer.valueOf(request.getParameter("page"));
        if (StringUtils.isNotEmpty(request.getParameter("rows")))
            pageSize = Integer.valueOf(request.getParameter("rows"));
        if (StringUtils.isNotEmpty(request.getParameter("sort")))
            orderBy = request.getParameter("sort").toString();
        if (StringUtils.isNotEmpty(request.getParameter("order")))
            order = request.getParameter("order").toString();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("orderBy", orderBy);
        map.put("order", order);
        return map;
    }
}
