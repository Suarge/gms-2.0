package gms.cuit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 通过该controller,返回请求的页面
 */
@Controller
public class ThymeleafController {
    //空需要特殊处理,不然路径有问题
    @RequestMapping("")
    public ModelAndView emptyLogin() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:admin/login");
        return mv;
    }
    @RequestMapping("/admin/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/admin/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/admin/summary")
    public String summary(){
        return "summary";
    }
    @RequestMapping("/admin/venue")
    public String venue(){
        return "venue";
    }
    @RequestMapping("/admin/order")
    public String order(){
        return "order";
    }
    @RequestMapping("/admin/notice")
    public String notice(){
        return "notice";
    }
    @RequestMapping("/admin/analytics")
    public String analytics(){
        return "analytics";
    }
}
