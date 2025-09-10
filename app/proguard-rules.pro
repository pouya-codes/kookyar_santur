# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\sdk/tools/proguard/proguard-android.txt
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

# Keep Pure Data classes and core functionality
-keep class org.puredata.android.** { *; }
-keep class org.puredata.core.** { *; }

# Keep PdListener implementations (critical for audio)
-keep class * extends org.puredata.core.PdListener { *; }
-keep class * extends org.puredata.core.PdListener$Adapter { *; }

# Keep MIDI classes if referenced
-dontwarn com.noisepages.nettoyeur.midi.MidiReceiver

# Keep animation library classes
-keep class com.daimajia.** { *; }

# Keep application classes from obfuscation
-keep class com.PouyaApp.KookYaRSantooR.** { *; }

# AndroidX rules
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Material Components
-keep class com.google.android.material.** { *; }

# Keep native methods and JNI
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep Runnable implementations (for audio threading)
-keep class * implements java.lang.Runnable {
    public void run();
}

# Keep ServiceConnection implementations
-keep class * implements android.content.ServiceConnection { *; }

# Conservative optimization - disable aggressive optimization
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
