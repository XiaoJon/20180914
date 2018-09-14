# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\SDK/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-ignorewarnings
-libraryjars libs/AMap_Location_V3.6.1_20171012.jar
-libraryjars libs/AMap_Search_V5.5.0_20171107.jar
-libraryjars libs/Android_Map3D_SDK_V5.5.0_20171107.jar
-libraryjars libs/jcore-android-1.2.3.jar
-libraryjars libs/jshare-android-1.6.0.jar
-libraryjars libs/jshare-wechat-android-1.6.0.jar
-libraryjars libs/x5webview.jar

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}