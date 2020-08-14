[要求描述](:https://docs.qq.com/doc/DVGNxb2pIVW5xQW1t)

## 前端

**注意：** 1.需要在idea中安装lombok插件 2. 没有装rabbitMq可以将代码中异步发送邮件的地方改为同步发送或者直接注释。

### 改造方案

1. 搭建环境把以前的项目跑起来

2. 改造想法：

   [改造思路](https://blog.csdn.net/qq_34037264/article/details/102837523)

   - 首先登陆界面的改造，ajax异步请求登录
   - 取消中间的订单页的界面，直接改在前台最后，去预约的地方改为马上预约
   - 所有操作都在首页进行，当我们需要点击个人中心的时候只是跳转到个人中心，进入个人中心时ajax加载所有的信息。
   - 通知界面暂时没想好怎样改

3. 改造首页：

   - 取消日期左侧的`去预约`按钮，将`确认预约`按钮和点击场馆获取得到的数据就展示在页尾

   - 删除订单详情界面，直接将点击场馆获取到的信息传到最下面的表单里

   - 用vue接受session的数据，调用后端接口获取session，后期改为security

     **注意：**1. vue页面加载的时候需要知道session里面是否有user，要在生命周期函数里面调用ajax获取user

     ​			2. 用vue写判断的时候不要写user_Id !=null   默认user = “” 写成user_Id !=“” 来判断

   - 点击`登录`按钮：跳转到首页会出现地址访问错误!!! [参考修改](https://www.jianshu.com/p/bd7a821515ec)  页面去掉.html后缀改造以及url映射 

   - 其他按钮点击都是通过上面方法改造

4. 改造个人中心

   - 将页面的通知消息以及下面的首页->个人中心取消，个人中心改为用户设置
   - 通过vue的mounted函数调用ajax获取用户id，后面继续调用页面加载函数进行加载页面
   - notice传递参数问题，通过html跳转页面传递参数，notice页面在页面加载的时候获取参数，并初始化vue。这个拼串恶心人一套一套的

5. 改造登录

   * 本想加验证啊，但是要改页面，太麻烦

### 待解决问题：

* [x] 登录界面返回的数据是字符换，改成security后修改
* [x] 每次获取用户都是在session中，可以从security中获取
* [x] 多个地方toast消息无法显示并且失败toast信息一致停留不消失
* [x] 订单成功后不会显示信息，而是直接跳转个人中心
* [x] 取消订单后没有第一时间刷新界面，导致信息展示不及时
* [x] 修改密码(这里暂时还没有加密)
* [x] notice页面显示解决
* [x] 用户注册



## 后端

### 登录

* 参考文章：较多，主要看这个博主   [这个是其中一篇](https://blog.csdn.net/yuanlaijike/article/details/84638745)
* security默认的登录数据是通过key/value的形式来传递的，不支持JSON格式的登录数据，改为json传递  [参考](https://blog.csdn.net/qq_33709508/article/details/104409200)
* 加security权限校验，从security里获取用户信息
* 记住我：没有实现
* 注册已补邮件消息丢失  [参考](https://blog.csdn.net/weixin_38087443/article/details/100167717)

### 首页

- 通过@RequestBody Gms_Order order封装的时候，如果有数据封装不进去  [参考链接](https://blog.csdn.net/u012190514/article/details/81913973)

  - 使用@RequestBody Map<String,Object> 来接收 (没有办法的办法，很不建议)
  - **使用URL拼接参数的方式（POST方法也可以拼接）@RequestParam** 
  - 继承ReqParam再创建一个类，把这两个字段放进去(麻烦)

- **时间转换问题：这个真的整吐了** ：json获取到的时间格式为CST 但是JS用的是GMT，导致我前端首页显示的时候日期总是提前了一天。在entity日期参数上面加上@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", **timezone = “GMT+08”**);  总之还是时区问题[参考](https://blog.csdn.net/xiaohoulove_/article/details/59484630)

- mybatis映射出错，可以设置别名解决：[参考](https://blog.csdn.net/baidu_35975930/article/details/80014087)

- 订单生成成功发送邮件 **写入订单和修改场馆状态同时执行，我们加事务保证** [参考](https://blog.csdn.net/aiming66/article/details/88376115)

  - 封装order: 雪花算法生成订单id  [参考](https://blog.csdn.net/nsxqf/article/details/85850232)
  - 生成订单: 1. 判断当前时间和场馆的订单状态是否为空或者是否已被预约，如果都不是，加锁。2. 进入锁，生成订单，根据返回值判断是否订单生成成功，如果成功，则先更改数据库当前状态为已被预约，同时发送邮件通知。
  - **注意：** 1.上面的返回值res接收后，更改数据库状态的代码还是必须放在锁里面执行，因为可能当前线程A执行完后，虽然线程A预约成功，但是当A释放锁后，我们的数据库状态并没有改变，这个时候其他阻塞的线程拿到锁后判断当前场馆状态是可预约的，又进行插入订单，导致多个用户下单成功，就会导致线程安全问题。2. 但是我们的邮件发送就可以放到锁后面，因为如果可以执行到这一步，表示当前用户订单生成成功。
  - 邮件：先通过当前order的id去获取订单，根据订单生成MailDto对象，传入mailService中，设置mailService是因为注册也需要邮件服务，抽取。[邮件配置](https://www.jianshu.com/p/a7097a21b42d)
  - 从application.propertise文件中获取数据出现乱码：idea设置即可 [参考](https://blog.csdn.net/zhw0596/article/details/86166462)
  - 使用jmeter压力测试：先把邮件port改为25测试,465是加密的ssl。第一次直接失败，100个用户同时请求生成100个订单，失败，添加双重检验锁继续测试，只生成一个订单！！！

- jmeter设置：[参考1](https://blog.csdn.net/Beat_Boxer/article/details/86497316?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase)  [参考2](https://blog.csdn.net/chenjie456789/article/details/94379393)

- 测试的时候真的傻逼：自己把场馆状态改了，这个订单已经生成过了，但是自己又去测试，发现一直测试失败，怀疑代码出问题。

- **出现问题：**  由于加了锁，事务，以及邮件发送，导致首页订单速度超过十秒，当把邮件取消发送后，速度变正常，所以可以通过消息队列来发送邮件，这里使用rabbitmq  [只参考邮件部分](https://blog.csdn.net/u013871100/article/details/82982235)   ， convertAndSend方法封装对象进入消息队列  [参考](https://www.jianshu.com/p/e647758a7c50)

- 插曲：上面并发判断订单是否为空或者为已被预约的时候，返回值是Integer，但是如果多个人都是预约了此场馆，并且取消了预约，就有多个订单的状态为已取消，就会导致mysql报错，

  ```
  org.apache.ibatis.exceptions.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), but found: 2
  ```
  
  在sql中查询时排除已经取消预约的订单  但是mybatis中的不等号需要转义：将`<>` 转为 `&lt;&gt; ` [参考](https://blog.csdn.net/lin252552/article/details/80895301)
  
  

### 个人中心

* 在mounted里面执行两个ajax时，由于后面的函数调用了vue实例，因为在mounted函数执行的时候，vue实例并没有创建完成，所以会报错，详细看vue的生命周期 [参考](https://www.jb51.net/article/149662.htm)  我们可以通过第一个ajax返回函数中调用第二个。

* vue给对象绑定属性的时候要使用 **$set** [参考链接](https://bbs.csdn.net/topics/393404643)

* 整吐了，改了半天，就差一步  totalPage始终是默认值，我初始化vue的时候不对，虽然在vue的mounted可以完成加载工作，但是vue先实例化了(即我刚开始的时候给了默认值为空串)，页面获取的值比ajax返回的值要早，导致totalPage始终为空，所以判断页数是否到了最后一页就一直失败。jquery加载lode的时候直接请求ajax获取值再初始化vue并赋值，而不是给个空串。

* mybatis模糊查询报错 加`binary`  [参考](https://blog.csdn.net/weixin_40357412/article/details/101761821)

* 取消订单：发送ajax请求即可，**修改订单和修改场馆状态同时执行，加事务保证**

* 导出数据，用户信息，修改密码等，无

  





