# ============================================================
# ProGuard / R8 Rules — Digital Tasbih
# ============================================================

# ── Google Mobile Ads (AdMob) SDK ───────────────────────────
# Prevents R8 from stripping or renaming AdMob SDK classes.
# Without these, ad creatives may fail to render (blank views)
# or cause VerifyError crashes in release builds.
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }
-keep class com.google.android.gms.internal.ads.** { *; }

# AdMob Mediation adapter safety (future-proof if mediation added)
-keep class com.google.ads.mediation.admob.AdMobAdapter { *; }
-keep class com.google.ads.mediation.AdUrlAdapter { *; }

# User Messaging Platform (UMP / Consent SDK)
-keep class com.google.android.ump.** { *; }

# ── Firebase ─────────────────────────────────────────────────
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.internal.firebase-analytics.** { *; }

# ── Google Play Services ─────────────────────────────────────
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.tasks.** { *; }

# ── Kotlin & Coroutines ──────────────────────────────────────
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }

# ── Gson (used for JSON parsing) ─────────────────────────────
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ── Suppress noisy warnings from Google SDKs ─────────────────
-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**

