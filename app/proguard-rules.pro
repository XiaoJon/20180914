#代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
#关闭压缩
-dontshrink
#关闭优化
-dontoptimize
#混淆时不使用大小写混合类名
-dontusemixedcaseclassnames
#不跳过library中的非public的类。
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
#不进行优化，建议使用此选项，因为根据proguard-android-optimize.txt中的描述，优化可能会造成一些潜在风险，不能保证在所有版本的Dalvik上都正常运行。
-dontoptimize
#不进行预校验。这个预校验是作用在Java平台上的，Android平台上不需要这项功能，去掉之后还可以加快混淆速度。
-dontpreverify
#混淆后产生映射文件
#包含有类名->混淆后类名的映射关系
-verbose
#指定混淆采用的算法，后面的参数是一个过滤器
#这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


#对注解中的参数进行保留。
-keepattributes *Annotation*
#避免混淆泛型
-keepattributes Signature
#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable


#保留我们使用的四大组件，自定义的Application等这些子类不被混淆
#因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.app.Fragment
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-keep class android.support.v4.** {*;}

-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**



#对android.support包下的代码不警告，因为兼容库的中代码是安全的
-dontwarn android.support.**

#不混淆声明的这两个类，这两个类我们基本也用不上，是接入Google原生的一些服务时使用的。
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

#不混淆keep注解
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}


#不混淆任何一个View中的setXxx()和getXxx()方法，因为属性动画需要有相应的setter和getter的方法实现，混淆了就无法工作了。
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
   public <init>(android.content.Context);
   public <init>(android.content.Context, android.util.AttributeSet);
   public <init>(android.content.Context, android.util.AttributeSet, int);
}

#不混淆任何包含native方法的类的类名以及native方法名，但当成员没有被引用时会被移除
-keepclasseswithmembernames class * {
    native <methods>;
}
#不混淆控件的实例方法
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#不混淆控件的实例方法
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#不混淆 protected 和 public
-keep public class * {
    public protected *;
}
#防止注解的方法名被混淆-Activity
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
   public boolean *(android.view.View);
   public boolean *(android.view.View, android.view.MotionEvent);
   public void *(android.view.View, boolean);
   public boolean *(android.view.View, int, android.view.KeyEvent);
   public boolean *(android.widget.AdapterView, android.view.View, int, long);
   public void *(android.widget.AdapterView, android.view.View, int , long );
   public void *(android.widget.AdapterView);
   public void *(android.widget.AdapterView, android.view.View , int , long );
}


#不混淆枚举中的values()和valueOf()方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#不混淆Parcelable实现类中的CREATOR字段，毫无疑问，CREATOR字段是绝对不能改变的，包括大小写都不能变，不然整个Parcelable工作机制都会失败。
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}
# 不混淆Serializable实现类
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#不混淆R文件中的所有静态字段，我们都知道R文件是通过字段来记录每个资源的id的，字段名要是被混淆了，id也就找不着了。
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}


#---------------------   基础混淆 end  -----------------------#







#---------------------   本地混淆 start  -----------------------#
#实体类
-keep class com.hjhq.teamface.bean.** { *; }
-keep class com.hjhq.teamface.basis.database.gen.** { *; }
-keep class com.hjhq.teamface.basis.zygote.** { *; }
#工具类
-keep class com.hjhq.teamface.**.utils.*{*;}
-keep class com.hjhq.teamface.**.util.*{*;}
#不能混淆指定包下的类
-keep public class com.hjhq.teamface.**.bean.**{*;}
#JavascriptInterface不混淆
-keep public class com.hjhq.teamface.email.presenter.NewEmailActivity
-keep public class com.hjhq.teamface.email.view.EmailDetailDelegate
-keep public class com.hjhq.teamface.email.view.EmailDetailDelegate
-keep public class com.hjhq.teamface.filelib.activity.SharedFileActivity
-keep public class com.hjhq.teamface.common.view.TextWebView
-keep public class com.hjhq.teamface.wxapi.WXEntryActivity
#自定义组件不混淆
-keep public class com.hjhq.teamface.customcomponent.widget2.**
-keep public class com.hjhq.teamface.customcomponent.widget2.**{*;}
#自定义View不混淆
-keep public class com.hjhq.teamface.view.**
-keep public class com.hjhq.teamface.view.**{*;}
-keep public class com.hjhq.teamface.**.view.**
-keep public class com.hjhq.teamface.**.view.**{*;}
-keep public class com.hjhq.teamface.**.widget.**
-keep public class com.hjhq.teamface.**.widget.**{*;}
#接口回调类
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
    void *(**On*);
}
#WebView相关
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
# 删除代码中Log相关的代码
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}








#---------------------   本地混淆 end  -----------------------#



#---------------------   第三方混淆 start  -----------------------#

#百度

-keep class com.baidu.** { *; }
-keep class com.baidu.**
-keep class  vi.com.gdi.bgl.android.**{*;}
-keep class  vi.com.gdi.bgl.android.**
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
#umeng
-keep class com.umeng.analytics.**{*;}
-keep class com.umeng.analytics.**
-keep class u.aly.** {*;}
-keep class u.aly.**

#fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
-keep class com.alibaba.fastjson.**

-keep class org.glassfish.**{*;}
-keep class org.glassfish.**

-keep class javax.**{*;}
-keep class javax.**

-keep class java.**{*;}
-keep class java.**

-keep class com.sun.**{*;}
-keep class com.sun.**

-keep class net.**{*;}
-keep class net.**

#freemarker
-dontwarn freemarker.**
-keep class javax.swing.**{*;}
-keep class javax.swing.**
-keep class freemarker.core.**{*;}
-keep class freemarker.core.**
-keep class freemarker.debug.**{*;}
-keep class freemarker.debug.**
-keep class freemarker.ext.**{*;}
-keep class freemarker.ext.**
#-dontwarn  freemarker.core.**

#腾讯
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}
-keep class com.tencent.**
#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#得到
-dontwarn com.luojilab.**
-keep class com.luojilab.**{*;}
-keep class com.luojilab.**

#greendao
-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.**{*;}
-keep class org.greenrobot.greendao.**

#web交互
-keepclassmembers class com.hjhq.teamface.feature.note.utils.JavaScriptInterface {
  public *;
}
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*


#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}




-dontwarn java.lang.invoke.*
-dontwarn org.apache.lang.**
-dontwarn org.apache.commons.**
-dontwarn com.com.nostra13.**
-dontwarn com.github.**
-dontwarn com.squareup.**




#retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-keep class com.life.me.entity.postentity{*;}
-keep class com.life.me.entity.resultentity{*;}
-dontwarn retrofit.
-keep class retrofit. { *; }
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}



#RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.** { *; }
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


#BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers public class * extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(android.view.View);
}


#okhttp
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**


# Gson specific classes
-keep public class com.google.gson.** {*;}
-keep class com.google.gson.stream.**{ *; }
-keep class com.google.protobuf.** {*;}
-keep class sun.misc.Unsafe { *; }


# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

 # 得到组件化
 -keep interface * {
   <methods>;
 }
 -keep class com.luojilab.component.componentlib.** {*;}
 -keep class com.luojilab.router.** {*;}
 -keep class com.luojilab.gen.** {*;}
 -keep class * implements com.luojilab.component.componentlib.router.ISyringe {*;}
 -keep class * implements com.luojilab.component.componentlib.applicationlike.IApplicationLike {*;}
