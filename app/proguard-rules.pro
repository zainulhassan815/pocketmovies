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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# For more details on R8 full mode, see https://r8.googlesource.com/r8/+/refs/heads/master/compatibility-faq.md

# With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

 # R8 full mode strips generic signatures from return types if not kept.
 -if interface * { @retrofit2.http.* public *** *(...); }
 -keep,allowoptimization,allowshrinking,allowobfuscation class <3>

 # With R8 full mode generic signatures are stripped for classes that are not kept.
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

# For Kotlin suspend functions the generic signature is reflectively read.
# Therefore keeping the Signature attribute is necessary. Full mode only
# keeps the signature for kept classes thus a keep on kotlin.coroutines.
# Continuation in addition to a keep on the api classes is needed:
 -keepattributes Signature
 -keep class kotlin.coroutines.Continuation

 -dontwarn kotlinx.serialization.KSerializer
 -dontwarn kotlinx.serialization.Serializable

# Keep data models
 -keep class org.dreamerslab.pocketmovies.data.models.MovieDto { public *; }
 -keep class org.dreamerslab.pocketmovies.data.models.CastDto { public *; }
 -keep class org.dreamerslab.pocketmovies.data.models.TorrentDto { public *; }
 -keep class org.dreamerslab.pocketmovies.data.models.SingleMovieApiResponse { public *; }
 -keep class org.dreamerslab.pocketmovies.data.models.SingleMovieResponseData { public *; }
 -keep class org.dreamerslab.pocketmovies.data.models.MoviesListApiResponse { public *; }
 -keep class org.dreamerslab.pocketmovies.data.models.MoviesListResponseData { public *; }
