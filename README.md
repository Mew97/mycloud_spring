# mycloud_spring
私有云实现 后端  
前端地址：[https://github.com/Mew97/-mycloud_web](https://github.com/Mew97/-mycloud_web) 

## 目前实现功能：
* 用户注册（⼿手机验证码注册），登陆，密码找回（通过⼿手机验证码），在线人数（活跃）
* 文件管理：
* ⽂件上传（断点，秒传），下载，回收站

## 服务框架介绍： 
- 前端：vue
- 后台：Spring boot
- 数据库：mysql，redis 存储服务：fastdfs

## 功能说明：

页面拦截（登陆保护），api拦截（token验证，uuid），活跃用户（redis，ttl，例例：10分钟不不进⾏行行任何操作的⽤用户将被强制下线）js表单验证
vue-uploader，分⽚片校验上传（分⽚片信 息保存在redies，上传时实时检验，若多个⽤用户同时上传同⼀一个⽂文件，不不考虑服务器器带宽，上传时间将成倍缩短）
跨域处理，⼿机验证码验证
回收站（⽤用户删除⽂文件将进⼊入回收站， 回收站内⽂文件可恢复，若超过30天，后台将⾃自动永久删除⽂文件，如果⽤用户上传的⽂文件在回收站中已存在，上传时将⾃自动从 回收站移到⽂文件列列表）
下载（直接让客户端从存储服务器点对点下载⽂文件（nginx代理理）与服务器器之间只保持少量量通信，将极⼤大减轻服务器器压力  

登陆页面  
<img src="https://github.com/Mew97/resources/blob/master/登陆.png" width=100% />

上传页面
<img src="https://github.com/Mew97/resources/blob/master/%E4%B8%8A%E4%BC%A0%E9%A1%B5%E9%9D%A2.png" width=100% />

文件列表
<img src="https://github.com/Mew97/resources/blob/master/%E6%96%87%E4%BB%B6%E5%88%97%E8%A1%A8.png" width=100% />


