package gms.cuit.controller;

import gms.cuit.entity.*;
import gms.cuit.service.AdminService;
import gms.cuit.utils.ExportExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController()
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    AdminService adminService;

    /*
    *  注意: 查询使用get, 其它使用post!!!!! restful规范!!!
    * */
    @GetMapping("/getAdminName")
    public Gms_Admin getAdminName(Principal principal){
        Gms_Admin admin = new Gms_Admin();
        admin.setAdmin_Username(principal.getName());
        return admin;
    }

    /*
    * 进行密码更新,最后返回空json代表更新成功了
    * */
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestBody Map<String,String> map){
        String username = map.get("username");
        String newpassword = map.get("newpassword");
        adminService.updatePassword(username,newpassword);
        return "{}";
    }

    /**
     * 一次把summary页面所需所有参数以json形式返回
     * @return summaryTodayVistors,summaryTodayOrderProfit,summaryTodayOrderCount,
     * summaryOrderCountByDateAndVen,summaryVenueVisDateByDate,summaryVenueVisCountByDate,
     * latestOrderList
     */
    @GetMapping("/getSummary")
    public Map<String,Object> getSummary(){
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String currentDate = dateFormat.format( new Date());
        //获取今天订单利润
        Double summaryTodayOrderProfit = adminService.getSummaryTodayOrderProfit(currentDate);
        //获取今天订单人数
        Integer summaryTodayOrderCount =  adminService.getSummaryTodayOrderCount(currentDate);
        //获取今天订单按场馆分类
        String summaryOrderCountByDateAndVen = adminService.getSummaryOrderCountByDateAndVen(currentDate);
        //获取近期的访问量日期
        String summaryVenueVisDateByDate = "";
        String summaryVenueVisCountByDate = "";
        //获取今天访问量
        Integer summaryTodayVistors = null;
        for(int i=-6;i<=0;i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, i);
            summaryVenueVisDateByDate += sdf.format(calendar.getTime());//日期拼接 05-20
            Integer vvInteger = (Integer) servletContext.getAttribute(dateFormat.format(calendar.getTime()));// 2020-05-20
            if(vvInteger==null) vvInteger= (new Random().nextInt(80)) + 10;//随机访问量
            servletContext.setAttribute(dateFormat.format(calendar.getTime()), vvInteger);
            summaryVenueVisCountByDate += vvInteger.toString();
            if(i!=0) {
                summaryVenueVisDateByDate += " ";
                summaryVenueVisCountByDate += " ";
            }
            if(i==0) summaryTodayVistors = vvInteger;
        }
        //获取最近订单
        int get_count = 5;
        List<Gms_Order> latestOrderList = adminService.getLatestOrderList(get_count);
        map.put("summaryTodayOrderProfit",summaryTodayOrderProfit);
        map.put("summaryTodayOrderCount",summaryTodayOrderCount);
        map.put("summaryOrderCountByDateAndVen",summaryOrderCountByDateAndVen);
        map.put("summaryVenueVisDateByDate",summaryVenueVisDateByDate);
        map.put("summaryVenueVisCountByDate",summaryVenueVisCountByDate);
        map.put("summaryTodayVistors",summaryTodayVistors);
        map.put("latestOrderList",latestOrderList);
        System.out.println(map);
        return map;
    }

    /**
     * 一次把venue页面所需所有参数以json形式返回
     * @return query_key,currentPage,currentCount,
     * totalCount,totalPage,list
     * 使用post是因为要传递json
     */
    @PostMapping("/getVenue")
    public Map<String,Object> getVenue(@RequestBody Map<String,String> reqmap){
        Map<String,Object> map = new HashMap<>();
        int currentPage = 1;
        int currentCount = 5;
        String currentPageStr = reqmap.get("currentPage");
        String currentCountStr = reqmap.get("currentCount");
        if(currentPageStr!=null&&currentPageStr.trim().equals("")==false) currentPage = Integer.parseInt(currentPageStr);
        if(currentCountStr!=null&&currentCountStr.trim().equals("")==false) currentCount = Integer.parseInt(currentCountStr);
        String query_key = reqmap.get("query_key");
        if(query_key==null) query_key="";
        PageBean<Gms_Venue> pageBean_venue = adminService.queryVenueByKey(currentPage, currentCount, query_key);
        map.put("query_key", query_key);
        map.put("currentPage",pageBean_venue.getCurrentPage());
        map.put("currentCount",pageBean_venue.getCurrentCount());
        map.put("totalCount",pageBean_venue.getTotalCount());
        map.put("totalPage",pageBean_venue.getTotalPage());
        map.put("list",pageBean_venue.getList());
        return map;
    }

    /**
     * 添加Venue
     * @return
     */
    @PostMapping("/addVenue")
    public String addVenue(@RequestBody Gms_Venue gms_venue){
        gms_venue.setVenue_Id(UUID.randomUUID().toString().replace("-", ""));
        adminService.addVenue(gms_venue);
        return "{}";
    }
    /**
     * 修改Venue
     * @return
     */
    @PostMapping("/updateVenue")
    public String updateVenue(@RequestBody Gms_Venue gms_venue){
        adminService.updateVenue(gms_venue);
        return "{}";
    }
    /**
     * 删除Venue
     * @return
     */
    @PostMapping("/delVenue")
    public String delVenue(@RequestBody Gms_Venue gms_venue){
        adminService.delVenue(gms_venue);
        return "{}";
    }

    /**
     * 一次把order页面所需所有参数以json形式返回
     * @return query_key,currentPage,currentCount,
     * totalCount,totalPage,sortStatus,list
     * 使用post是因为要传递json
     */
    @PostMapping("/getOrder")
    public Map<String,Object> getOrder(@RequestBody Map<String,String> reqmap){
        Map<String,Object> map = new HashMap<>();
        int currentPage = 1;
        int currentCount = 5;
        String currentPageStr = reqmap.get("currentPage");
        String currentCountStr = reqmap.get("currentCount");
        if(currentPageStr!=null&&currentPageStr.trim().equals("")==false) currentPage = Integer.parseInt(currentPageStr);
        if(currentCountStr!=null&&currentCountStr.trim().equals("")==false) currentCount = Integer.parseInt(currentCountStr);
        String query_key = reqmap.get("query_key");
        if(query_key==null) query_key="";

        //分页数据处理
        PageBean<Gms_Order> pageBean_order = new PageBean<Gms_Order>();
        pageBean_order.setCurrentPage(currentPage);
        pageBean_order.setCurrentCount(currentCount);
        int totalCount = adminService.getOrderTotalCountByKeyQuery(query_key);
        pageBean_order.setTotalCount(totalCount);
        int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
        pageBean_order.setTotalPage(totalPage);
        int index = (currentPage-1)*currentCount;
        String sortStatus = reqmap.get("sortStatus");
        if(sortStatus==null) sortStatus="7";
        pageBean_order.setList(adminService.queryOrderByKey(index, currentCount, query_key, sortStatus));
        map.put("sortStatus",sortStatus);
        map.put("query_key", query_key);
        map.put("currentPage",pageBean_order.getCurrentPage());
        map.put("currentCount",pageBean_order.getCurrentCount());
        map.put("totalCount",pageBean_order.getTotalCount());
        map.put("totalPage",pageBean_order.getTotalPage());
        map.put("list",pageBean_order.getList());
        return map;
    }

    /**
     * 一次把notice页面所需所有参数以json形式返回
     * @return query_key,currentPage,currentCount,
     * totalCount,totalPage,list
     * 使用post是因为要传递json
     */
    @PostMapping("/getNotice")
    public Map<String,Object> getNotice(@RequestBody Map<String,String> reqmap){
        Map<String,Object> map = new HashMap<>();
        int currentPage = 1;
        int currentCount = 5;
        String currentPageStr = reqmap.get("currentPage");
        String currentCountStr = reqmap.get("currentCount");
        if(currentPageStr!=null&&currentPageStr.trim().equals("")==false) currentPage = Integer.parseInt(currentPageStr);
        if(currentCountStr!=null&&currentCountStr.trim().equals("")==false) currentCount = Integer.parseInt(currentCountStr);
        String query_key = reqmap.get("query_key");
        if(query_key==null) query_key="";
        PageBean<Gms_Notice> pageBean_notice = adminService.queryNoticeByKey(currentPage, currentCount, query_key);
        map.put("query_key", query_key);
        map.put("currentPage",pageBean_notice.getCurrentPage());
        map.put("currentCount",pageBean_notice.getCurrentCount());
        map.put("totalCount",pageBean_notice.getTotalCount());
        map.put("totalPage",pageBean_notice.getTotalPage());
        map.put("list",pageBean_notice.getList());
        return map;
    }
    /**
     * 添加notice
     * @return
     */
    @PostMapping("/addNotice")
    public String addNotice(@RequestBody Gms_Notice gms_notice){
        gms_notice.setNotice_Id(UUID.randomUUID().toString().replace("-", ""));
        adminService.addNotice(gms_notice);
        return "{}";
    }
    /**
     * 修改notice
     * @return
     */
    @PostMapping("/updateNotice")
    public String updateNotice(@RequestBody Gms_Notice gms_notice){
        adminService.updateNotice(gms_notice);
        return "{}";
    }
    /**
     * 删除notice
     * @return
     */
    @PostMapping("/delNotice")
    public String delNotice(@RequestBody Gms_Notice gms_notice){
        adminService.delNotice(gms_notice);
        return "{}";
    }

    /**
     * 一次把analytics页面所需所有参数以json形式返回
     * @return date_st,date_ed,category,list
     * 使用post是因为要传递json
     */
    @PostMapping("/getAnalytics")
    public Map<String,Object> getAnalytics(@RequestBody Map<String,String> reqmap){
        Map<String,Object> map = new HashMap<>();
        List<Gms_Bck_analytics> analyticslist = null;
        //日期处理
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentdateString = formatter.format(new Date());
        String date_st = reqmap.get("date_st");
        if(date_st==null||date_st.trim().equals("")) date_st=currentdateString+" 10:00";
        String date_ed = reqmap.get("date_ed");
        if(date_ed==null||date_ed.trim().equals("")) date_ed=currentdateString+" 22:00";
        String category = reqmap.get("category");
        if("venue".equals(category)) analyticslist = adminService.getAnalyticsByDateAndVen(date_st, date_ed);
        else analyticslist = adminService.getAnalyticsByDateAndCat(date_st, date_ed);
        map.put("date_st", date_st);
        map.put("date_ed",date_ed);
        map.put("category",category);
        map.put("list",analyticslist);
        return map;
    }

    /**
     * 导出venue表单
     */
    @GetMapping("/exportVenue")
    public void exportVenue(HttpServletResponse response) throws IOException {
        Map<String, String> title = new HashMap(); // 表头
        List<Map<String, Object>> data = new ArrayList(); // 需要导出的数据
        Map<String, Integer> position = new HashMap(); // 表头字段对应的位置（自定义位置）
        List<Gms_Venue> vList = adminService.queryAllVenue();

        // 设置表头字段位置
        position.put("venue_Name", 0);
        position.put("venue_Type", 1);
        position.put("venue_Capacity", 2);
        position.put("venue_Price", 3);
        position.put("venue_Open", 4);
        position.put("venue_Close", 5);

        // 设置表头信息
        title.put("venue_Name", "场馆名字");
        title.put("venue_Type", "场馆类别");
        title.put("venue_Capacity", "接待能力");
        title.put("venue_Price", "场馆价格");
        title.put("venue_Open", "开放时间");
        title.put("venue_Close", "关闭时间");

        Map<String, Object> venueMap = null;

        for (Gms_Venue venue : vList) {
            venueMap = new HashMap();
            venueMap.put("venue_Name", venue.getVenue_Name());
            venueMap.put("venue_Type", venue.getVenue_Type());
            venueMap.put("venue_Capacity", venue.getVenue_Capacity());
            venueMap.put("venue_Price", venue.getVenue_Price().toString());
            venueMap.put("venue_Open", venue.getVenue_Open()+":00");
            venueMap.put("venue_Close", venue.getVenue_Close()+":00");
            data.add(venueMap);     // 将userMap添加到List集合中
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String date = df.format(new java.util.Date());
        String excelName = "场地列表-" + date + ".xlsx";
        String sheetName = "场地列表数据";
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + excelName);
        response.setContentType("application/x-download");
        // 调用写好的工具类的导出数据方法   传入对应的参数
        ExportExcelUtils.exportDataToExcel(title, position, data, sheetName, response.getOutputStream());
    }
    /**
     * 导出order表单
     */
    @GetMapping("/exportOrder")
    public void exportOrder(HttpServletResponse response) throws IOException {
        Map<String, String> title = new HashMap(); // 表头
        List<Map<String, Object>> data = new ArrayList(); // 需要导出的数据
        Map<String, Integer> position = new HashMap(); // 表头字段对应的位置（自定义位置）
        List<Gms_Order> orderlist = adminService.queryOrderByKey(0,9999999,"","7");

        // 设置表头字段位置
        position.put("Venue_Name", 0);
        position.put("Venue_Type", 1);
        position.put("User_Id", 2);
        position.put("Order_Date", 3);
        position.put("Order_St", 4);
        position.put("Order_Ed", 5);
        position.put("Order_Price", 6);
        position.put("Order_Mktime", 7);
        position.put("Order_State", 8);

        // 设置表头信息
        title.put("Venue_Name", "预约场地");
        title.put("Venue_Type", "场地类别");
        title.put("User_Id", "预约人");
        title.put("Order_Date", "预约日期");
        title.put("Order_St", "开始时间");
        title.put("Order_Ed", "结束时间");
        title.put("Order_Price", "预约价格");
        title.put("Order_Mktime", "预约生成时间");
        title.put("Order_State", "预约状态");

        Map<String, Object> orderMap = null;

        for (Gms_Order order : orderlist) {
            orderMap = new HashMap();
            orderMap.put("Venue_Name", order.getOrder_Venue().getVenue_Name());
            orderMap.put("Venue_Type", order.getOrder_Venue().getVenue_Type());
            orderMap.put("User_Id", order.getOrder_User().getUser_Id());
            orderMap.put("Order_Date", order.getOrder_Date());
            orderMap.put("Order_St", order.getOrder_St()+":00");
            orderMap.put("Order_Ed", order.getOrder_Ed()+":00");
            orderMap.put("Order_Price", order.getOrder_Price());
            orderMap.put("Order_Mktime", order.getOrder_Mktime());
            String Order_State = "";
            if(order.getOrder_State()==0) Order_State="进行中";
            else if(order.getOrder_State()==1) Order_State="已取消";
            else if(order.getOrder_State()==2) Order_State="已成功";
            orderMap.put("Order_State", Order_State);
            data.add(orderMap);     // 将userMap添加到List集合中
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String date = df.format(new Date());
        String excelName = "订单列表-" + date + ".xlsx";
        String sheetName = "订单列表数据";
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + excelName);
        response.setContentType("application/x-download");
        // 调用写好的工具类的导出数据方法   传入对应的参数
        ExportExcelUtils.exportDataToExcel(title, position, data, sheetName, response.getOutputStream());
    }
    /**
     * 导出notice表单
     */
    @GetMapping("/exportNotice")
    public void exportNotice(HttpServletResponse response) throws IOException {
        Map<String, String> title = new HashMap(); // 表头
        List<Map<String, Object>> data = new ArrayList(); // 需要导出的数据
        Map<String, Integer> position = new HashMap(); // 表头字段对应的位置（自定义位置）

        List<Gms_Notice> nList = adminService.queryAllNotice();

        // 设置表头字段位置
        position.put("notice_Man", 0);
        position.put("notice_Title", 1);
        position.put("notice_Content", 2);
        position.put("notice_Time", 3);

        // 设置表头信息
        title.put("notice_Man", "通知人");
        title.put("notice_Title", "通知标题");
        title.put("notice_Content", "通知内容");
        title.put("notice_Time", "通知日期");

        Map<String, Object> noticeMap = null;

        for (Gms_Notice notice : nList) {
            noticeMap = new HashMap();
            noticeMap.put("notice_Man", notice.getNotice_Man());
            noticeMap.put("notice_Title", notice.getNotice_Title());
            noticeMap.put("notice_Content", notice.getNotice_Content());
            noticeMap.put("notice_Time", notice.getNotice_Time());
            data.add(noticeMap);     // 将userMap添加到List集合中
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String date = df.format(new java.util.Date());
        String excelName = "通知清单-" + date + ".xlsx";
        String sheetName = "通知清单数据";
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + excelName);
        response.setContentType("application/x-download");
        // 调用写好的工具类的导出数据方法   传入对应的参数
        ExportExcelUtils.exportDataToExcel(title, position, data, sheetName, response.getOutputStream());
    }
}
