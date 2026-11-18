package com.tasbih.digitaltasbih.data

data class QuranTourStepItem(
    val title: String,
    val desc: String
)

data class QuranTourStrings(
    val stepOf: String,
    val skip: String,
    val next: String,
    val finish: String,
    val steps: List<QuranTourStepItem>
)

object QuranTourLocalization {
    private val data = mapOf(
        "en" to QuranTourStrings(
            stepOf = "Step {s} of {total}",
            skip = "Skip",
            next = "Next →",
            finish = "Start Reading ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Translation Language",
                    desc = "Select your preferred translation language directly from this bar, or tap 🌐 +19 to choose from 25 international languages."
                ),
                QuranTourStepItem(
                    title = "Browse by Surah, Juz & Favorites",
                    desc = "Switch seamlessly between all 114 Surahs, 30 Juz (Siparahs), or your bookmarked Favorite Ayahs."
                ),
                QuranTourStepItem(
                    title = "3 Reading Modes & Settings",
                    desc = "Tap ⚙️ Settings to switch between List View (with Ayah cards), Translation Only, and Real 15-Line Mushaf page layout."
                ),
                QuranTourStepItem(
                    title = "Pinch-to-Zoom & Ayah Tools",
                    desc = "Pinch with two fingers (zoom in/out) directly on Mushaf pages and List view. Each Ayah card also features 4 tools: Star, Copy, Audio & Tafsir."
                ),
            )
        ),
        "hi" to QuranTourStrings(
            stepOf = "कदम {s} / {total}",
            skip = "छोड़ें",
            next = "आगे →",
            finish = "पढ़ना शुरू करें ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "तर्जुमा भाषा चुनें",
                    desc = "यहाँ से सीधे अपनी पसंद की भाषा (हिन्दी, उर्दू, अंग्रेज़ी आदि) चुनें या '🌐 +19' पर टैप करके सभी 25 भाषाओं में तर्जुमा पढ़ें।"
                ),
                QuranTourStepItem(
                    title = "सूरह, पारा और पसंदीदा लिस्ट",
                    desc = "सभी 114 सूरह, 30 पारे (सिपारे), या अपनी पसंदीदा आयतों की लिस्ट के बीच आसानी से स्विच करें।"
                ),
                QuranTourStepItem(
                    title = "3 पढ़ने के मोड और सेटिंग्स",
                    desc = "⚙️ सेटिंग्स पर टैप करके 3 रीडिंग मोड्स (लिस्ट व्यू, सिर्फ़ तर्जुमा, या 15-लाइन मुसहफ़ पेज) और अरबी फ़ॉन्ट साइज़ चुनें।"
                ),
                QuranTourStepItem(
                    title = "पिंच ज़ूम और 4 आयत टूल्स",
                    desc = "मुसहफ़ पेज और लिस्ट व्यू पर दो उँगलियों से पिंच करके ज़ूम इन/आउट करें। हर आयत पर 4 टूल्स हैं: पसंदीदा, कॉपी, तिलावत और तफ़सीर।"
                ),
            )
        ),
        "ur" to QuranTourStrings(
            stepOf = "مرحلہ {s} از {total}",
            skip = "چھوڑیں",
            next = "اگلا →",
            finish = "تلاوت شروع کریں ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "ترجمہ کی زبان",
                    desc = "یہاں سے براہِ راست اپنی پسندیدہ ترجمہ کی زبان منتخب کریں یا '🌐 +19' پر ٹیپ کر کے تمام 25 زبانوں میں ترجمہ پڑھیں۔"
                ),
                QuranTourStepItem(
                    title = "سورت، پارہ اور فیورٹس",
                    desc = "تمام 114 سورتوں، 30 پاروں، یا اپنی محفوظ شدہ پسندیدہ آیات کی فہرست کے درمیان آسانی سے سوئچ کریں۔"
                ),
                QuranTourStepItem(
                    title = "پڑھنے کے 3 موڈز اور سیٹنگز",
                    desc = "⚙️ سیٹنگز پر ٹیپ کر کے 3 موڈز (لسٹ ویو، صرف ترجمہ، یا 15 سطری مصحف پیج) اور عربی فونٹ سائز کا انتخاب کریں۔"
                ),
                QuranTourStepItem(
                    title = "پنچ زوم اور 4 آیت ٹولز",
                    desc = "مصحف پیج اور لسٹ ویو پر دو انگلیوں سے پنچ کر کے زوم ان/آؤٹ کریں۔ ہر آیت پر 4 ٹولز ہیں: فیورٹ، کاپی، تلاوت اور تفسیر۔"
                ),
            )
        ),
        "ar" to QuranTourStrings(
            stepOf = "الخطوة {s} من {total}",
            skip = "تخطي",
            next = "التالي ←",
            finish = "ابدأ القراءة ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "لغة الترجمة",
                    desc = "اختر لغة الترجمة المفضلة مباشرة من هذا الشريط، أو اضغط 🌐 +19 للاختيار من بين 25 لغة عالمية."
                ),
                QuranTourStepItem(
                    title = "التنقل بالسورة والجزء والمفضلة",
                    desc = "تنقل بسلاسة بين جميع سور القرآن الـ 114، والأجزاء الثلاثين، أو آياتك المفضلة المحفوظة."
                ),
                QuranTourStepItem(
                    title = "3 أنماط للقراءة والإعدادات",
                    desc = "اضغط ⚙️ الإعدادات للتبديل بين عرض القائمة، والترجمة فقط، ومصحف المدينة المنورة الحقيقي (15 سطراً)."
                ),
                QuranTourStepItem(
                    title = "التكبير باللمس وأدوات الآية",
                    desc = "قم بالتكبير والتصغير بإصبعين مباشرة على صفحات المصحف وقائمة الآيات. وتحتوي كل آية على 4 أدوات: المفضلة، النسخ، التلاوة والتفسير."
                ),
            )
        ),
        "bn" to QuranTourStrings(
            stepOf = "ধাপ {s} / {total}",
            skip = "এড়িয়ে যান",
            next = "পরবর্তী →",
            finish = "পড়া শুরু করুন ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "অনুবাদ ভাষা নির্বাচন",
                    desc = "এই বার থেকে সরাসরি আপনার পছন্দের অনুবাদের ভাষা নির্বাচন করুন, অথবা ২৫টি ভাষার জন্য 🌐 +19 চাপুন।"
                ),
                QuranTourStepItem(
                    title = "সূরা, পারা ও প্রিয় আয়াত",
                    desc = "১১৪টি সূরা, ৩০টি পারা বা আপনার সংরক্ষিত প্রিয় আয়াতসমূহের মধ্যে সহজেই পরিবর্তন করুন।"
                ),
                QuranTourStepItem(
                    title = "৩টি পড়ার মোড ও সেটিংস",
                    desc = "⚙️ সেটিংসে ট্যাপ করে লিস্ট ভিউ, শুধু অনুবাদ, অথবা আসল ১৫-লাইনের ছাপা কুরআন মুসহাফ পেজ নির্বাচন করুন।"
                ),
                QuranTourStepItem(
                    title = "পিঞ্চ জুম ও ৪টি আয়াত টুল",
                    desc = "মুসহাফ পৃষ্ঠা ও লিস্ট ভিউতে দুই আঙুল দিয়ে পিঞ্চ করে জুম ইন/আউট করুন। প্রতিটি আয়াতে রয়েছে ৪টি টুল: স্টার, কপি, অডিও ও তাফসীর।"
                ),
            )
        ),
        "id" to QuranTourStrings(
            stepOf = "Langkah {s} dari {total}",
            skip = "Lewati",
            next = "Lanjut →",
            finish = "Mulai Membaca ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Bahasa Terjemahan",
                    desc = "Pilih bahasa terjemahan langsung dari bilah ini, atau ketuk 🌐 +19 untuk memilih dari 25 bahasa."
                ),
                QuranTourStepItem(
                    title = "Navigasi Surah, Juz & Favorit",
                    desc = "Beralih dengan mudah antara 114 Surah, 30 Juz, atau daftar Ayat Favorit yang Anda simpan."
                ),
                QuranTourStepItem(
                    title = "3 Mode Membaca & Pengaturan",
                    desc = "Ketuk ⚙️ Pengaturan untuk beralih antara Tampilan Daftar, Hanya Terjemahan, atau Mushaf Asli 15 Baris."
                ),
                QuranTourStepItem(
                    title = "Pinch-to-Zoom & Alat Ayat",
                    desc = "Cubit dengan 2 jari untuk memperbesar/memperkecil Mushaf dan teks. Setiap ayat memiliki 4 alat: Favorit, Salin, Audio & Tafsir."
                ),
            )
        ),
        "tr" to QuranTourStrings(
            stepOf = "Adım {s} / {total}",
            skip = "Geç",
            next = "İleri →",
            finish = "Okumaya Başla ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Çeviri Dili Seçimi",
                    desc = "Bu çubuktan doğrudan çeviri dilinizi seçin veya 25 dil arasından seçim yapmak için 🌐 +19'a dokunun."
                ),
                QuranTourStepItem(
                    title = "Sure, Cüz ve Favoriler",
                    desc = "114 Sure, 30 Cüz veya kaydettiğiniz Favori Ayetler arasında kolayca geçiş yapın."
                ),
                QuranTourStepItem(
                    title = "3 Okuma Modu ve Ayarlar",
                    desc = "Liste Görünümü, Yalnızca Çeviri veya Gerçek 15 Satırlı Mushaf sayfası arasında geçiş yapmak için ⚙️ Ayarlar'a dokunun."
                ),
                QuranTourStepItem(
                    title = "İki Parmakla Yakınlaştırma",
                    desc = "Mushaf ve liste görünümünde iki parmağınızla yakınlaştırıp uzaklaştırın. Her ayette 4 araç bulunur: Favori, Kopyala, Ses ve Tefsir."
                ),
            )
        ),
        "fr" to QuranTourStrings(
            stepOf = "Étape {s} sur {total}",
            skip = "Passer",
            next = "Suivant →",
            finish = "Commencer la lecture ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Langue de traduction",
                    desc = "Choisissez directement votre langue de traduction ici, ou appuyez sur 🌐 +19 pour choisir parmi 25 langues."
                ),
                QuranTourStepItem(
                    title = "Sourates, Juz et Favoris",
                    desc = "Basculez facilement entre les 114 sourates, les 30 Juz ou vos versets favoris enregistrés."
                ),
                QuranTourStepItem(
                    title = "3 modes de lecture et réglages",
                    desc = "Appuyez sur ⚙️ Réglages pour basculer entre la vue Liste, Traduction seule et le vrai Mushaf 15 lignes."
                ),
                QuranTourStepItem(
                    title = "Zoom par pincement et outils",
                    desc = "Pincez avec deux doigts pour zoomer/dézoomer sur les pages du Mushaf et la liste. 4 outils par verset : Favori, Copier, Audio & Tafsir."
                ),
            )
        ),
        "es" to QuranTourStrings(
            stepOf = "Paso {s} de {total}",
            skip = "Omitir",
            next = "Siguiente →",
            finish = "Empezar a leer ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Idioma de traducción",
                    desc = "Seleccione su idioma de traducción directamente aquí, o toque 🌐 +19 para elegir entre 25 idiomas."
                ),
                QuranTourStepItem(
                    title = "Suras, Juz y Favoritos",
                    desc = "Cambie fácilmente entre las 114 Suras, los 30 Juz o sus aleyas favoritas guardadas."
                ),
                QuranTourStepItem(
                    title = "3 modos de lectura y ajustes",
                    desc = "Toque ⚙️ Ajustes para alternar entre Vista de lista, Solo traducción y el auténtico Mushaf de 15 líneas."
                ),
                QuranTourStepItem(
                    title = "Zoom con pellizco y herramientas",
                    desc = "Pellizque con dos dedos para acercar o alejar el Mushaf y la lista. Cada aleya incluye 4 herramientas: Favorito, Copiar, Audio y Tafsir."
                ),
            )
        ),
        "ru" to QuranTourStrings(
            stepOf = "Шаг {s} из {total}",
            skip = "Пропустить",
            next = "Далее →",
            finish = "Начать чтение ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Язык перевода",
                    desc = "Выберите язык перевода прямо здесь или нажмите 🌐 +19 для выбора из 25 языков мира."
                ),
                QuranTourStepItem(
                    title = "Суры, Джузы и Избранное",
                    desc = "Легко переключайтесь между 114 сурами, 30 джузами или сохраненными избранными аятами."
                ),
                QuranTourStepItem(
                    title = "3 режима чтения и настройки",
                    desc = "Нажмите ⚙️ Настройки для выбора между списком карточек, только переводом и настоящим 15-строчным Мусхафом."
                ),
                QuranTourStepItem(
                    title = "Масштабирование щипком",
                    desc = "Масштабируйте страницу двумя пальцами (зум). Для каждого аята доступны 4 кнопки: Избранное, Копировать, Аудио и Тафсир."
                ),
            )
        ),
        "de" to QuranTourStrings(
            stepOf = "Schritt {s} von {total}",
            skip = "Überspringen",
            next = "Weiter →",
            finish = "Lesen beginnen ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Übersetzungssprache",
                    desc = "Wählen Sie Ihre Übersetzungssprache direkt hier oder tippen Sie auf 🌐 +19 für 25 Sprachen."
                ),
                QuranTourStepItem(
                    title = "Suren, Juz & Favoriten",
                    desc = "Wechseln Sie mühelos zwischen allen 114 Suren, 30 Juz oder Ihren gespeicherten Versen."
                ),
                QuranTourStepItem(
                    title = "3 Lesemodi & Einstellungen",
                    desc = "Tippen Sie auf ⚙️ Einstellungen für Listenansicht, Nur Übersetzung oder den echten 15-Zeilen-Mushaf."
                ),
                QuranTourStepItem(
                    title = "Pinch-to-Zoom & Werkzeuge",
                    desc = "Zoomen Sie mit zwei Fingern auf den Mushaf-Seiten und der Liste. Jeder Vers bietet 4 Werkzeuge: Stern, Kopieren, Audio & Tafsir."
                ),
            )
        ),
        "fa" to QuranTourStrings(
            stepOf = "گام {s} از {total}",
            skip = "رد کردن",
            next = "بعدی →",
            finish = "شروع قرائت ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "زبان ترجمه",
                    desc = "زبان ترجمه مورد نظر خود را مستقیم از این نوار انتخاب کنید یا برای ۲۵ زبان روی 🌐 +19 بزنید."
                ),
                QuranTourStepItem(
                    title = "سوره‌ها، جزءها و نشان‌شده‌ها",
                    desc = "به راحتی بین ۱۱۴ سوره، ۳۰ جزء و آیات برگزیده خود جابجا شوید."
                ),
                QuranTourStepItem(
                    title = "۳ حالت خواندن و تنظیمات",
                    desc = "با زدن روی ⚙️ تنظیمات، بین نمایش فهرستی، فقط ترجمه، و مصحف واقعی ۱۵ خطی تغییر حالت دهید."
                ),
                QuranTourStepItem(
                    title = "بزرگ‌نمایی با دو انگشت",
                    desc = "با دو انگشت روی صفحات مصحف و فهرست زوم کنید. هر آیه دارای ۴ ابزار است: برگزیده، کپی، صوت و تفسیر."
                ),
            )
        ),
        "ms" to QuranTourStrings(
            stepOf = "Langkah {s} dari {total}",
            skip = "Langkau",
            next = "Seterusnya →",
            finish = "Mula Membaca ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Bahasa Terjemahan",
                    desc = "Pilih bahasa terjemahan langsung dari bar ini, atau ketik 🌐 +19 untuk 25 pilihan bahasa."
                ),
                QuranTourStepItem(
                    title = "Surah, Juzuk & Kegemaran",
                    desc = "Beralih dengan mudah antara 114 Surah, 30 Juzuk, atau senarai Ayat Kegemaran anda."
                ),
                QuranTourStepItem(
                    title = "3 Mod Membaca & Tetapan",
                    desc = "Ketik ⚙️ Tetapan untuk memilih antara Paparan Senarai, Terjemahan Sahaja, atau Mushaf Sebenar 15 Baris."
                ),
                QuranTourStepItem(
                    title = "Cubit untuk Zum & Alat Ayat",
                    desc = "Cubit dengan 2 jari untuk zum masuk/keluar pada halaman Mushaf dan senarai. Setiap ayat mempunyai 4 alat: Kegemaran, Salin, Audio & Tafsir."
                ),
            )
        ),
        "ta" to QuranTourStrings(
            stepOf = "படி {s} / {total}",
            skip = "தவிர்",
            next = "அடுத்து →",
            finish = "ஓத தொடங்குங்கள் ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "மொழிபெயர்ப்பு மொழி",
                    desc = "இங்கிருந்து நேரடியாக உங்கள் மொழிபெயர்ப்பு மொழியைத் தேர்வுசெய்யவும், அல்லது 25 மொழிகளுக்கு 🌐 +19 ஐ அழுத்தவும்."
                ),
                QuranTourStepItem(
                    title = "சூரா, ஜுஸ் & விருப்பமானவை",
                    desc = "114 சூராக்கள், 30 ஜுஸ்கள் அல்லது உங்கள் விருப்பமான ஆயத்துகளுக்கு எளிதாக மாறவும்."
                ),
                QuranTourStepItem(
                    title = "3 வாசிப்பு முறைகள்",
                    desc = "⚙️ அமைப்புகளில் பட்டியல் பார்வை, மொழிபெயர்ப்பு மட்டும், அல்லது உண்மையான 15-வரி முஸஹஃப் பக்கத்தைத் தேர்வுசெய்யவும்."
                ),
                QuranTourStepItem(
                    title = "இரண்டு விரல் ஜூம் & கருவிகள்",
                    desc = "முஸஹஃப் மற்றும் பட்டியல் பக்கங்களில் இரண்டு விரல்களால் பிஞ்ச் செய்து ஜூம் செய்யவும். ஒவ்வொரு ஆயத்திலும் 4 கருவிகள் உள்ளன: ஸ்டார், காப்பி, ஆடியோ & தஃப்சீர்."
                ),
            )
        ),
        "te" to QuranTourStrings(
            stepOf = "దశ {s} / {total}",
            skip = "దాటవేయి",
            next = "తదుపరి →",
            finish = "పఠనం ప్రారంభించండి ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "అనువాద భాష",
                    desc = "ఇక్కడ నుండి నేరుగా మీ అనువాద భాషను ఎంచుకోండి, లేదా 25 భాషల కోసం 🌐 +19 నొక్కండి."
                ),
                QuranTourStepItem(
                    title = "సూరా, జుజ్ మరియు ఇష్టమైనవి",
                    desc = "114 సూరాలు, 30 జుజ్‌లు లేదా మీరు సేవ్ చేసిన ఇష్టమైన ఆయత్‌ల మధ్య సులభంగా మారండి."
                ),
                QuranTourStepItem(
                    title = "3 పఠన రీతులు & సెట్టింగ్‌లు",
                    desc = "లిస్ట్ వ్యూ, కేవలం అనువాదం లేదా అసలైన 15-లైన్ల ముస్హఫ్ పేజీని ఎంచుకోవడానికి ⚙️ సెట్టింగ్‌లను నొక్కండి."
                ),
                QuranTourStepItem(
                    title = "పించ్ జూమ్ & ఆయత్ టూల్స్",
                    desc = "ముస్హఫ్ మరియు లిస్ట్‌పై రెండు వేళ్లతో పించ్ చేసి జూమ్ చేయండి. ప్రతి ఆయత్‌లో 4 సాధనాలు ఉన్నాయి: ఇష్టమైనవి, కాపీ, ఆడియో మరియు తఫ్సీర్."
                ),
            )
        ),
        "ml" to QuranTourStrings(
            stepOf = "ഘട്ടം {s} / {total}",
            skip = "ഒഴിവാക്കുക",
            next = "അടുത്തത് →",
            finish = "വായിച്ചു തുടങ്ങുക ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "വിവർത്തന ഭാഷ",
                    desc = "ഇവിടെ നിന്ന് നേരിട്ട് വിവർത്തന ഭാഷ തിരഞ്ഞെടുക്കുക, അല്ലെങ്കിൽ 25 ഭാഷകൾക്കായി 🌐 +19 ടാപ്പ് ചെയ്യുക."
                ),
                QuranTourStepItem(
                    title = "സൂറത്ത്, ജുസ് & പ്രിയപ്പെട്ടവ",
                    desc = "114 സൂറത്തുകൾ, 30 ജുസുകൾ, അല്ലെങ്കിൽ നിങ്ങൾ സൂക്ഷിച്ച പ്രിയപ്പെട്ട ആയത്തുകൾ എന്നിവയിലേക്ക് മാറുക."
                ),
                QuranTourStepItem(
                    title = "3 വായനാ രീതികൾ",
                    desc = "ലിസ്റ്റ് വ്യൂ, വിവർത്തനം മാത്രം, അല്ലെങ്കിൽ യഥാർത്ഥ 15-വരി മുസ്ഹഫ് പേജ് തിരഞ്ഞെടുക്കാൻ ⚙️ സെറ്റിംഗ്സ് ടാപ്പ് ചെയ്യുക."
                ),
                QuranTourStepItem(
                    title = "പിഞ്ച് സൂമും 4 ടൂളുകളും",
                    desc = "മുസ്ഹഫിലും ലിസ്റ്റിലും രണ്ട് വിരലുകൾ കൊണ്ട് പിഞ്ച് ചെയ്ത് സൂം ചെയ്യുക. ഓരോ ആയത്തിലും 4 ടൂളുകളുണ്ട്: സ്റ്റാർ, കോപ്പി, ഓഡിയോ & തഫ്സീർ."
                ),
            )
        ),
        "gu" to QuranTourStrings(
            stepOf = "પગલું {s} / {total}",
            skip = "છોડો",
            next = "આગળ →",
            finish = "વાંચવાનું શરૂ કરો ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "અનુવાદ ભાષા પસંદ કરો",
                    desc = "અહીંથી સીધી તમારી ભાષા પસંદ કરો અથવા 25 ભાષાઓ માટે 🌐 +19 પર ટેપ કરો."
                ),
                QuranTourStepItem(
                    title = "સૂરહ, પારા અને ફેવરિટ્સ",
                    desc = "114 સૂરહ, 30 પારા અથવા તમારી મનપસંદ આયતો વચ્ચે સરળતાથી સ્વિચ કરો."
                ),
                QuranTourStepItem(
                    title = "3 રીડિંગ મોડ્સ અને સેટિંગ્સ",
                    desc = "લિસ્ટ વ્યૂ, માત્ર અનુવાદ અથવા અસલી 15-લાઇન મુસહફ પેજ પસંદ કરવા ⚙️ સેટિંગ્સ પર ટેપ કરો."
                ),
                QuranTourStepItem(
                    title = "પિંચ ઝૂમ અને 4 આયત ટૂલ્સ",
                    desc = "મુસહફ પેજ અને લિસ્ટ પર બે આંગળીઓથી પિંચ કરીને ઝૂમ ઇન/આઉટ કરો. દરેક આયત પર 4 ટૂલ્સ છે: ફેવરિટ, કૉપિ, ઑડિયો અને તફસીર."
                ),
            )
        ),
        "mr" to QuranTourStrings(
            stepOf = "पायरी {s} / {total}",
            skip = "वगळा",
            next = "पुढे →",
            finish = "वाचन सुरू करा ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "भाषांतर भाषा",
                    desc = "येथून थेट तुमची भाषा निवडा किंवा 25 भाषांसाठी 🌐 +19 वर टॅप करा."
                ),
                QuranTourStepItem(
                    title = "सुरा, पारा आणि आवडते",
                    desc = "114 सुरा, 30 पारे किंवा आपल्या आवडत्या आयतांच्या यादीमध्ये सहज स्विच करा."
                ),
                QuranTourStepItem(
                    title = "3 वाचन पद्धती आणि सेटिंग्ज",
                    desc = "यादी दृश्य, फक्त भाषांतर किंवा अस्सल 15-ओळींचा मुसहफ निवडण्यासाठी ⚙️ सेटिंग्जवर टॅप करा."
                ),
                QuranTourStepItem(
                    title = "पिंच झूम आणि 4 साधने",
                    desc = "मुसहफ आणि यादीवर दोन बोटांनी पिंच करून झूम करा. प्रत्येक आयतीवर 4 साधने आहेत: स्टार, कॉपी, ऑडिओ आणि तफसीर."
                ),
            )
        ),
        "pa" to QuranTourStrings(
            stepOf = "ਕਦਮ {s} / {total}",
            skip = "ਛੱਡੋ",
            next = "ਅੱਗੇ →",
            finish = "ਪੜ੍ਹਨਾ ਸ਼ੁਰੂ ਕਰੋ ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "ਅਨੁਵਾਦ ਭਾਸ਼ਾ",
                    desc = "ਇੱਥੋਂ ਸਿੱਧਾ ਆਪਣੀ ਪਸੰਦੀਦਾ ਭਾਸ਼ਾ ਚੁਣੋ ਜਾਂ 25 ਭਾਸ਼ਾਵਾਂ ਲਈ 🌐 +19 'ਤੇ ਟੈਪ ਕਰੋ।"
                ),
                QuranTourStepItem(
                    title = "ਸੂਰਾ, ਪਾਰਾ ਅਤੇ ਪਸੰਦੀਦਾ",
                    desc = "ਸਾਰੀਆਂ 114 ਸੂਰਤਾਂ, 30 ਪਾਰਿਆਂ ਜਾਂ ਆਪਣੀ ਪਸੰਦੀਦਾ ਆਇਤਾਂ ਵਿੱਚ ਆਸਾਨੀ ਨਾਲ ਸਵਿੱਚ ਕਰੋ।"
                ),
                QuranTourStepItem(
                    title = "3 ਪੜ੍ਹਨ ਦੇ ਢੰਗ ਅਤੇ ਸੈਟਿੰਗਾਂ",
                    desc = "ਲਿਸਟ ਵਿਊ, ਸਿਰਫ਼ ਅਨੁਵਾਦ ਜਾਂ ਅਸਲ 15-ਲਾਈਨ ਮੁਸਹਫ਼ ਚੁਣਨ ਲਈ ⚙️ ਸੈਟਿੰਗਾਂ 'ਤੇ ਟੈਪ ਕਰੋ।"
                ),
                QuranTourStepItem(
                    title = "ਪਿੰਚ ਜ਼ੂਮ ਅਤੇ 4 ਆਇਤ ਟੂਲ",
                    desc = "ਮੁਸਹਫ਼ ਅਤੇ ਲਿਸਟ 'ਤੇ ਦੋ ਉਂਗਲਾਂ ਨਾਲ ਪਿੰਚ ਕਰਕੇ ਜ਼ੂਮ ਕਰੋ। ਹਰ ਆਇਤ 'ਤੇ 4 ਟੂਲ ਹਨ: ਸਟਾਰ, ਕਾਪੀ, ਆਡੀਓ ਅਤੇ ਤਫ਼ਸੀਰ।"
                ),
            )
        ),
        "kn" to QuranTourStrings(
            stepOf = "ಹಂತ {s} / {total}",
            skip = "ಬಿಟ್ಟುಬಿಡಿ",
            next = "ಮುಂದೆ →",
            finish = "ಓದಲು ಪ್ರಾರಂಭಿಸಿ ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "ಅನುವಾದ ಭಾಷೆ",
                    desc = "ಇಲ್ಲಿಂದ ನೇರವಾಗಿ ನಿಮ್ಮ ಅನುವಾದ ಭಾಷೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ, ಅಥವಾ 25 ಭಾಷೆಗಳಿಗಾಗಿ 🌐 +19 ಟ್ಯಾಪ್ ಮಾಡಿ."
                ),
                QuranTourStepItem(
                    title = "ಸೂರಾ, ಜುಜ್ ಮತ್ತು ಮೆಚ್ಚಿನವು",
                    desc = "114 ಸೂರಾಗಳು, 30 ಜುಜ್‌ಗಳು ಅಥವಾ ನಿಮ್ಮ ಮೆಚ್ಚಿನ ಆಯತ್‌ಗಳ ನಡುವೆ ಸುಲಭವಾಗಿ ಬದಲಾಯಿಸಿ."
                ),
                QuranTourStepItem(
                    title = "3 ಓದುವ ವಿಧಾನಗಳು",
                    desc = "ಪಟ್ಟಿ ವೀಕ್ಷಣೆ, ಅನುವಾದ ಮಾತ್ರ, ಅಥವಾ 15-ಸಾಲುಗಳ ಮುಸ್ಹಫ್ ಆಯ್ಕೆ ಮಾಡಲು ⚙️ ಸೆಟ್ಟಿಂಗ್‌ಗಳನ್ನು ಟ್ಯಾಪ್ ಮಾಡಿ."
                ),
                QuranTourStepItem(
                    title = "ಪಿಂಚ್ ಜೂಮ್ ಮತ್ತು 4 ಪರಿಕರಗಳು",
                    desc = "ಮುಸ್ಹಫ್ ಮತ್ತು ಪಟ್ಟಿಯಲ್ಲಿ ಎರಡು ಬೆರಳುಗಳಿಂದ ಪಿಂಚ್ ಮಾಡಿ ಜೂಮ್ ಮಾಡಿ. ಪ್ರತಿ ಆಯತ್‌ನಲ್ಲಿ 4 ಪರಿಕರಗಳಿವೆ: ಸ್ಟಾರ್, ನಕಲು, ಆಡಿಯೋ ಮತ್ತು ತಫ್ಸೀರ್."
                ),
            )
        ),
        "ps" to QuranTourStrings(
            stepOf = "ګام {s} له {total}",
            skip = "تېرېدل",
            next = "بل →",
            finish = "تلاوت پیل کړئ ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "د ژباړې ژبه",
                    desc = "دلته له شريط څخه خپله ژبه وټاکئ، یا د ۲۵ ژبو لپاره په 🌐 +19 ووهئ."
                ),
                QuranTourStepItem(
                    title = "سورتونه، سیپارې او خوښ شوي",
                    desc = "د ټولو ۱۱۴ سورتونو، ۳۰ سیپارو او خوښ شویو آیتونو ترمنځ وګرځئ."
                ),
                QuranTourStepItem(
                    title = "د لوستلو ۳ حالتونه",
                    desc = "د لست، یوازې ژباړې، او ۱۵ کرښو مصحف ترمنځ د بدلولو لپاره په ⚙️ ترتیبات ووهئ."
                ),
                QuranTourStepItem(
                    title = "په دوه ګوتو زوم او ۴ وسیلې",
                    desc = "په مصحف او لست پاڼو باندې په دوه ګوتو زوم ان او اوټ وکړئ. هر آیت ۴ تڼۍ لري: خوښول، کاپي، تلاوت او تفسیر."
                ),
            )
        ),
        "uz" to QuranTourStrings(
            stepOf = "{s} / {total}-qadam",
            skip = "O'tkazib yuborish",
            next = "Keyingi →",
            finish = "O'qishni boshlash ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Tarjima tili",
                    desc = "Tarjima tilini to'g'ridan-to'g'ri shu yerdan tanlang yoki 25 ta til uchun 🌐 +19 tugmasini bosing."
                ),
                QuranTourStepItem(
                    title = "Suralar, Juzlar va Sevimlilar",
                    desc = "114 ta sura, 30 ta juz yoki saqlangan sevimli oyatlaringiz o'rtasida osongina o'ting."
                ),
                QuranTourStepItem(
                    title = "3 ta o'qish rejimi va sozlamalar",
                    desc = "Ro'yxat ko'rinishi, faqat tarjima yoki 15 qatorli haqiqiy Mus'hafni tanlash uchun ⚙️ Sozlamalarga kiring."
                ),
                QuranTourStepItem(
                    title = "Barmoq bilan masshtablash",
                    desc = "Mus'haf va ro'yxat sahifalarida ikki barmoq bilan yaqinlashtiring/uzoqlashtiring. Har bir oyatda 4 ta vosita mavjud: Sevimli, Nusxa, Audio va Tafsir."
                ),
            )
        ),
        "so" to QuranTourStrings(
            stepOf = "Tallaabada {s} ee {total}",
            skip = "Ka gudub",
            next = "Xiga →",
            finish = "Bilow Akhriska ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Luuqadda Tarjumaadda",
                    desc = "Ka dooro luuqadda tarjumaadda halkan, ama taabo 🌐 +19 si aad u hesho 25 luuqadood."
                ),
                QuranTourStepItem(
                    title = "Suuradaha, Juska & Xulashada",
                    desc = "U kala beddel 114-ka Suuradood, 30-ka Juz, ama Aayadaha aad jeceshahay si fudud."
                ),
                QuranTourStepItem(
                    title = "3 Hab oo Akhris & Dejinta",
                    desc = "Taabo ⚙️ Dejinta si aad u kala doorato Liiska, Tarjumaad kaliya, ama Bogga 15-Sadarka ah ee Mushafka."
                ),
                QuranTourStepItem(
                    title = "Ku weyneey Labo Far & Aaladaha",
                    desc = "Labo farood ku weyneey ama ku yaree bogagga Mushafka. Aayad kasta waxay leedahay 4 aaladood: Xiddig, Nuqul, Dhageyso & Tafsiir."
                ),
            )
        ),
        "ha" to QuranTourStrings(
            stepOf = "Mataki {s} cikin {total}",
            skip = "Tsallake",
            next = "Na gaba →",
            finish = "Fara Karatu ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Harshen Fassara",
                    desc = "Zaɓi harshen fassarar Kur'ani kai tsaye a nan, ko danna 🌐 +19 don zaɓar daga harsuna 25."
                ),
                QuranTourStepItem(
                    title = "Surori, Juz'i da Masoya",
                    desc = "Sauya cikin sauƙi tsakanin Surori 114, Juz'i 30, ko ayoyin da kuka fi so."
                ),
                QuranTourStepItem(
                    title = "Hanyoyin Karatu 3 & Saituna",
                    desc = "Danna ⚙️ Saituna don sauyawa tsakanin Jerin Aaya, Fassara kaɗai, da shafin Mus'hafi na layi 15."
                ),
                QuranTourStepItem(
                    title = "Faɗaɗa da Yatsu biyu",
                    desc = "Yi amfani da yatsu biyu don faɗaɗa shafin Mus'hafi da rubutu. Kowane aya yana da kayan aiki 4: So, Kwafi, Sauti da Tafsiri."
                ),
            )
        ),
        "sw" to QuranTourStrings(
            stepOf = "Hatua ya {s} kati ya {total}",
            skip = "Ruka",
            next = "Inayofuata →",
            finish = "Anza Kusoma ✨",
            steps = listOf(
                QuranTourStepItem(
                    title = "Lugha ya Tafsiri",
                    desc = "Chagua lugha ya tafsiri moja kwa moja hapa, au gusa 🌐 +19 kuchagua kutoka lugha 25."
                ),
                QuranTourStepItem(
                    title = "Sura, Juzuu na Vipendwa",
                    desc = "Badilisha kwa urahisi kati ya Sura 114, Juzuu 30, au Aya zako ulizozihifadhi."
                ),
                QuranTourStepItem(
                    title = "Njia 3 za Kusoma & Mipangilio",
                    desc = "Gusa ⚙️ Mipangilio kubadili kati ya Orodha, Tafsiri Pekee, na ukurasa wa Mushaf wa mistari 15."
                ),
                QuranTourStepItem(
                    title = "Kukuza kwa Vidole viwili",
                    desc = "Bana kwa vidole viwili kukuza ukurasa wa Mushaf na maandishi. Kila aya ina zana 4: Nyota, Nakili, Sauti na Tafsiri."
                ),
            )
        ),
    )

    fun get(lang: String): QuranTourStrings {
        val code = lang.lowercase().split("-")[0]
        return data[code] ?: data["en"]!!
    }
}
