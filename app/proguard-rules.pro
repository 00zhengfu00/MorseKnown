# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\cunbao\androidsdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-keep public class * extends  android.os.Build
-keep public class * extends  android.support.design.widget.TabLayout
-keep public class * extends  android.support.v4.app.FragmentStatePagerAdapter
-keep public class * extends android.support.v4.content.res.ResourcesCompat
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends  android.view.WindowManager
-keep public class * extends android.support.v4.**
