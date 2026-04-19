# 📝 notepad — Görev Listesi

> Raporun 9 aşamasının görev-görev, örnekli dökümü. Her kutuyu işaretleyerek ilerle.
> Tahmini süreler, **haftada 10-15 saat** çalıştığını varsayarak yazıldı.

---

## İçindekiler

1. [Yönetici Özeti — Durum Tespiti](#aşama-1--yönetici-özeti--durum-tespiti)
2. [Tema ve Görünüm](#aşama-2--tema-ve-görünüm)
3. [İşlevsellik — Yeni Özellikler](#aşama-3--i̇şlevsellik--yeni-özellikler)
4. [Teknik Altyapı — Refactoring](#aşama-4--teknik-altyapı--refactoring)
5. [UX İyileştirmeleri](#aşama-5--ux-i̇yileştirmeleri)
6. [Öncelik Matrisi](#aşama-6--öncelik-matrisi)
7. [8 Haftalık Yol Haritası](#aşama-7--8-haftalık-yol-haritası)
8. [Farklılaşma Stratejisi](#aşama-8--farklılaşma-stratejisi)
9. [Nereden Başlamalısın — Quick Wins](#aşama-9--nereden-başlamalısın--quick-wins)

---

## Aşama 1 — Yönetici Özeti — Durum Tespiti

> Bu aşamada kod yazmıyorsun, **anlıyorsun**. Sonraki aşamalara geçmeden önce mevcut durumu zihnine yerleştir.

### Görev 1.1 — Kod tabanının haritasını çıkar (30 dk)

- [ ] `MainActivity.kt`'de kaç satır olduğunu say — `wc -l` ile
- [ ] `SharedPreferences` kaç yerde çağrılıyor? → `grep -rn "getSharedPreferences" app/src`
- [ ] Hangi dosyalar 500+ satır?
- [ ] Bir dosyanın çok büyük olduğunu gör

**Başarı kriteri:** Kodun hangi noktalarının kırılgan olduğunu yazılı olarak not ettin.

### Görev 1.2 — Sağlam/zayıf taraf listesi çıkar (15 dk)

- [ ] Kağıda/Notion'a iki sütun: "**Ne iyi gidiyor**" ve "**Beni rahatsız eden**"
- [ ] En az 5 madde yaz her sütuna

**Başarı kriteri:** Kendi subjektif analizin raporumla örtüşüyor mu, bak.

---

## Aşama 2 — Tema ve Görünüm

> Uygulamanın görsel karakterini yaratacağımız aşama. 6 kritik görev, 2-3 hafta sürer.

### Görev 2.1 — 🔴 KRİTİK BUG: Outfit fontunu aktifleştir (10 dk)

Şu anda `Typography.kt`'de Outfit fontu tanımlı ama `Burak2Theme.kt`'de kullanılmıyor.

**Dosya:** `app/src/main/java/com/burakgurgil/burak2/ui/theme/Burak2Theme.kt`

**Öncesi:**
```kotlin
MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,   // ← Bu Theme.kt'deki GENERİK olan
    content = content
)
```

**Sonrası:**
```kotlin
MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,      // ← Type.kt'deki Outfit fontlu olan
    content = content
)
```

**Başarı kriteri:** Uygulamayı çalıştırdığında fontlar belirgin şekilde değişti.

### Görev 2.2 — Material You (dinamik renk) desteği (30 dk)

Android 12+ kullanıcıları kendi duvar kağıdı renklerinin uygulamaya yansımasını bekler.

**Dosya:** `Burak2Theme.kt`

```kotlin
import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.platform.LocalContext

@Composable
fun Burak2Theme(
    themeType: ThemeType = ThemeType.DEFAULT,
    useDynamicColor: Boolean = false,   // ← Yeni parametre
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDark = themeType == ThemeType.DARK

    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDark) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        else -> {
            val colors = themeColors[themeType] ?: themeColors[ThemeType.DEFAULT]!!
            lightColorScheme(
                primary = colors.primary,
                // ... (mevcut kod)
            )
        }
    }
    // ... kalan kod
}
```

**Ayarlarda toggle ekle:**
```kotlin
// SettingsScreen.kt
Switch(
    checked = useDynamicColor,
    onCheckedChange = { 
        useDynamicColor = it
        sharedPreferences.edit().putBoolean("dynamic_color", it).apply()
    }
)
```

- [x] `Burak2Theme`'a `useDynamicColor` parametresi ekle
- [x] `SettingsScreen`'de toggle ekle
- [x] `MainActivity.kt`'de SharedPreferences'tan oku
- [x] Android 12+ cihazda test et

**Başarı kriteri:** Toggle açıkken telefonun duvar kağıdı temasına göre uygulama renkleri değişiyor.

### Görev 2.3 — Mood temaları ekle (1-2 hafta)

7 mevsim temasını bırak, **mood** temaları yap.

**Dosya:** `app/src/main/java/com/burakgurgil/burak2/ui/theme/ThemeType.kt`

```kotlin
enum class ThemeType {
    PAPER,       // Kirli beyaz + siyah mürekkep
    MIDNIGHT,    // Derin lacivert gece
    FOCUS,       // Minimalist gri
    SUNSET,      // Sıcak turuncu-pembe
    FOREST,      // Koyu yeşil + toprak
    TERMINAL,    // Siyah + neon yeşil
    DEFAULT,     // Standart
    DARK         // OLED siyah
}
```

**Her tema için örnek (`ThemeColors.kt`):**

```kotlin
ThemeType.PAPER to ThemeColors(
    primary = Color(0xFF2C2C2E),      // Mürekkep siyahı
    secondary = Color(0xFF8B7355),    // Toprak tonu
    textPrimary = Color(0xFF1C1C1E),
    textSecondary = Color(0xFF6E6E73),
    background = Color(0xFFFBF8F3),   // Kirli beyaz (kağıt)
    surface = Color(0xFFFDFBF7),      // Hafif krem
    surfaceVariant = Color(0xFFF0ECE5),
    error = Color(0xFFB00020),
    addButton = Color(0xFF2C2C2E),
    editButton = Color(0xFF8B7355),
    deleteButton = Color(0xFFB00020),
    settingsButton = Color(0xFFF0ECE5)
),

ThemeType.MIDNIGHT to ThemeColors(
    primary = Color(0xFF6B9FFF),      // Yumuşak gece mavisi
    secondary = Color(0xFF9BB5FF),
    textPrimary = Color(0xFFE8EAED),
    textSecondary = Color(0xFF9AA0A6),
    background = Color(0xFF0A1628),   // Derin gece
    surface = Color(0xFF14243A),
    surfaceVariant = Color(0xFF1E2F47),
    error = Color(0xFFFF6B6B),
    addButton = Color(0xFF6B9FFF),
    editButton = Color(0xFF9BB5FF),
    deleteButton = Color(0xFFFF6B6B),
    settingsButton = Color(0xFF1E2F47)
),

ThemeType.TERMINAL to ThemeColors(
    primary = Color(0xFF00FF41),      // Neon yeşil (Matrix)
    secondary = Color(0xFF39FF14),
    textPrimary = Color(0xFF00FF41),
    textSecondary = Color(0xFF009933),
    background = Color(0xFF000000),
    surface = Color(0xFF0D0D0D),
    surfaceVariant = Color(0xFF1A1A1A),
    error = Color(0xFFFF3B30),
    addButton = Color(0xFF00FF41),
    editButton = Color(0xFF39FF14),
    deleteButton = Color(0xFFFF3B30),
    settingsButton = Color(0xFF1A1A1A)
),
```

- [x] `ThemeType` enum'ını güncelle
- [x] Her mood için `ThemeColors` palet tanımla
- [x] `SettingsScreen`'de tema seçiciyi yeniden tasarla (canlı önizleme ile)
- [x] Her tema için uygun font eşlemesi (Fraunces, Inter, JetBrains Mono eklendi)
- [x] Eski `ThemeType.SPRING/SUMMER/AUTUMN/WINTER` migration: kullanıcının eski seçimi varsa en yakın mood'a map et

**Başarı kriteri:** 6 mood tema aktif, her birinin kendine has karakteri var.

### Görev 2.4 — Çift font sistemi (serif + sans-serif) (2-3 saat)

**Dosya:** `app/src/main/res/font/` klasörüne yeni fontları ekle

- [x] Google Fonts'tan indir: `fraunces`, `inter`, `jetbrainsmono`
- [x] `res/font/` klasörüne kopyala

**Dosya:** `Type.kt` [DONE]
```kotlin
val FrauncesFontFamily = FontFamily(
    Font(R.font.fraunces_regular, FontWeight.Normal),
    Font(R.font.fraunces_bold, FontWeight.Bold),
)

val InterFontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_bold, FontWeight.Bold),
)

val JetBrainsMonoFontFamily = FontFamily(
    Font(R.font.jetbrainsmono_regular, FontWeight.Normal),
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FrauncesFontFamily,   // Serif — başlıklar
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
    ),
    // ... başlıklar FrauncesFontFamily
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,      // Sans — gövde
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    // ...
)
```

**Başarı kriteri:** Başlıklar karakterli bir serif, gövde metin temiz bir sans. Birbirinden ayrıldığı net.

### Görev 2.5 — Haptic feedback ekle (1 saat)

**Dosya:** `MainActivity.kt` (veya `NoteItem.kt`)

```kotlin
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun NoteItem(...) {
    val haptic = LocalHapticFeedback.current
    
    Card(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onView()
        }
    )
    // ...
    
    // Yıldızlama butonu
    IconButton(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onStarred()
        }
    )
}
```

- [x] Yıldızlama: `LongPress`
- [x] Silme: `LongPress`
- [x] Kaydetme: `TextHandleMove`
- [x] Tema değişimi: `LongPress`
- [x] Drag başlangıcı: `LongPress`

**Başarı kriteri:** Tüm kritik aksiyonlarda titreşim hissediliyor.

### Görev 2.6 — Swipe-to-delete ve swipe-to-archive (2-3 saat)

**Dosya:** `NoteItem.kt` veya yeni `SwipeableNoteItem.kt`

```kotlin
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableNoteItem(
    note: Note,
    onDelete: () -> Unit,
    onArchive: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onArchive()
                    true
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true
                }
                else -> false
            }
        }
    )
    
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Color(0xFF34C759)  // Yeşil = arşiv
                SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF3B30)  // Kırmızı = sil
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(24.dp))
                    .padding(horizontal = 24.dp),
                contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
                    Alignment.CenterStart else Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
                        Icons.Default.Archive else Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) {
        content()
    }
}
```

- [x] `SwipeableNoteItem` oluştur (SwipeToDismissBox kullanıldı)
- [x] `LazyColumn`'da `NoteItem` yerine bunu kullan
- [ ] Undo snackbar ekle ("Not silindi • GERİ AL")

**Başarı kriteri:** Notu sağa kaydır → arşiv, sola kaydır → çöpe.

---

## Aşama 3 — İşlevsellik — Yeni Özellikler

> Üç seviyede özellikler: olmazsa olmazlar, rekabet üstünlüğü ve vizyon.

### Görev 3.1 — Kontrol listeleri (Checklist) (3-5 gün)

**Dosya:** `data/Note.kt` — yeni alan

```kotlin
@Entity(tableName = "notes")
data class Note(
    // ... mevcut alanlar
    val isChecklist: Boolean = false,
    val checklistItems: String = ""  // JSON: [{"text":"...","done":false}]
)
```

**Migration:** `NoteDatabase.kt`
```kotlin
private val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE notes ADD COLUMN isChecklist INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE notes ADD COLUMN checklistItems TEXT NOT NULL DEFAULT ''")
    }
}
```

**Yeni dosya:** `data/ChecklistItem.kt`
```kotlin
@Serializable
data class ChecklistItem(
    val text: String,
    val done: Boolean = false
)
```

**Yeni composable:** `ui/components/ChecklistEditor.kt`
```kotlin
@Composable
fun ChecklistEditor(
    items: List<ChecklistItem>,
    onItemsChange: (List<ChecklistItem>) -> Unit
) {
    Column {
        items.forEachIndexed { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.done,
                    onCheckedChange = { checked ->
                        onItemsChange(
                            items.toMutableList().also {
                                it[index] = item.copy(done = checked)
                            }
                        )
                    }
                )
                TextField(
                    value = item.text,
                    onValueChange = { text ->
                        onItemsChange(
                            items.toMutableList().also {
                                it[index] = item.copy(text = text)
                            }
                        )
                    },
                    textStyle = if (item.done) 
                        LocalTextStyle.current.copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    else LocalTextStyle.current
                )
                IconButton(onClick = {
                    onItemsChange(items.toMutableList().also { it.removeAt(index) })
                }) {
                    Icon(Icons.Default.Close, null)
                }
            }
        }
        TextButton(onClick = {
            onItemsChange(items + ChecklistItem(text = ""))
        }) {
            Text("+ Yeni madde")
        }
    }
}
```

- [ ] Migration yaz (6→7)
- [ ] `ChecklistItem` data class + JSON serialization
- [ ] `ChecklistEditor` composable
- [ ] `NoteEditScreen`'de toggle: "Not / Liste"
- [ ] `NoteItem` kartında progress göster: "3/5 tamamlandı"
- [ ] Tamamlananları gizle/göster butonu

**Başarı kriteri:** Bir notu checklist'e çevirip madde ekleyebilip işaretleyebiliyorsun.

### Görev 3.2 — Görsel ekleme (1 hafta)

**Dependency:** `build.gradle.kts`
```kotlin
implementation("io.coil-kt:coil-compose:2.5.0")
```

**Dosya:** `data/Note.kt`
```kotlin
val imageUris: String = ""  // Virgülle ayrılmış URI listesi
```

**Permission:** `AndroidManifest.xml`
```xml
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.CAMERA" />
```

**Image picker:**
```kotlin
val imagePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
) { uri ->
    if (uri != null) {
        // Dosyayı internal storage'a kopyala
        val savedPath = copyImageToAppStorage(context, uri)
        imageUris = imageUris + savedPath
    }
}

Button(onClick = {
    imagePickerLauncher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
}) {
    Text("Resim Ekle")
}
```

**Görüntüleme (`ViewNoteScreen.kt`):**
```kotlin
import coil.compose.AsyncImage

LazyRow {
    items(note.imageUris.split(",").filter { it.isNotBlank() }) { uri ->
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { /* tam ekran zoom */ }
        )
    }
}
```

- [ ] Coil dependency ekle
- [ ] Migration: `imageUris` kolonu
- [ ] Permission'ları ekle
- [ ] Image picker composable
- [ ] Internal storage'a kopyalama fonksiyonu
- [ ] Not silinince dosyaları da sil (cascade)
- [ ] Tam ekran zoom view

**Başarı kriteri:** Nota resim ekleyip silebiliyorsun, uygulama restart sonrası resimler kalıyor.

### Görev 3.3 — Ana ekran widget'ı (3-5 gün)

**Dependency:** `build.gradle.kts`
```kotlin
implementation("androidx.glance:glance-appwidget:1.1.1")
implementation("androidx.glance:glance-material3:1.1.1")
```

**Yeni dosya:** `widget/NotesWidget.kt`
```kotlin
class NotesWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val notes = /* Room'dan son 3 notu çek */
        
        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.background)
                        .padding(12.dp)
                ) {
                    Text("Son Notlar", style = TextStyle(fontWeight = FontWeight.Bold))
                    notes.forEach { note ->
                        Text(
                            note.title,
                            modifier = GlanceModifier
                                .clickable(actionStartActivity<MainActivity>())
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

class NotesWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NotesWidget()
}
```

**Manifest kaydı:**
```xml
<receiver
    android:name=".widget.NotesWidgetReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/notes_widget_info" />
</receiver>
```

- [ ] Glance dependency ekle
- [ ] `NotesWidget` composable
- [ ] `NotesWidgetReceiver`
- [ ] `res/xml/notes_widget_info.xml`
- [ ] Notlar değişince widget güncelle: `NotesWidget().updateAll(context)`

**Başarı kriteri:** Ana ekrana widget eklenebiliyor, son 3 not görünüyor.

### Görev 3.4 — App Shortcuts (2-3 saat)

**Yeni dosya:** `res/xml/shortcuts.xml`
```xml
<shortcuts xmlns:android="http://schemas.android.com/apk/res/android">
    <shortcut
        android:shortcutId="new_note"
        android:enabled="true"
        android:icon="@drawable/ic_new_note"
        android:shortcutShortLabel="@string/shortcut_new_note">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetPackage="com.burakgurgil.burak2"
            android:targetClass="com.burakgurgil.burak2.MainActivity"
            android:data="notepad://new" />
    </shortcut>
    <shortcut
        android:shortcutId="search"
        android:enabled="true"
        android:icon="@drawable/ic_search"
        android:shortcutShortLabel="@string/shortcut_search">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetPackage="com.burakgurgil.burak2"
            android:data="notepad://search" />
    </shortcut>
</shortcuts>
```

**Manifest'e kaydet:**
```xml
<activity android:name=".MainActivity">
    <meta-data
        android:name="android.app.shortcuts"
        android:resource="@xml/shortcuts" />
</activity>
```

- [ ] `shortcuts.xml` oluştur
- [ ] Manifest'e bağla
- [ ] `MainActivity`'de `intent.data` okuyup ilgili ekrana git

**Başarı kriteri:** Uygulama ikonuna uzun basınca "Yeni Not", "Ara" menüsü çıkıyor.

### Görev 3.5 — İçe Aktarma (Import) (2-3 gün)

**Dosya:** `MainActivity.kt`

```kotlin
private val importNotesLauncher = registerForActivityResult(
    ActivityResultContracts.OpenDocument()
) { uri ->
    if (uri != null) {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val json = inputStream.bufferedReader().readText()
            val type = object : TypeToken<List<Note>>() {}.type
            val notes: List<Note> = Gson().fromJson(json, type)
            notes.forEach { viewModel.insert(it.copy(id = 0)) }
            Toast.makeText(this, "${notes.size} not içe aktarıldı", Toast.LENGTH_SHORT).show()
        }
    }
}

// SettingsScreen'den çağır:
importNotesLauncher.launch(arrayOf("application/json", "text/plain", "text/markdown"))
```

**Markdown import (her .md dosyası → yeni not):**
```kotlin
if (fileName.endsWith(".md") || fileName.endsWith(".txt")) {
    val newNote = Note(
        title = fileName.substringBeforeLast("."),
        content = fileContent,
        createdAt = Date()
    )
    viewModel.insert(newNote)
}
```

- [ ] JSON import
- [ ] `.txt` import
- [ ] `.md` import
- [ ] Google Keep export JSON parser (bonus)
- [ ] Import edilirken progress dialog

**Başarı kriteri:** JSON yedeğini geri alabiliyorsun.

### Görev 3.6 — Arşivleme (2 gün)

**Migration:**
```kotlin
private val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE notes ADD COLUMN isArchived INTEGER NOT NULL DEFAULT 0")
    }
}
```

**NoteDao.kt:**
```kotlin
@Query("SELECT * FROM notes WHERE isDeleted = 0 AND isArchived = 0 ORDER BY isStarred DESC, createdAt DESC")
fun getAllNotes(): Flow<List<Note>>

@Query("SELECT * FROM notes WHERE isArchived = 1 ORDER BY createdAt DESC")
fun getArchivedNotes(): Flow<List<Note>>

@Query("UPDATE notes SET isArchived = :archived WHERE id = :noteId")
suspend fun toggleArchived(noteId: Long, archived: Boolean)
```

- [x] Migration 6→7 (isArchived eklendi)
- [x] DAO metodları
- [x] `Arşiv` filter chip eklendi
- [x] Menüde "Arşiv" butonu (Filter chip olarak eklendi)
- [x] Swipe-right gesture arşivleme tetiklesin

**Başarı kriteri:** Not arşivleyebiliyorsun, arşivden geri getirebiliyorsun.

### Görev 3.7 — Sıralama ve gruplama (1 gün)

```kotlin
enum class SortOrder {
    DATE_DESC,      // Yeni → Eski
    DATE_ASC,       // Eski → Yeni
    TITLE_ASC,      // A → Z
    TITLE_DESC,     // Z → A
    MODIFIED_DESC   // Son düzenleme
}

// ViewModel'de
fun sortNotes(notes: List<Note>, order: SortOrder): List<Note> = when (order) {
    SortOrder.DATE_DESC -> notes.sortedByDescending { it.createdAt }
    SortOrder.TITLE_ASC -> notes.sortedBy { it.title }
    // ...
}

// Gruplama
fun groupByDate(notes: List<Note>): Map<String, List<Note>> {
    val now = Calendar.getInstance()
    return notes.groupBy { note ->
        val cal = Calendar.getInstance().apply { time = note.createdAt }
        when {
            cal.isToday() -> "Bugün"
            cal.isYesterday() -> "Dün"
            cal.isThisWeek() -> "Bu Hafta"
            cal.isThisMonth() -> "Bu Ay"
            else -> "Daha Eski"
        }
    }
}
```

- [ ] `SortOrder` enum
- [ ] TopBar'da sort menü
- [ ] Gruplama toggle'ı
- [ ] Tercihleri DataStore'da sakla

**Başarı kriteri:** Notları farklı kriterlere göre sıralayabiliyorsun.

### Görev 3.8 — Markdown desteği (1 hafta)

**Dependency:**
```kotlin
implementation("io.noties.markwon:core:4.6.2")
implementation("io.noties.markwon:linkify:4.6.2")
implementation("io.noties.markwon:syntax-highlight:4.6.2")
```

**Compose wrapper:**
```kotlin
@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val markwon = remember { Markwon.create(context) }
    
    AndroidView(
        factory = { ctx ->
            TextView(ctx).apply {
                textSize = 16f
            }
        },
        update = { textView ->
            markwon.setMarkdown(textView, markdown)
        },
        modifier = modifier
    )
}
```

**Edit ↔ Preview toggle:**
```kotlin
var isPreview by remember { mutableStateOf(false) }

if (isPreview) {
    MarkdownText(content)
} else {
    TextField(value = content, onValueChange = { content = it })
}

IconButton(onClick = { isPreview = !isPreview }) {
    Icon(if (isPreview) Icons.Default.Edit else Icons.Default.Visibility, null)
}
```

- [ ] Markwon dependency
- [ ] `MarkdownText` composable
- [ ] Edit/Preview toggle
- [ ] Markdown ↔ HTML dönüştürücü (mevcut rich text editor ile uyumlu)
- [ ] Markdown olarak export

**Başarı kriteri:** `# Başlık`, `**kalın**`, `- liste` yazınca render ediliyor.

### Görev 3.9 — Not şablonları (3-5 gün)

**Yeni entity:** `data/Template.kt`
```kotlin
@Entity(tableName = "templates")
data class Template(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val titleTemplate: String,
    val contentTemplate: String,
    val isDefault: Boolean = false
)
```

**Varsayılan şablonlar:**
```kotlin
val defaultTemplates = listOf(
    Template(
        name = "Toplantı",
        titleTemplate = "Toplantı - {date}",
        contentTemplate = """
            # Katılımcılar
            - 
            
            # Gündem
            1. 
            
            # Alınan Kararlar
            - 
            
            # Aksiyonlar
            - [ ] 
        """.trimIndent()
    ),
    Template(
        name = "Günlük",
        titleTemplate = "{date}",
        contentTemplate = """
            # Bugün nasıl geçti?
            
            # Minnettar olduğum 3 şey
            1. 
            2. 
            3. 
            
            # Yarın için öncelik
            - 
        """.trimIndent()
    ),
    Template(
        name = "Kitap Özeti",
        titleTemplate = "{kitap adı}",
        contentTemplate = """
            # Yazar
            
            # Ana tezler
            - 
            
            # Öne çıkan alıntılar
            > 
            
            # Kişisel notlar
        """.trimIndent()
    )
)
```

- [ ] `Template` entity + DAO
- [ ] Varsayılan şablonları seed et (ilk açılış)
- [ ] "Yeni not" tıklanınca şablon seçme dialog
- [ ] Placeholder replacement: `{date}` → bugünün tarihi
- [ ] Kullanıcı kendi şablonunu ekleyebilsin

**Başarı kriteri:** Yeni not oluştururken şablon seçebiliyorsun.

---

## Aşama 4 — Teknik Altyapı — Refactoring

> Yeni özellik eklemeden önce temel sağlamlaştırılmalı.

### Görev 4.1 — 🔴 KRİTİK: fallbackToDestructiveMigration kaldır (5 dk)

**Dosya:** `NoteDatabase.kt`

**Öncesi:**
```kotlin
Room.databaseBuilder(...)
    .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
    .fallbackToDestructiveMigration()   // ← TEHLIKE
    .build()
```

**Sonrası:**
```kotlin
Room.databaseBuilder(...)
    .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
    // fallbackToDestructiveMigration() SİLİNDİ
    .build()
```

- [ ] Satırı sil
- [ ] Migration test yaz (Room `MigrationTestHelper`)

**Başarı kriteri:** Migration unutulursa uygulama crash eder (iyi) — silmez.

### Görev 4.2 — MainActivity'yi böl (2 saat)

**Hedef yapı:**
```
ui/
├── screens/
│   ├── HomeScreen.kt         (eski NoteApp composable)
│   ├── NoteEditScreen.kt     (mevcut, içerideydi)
│   ├── TrashScreen.kt        (çöp kutusu)
│   ├── ViewNoteScreen.kt     (mevcut)
│   └── SettingsScreen.kt     (mevcut)
└── components/
    ├── NoteItem.kt
    ├── DeletedNoteItem.kt
    ├── TagChip.kt
    ├── SearchBar.kt
    └── FormatToolbar.kt
```

- [ ] `NoteApp` composable'ını `HomeScreen.kt`'ye taşı
- [ ] `NoteItem` composable'ını `ui/components/NoteItem.kt`'ye taşı
- [ ] `NoteEditScreen`'i ayrı dosyaya al
- [ ] `DeletedNoteItem`'ı ayrı dosyaya al
- [ ] `MainActivity.kt`'de sadece `setContent { AppRoot() }` kalsın

**Başarı kriteri:** `MainActivity.kt` 100 satırın altında.

### Görev 4.3 — Jetpack Navigation Compose (2-3 gün)

**Dependency:**
```kotlin
implementation("androidx.navigation:navigation-compose:2.7.7")
```

**Yeni dosya:** `navigation/AppNavigation.kt`
```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { 
            HomeScreen(
                onNoteClick = { id -> navController.navigate("view/$id") },
                onEditClick = { id -> navController.navigate("edit/$id") },
                onNewNote = { navController.navigate("edit/new") },
                onSettings = { navController.navigate("settings") },
                onTrash = { navController.navigate("trash") }
            )
        }
        composable(
            "view/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStack ->
            ViewNoteScreen(
                noteId = backStack.arguments?.getLong("noteId") ?: 0,
                onBack = { navController.popBackStack() }
            )
        }
        composable("edit/{noteId}") { /* ... */ }
        composable("settings") { SettingsScreen(onBack = { navController.popBackStack() }) }
        composable("trash") { TrashScreen(onBack = { navController.popBackStack() }) }
    }
}
```

**Deep link (bonus):**
```kotlin
composable(
    "view/{noteId}",
    deepLinks = listOf(navDeepLink { uriPattern = "notepad://note/{noteId}" })
) { /* ... */ }
```

- [ ] Navigation dependency
- [ ] Tüm `if/else` state flag'lerini sil (`showSettings`, `showTrash`, vb.)
- [ ] Her ekranın kendi route'u
- [ ] Back stack yönetimini NavController'a bırak
- [ ] Deep link desteği ekle

**Başarı kriteri:** Back tuşu doğal çalışıyor, her ekran ayrı route.

### Görev 4.4 — SharedPreferences → DataStore (1 gün)

**Dependency:**
```kotlin
implementation("androidx.datastore:datastore-preferences:1.1.1")
```

**Yeni dosya:** `data/preferences/UserPreferences.kt`
```kotlin
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {
    private val THEME_KEY = stringPreferencesKey("theme")
    private val AUTO_DELETE_KEY = booleanPreferencesKey("auto_delete")
    private val GRID_VIEW_KEY = booleanPreferencesKey("is_grid_view")
    private val DYNAMIC_COLOR_KEY = booleanPreferencesKey("dynamic_color")
    
    val theme: Flow<ThemeType> = context.dataStore.data
        .map { prefs -> 
            ThemeType.valueOf(prefs[THEME_KEY] ?: ThemeType.DEFAULT.name)
        }
    
    val isAutoDeleteEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[AUTO_DELETE_KEY] ?: true }
    
    suspend fun setTheme(theme: ThemeType) {
        context.dataStore.edit { it[THEME_KEY] = theme.name }
    }
    
    suspend fun setAutoDelete(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_DELETE_KEY] = enabled }
    }
}
```

- [ ] DataStore dependency
- [ ] `UserPreferencesRepository` yaz
- [ ] Tüm `getSharedPreferences` çağrılarını değiştir
- [ ] Flow tabanlı observer'lar

**Başarı kriteri:** Artık `SharedPreferences` hiçbir yerde yok.

### Görev 4.5 — Hilt (Dependency Injection) (2-3 gün)

**Dependency:**
```kotlin
// Proje-seviye build.gradle.kts
id("com.google.dagger.hilt.android") version "2.50" apply false

// app build.gradle.kts
plugins {
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")   // kapt yerine
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}
```

**Application class:**
```kotlin
@HiltAndroidApp
class NotepadApplication : Application()
```

**Manifest:**
```xml
<application android:name=".NotepadApplication" ...>
```

**Module:**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase =
        NoteDatabase.getDatabase(context)
    
    @Provides
    fun provideNoteDao(db: NoteDatabase): NoteDao = db.noteDao()
    
    @Provides
    @Singleton
    fun provideRepository(dao: NoteDao): NoteRepository = NoteRepository(dao)
}
```

**ViewModel:**
```kotlin
@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val prefsRepo: UserPreferencesRepository
) : ViewModel() { /* ... */ }
```

**Composable:**
```kotlin
@Composable
fun HomeScreen(viewModel: NoteViewModel = hiltViewModel()) { /* ... */ }
```

- [ ] Hilt dependency
- [ ] kapt → ksp geçişi
- [ ] `NotepadApplication` sınıfı
- [ ] `DatabaseModule`, `RepositoryModule`
- [ ] Tüm ViewModel'leri `@HiltViewModel`'e çevir
- [ ] `NoteViewModelFactory`'yi sil (artık gerek yok)

**Başarı kriteri:** `ViewModelProvider` ve manuel factory kodu kalktı.

### Görev 4.6 — UseCase katmanı (2-3 gün)

**Yeni klasör:** `domain/usecase/`

```kotlin
class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.allNotes
}

class SearchNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(query: String, tag: String?): Flow<List<Note>> =
        repository.allNotes.map { notes ->
            notes.filter { note ->
                val matchesSearch = query.isBlank() ||
                    note.title.contains(query, ignoreCase = true) ||
                    note.content.contains(query, ignoreCase = true)
                val matchesTag = tag == null || note.tag == tag
                matchesSearch && matchesTag
            }
        }
}

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val reminderScheduler: ReminderScheduler
) {
    suspend operator fun invoke(noteId: Long) {
        repository.moveToTrash(noteId)
        reminderScheduler.cancel(noteId)
    }
}
```

- [ ] `GetNotesUseCase`
- [ ] `SearchNotesUseCase`
- [ ] `DeleteNoteUseCase`
- [ ] `ToggleStarredUseCase` (max 3 yıldız kontrolünü buraya taşı)
- [ ] `ScheduleReminderUseCase`
- [ ] ViewModel'leri UseCase'leri çağıracak şekilde düzenle

**Başarı kriteri:** ViewModel'de iş mantığı kalmadı, sadece UI state yönetimi var.

### Görev 4.7 — Kotlin 2.0+ ve modernleştirme (2 saat)

**libs.versions.toml:**
```toml
[versions]
agp = "8.6.0"
kotlin = "2.0.21"                # 1.9.0 → 2.0.21
ksp = "2.0.21-1.0.27"            # kapt yerine
composeBom = "2024.11.00"        # güncel
coreKtx = "1.15.0"
room = "2.6.1"

[libraries]
androidx-room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
androidx-room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
androidx-room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
# ... diğer library'ler

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

**app/build.gradle.kts:**
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)   // Kotlin 2.0 compose plugin
    alias(libs.plugins.ksp)
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17   // 1.8 → 17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"   // 1.8 → 17
    }
    // composeOptions BLOK'UNU SİL — plugin artık yönetiyor
}

dependencies {
    // kapt("androidx.room:room-compiler:...") → ksp
    ksp(libs.androidx.room.compiler)
}
```

- [ ] Kotlin 2.0.21
- [ ] kapt → ksp
- [ ] JVM 17
- [ ] Compose Compiler plugin (1.5.1 → 2.0 ile plugin)
- [ ] Tüm versiyonları `libs.versions.toml`'e taşı
- [ ] Build hızını karşılaştır (2-3x hızlanma beklenir)

**Başarı kriteri:** Clean build süren gözle görülür azaldı.

### Görev 4.8 — Test yazma (3-4 gün)

**Dependency:**
```kotlin
testImplementation("app.cash.turbine:turbine:1.0.0")
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
androidTestImplementation("androidx.room:room-testing:2.6.1")
```

**Örnek DAO testi:**
```kotlin
@RunWith(AndroidJUnit4::class)
class NoteDaoTest {
    private lateinit var db: NoteDatabase
    private lateinit var dao: NoteDao
    
    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.noteDao()
    }
    
    @After
    fun tearDown() { db.close() }
    
    @Test
    fun `not eklenince listede görünür`() = runTest {
        val note = Note(title = "Test", content = "...", createdAt = Date())
        val id = dao.insert(note)
        
        dao.getAllNotes().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Test", result[0].title)
        }
    }
    
    @Test
    fun `çöpe taşınan not aktif listede olmaz`() = runTest {
        val note = Note(title = "Test", content = "...", createdAt = Date())
        val id = dao.insert(note)
        dao.moveToTrash(id, System.currentTimeMillis())
        
        dao.getAllNotes().test {
            assertTrue(awaitItem().isEmpty())
        }
        dao.getDeletedNotes().test {
            assertEquals(1, awaitItem().size)
        }
    }
}
```

**Örnek ViewModel testi:**
```kotlin
@Test
fun `maksimum 3 yıldızlı not kontrolü`() = runTest {
    val viewModel = NoteViewModel(mockRepo, mockPrefsRepo)
    // 3 yıldızlı not ekle
    // 4. yıldızlamayı dene
    // SnackBar event emit edildi mi?
}
```

- [ ] DAO testleri (insert, update, delete, query'ler)
- [ ] Migration testleri (`MigrationTestHelper`)
- [ ] ViewModel testleri (en az 5 senaryo)
- [ ] UseCase testleri
- [ ] UI testi: Not oluşturma → listede görünmesi
- [ ] Hedef: `./gradlew test` geçiyor, coverage %60+

**Başarı kriteri:** CI/CD'de testler yeşil.

### Görev 4.9 — Accessibility (1-2 gün)

- [ ] Tüm `IconButton`'larda `contentDescription` anlamlı
- [ ] TalkBack ile uygulamayı baştan sona gezmeyi dene
- [ ] Kontrast kontrolü (WebAIM Contrast Checker)
- [ ] Dinamik font boyutu: Sistem ayarından maks'a al, layout kırılıyor mu?
- [ ] Minimum dokunma alanı: 48dp'den küçük hiçbir buton olmasın
- [ ] Screen reader için `Modifier.semantics { }` kullan

**Örnek:**
```kotlin
IconButton(
    onClick = { onDelete() },
    modifier = Modifier.semantics { 
        contentDescription = "Notu sil: ${note.title}"
    }
)
```

**Başarı kriteri:** Accessibility Scanner (Google Play) 0 kritik uyarı.

### Görev 4.10 — i18n / çok dillilik (2-3 gün)

**Yeni dosya:** `res/values/strings.xml` (mevcut) — tüm Türkçe'yi buraya
**Yeni dosya:** `res/values-en/strings.xml` — İngilizce

```xml
<!-- values/strings.xml -->
<string name="new_note">Yeni Not</string>
<string name="save">Kaydet</string>
<string name="trash">Çöp Kutusu</string>
<string name="search_placeholder">Notlarda ara…</string>
<string name="delete_all">Hepsini Sil</string>
<string name="confirm_delete_all">Çöp kutusundaki tüm notlar kalıcı olarak silinecek. Bu işlem geri alınamaz.</string>
<plurals name="starred_count">
    <item quantity="one">%d yıldızlı not</item>
    <item quantity="other">%d yıldızlı not</item>
</plurals>
```

**Kod:**
```kotlin
// Önce: Text("Kaydet")
// Sonra: Text(stringResource(R.string.save))

// Önce: Toast.makeText(context, "Notlar başarıyla dışa aktarıldı", ...)
// Sonra: Toast.makeText(context, context.getString(R.string.export_success), ...)
```

**Tarih formatları:**
```kotlin
// Önce
SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

// Sonra — Locale-aware
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    .withLocale(Locale.getDefault())
```

- [ ] Tüm string'leri çıkar (grep `"` içinde Türkçe metin ara)
- [ ] `values/strings.xml` (Türkçe)
- [ ] `values-en/strings.xml` (İngilizce)
- [ ] Tarih formatlarını `DateTimeFormatter.ofLocalizedDateTime`'a çevir
- [ ] Bonus: Almanca, İspanyolca, Arapça (RTL testi için)

**Başarı kriteri:** Cihaz dilini İngilizce yap, uygulama tamamen İngilizce.

---

## Aşama 5 — UX İyileştirmeleri

### Görev 5.1 — Onboarding (1-2 gün)

**Dependency:** `com.google.accompanist:accompanist-pager` (artık Compose'da yerli var)

```kotlin
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val pages = listOf(
        OnboardingPage("Hızlı not alın", "Fikirleri anlık yakalayın", R.drawable.onb_1),
        OnboardingPage("Güvende tutun", "Biyometrik kilitle önemli notları koruyun", R.drawable.onb_2),
        OnboardingPage("Organize edin", "Etiketler, yıldızlar, hatırlatıcılar", R.drawable.onb_3),
    )
    
    Column {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            OnboardingPageContent(pages[page])
        }
        PageIndicator(pagerState.currentPage, pages.size)
        
        Button(
            onClick = {
                if (pagerState.currentPage == pages.lastIndex) onComplete()
                else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            }
        ) {
            Text(if (pagerState.currentPage == pages.lastIndex) "Başla" else "İleri")
        }
    }
}
```

**İlk açılış kontrolü:**
```kotlin
// MainActivity
val isFirstLaunch = prefs.isFirstLaunch.collectAsState(initial = true)
if (isFirstLaunch.value) {
    OnboardingScreen(onComplete = { prefs.setFirstLaunchDone() })
} else {
    AppNavigation()
}
```

**Örnek not seed'i:**
```kotlin
suspend fun seedExampleNote(dao: NoteDao) {
    if (dao.getNoteCount() == 0) {
        dao.insert(Note(
            title = "Notepad'e hoş geldin! 👋",
            content = "Bu ilk notun. İstediğin zaman silebilirsin.\n\nSol tarafta etiketler, sağ üstte ayarlar.",
            createdAt = Date()
        ))
    }
}
```

- [ ] 3 sayfalık onboarding
- [ ] Page indicator
- [ ] Skip butonu
- [ ] İlk açılışta örnek not
- [ ] İlk açılışta tema seç dialog
- [ ] Permission isteklerini bağlamlı yap (bildirim izni = ilk hatırlatıcı kurarken)

**Başarı kriteri:** Fresh install'da kullanıcı yön bulabiliyor.

### Görev 5.2 — Boş ekran iyileştirmesi (2-3 saat)

**Dependency:** Lottie Compose
```kotlin
implementation("com.airbnb.android:lottie-compose:6.3.0")
```

**Dosya:** `res/raw/empty_notebook.json` (lottiefiles.com'dan ücretsiz indir)

```kotlin
@Composable
fun EmptyState(onCreateClick: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_notebook))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Boş defter — saygı duyuyorum",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Hemingway'in ilham anı da böyle başladı",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onCreateClick, modifier = Modifier.fillMaxWidth(0.7f)) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.width(8.dp))
            Text("İlk notunu ekle")
        }
    }
}
```

- [ ] Lottie dependency
- [ ] Lottie animation dosyası
- [ ] `EmptyState` composable
- [ ] Mevcut boş durum kodunu değiştir

**Başarı kriteri:** Boş ekran artık sade değil, karakterli.

### Görev 5.3 — Toplu seçim (batch selection) (2-3 gün)

```kotlin
var selectionMode by remember { mutableStateOf(false) }
var selectedNotes by remember { mutableStateOf(setOf<Long>()) }

// NoteItem'a onLongPress eventi
NoteItem(
    modifier = Modifier
        .combinedClickable(
            onClick = {
                if (selectionMode) {
                    selectedNotes = if (note.id in selectedNotes) 
                        selectedNotes - note.id 
                    else selectedNotes + note.id
                } else {
                    onView(note)
                }
            },
            onLongClick = {
                selectionMode = true
                selectedNotes = selectedNotes + note.id
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
)

// TopBar — seçim modunda
if (selectionMode) {
    TopAppBar(
        title = { Text("${selectedNotes.size} seçili") },
        navigationIcon = {
            IconButton(onClick = { 
                selectionMode = false
                selectedNotes = emptySet() 
            }) {
                Icon(Icons.Default.Close, null)
            }
        },
        actions = {
            IconButton(onClick = { /* toplu sil */ }) {
                Icon(Icons.Default.Delete, null)
            }
            IconButton(onClick = { /* toplu arşivle */ }) {
                Icon(Icons.Default.Archive, null)
            }
            IconButton(onClick = { /* toplu etiketle */ }) {
                Icon(Icons.Default.Label, null)
            }
        }
    )
}
```

- [ ] Uzun basma → seçim modu
- [ ] Seçili notları vurgula (border + checkmark)
- [ ] Seçim modunda TopBar değişsin
- [ ] Toplu sil, arşivle, etiketle
- [ ] Back tuşu seçimi iptal etsin

**Başarı kriteri:** 10 notu tek tek silmek yerine toplu seçebiliyorsun.

### Görev 5.4 — Arama iyileştirmeleri (1 gün)

```kotlin
// Etikette ara
val matchesSearch = note.title.contains(query, ignoreCase = true) ||
    note.content.contains(query, ignoreCase = true) ||
    (note.tag?.contains(query, ignoreCase = true) == true)

// Tarih aralığı filtresi
enum class DateRange { ALL, TODAY, WEEK, MONTH, YEAR }

fun filterByDate(notes: List<Note>, range: DateRange): List<Note> {
    val now = System.currentTimeMillis()
    val threshold = when (range) {
        DateRange.TODAY -> now - 86400000L
        DateRange.WEEK -> now - 604800000L
        DateRange.MONTH -> now - 2592000000L
        DateRange.YEAR -> now - 31536000000L
        DateRange.ALL -> 0L
    }
    return notes.filter { it.createdAt.time >= threshold }
}

// Arama geçmişi (DataStore'da saklansın)
val searchHistory: Flow<List<String>> = prefs.searchHistory

fun addToHistory(query: String) {
    // Son 5 unique sorguyu tut
}
```

- [ ] Etiketlerde arama
- [ ] Tarih aralığı filtresi (chip'ler)
- [ ] Arama geçmişi (son 5)
- [ ] Boş aramada öneriler göster

**Başarı kriteri:** Aramada daha fazla seçenek var, geçmiş öneriliyor.

### Görev 5.5 — Klavye shortcut'ları (tablet) (2-3 saat)

```kotlin
@Composable
fun HomeScreen(...) {
    val focusRequester = remember { FocusRequester() }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown && event.isCtrlPressed) {
                    when (event.key) {
                        Key.N -> { onNewNote(); true }
                        Key.F -> { isSearchActive = true; true }
                        Key.Comma -> { onSettings(); true }
                        else -> false
                    }
                } else false
            }
    ) {
        // ...
    }
    
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

// NoteEditScreen
Modifier.onKeyEvent { event ->
    if (event.type == KeyEventType.KeyDown && event.isCtrlPressed) {
        when (event.key) {
            Key.S -> { onSave(); true }
            Key.B -> { richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)); true }
            Key.I -> { richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)); true }
            else -> false
        }
    } else false
}
```

- [ ] Ctrl+N → yeni not
- [ ] Ctrl+F → ara
- [ ] Ctrl+S → kaydet
- [ ] Ctrl+B, I, U → bold/italic/underline
- [ ] Esc → geri

**Başarı kriteri:** Tablet + klavye ile verimli kullanılabiliyor.

---

## Aşama 6 — Öncelik Matrisi

> Bu bir "görev" değil, **karar rehberi**. Bir sonraki ne yapacağına karar verirken buradan bak.

### 🟢 Yüksek Etki × Düşük Efor (HEMEN YAP)

- [ ] Typography fix (10 dk)
- [ ] fallbackToDestructiveMigration kaldır (5 dk)
- [ ] Haptic feedback (1 saat)
- [ ] Material You (30 dk)
- [ ] Swipe-to-delete (2 saat)
- [ ] Hardcoded string'leri taşı (3 saat)
- [ ] Empty state Lottie (1 saat)
- [ ] MainActivity'yi böl (2 saat)

**Toplam:** ~10 saat. Bir hafta sonu.

### 🔵 Yüksek Etki × Yüksek Efor (PLANLA)

- [ ] Mood tema sistemi
- [ ] Kontrol listeleri
- [ ] Görsel ekleme
- [ ] Jetpack Navigation
- [ ] Widget
- [ ] Markdown
- [ ] Hilt + UseCase
- [ ] Şablonlar

### 🟡 Düşük Etki × Düşük Efor (BOŞ ZAMANDA)

- [ ] App icon redesign
- [ ] Splash screen
- [ ] About ekranı
- [ ] Ripple renkleri

### 🔴 Düşük Etki × Yüksek Efor (KAÇIN)

- [ ] Çok özel animasyonlar (önce temel UX)
- [ ] Premium abonelik modeli (önce kullanıcı tabanı)

---

## Aşama 7 — 8 Haftalık Yol Haritası

> Haftada 10-15 saat varsayımıyla.

### Hafta 1: Hızlı Kazanımlar
- [ ] Typography fix
- [ ] fallbackToDestructiveMigration kaldır
- [ ] MainActivity böl
- [ ] Hardcoded string'leri taşı
- [ ] Haptic feedback
- [ ] Material You

### Hafta 2: Yeni Tema Sistemi
- [ ] Mood temalar tasarla (Paper, Midnight, Focus, Sunset, Forest, Terminal)
- [ ] Her tema için font eşlemesi
- [ ] Canlı önizleme
- [ ] Tema seçim ekranı redesign

### Hafta 3: Mimari Yenileme
- [ ] Hilt ekle
- [ ] Navigation Compose
- [ ] Deep link desteği
- [ ] DataStore geçişi
- [ ] UseCase katmanı

### Hafta 4: UX İyileştirmeleri
- [ ] Swipe gestures
- [ ] Onboarding
- [ ] Empty state Lottie
- [ ] Batch selection
- [ ] Pull-to-new-note

### Hafta 5: Kontrol Listeleri + Görseller
- [ ] Checklist tipi
- [ ] Progress indicator
- [ ] Görsel ekleme
- [ ] Coil ile image loading
- [ ] Zoom view

### Hafta 6: Import/Export + Arşiv
- [ ] JSON/MD/TXT import
- [ ] PDF export
- [ ] Markdown export
- [ ] Arşivleme
- [ ] Şablonlar

### Hafta 7: Widget + Shortcuts + Gelişmiş Hatırlatıcılar
- [ ] Jetpack Glance widget
- [ ] App shortcuts
- [ ] Tekrarlayan hatırlatıcılar
- [ ] Sürüm geçmişi
- [ ] Uygulama kilidi

### Hafta 8: Polish + Yayın Hazırlığı
- [ ] Accessibility testleri
- [ ] i18n: İngilizce
- [ ] App icon + splash screen
- [ ] Play Store materyalleri
- [ ] Unit/UI test coverage %60
- [ ] Crashlytics

---

## Aşama 8 — Farklılaşma Stratejisi

> Bu bir teknik görev değil, **stratejik karar**. 1-2 saat düşünmeye değer.

### Görev 8.1 — Niche seç (1-2 saat düşünme)

Bu 6 niş arasından birini seç:

- [ ] **Günlük / Journaling** — ruh hali takibi, seri takibi, yıllık özet
- [ ] **Yazarlar için** — kelime sayacı, odak modu, markdown, distraction-free
- [ ] **Öğrenciler için** — ders bazlı organize, flashcard, OCR
- [ ] **Minimalist** — sadece gerekli özellikler, muhteşem görsellik
- [ ] **Developer notları** — code block, syntax highlight, terminal teması, Gist export ⭐ **ÖNERİM**
- [ ] **İkinci Beyin** — bi-directional linking, graph, şablonlar

### Görev 8.2 — Nişe uygun 3 "imza" özellik belirle

Developer niche için örnek:

- [ ] Code block syntax highlight (100+ dil)
- [ ] GitHub Gist'e tek tıkla export
- [ ] Git-benzeri versiyon geçmişi
- [ ] Terminal teması (default)
- [ ] Command palette (Ctrl+K — VSCode tarzı)
- [ ] Snippet library (kod parçaları kütüphanesi)

### Görev 8.3 — App Store açıklamasını şimdiden yaz

Uygulamanın pozisyonunu net belirler:

```
"Developer'lar için notepad — 
Markdown, syntax highlight, 
git-benzeri versiyon geçmişi, 
terminal teması.

Monokai dark ile tanışın."
```

**Başarı kriteri:** Bir arkadaşına 1 cümleyle anlatabildiğin bir konsep.

---

## Aşama 9 — Nereden Başlamalısın — Quick Wins

> **Bu hafta sonu 5-6 saatte bitirmen gerekenler.** Liste sırayla.

### Cumartesi Sabahı (2 saat)

- [ ] **09:00-09:10** — `Burak2Theme.kt`'de `typography = Typography` yap
- [ ] **09:10-09:15** — `fallbackToDestructiveMigration()` kaldır
- [ ] **09:15-09:45** — Material You dynamic color ekle
- [ ] **09:45-10:45** — Haptic feedback ekle (yıldız, sil, kaydet)
- [ ] **10:45-11:00** — Test et, screenshot al, önce/sonra karşılaştır

### Cumartesi Öğleden Sonra (2 saat)

- [ ] **14:00-16:00** — Swipe-to-delete ekle, undo snackbar ile

### Pazar (2 saat)

- [ ] **10:00-12:00** — `MainActivity.kt`'yi böl:
  - [ ] `HomeScreen.kt`
  - [ ] `NoteEditScreen.kt` (zaten var, ayrı dosyaya al)
  - [ ] `components/NoteItem.kt`
  - [ ] `components/DeletedNoteItem.kt`

### Haftasonu sonu kontrol listesi

- [ ] `MainActivity.kt` 200 satırın altında
- [ ] Outfit fontu görünüyor
- [ ] Swipe çalışıyor
- [ ] Yıldızlayınca titreşim
- [ ] Android 12+ cihazda dinamik renk çalışıyor
- [ ] `git commit -m "Quick wins: font, haptic, swipe, refactor"`

**Başarı kriteri:** Pazar akşamı "bir şey değişti" diyeceğin 5 gözle görülür iyileştirme.

---

## 📌 Notlar

- Her görevin sonunda **commit at**. "Huge refactor" commit'leri kötüdür.
- Bir aşamayı atla**. Örneğin Hilt'i atla, direkt özellik ekle — bu kabul edilebilir bir karardır, sadece sonra geri dönmek gerekir.
- **Mükemmel düşman iyiye**. Yarı yarıya yapılmış özellik, hiç yapılmamış özellikten iyidir. Ship et, feedback al, iterate.
- **Beta testçi bul**. 3 arkadaşına APK ver, 2 hafta kullansınlar, geri bildirim al.
- **Play Store'a erken koy**. Mükemmel olması gerekmiyor — kullanıcı feedback'i mükemmelliğe giden yoldur.

---

**İyi şanslar. Başar.** 🚀
