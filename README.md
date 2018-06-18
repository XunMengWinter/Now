# NowView 4.1

一款Android图文精选app，通过抓取网页获得图文列表。目前包含站酷（Zcool）精选、国家地理（National Geographic）每日一图、MONO早午茶、知乎日报、豆瓣一刻（Moment）。

![NowView](https://raw.githubusercontent.com/XunMengWinter/source/master/gif/NowView.gif)


Get Now in [Google Play](https://play.google.com/store/apps/details?id=top.wefor.nowview)

Download release Apk in :
[GitHub(always the latest)](https://raw.githubusercontent.com/XunMengWinter/source/master/apk/NowView.apk)
[七牛云](http://p198xpq7l.bkt.clouddn.com/NowView.apk)

[Web App](http://www.wefor.top/now)

### IDEA & TODO
No wait, no ad, no mess.

想要看得更多，想要得到更快，想要发现更美。

So, view more, just Now!


[Now 设计: ](https://www.jianshu.com/p/411402059f6b)无账号、无后台、依赖最小化，不过度设计，不打扰用户。


- [x] 添加MONO模块
- [ ] 添加Dribbble模块
- [ ] Gank模块进行网络缓存
- [ ] Web详情页支持图片点击后显示大图
- [ ] 长按图片支持GIF保存
- [ ] Realm数据库实体类更改或增加支持migrate
- [ ] 支持根据字段模糊搜索本地条目，即Realm数据库查询
- [ ] 支持本地条目备份为文件&从文件生成本地条目，即Realm数据库的导出与导入
- [ ] 支持动态添加模块（用户仅需提供一个网址，即可自动生成一个图文列表）


### 4.1 更新日志
* 修复顶部图片显示的bug。

#### 4.0 更新日志
* 添加 MONO 模块！
* 图片显示优化；
* 内存泄露修复；
* 添加 Apache开源协议。


#### NowView 2.0 -> 3.1
* RxJava升级为RxJava2，优化主页数据获取逻辑；
* Realm升级，兼容先前数据；
* 优化并加强共享元素动画；
* 大图模式下长按屏幕可保存图片，统一使用Glide加载与获取图片；
* gank.io页面优化，获取10日内最近一天的数据；
* 升级部分第三方库，去除部分sdk。

> Where is 3.0 ？

> NowView 3.0 升级Realm后未能兼容之前数据，执行了Realm数据表重置操作...嗯，一句try catch引发的结果...遂重新排坑打包。


#### 2.0 更新日志
* 将Other移至左侧Drawer；
* 使用RxBinding 处理点击事件；
* 将App中所用到的SharedPreferences整个成一个帮助类；
* 顶部图片选择逻辑完善；
* 包结构改动。


## License
<pre>
Copyright [2018] [XunMengWinter]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>


---
如果你也喜欢Now，那么为ta贡献代码或灵感吧～


### NowView 1.0+ 历史
[See the old banch: 1.0+](https://github.com/XunMengWinter/Now/tree/master)
