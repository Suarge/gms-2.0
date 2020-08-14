package gms.cuit.controller;

import gms.cuit.entity.Gms_Vdstate;
import gms.cuit.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasksController {

    @Autowired
    AdminService adminService;

    /*
    * 从项目启动开始,每隔1min进行一次数据更新
    * */
    @Scheduled(fixedDelay  = 60000)
    public void updateData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");
        String currenttime = formatter.format(new Date());
        String [] str= currenttime.split(" ");
        String date = str[0];
        String hour = str[1];
        int hourr = Integer.parseInt(hour);
        System.out.println(currenttime+" 进行定时任务中...尝试检查是否有数据需要更新");
        //把超过时间的订单由进行中->已完成
        int updateOrderCount = adminService.updateOrderStateByTime(date,hour);
        List<Gms_Vdstate> queryOrderStateByTime = adminService.queryOrderStateByTime(date);
        int updateStateCount = 0;
        for(Gms_Vdstate vdstate:queryOrderStateByTime) {
            String tp = vdstate.getVdstate_St();
            //把过期且未被预定的场馆设置为不可用的状态
            for(int i=0;i<=hourr;i++) {
                if(tp.charAt(i)=='1'){
                    updateStateCount++;
                    tp = tp.replaceFirst("1", "0");
                }
            }
            vdstate.setVdstate_St(tp);
            adminService.updateVdState(vdstate);
        }
        System.out.println("本次任务成功更新订单数 : "+updateOrderCount+"条 --- 场馆预定状态数 : "+updateStateCount+"条");
    }
}
