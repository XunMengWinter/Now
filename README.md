# Now
一款Android图文精选app，通过抓取网页获得图文列表。目前包含站酷（Zcool）精选、国家地理（National Geographic）每日一图、知乎日报、豆瓣一刻（Moment）,详情页用webview显示，带QQ、微信等分享方式。
视图基于 https://github.com/florent37/MaterialViewPager 

Download NowView 1.0 in GooglePlay: https://play.google.com/store/apps/details?id=top.wefor.nowview

效果图
![alt tag](https://raw.githubusercontent.com/XunMengWinter/Now/3f391cd9d02152a6e8dca4d9b5ac6dbc453dbadc/images/now_introduce.jpg)

Changes
相比第一个版本添加了版块删选、图片源选择及新增 National Geographic 版块，基本通过SharedPreferences来实现。

不懂后台，只能用Jsoup从网页抓取数据。本来也想添加500px，Pinterst等图文，结果Jsoup无法抓取其网页，后续只能通过api来获得了。啊，api获取还得申请key，填一大堆资料，走些乱七八糟的流程，瞬间没了动力。

万年不弄PS，所以界面尽量在间距布局上做文章，图标杂线基本能省的都省了。CardView放图片无法铺满边界有点不好。Other页面的交互事件几乎全部用Dialog来处理，Dialog.setView()感觉蛮好用的。

对Glide的缓存机制不大了解，所以只通过文件路径的方式存了一张封面图。

总结：Now基本用第三方库拼凑而成（再次感谢开源），主要在界面与用户交互方面加了点自己的见解，最后献上compile.
    // forgive me too lazy to introduce
    
    compile 'com.android.support:appcompat-v7:23.0.1'
    
    compile 'com.android.support:recyclerview-v7:23.0.1'
    
    compile 'com.android.support:cardview-v7:23.0.1'
    
    compile 'com.android.support:support-v4:23.0.1'
    
    compile 'com.google.code.gson:gson:2.3'
    
    compile 'com.race604.flyrefresh:library:2.0.0'
    
    compile 'com.jakewharton:butterknife:7.0.1'
    
    compile('com.github.florent37:materialviewpager:1.1.2@aar') {
        transitive = true
    }
    
    compile 'com.orhanobut:logger:1.10'
    
    compile 'com.github.bumptech.glide:glide:3.6.1'
    
    compile 'com.makeramen:roundedimageview:2.2.1'
    
    compile 'com.loopj.android:android-async-http:1.4.8'
    
    compile 'org.apache.httpcomponents:httpcore:4.4.2'
    
    compile files('libs/jsoup-1.8.3.jar')
    
    compile 'com.alibaba:fastjson:1.2.7'
    
    compile 'com.umeng.analytics:analytics:latest.integration'
    
    compile files('libs/umeng-analytics-v5.6.1.jar')
    
    compile files('libs/umeng-update-v2.6.0.1.jar')
    
    compile files('libs/open_sdk_r5276.jar')
    
    compile files('libs/libammsdk.jar')
    
    compile 'com.github.jkwiecien:EasyImage:1.0.9'
    
    compile 'com.wang.avi:library:1.0.1'
    
    compile 'com.nineoldandroids:library:2.4.0'
