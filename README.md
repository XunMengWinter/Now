# NowView 3.1
一款Android图文精选app，通过抓取网页获得图文列表。目前包含站酷（Zcool）精选、国家地理（National Geographic）每日一图、知乎日报、豆瓣一刻（Moment）。

![NowView](https://raw.githubusercontent.com/XunMengWinter/source/master/gif/NowView.gif)

[Download Apk](https://raw.githubusercontent.com/XunMengWinter/source/master/apk/NowView.apk)

[Web App](http://www.wefor.top/now)

### 更新日志
NowView 2.0 -> 3.1
* RxJava升级为RxJava2，优化主页数据获取逻辑；
* Realm升级，兼容先前数据；
* 优化并加强共享元素动画；
* 大图模式下长按屏幕可保存图片，统一使用Glide加载与获取图片；
* gank.io页面优化，获取10日内最近一天的数据；
* 升级部分第三方库，去除部分sdk。

> Where is 3.0 ？
> NowView 3.0 升级Realm后未能兼容之前数据，执行了Realm数据表重置操作...嗯，一句try catch引发的结果...遂重新排坑打包。


### 2.0 更新日志
* 将Other移至左侧Drawer；
* 使用RxBinding 处理点击事件；
* 将App中所用到的SharedPreferences整个成一个帮助类；
* 顶部图片选择逻辑完善；
* 包结构改动。

### NowView 1.0+ 历史
[See the old banch: 1.0+](https://github.com/XunMengWinter/Now/tree/master)

