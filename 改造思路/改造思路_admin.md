要求描述:https://docs.qq.com/doc/DVGNxb2pIVW5xQW1t

## 前端

### 改造过程记录

1. 先搭建简易的环境跑起来,错误忽略
2. 从登录开始改造
	改造思路:
		参照 https://blog.csdn.net/qq_34037264/article/details/102837523
		点击login->异步请求
						服务器返回json标识,成功为1,失败为0并携带信息
		注意:chrome不能跨域,所以使用edge开发
3. 改造index:
		.jsp->.html
		XXXX->..
		index进去获得用户名改成异步获取
4. 改造summary:
		XXXXXX
		改造接口即可
5. 改造venue:
		XXXXXX
		初始进入ajax获取一次,点击查询再次异步提交
		修改和删除按钮放到初始ajax中作为vue的方法进行调用
		分页重构: 核心部分改成计算属性
6. 改造analytics:
		改造结果分类方式,绑定查询按钮

### 接口汇总

- bck_login:
  - doLogin接口 -> 返回status&info, 参照login.json
- bck_index:
  - getAdminName接口 -> 返回admin_Username, 参照index_admin_name.json
  - updatePassword接口 -> 啥都不返回
- bck_summary:
  - getSummary接口 -> 返回详情参照summary.json
  - 注意参数对应的bean.
- bck_venue:
  - getVenue接口
- bck_order:
  - getOrder接口
- bck_notice:
  - getNotice接口
- bck_analytics:
  - getAnalytics接口
    	

### 注意

- **使用vue的时候不要使用this.XX=XX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!**

### 待操作

- [x] 各个接口待修改
- [x] bck_index中绑定的ajax方法中的请求url
- [x] bck_index中的更改密码提交
- [x] bck_index中的各个action
- [x] 以前设计的bean有问题,很多应该是bean的命名写了id,如order中的
- [x] bck_summary改造
- [x] bck_venue中,初始请求的时候携带的参数类型需要考虑
- [x] vue数据刷新?
- [x] 数据导出单独考虑->可能使用同步
- [x] bck_order多一个sortStatus,注意!
- [x] 弹出框需要初始化! 注意初始化的顺序!!
- [x] bck_order通过计算属性来操作弹出框内容
- [x] 引用js插件需放在vue中



## 后台

### 登录改造

- spring-security解决登录部分,思路如下
  - AdminLoginSecurityConfigurationAdapter进行admin相关操作的拦截, 设置相应的登录界面,登录接口,以及成功/失败的返回json,并添加自定义的CustomAuthenticationFilter来进行json登录的处理;
  - 数据库认证的部分通过指定userService实现,具体使用的是adminservice,内部的角色部分直接返回admin, 由于我们访问的是不同的表, 所以角色只是名义上存在, 并不实际使用来区分用户权限;

### 接口统一处理

- 在AdminController上添加 @RequestMapping("/admin"), 对url做统一处理, 添加admin前缀

### 分页面进行后台改造

- login

  - 主要是登录部分, 前面已讨论

- index

  - 先改造注销登录的部分, 修改url, 设置注销登录后的方法

  - 创建getAdminName接口, 返回带有name的admin, password不传递

  - 改造修改密码部分的前端, 创建updatePassword接口

  - 注意: 前台传入json参数的时候, 需指定json头并json化, 操作模板如下

    - ```json
      $.ajax({ type: 'POST', url: "/admin/updatePassword", dataType: "json",
      data:JSON.stringify({"username":adminVue.admin_Username,"newpassword":$('#newpassword').val().trim()}), contentType : "application/json", 
      	success: function(data) { console.log(data) } });
      ```

    - 此情况针对后台要解析参数的情况!

- 嵌套iframe解决

  - <https://blog.csdn.net/yangfanj/article/details/80858831>

- summary

  - 注意点: 访问量修改?
  - [日期对应](https://blog.csdn.net/zhou520yue520/article/details/81348926) 添加注解即可
  - 奇怪点: 方法需放在初始化的位置才能被访问到

- venue

  - get接口
  - 添加
  - 修改
    - 以前没有传入id???
  - 查询时设置页数为1

- order

  - get接口
    - **天坑!!!!** ,mybatis单个字符不能用双引号 , [参考链接](https://www.cnblogs.com/grasp/p/11268049.html)

- notice

- analytics

  - 数据错位???
    - chart.js重影 , 先删除再添加 [参考](https://blog.csdn.net/qq_26906345/article/details/79497354?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase)
  - 数据显示不出来??
    - 在vue渲染完后再进行操作 [nextTick()](https://blog.csdn.net/zhouzuoluo/article/details/84752280)
    - 清除其它的active, 设置第一个
  - 点击日期修改没有反应???
    - [手动绑定修改事件](<https://blog.csdn.net/zwl18210851801/article/details/82699307?utm_source=blogxgwz6>)

## 页面去掉.html后缀改造

### 大体思路

- 配置thymeleaf前缀后缀, 并提供一个controller来作为请求页面接口, 由接口跳转到页面对应的html
- [参考链接](https://www.jianshu.com/p/bd7a821515ec)