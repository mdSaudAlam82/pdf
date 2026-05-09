# Room Database Shield
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Hilt & Dagger Shield
-keep,allowobfuscation,allowshrinking @dagger.hilt.android.AndroidEntryPoint class *
-keep class dagger.hilt.internal.aggregatedroot.AggregatedRoot { *; }

# Coil (Image Library) Shield
-keep class coil3.** { *; }

# Kotlinx Serialization & Navigation Shield
-keepattributes *Annotation*, InnerClasses
-keep,includedescriptorclasses class com.edu.pdf.presentation.navigation.Screen** { *; }