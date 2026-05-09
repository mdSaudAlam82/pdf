🛠 Tech Stack & Architecture
Language: 100% Kotlin 2.3.20 (K2 Compiler)

Min SDK: 33 (Android 13) | Compile SDK: 36 (Android 16)

UI: Jetpack Compose (Multi-module) with Adaptive Layouts

Architecture: Strict MVI (Model-View-Intent) with Unidirectional Data Flow (UDF)

Database: Room with FTS4 (Full-Text Search) for instant results

Security: Google Tink (AES-256 GCM) for Private Vault encryption

Performance: Paging 3 + custom PdfRenderer for 120fps scrolling

✨ Key Features
Private Vault: Biometric-protected encryption for sensitive PDFs.

Adaptive UI: Tablet ke liye 'List-Detail' aur Phone ke liye 'Edge-to-Edge' experience.

Smart Search: Instant searching across thousands of files using FTS4.

Deep Learning Thumbnails: Coil-based optimized thumbnail rendering with bitmap pooling.

🏗 Modular Structure
Project ko clean and scalable rakha gaya hai:

:data - Local Database, Preferences, aur Crypto logic.

:domain - Pure business logic aur Use-cases.

:presentation - Compose Screens, ViewModels (MVI), aur UI Components.
