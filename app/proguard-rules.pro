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
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}


-keep class me.hgj.jetpackmvvm.**{*;}
################ ViewBinding & DataBinding ###############
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static * inflate(android.view.LayoutInflater);
  public static * inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
  public static * bind(android.view.View);
}

# 保留应用程序入口点
-keep class com.mxsella.smartrecharge.ui.activity.WelcomeActivity

# 保留Android框架类和方法
-keep class android.**
-keep interface android.**
-keep enum android.**
-keepattributes Signature

# 保留自定义类和方法
-keep class com.mxsella.smartrecharge.model.** { *; }
-keep class com.mxsella.smartrecharge.util.** { *; }

# 保留用于反射和依赖注入的类和方法
-keepclassmembers class * {
    @javax.inject.* *;
    @dagger.* *;
    @com.google.inject.* *;
    @javax.annotation.* *;
    @com.squareup.inject.* *;
}

# 保留用于序列化的类和方法
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
