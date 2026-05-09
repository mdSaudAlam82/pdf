# ==========================================
# 🌟 HI-READ PROGUARD / R8 RULES (MAY 2026)
# ==========================================
# Note: Room, Hilt, Coil, and Tink automatically supply their own Consumer Rules.
# Do not add overly broad rules (**) for them to avoid breaking R8 optimizations.

# 1. Kotlinx Serialization & Navigation Shield
# Keeps the names of our Navigation Screens intact so deep-linking and serialization don't crash
-keepattributes *Annotation*, InnerClasses
-keep,includedescriptorclasses class com.edu.pdf.presentation.navigation.Screen** { *; }

# 2. General Android Optimizations
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose