# Now
一款Android图文精选app，通过抓取网页获得图文列表。目前包含站酷（Zcool）精选、国家地理（National Geographic）每日一图、知乎日报、豆瓣一刻（Moment）,详情页用webview显示，带QQ、微信等分享方式。
视图基于 https://github.com/florent37/MaterialViewPager 

> Download NowView 1.0 in GooglePlay: https://play.google.com/store/apps/details?id=top.wefor.nowview
> Download in BaiduYun: https://pan.baidu.com/s/1mhOfWrY

## 效果图
![alt tag](https://raw.githubusercontent.com/XunMengWinter/Now/master/images/nowview20160129.jpg)

Changes

## v1.5.1更新日志
    1.点击图片查看大图；
    2.bug修复。
    P.s 下个版本为 [NowView 2.0][https://github.com/XunMengWinter/Now/tree/now2]，代码重构。

## v1.5更新日志：
    1.去除标题栏（将ToolBar高度设置为0）；
    2.加入Realm，实现本地存储；
    3.加入Fresco，使用SimpleDraweeView替换ImageView;
    4.WebView设置缓存并在非Wifi下读取缓存；
    5.自定义Recyclerview，实现上拉加载更多与下拉刷新（通过监听onScrollStateChanged(int state),当state＝SCROLL_STATE_SETTLING时表示列表拉不动了）；
    6.Fragment代码整理。

### v1.4更新日志：
    1.加入rxAndroid；
    2.加入retrofit；
    3.代码优化。

### v1.3更新日志：
    1.Zcool模块界面由一列变成竖屏2列横屏3列；
    2.适配Zcool与Moment横屏模式;
    3.抛弃Apache Http，使用OkHttp；
    4.代码优化。
    
### v1.2更新日志：
    相比第一个版本添加了版块删选、图片源选择及新增 National Geographic 版块，基本通过SharedPreferences来实现。

### 之前日志：
［不懂后台，只能用Jsoup从网页抓取数据。本来也想添加500px，Pinterst等图文，结果Jsoup无法抓取其网页，后续只能通过api来获得了。啊，api获取还得申请key，填一大堆资料，走些乱七八糟的流程，瞬间没了动力。

万年不弄PS，所以界面尽量在间距布局上做文章，图标杂线基本能省的都省了。CardView放图片无法铺满边界有点不好。Other页面的交互事件几乎全部用Dialog来处理，Dialog.setView()感觉蛮好用的。

对Glide的缓存机制不大了解，所以只通过文件路径的方式存了一张封面图。］

总结：Now基本用第三方库拼凑而成（再次感谢开源），主要在界面与用户交互方面加了点自己的见解，最后献上compile.

    // forgive me too lazy to introduce
    
    compile 'com.jakewharton:butterknife:8.1.0'
    apt 'com.jakewharton:butterknife-compiler:8.1.0'
    compile('com.github.florent37:materialviewpager:1.1.3@aar') {
        transitive = true
    }
    compile 'com.orhanobut:logger:1.10'
    //Fresco:图片显示、加载、定制
    compile 'com.facebook.fresco:fresco:0.11.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.commit451:PhotoView:1.2.4'

    compile 'com.zhy:okhttputils:2.2.0'
    compile 'com.alibaba:fastjson:1.2.7'
    compile 'com.wang.avi:library:1.0.1'
    compile 'jp.wasabeef:recyclerview-animators:2.1.0'

    compile 'io.reactivex:rxandroid:1.1.0'
    compile 'io.reactivex:rxjava:1.1.0'
    compile 'com.squareup.retrofit2:retrofit:2.0.0-beta4'
    compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta4'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4'
