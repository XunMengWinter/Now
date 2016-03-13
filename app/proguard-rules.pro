# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ice/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#友盟
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class [top.wefor.now].R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#微信
-keep class com.tencent.mm.sdk.** {*;}

#-libraryjars libs/fastjson.jar
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

-keepclassmembers class * {
    public <methods>;
}

#-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
    public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#OKhttp
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-keep class com.squareup.okhttp.** { *;}

#不混淆android-async-http(这里的与你用的httpClient框架决定)
-keep class com.loopj.android.http.**{*;}

# 不混淆org.apache.http.legacy.jar
 -dontwarn android.net.compatibility.**
 -dontwarn android.net.http.**
 -dontwarn com.android.internal.http.multipart.**
 -dontwarn org.apache.commons.**
 -dontwarn org.apache.http.**
 -keep class android.net.compatibility.**{*;}
 -keep class android.net.http.**{*;}
 -keep class com.android.internal.http.multipart.**{*;}
 -keep class org.apache.commons.**{*;}
 -keep class org.apache.http.**{*;}

#下面三个是SDK23打包时的warning处理
#tencent
-dontwarn com.tencent.**
#umeng
-dontwarn com.umeng.**

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}

