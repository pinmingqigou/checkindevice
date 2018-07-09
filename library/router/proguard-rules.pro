# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
-dontwarn com.droi.sdk.**
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn net.jpountz.**
-keep class com.droi.sdk.** { *; }
-keep interface com.droi.sdk.** { *; }
-keep class com.tyd.aidlservice.internal.** { *; }
-keep interface com.tyd.aidlservice.internal.** { *; }
-keep class net.jpountz.** { *; }
-keep class * extends com.droi.sdk.core.DroiObject { @com.droi.sdk.core.DroiExpose *; @com.droi.sdk.core.DroiReference *;}
-keep class * extends com.droi.sdk.core.DroiUser { @com.droi.sdk.core.DroiExpose *; @com.droi.sdk.core.DroiReference *;}
#如果使用eclipse请再增加以下规则
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes EnclosingMethod