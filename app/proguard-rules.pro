# ==========================================
# 🌟 HI-READ PROGUARD / R8 RULES (MAY 2026)
# ==========================================
# Note: Room, Hilt, Coil, and Tink automatically supply their own Consumer Rules.

# 1. Kotlinx Serialization & Navigation Shield
-keepattributes *Annotation*, InnerClasses
-keep,includedescriptorclasses class com.edu.pdf.presentation.navigation.Screen** { *; }

# 2. iText 9.x & SLF4J 2.x Shield (CRITICAL)
# Prevents R8 from stripping the ServiceLoader files needed for iText to find the logger
-keepattributes Signature, InnerClasses, EnclosingMethod
-keep class org.slf4j.impl.** { *; }
-keep class org.slf4j.spi.** { *; }
-keepclassmembers class org.slf4j.LoggerFactory {
    public static *** getLogger(...);
}
-keep class * implements org.slf4j.spi.SLF4JServiceProvider { *; }
-dontwarn org.slf4j.**

# 3. General Android Optimizations
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
