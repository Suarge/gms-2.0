# Stadium-management-system---体育馆场地管理系统-2.0

## 项目说明

该项目为体育馆场地管理系统2.0版本，使用SpringBoot+Vue搭建，MyBatis+RabbitMq+Spring Security+锁+邮件服务+定时任务等技术。

* 该项目在1.0的基础上进行的改进，前后端采用json数据格式交互，实现前后端分离
* 并不是完全的前后端分离，只是用了vue做模板引擎，jquery+vue
* 该项目使用了Thymeleaf，只是为了跳转更方便
* 该项目分为两个部分，都是对上个版本的重构，使用了两个端口，只是操作的同一个数据库
* 这里订单ID生成使用了雪花算法，uuid也行

## 项目演示地址

该项目暂时没有上传到服务器上

## 项目详细说明介绍

https://blog.csdn.net/weixin_43786818/article/details/107980802

## 1.0版本地址

https://github.com/Suarge/gms-1.0

---

## 项目截图

>前台用户部分
![image](https://github.com/Suarge/gms-2.0/blob/master/readm_photo/1.png)
![image](https://github.com/Suarge/gms-2.0/blob/master/readm_photo/2.png)
![image](https://github.com/Suarge/gms-2.0/blob/master/readm_photo/3.png)
![image](https://github.com/Suarge/gms-2.0/blob/master/readm_photo/4.png)

>后台管理员部分
![image](https://github.com/Suarge/gms-1.0/blob/master/readme_photo/6.png)
![image](https://github.com/Suarge/gms-1.0/blob/master/readme_photo/7.png)
![image](https://github.com/Suarge/gms-1.0/blob/master/readme_photo/8.png)

## 功能

用户部分：

* 登录、注册、修改密码、注销登录
* 首页各种加载、场馆类型展示、通知信息展示、场馆详情展示
* 模糊查询订单
* 打印订单

新增：

* 异步邮件发送
* 事务控制
* 双重检验锁控制线程安全
* 异步邮件发送

管理员部分：

* 登录、注销
* 总览
* 场地管理
* 预约查询
* 通知发布
* 情况分析

## 技术栈

* SpringBoot+Vue搭建
* MyBatis+mysql+durid
* 消息队列RabbitMq
* 定时任务
* Spring Security
* 线程同步锁
* apache的poi文件导出api

## 改进

* 由于前后端分离，项目加载速度快了很多，如果页面加载增加redis作为缓存会更好
* 使用了更好地框架进行搭建，更方便简单
* 项目的主要业务逻辑增加，主要是加锁和异步邮件，以及事务控制的问题(这里都在一个代码里面)
* 主要的改造思路在项目中的**改造思路**文件夹中


## 安装

### 1、下载项目到本地

```xml
git clone https://github.com/Suarge/gms-2.0.git
```

### 2. 导入项目

该项目是用idea创建的，可以直接导入到idea中

### 3.设置durid连接数据库

* 将`gms.sql`中的sql文件运行，并修改对应的配置文件durid

* 具体设置在`application.properties`里修改，如果出现乱码请修改编码

### 4.修改application.properties

* 在`application.properties`配置文件中修改成自己的qq和stmp的密码
* 其他信息可以根据自己需要修改

### 5.下载rabbitmq消息队列

我使用的是**rabbitmq3.6.9** 和**erlang8.3**直接在windows上安装的

### 4.启动项目

* 因为前端是ajax访问的，所以日期不对的话下面的场馆表格是不会加载的，gms.sql的日期是2020-8-13，你有两种办法解决这个问题
  1. 通过修改`index.html`里面的日期即可，具体位置在js代码的第一个部分，注释掉获取当天日期的函数，手动设置三个参数，
     var nowday = "2020-8-13";
       var nextday = "2020-8-14";
       var nextnextday = "2020-8-15";
  2. 项目中有一个cpp文件，该文件打开后可以根据你设置的日期，生成sql语句，前台只显示3天的数据，但是由于管理员部分的数据分析需要，默认会生成4天的sql，比如今天是2020-11-13，那你在cpp文件中将变量设置为 prdate = 12 提前一天
* 这样操作后你就可以成功运行了

## 最后

有问题的话可以邮件联系：1274334685@qq.com
