
}
const JUMMAH_DEFAULT_ITEMS = [
  // 1. Using Miswak & Oral Cleaning
  { id: 'miswak', icon: '🪥', hadiths: {
    en: 'Sahih Muslim 846', hi: 'सहीह मुस्लिम 846', ur: 'صحیح مسلم 846', ar: 'صحيح مسلم ٨٤٦',
    bn: 'সহীহ মুসলিম ৮৪৬', ta: 'ஸஹீஹ் முஸ்லிம் 846', te: 'సహీహ్ ముస్లిం 846', ml: 'സ്വഹീഹ് മുസ്ലിം 846',
    gu: 'સહીહ મુસ્લિમ 846', mr: 'सहीह मुस्लिम 846', pa: 'ਸਹੀਹ ਮੁਸਲਿਮ 846', kn: 'ಸಹೀಹ್ ಮುಸ್ಲಿಮ್ 846',
    or: 'ସହିହ ମୁସଲିମ 846', as: 'ছহিহ মুছলিম ৮৪৬', id: 'Shahih Muslim 846', ms: 'Shahih Muslim 846',
    tr: 'Sahih-i Müslim 846', fa: 'صحیح مسلم ۸۴۶', ha: 'Sahih Muslim 846', sw: 'Sahih Muslim 846',
    so: 'Saxiiq Muslim 846', fr: 'Sahih Muslim 846', de: 'Sahih Muslim 846', ru: 'Сахих Муслим 846', zh: '《穆斯林圣训实录》846'
  }, titles: {
    en: 'Using Miswak & Oral Cleaning', hi: 'मिस्वाक व दांत साफ़ करना', ur: 'مسواک کرنا و دانت صاف کرنا', ar: 'السواك وطهارة الفم',
    bn: 'মিসওয়াক করা ও মুখ পরিষ্কার', ta: 'மிஸ்வாக் செய்தல்', te: 'మిస్వాక్ చేయడం', ml: 'മിസ്‌വാക്ക് ചെയ്യുക',
    gu: 'મિસ્વાક કરવું', mr: 'मिस्वाक करणे', pa: 'ਮਿਸਵਾਕ ਕਰਨਾ', kn: 'ಮಿಸ್ವಾಕ್ ಮಾಡುವುದು',
    or: 'ମିସଓ୍ବାକ କରିବା', as: 'মিছৱাক কৰা', id: 'Bersiwak & Membersihkan Gigi', ms: 'Bersiwak & Membersihkan Gigi',
    tr: 'Misvak Kullanmak ve Ağız Temizliği', fa: 'مسواک زدن و پاکیزگی دهان', ha: 'Yin Asawaki', sw: 'Kupiga Mswaki',
    so: 'Cadayanashada (Miswaak)', fr: 'Utiliser le Siwak et nettoyer les dents', de: 'Verwendung des Siwak & Mundhygiene',
    ru: 'Использование сивака и чистка зубов', zh: '使用密斯瓦克刷牙洁齿'
  }, descs: {
    en: 'Cleaning teeth and purifying the mouth before the Friday prayer.',
    hi: 'नमाज़-ए-जुमा से पहले मुंह को पाक और ताज़ा रखना।',
    ur: 'نمازِ جمعہ سے پہلے منہ کو پاک اور تروتازہ رکھنا۔',
    ar: 'تطهير الفم بالسواك قبل صلاة الجمعة.',
    bn: 'জুমার নামাযের পূর্বে মিসওয়াক করে মুখ পরিষ্কার রাখা।'
  }},
  // 2. Taking a Bath (Ghusl)
  { id: 'ghusl', icon: '🚿', hadiths: {
    en: 'Sahih al-Bukhari 877', hi: 'सहीह अल-बुख़ारी 877', ur: 'صحیح بخاری 877', ar: 'صحيح البخاري ٨٧٧',
    bn: 'সহীহ বুখারী ৮৭৭', ta: 'ஸஹீஹ் புகாரி 877', te: 'సహీహ్ అల్-బుఖారీ 877', ml: 'സ്വഹീഹ് അൽ-ബുഖാരി 877',
    gu: 'સહીહ અલ-બુખારી 877', mr: 'सहीह अल-बुखारी 877', pa: 'ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 877', kn: 'ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 877',
    or: 'ସହିହ ଅଲ-ବୁଖାରୀ 877', as: 'ছহিহ বুখাৰী ৮৭৭', id: 'Shahih al-Bukhari 877', ms: 'Shahih al-Bukhari 877',
    tr: 'Sahih-i Buhari 877', fa: 'صحیح بخاری ۸۷۷', ha: 'Sahih al-Bukhari 877', sw: 'Sahih al-Bukhari 877',
    so: 'Saxiiq al-Bukhaari 877', fr: 'Sahih al-Bukhari 877', de: 'Sahih al-Bukhari 877', ru: 'Сахих аль-Бухари 877', zh: '《布哈里圣训实录》877'
  }, titles: {
    en: 'Taking a Bath (Ghusl)', hi: 'ग़ुस्ल करना (जुमे का स्नान)', ur: 'غسل کرنا', ar: 'الاغتسال يوم الجمعة',
    bn: 'গোসল করা', ta: 'குளித்தல் (குஸ்ல்)', te: 'స్నానం చేయడం (గుస్ల్)', ml: 'കുളിക്കുക (ഗുസ്ൽ)',
    gu: 'ગુસ્લ કરવું (સ્નાન)', mr: 'गुस्ल करणे (स्नान)', pa: 'ਗੁਸਲ ਕਰਨਾ (ਇਸ਼ਨਾਨ)', kn: 'ಸ್ನಾನ ಮಾಡುವುದು (ಗುಸ್ಲ್)',
    or: 'ଗୋସଲ କରିବା', as: 'গোসল কৰা', id: 'Mandi Hari Jumat (Ghusl)', ms: 'Mandi Sunat Jumaat (Ghusl)',
    tr: 'Cuma Guslü Almak', fa: 'غسل روز جمعه', ha: 'Wankan Ranar Jumaa', sw: 'Kuoga Siku ya Ijumaa',
    so: 'Maydhashada Jimcaha', fr: 'Prendre le bain rituel (Ghusl)', de: 'Das Freitagsbad (Ghusl) vollziehen',
    ru: 'Совершение полного омовения (Гусль)', zh: '主麻日沐浴（大净）'
  }, descs: {
    en: 'Taking a bath on Friday is an emphasized Sunnah for purification.',
    hi: 'जुमे के दिन ग़ुस्ल करना सुन्नते मौकदा है।',
    ur: 'جمعہ کے دن غسل کرنا سنتِ مؤکدہ ہے۔',
    ar: 'الغسل يوم الجمعة سنة مؤكدة للطهارة.',
    bn: 'জুমার দিন গোসল করা সুন্নাতে মুয়াক্কাদাহ।'
  }},
  // 3. Clipping Nails & Grooming
  { id: 'nails', icon: '<svg width="28" height="28" viewBox="0 0 36 36" fill="none" style="filter:drop-shadow(0 2px 4px rgba(0,0,0,0.18));"><path d="M8 26C6.8 24.8 7.2 22.8 8.8 22L26 13C27.5 12.2 29.5 13.2 29.5 15V21C29.5 22.8 27.5 23.8 26 23L11.5 28.5C10 29 8.8 27.5 8 26Z" fill="url(#nc_base)" stroke="#64748B" stroke-width="1.2"/><path d="M10 13L26 5.8C27.8 5 30 6.4 30 8.4V10L14 26C13 27 11.2 26.8 10.2 25.8L8.2 23.8C7.2 22.8 7.5 21 8.8 20.2L24 12" fill="url(#nc_lever)" stroke="#475569" stroke-width="1.2"/><path d="M11.5 12.5L26 6.2C27.2 5.6 28.5 6.5 28.5 7.8L14 24" stroke="#FFFFFF" stroke-width="1.2" stroke-linecap="round"/><circle cx="12" cy="22" r="2.2" fill="#334155" stroke="#E2E8F0" stroke-width="0.8"/><line x1="20" y1="9" x2="22" y2="12" stroke="#64748B" stroke-width="1" stroke-linecap="round"/><line x1="23" y1="7.8" x2="25" y2="10.8" stroke="#64748B" stroke-width="1" stroke-linecap="round"/><defs><linearGradient id="nc_lever" x1="8" y1="5" x2="30" y2="26" gradientUnits="userSpaceOnUse"><stop stop-color="#FFFFFF"/><stop offset="0.3" stop-color="#E2E8F0"/><stop offset="0.7" stop-color="#94A3B8"/><stop offset="1" stop-color="#64748B"/></linearGradient><linearGradient id="nc_base" x1="7" y1="12" x2="30" y2="28" gradientUnits="userSpaceOnUse"><stop stop-color="#CBD5E1"/><stop offset="0.6" stop-color="#94A3B8"/><stop offset="1" stop-color="#475569"/></linearGradient></defs></svg>', hadiths: {
    en: 'Sunan al-Bayhaqi', hi: 'सुनन अल-बैहक़ी', ur: 'سنن البیہقی', ar: 'سنن البيهقي',
    bn: 'সুনানুল বায়হাকী', ta: 'ஸுனன் பைஹகீ', te: 'సునన్ అల్-బైహఖీ', ml: 'സുനൻ അൽ-ബൈഹഖി',
    gu: 'સુનન અલ-બયહકી', mr: 'सुनन अल-बैहकी', pa: 'ਸੁਨਨ ਅਲ-ਬੈਹਕੀ', kn: 'ಸುನನ್ ಅಲ್-ಬೈಹಖಿ',
    or: 'ସୁନନ ଅଲ-ବାଇହାକୀ', as: 'সুনান বাইহাকী', id: 'Sunan al-Baihaqi', ms: 'Sunan al-Baihaqi',
    tr: 'Sünen-i Beyhaki', fa: 'سنن بیهقی', ha: 'Sunan al-Bayhaqi', sw: 'Sunan al-Bayhaqi',
    so: 'Sunan al-Bayhaqi', fr: 'Sunan al-Bayhaqi', de: 'Sunan al-Bayhaqi', ru: 'Сунан аль-Байхаки', zh: '《白哈奇圣训集》'
  }, titles: {
    en: 'Clipping Nails & Grooming', hi: 'नाख़ून तराशना व ज़ीनत', ur: 'नाخن تراشنا و صفائی', ar: 'تقليم الأظافر والنظافة',
    bn: 'নখ কাটা ও পরিচ্ছন্নতা', ta: 'நகங்களை வெட்டுதல்', te: 'గోళ్లు కత్తిరించడం', ml: 'നഖം മുറിക്കുക',
    gu: 'નખ કાપવા અને સફાઈ', mr: 'नखे कापणे व स्वच्छता', pa: 'ਨਹੁੰ ਕੱਟਣਾ ਤੇ ਸਫਾਈ', kn: 'ಉಗುರು ಕತ್ತರಿಸುವುದು',
    or: 'ନଖ କାଟିବା', as: 'নখ কটা আৰু পৰিষ্কাৰ কৰা', id: 'Memotong Kuku & Merapikan Diri', ms: 'Memotong Kuku & Kekemasan Diri',
    tr: 'Tırnak Kesmek ve Bakım Yapmak', fa: 'کوتاه کردن ناخن‌ها و پیراستگی', ha: 'Yanke Fikoci', sw: 'Kukata Kucha',
    so: 'Ciddiyaha oo la Jaro', fr: 'Se couper les ongles et se soigner', de: 'Nägel schneiden & Körperpflege',
    ru: 'Стрижка ногтей и личная гигиена', zh: '修剪指甲与修饰个人仪表'
  }, descs: {
    en: 'Personal hygiene, clipping nails, and neat Sunnah grooming.',
    hi: 'सुन्नत के मुताबिक़ नाख़ून तराशना और ज़ाती सफ़ाई।',
    ur: 'سنت کے مطابق ناخن کاٹنا اور ذاتی صفائی۔',
    ar: 'قص الأظافر والتطهر والاعتناء بالنظافة الشخصية.',
    bn: 'সুন্নাত মোতাবেক নখ কাটা ও ব্যক্তিগত পরিচ্ছন্নতা অর্জন।'
  }},
  // 4. Wearing Clean & Best Clothes
  { id: 'clothes', icon: '👔', hadiths: {
    en: 'Sunan Abi Dawood 343', hi: 'सुनन अबू दाऊद 343', ur: 'سنن ابوداؤد 343', ar: 'سنن أبي داود ٣٤٣',
    bn: 'সুনান আবু দাউদ ৩৪৩', ta: 'ஸுனன் அபூ தாவூத் 343', te: 'సునన్ అబూ దావూద్ 343', ml: 'സുനൻ അബൂ ദാവൂദ് 343',
    gu: 'સુનન અબુ દાઉદ 343', mr: 'सुनन अबू दाऊद 343', pa: 'ਸੁਨਨ ਅਬੂ ਦਾਊਦ 343', kn: 'ಸುನನ್ ಅಬೂ ದಾವೂದ್ 343',
    or: 'ସୁନନ ଅବୁ ଦାଉଦ 343', as: 'সুনান আবু দাউদ ৩৪৩', id: 'Sunan Abu Dawud 343', ms: 'Sunan Abu Dawud 343',
    tr: 'Sünen-i Ebu Davud 343', fa: 'سنن ابوداود ۳۴۳', ha: 'Sunan Abi Dawud 343', sw: 'Sunan Abi Dawud 343',
    so: 'Sunan Abii Daawood 343', fr: 'Sunan Abi Dawood 343', de: 'Sunan Abi Dawud 343', ru: 'Сунан Аби Давуд 343', zh: '《艾布·达吾德圣训集》343'
  }, titles: {
    en: 'Wearing Clean & Best Clothes', hi: 'साफ़-सुथरे / बेहतरीन कपड़े पहनना', ur: 'صاف اور بہترین لباس پہننا', ar: 'لبس أحسن الثياب وأطهرها',
    bn: 'উত্তম ও পরিষ্কার পোশাক পরিধান', ta: 'சிறந்த ஆடைகளை அணிதல்', te: 'మంచి బట్టలు ధరించడం', ml: 'മികച്ച വസ്ത്രം ധരിക്കുക',
    gu: 'શ્રેષ્ઠ અને સ્વચ્છ કપડાં પહેરવા', mr: 'स्वच्छ व उत्तम कपडे घालणे', pa: 'ਸਾਫ ਸੁਥਰੇ ਕੱਪੜੇ ਪਹਿਨਣਾ', kn: 'ಉತ್ತಮ ಬಟ್ಟೆ ಧರಿಸುವುದು',
    or: 'ସଫା ଓ ଉତ୍ତମ ବସ୍ତ୍ର ପିନ୍ଧିବା', as: 'উত্তম পোছাক পৰিধান কৰা', id: 'Memakai Pakaian Terbaik & Bersih', ms: 'Memakai Pakaian Terbaik & Bersih',
    tr: 'En Güzel ve Temiz Elbiseleri Giymek', fa: 'پوشیدن پاکیزه‌ترین و بهترین لباس', ha: 'Sanya Kaya Masu Kyau da Tsabta', sw: 'Kuvaa Nguo Safi na Nzuri',
    so: 'Xidhashada Dharka Ugu Wanaagsan', fr: 'Porter ses plus beaux habits propres', de: 'Die beste und sauberste Kleidung tragen',
    ru: 'Надевание лучшей и чистой одежды', zh: '穿着整洁优美的衣裳'
  }, descs: {
    en: 'Dressing in one’s finest clean and dignified attire.',
    hi: 'जुमे के मुक़द्दस दिन के लिए सबसे अच्छे और साफ़ कपड़े पहनना।',
    ur: 'جمعہ کے مقدس دن کے لیے بہترین اور پاک لباس زیب تن کرنا۔',
    ar: 'التجمل بأحسن الثياب وأطهرها ليوم الجمعة.',
    bn: 'জুমার বরকতময় দিনের জন্য সবচেয়ে উত্তম ও পরিচ্ছন্ন পোশাক পরা।'
  }},
  // 5. Applying Perfume (Attar) & Oil
  { id: 'attar', icon: '🌸', hadiths: {
    en: 'Sahih al-Bukhari 880', hi: 'सहीह अल-बुख़ारी 880', ur: 'صحیح بخاری 880', ar: 'صحيح البخاري ٨٨٠',
    bn: 'সহীহ বুখারী ৮৮০', ta: 'ஸஹீஹ் புகாரி 880', te: 'సహీహ్ అల్-బుఖారీ 880', ml: 'സ്വഹീഹ് അൽ-ബുഖാരി 880',
    gu: 'સહીહ અલ-બુખારી 880', mr: 'सहीह अल-बुखारी 880', pa: 'ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 880', kn: 'ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 880',
    or: 'ସହିହ ଅଲ-ବୁଖାରୀ 880', as: 'ছহিহ বুখাৰী ৮৮০', id: 'Shahih al-Bukhari 880', ms: 'Shahih al-Bukhari 880',
    tr: 'Sahih-i Buhari 880', fa: 'صحیح بخاری ۸۸۰', ha: 'Sahih al-Bukhari 880', sw: 'Sahih al-Bukhari 880',
    so: 'Saxiiq al-Bukhaari 880', fr: 'Sahih al-Bukhari 880', de: 'Sahih al-Bukhari 880', ru: 'Сахих аль-Бухари 880', zh: '《布哈里圣训实录》880'
  }, titles: {
    en: 'Applying Perfume (Attar) & Oil', hi: 'इत्र / ख़ुशबू व तेल लगाना', ur: 'عطر / خوشبو لگانا', ar: 'التطيب بالمسك والعطر',
    bn: 'আতর ও সুগন্ধি ব্যবহার করা', ta: 'அத்தர் / நறுமணம் பூசுதல்', te: 'అత్తరు పూసుకోవడం', ml: 'സുഗന്ധം ഉപയോഗിക്കുക',
    gu: 'અત્તર / ખુશબૂ લગાવવી', mr: 'અત્તર / सुगंध लावणे', pa: 'ਇਤਰ / ਖੁਸ਼ਬੂ ਲਗਾਉਣਾ', kn: 'ಅತ್ತರ್ / ಸುಗಂಧ ಲೇಪಿಸುವುದು',
    or: 'ଅତର / ସୁଗନ୍ଧ ଲଗାଇବା', as: 'সুগন্ধি ব্যৱহাৰ কৰা', id: 'Memakai Minyak Wangi (Parfum)', ms: 'Memakai Wangian (Minyak Wangi)',
    tr: 'Güzel Koku ve Yağ Sürünmek', fa: 'استعمال عطر و بوی خوش', ha: 'Shiryawa da Turare', sw: 'Kupaka Manukato',
    so: 'Catariska iyo Udgoonka', fr: 'Se parfumer (Attar) et se huiler', de: 'Parfüm (Attar) auftragen & Ölen',
    ru: 'Использование благовоний (миск) и масел', zh: '使用香水和香油'
  }, descs: {
    en: 'Applying fine non-alcoholic fragrance for the gathering of believers.',
    hi: 'जुमे की नमाज़ के लिए बेहतरीन पाकीज़ा ख़ुशबू लगाना।',
    ur: 'نمازیوں کے اجتماع کے لیے عمدہ اور پاکیزہ خوشبو لگانا۔',
    ar: 'استعمال الطيب ودهن الشعر لحضور الجمعة.',
    bn: 'জুমার জামাতের উদ্দেশ্যে উত্তম সুগন্ধি ব্যবহার করা।'
  }},
  // 6. Going Early to the Mosque
  { id: 'early_mosque', icon: '🕌', hadiths: {
    en: 'Sahih al-Bukhari 881', hi: 'सहीह अल-बुख़ारी 881', ur: 'صحیح بخاری 881', ar: 'صحيح البخاري ٨٨١',
    bn: 'সহীহ বুখারী ৮৮১', ta: 'ஸஹீஹ் புகாரி 881', te: 'సహీహ్ అల్-బుఖారీ 881', ml: 'സ്വഹീഹ് അൽ-ബുഖാരി 881',
    gu: 'સહીહ અલ-બુખારી 881', mr: 'सहीह अल-बुखारी 881', pa: 'ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 881', kn: 'ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 881',
    or: 'ସହିହ ଅଲ-ବୁଖାରୀ 881', as: 'ছহিহ বুখাৰী ৮৮১', id: 'Shahih al-Bukhari 881', ms: 'Shahih al-Bukhari 881',
    tr: 'Sahih-i Buhari 881', fa: 'صحیح بخاری ۸۸۱', ha: 'Sahih al-Bukhari 881', sw: 'Sahih al-Bukhari 881',
    so: 'Saxiiq al-Bukhaari 881', fr: 'Sahih al-Bukhari 881', de: 'Sahih al-Bukhari 881', ru: 'Сахих аль-Бухари 881', zh: '《布哈里圣训实录》881'
  }, titles: {
    en: 'Going Early to the Mosque', hi: 'मस्जिद जल्दी पहुंचना', ur: 'مسجد جلدی جانا', ar: 'التبكير إلى المسجد',
    bn: 'তাড়াতাড়ি মসজিদে যাওয়া', ta: 'பள்ளிவாசலுக்கு முன்கூட்டியே செல்லுதல்', te: 'మసీదుకు త్వరగా వెళ్లడం', ml: 'നേരത്തെ പള്ളിയിലെത്തുക',
    gu: 'મસ્જિદ વહેલા પહોંચવું', mr: 'मशीदीत लवकर जाणे', pa: 'ਮਸਜਿਦ ਜਲਦੀ ਜਾਣਾ', kn: 'ಬೇಗನೆ ಮಸೀದಿಗೆ ಹೋಗುವುದು',
    or: 'ମସଜିଦକୁ ଶୀଘ୍ର ଯିବା', as: 'মছজিদলৈ সোনকালে যোৱা', id: 'Berangkat Awal ke Masjid', ms: 'Pergi Awal ke Masjid',
    tr: 'Camiye Erken Gitmek', fa: 'زود رفتن به مسجد', ha: 'Tafiya Masallaci da Wuri', sw: 'Kwenda Mapema Msikitini',
    so: 'Hore u Tagidda Masaajidka', fr: 'Se rendre tôt à la mosquée', de: 'Frühzeitig zur Moschee gehen',
    ru: 'Ранний приход в мечеть на пятничную молитву', zh: '提早前往清真寺'
  }, descs: {
    en: 'Arriving early earns a massive reward equivalent to sacrificing a camel.',
    hi: 'अव्वल वक़्त मस्जिद जाने पर ऊंट की क़ुर्बानी जैसा अज़ीम सवाब।',
    ur: 'پہلے وقت مسجد جانے پر اونٹ کی قربانی جیسا ثواب۔',
    ar: 'فضل عظيم كالذي يقرّب بدنة في سبيل الله.',
    bn: 'প্রথম ওয়াক্তে মসজিদে যাওয়ায় উট কুরবানীর সমতুল্য অসীম সওয়াব।'
  }},
  // 7. Walking on Foot to Mosque
  { id: 'walk_mosque', icon: '🚶', hadiths: {
    en: 'Jami at-Tirmidhi 496', hi: 'जामेअ तिर्मिज़ी 496', ur: 'جامع ترمذی 496', ar: 'جامع الترمذي ٤٩٦',
    bn: 'জামে তিরমিযী ৪৯৬', ta: 'ஜாமிஉத் திர்மிதி 496', te: 'జామి అత్-తిర్మిది 496', ml: 'ജാമിഅ് തിർമിദി 496',
    gu: 'જામેઅ તિર્મિઝી 496', mr: 'जामेअ तिर्मिझी 496', pa: 'ਜਾਮੇਅ ਤਿਰਮਿਜ਼ੀ 496', kn: 'ಜಾಮಿಅ್ ಅತ್-ತಿರ್ಮಿದಿ 496',
    or: 'ଜାମେ ଅତ-ତିର୍ମିଜି 496', as: 'জামে তিৰমিজি ৪৯৬', id: 'Jami at-Tirmidzi 496', ms: 'Jami at-Tirmidhi 496',
    tr: 'Camiu\'t-Tirmizi 496', fa: 'جامع ترمذی ۴۹۶', ha: 'Jami at-Tirmidhi 496', sw: 'Jami at-Tirmidhi 496',
    so: 'Jaamic at-Tirmidhi 496', fr: 'Jami at-Tirmidhi 496', de: 'Dschami at-Tirmidhi 496', ru: 'Джами ат-Тирмизи 496', zh: '《提尔密济圣训集》496'
  }, titles: {
    en: 'Walking on Foot to Mosque', hi: 'पैदल मस्जिद जाना (अगर मुमकिन हो)', ur: 'پیدل مسجد جانا', ar: 'المشي إلى المسجد على الأقدام',
    bn: 'হেঁটে মসজিদে যাওয়া', ta: 'மசூதிக்கு நடந்து செல்லுதல்', te: 'నడిచి మసీదుకు వెళ్లడం', ml: 'നടന്നു പള്ളിയിലേക്ക് പോവുക',
    gu: 'પગપાળા મસ્જિદ જવું', mr: 'मशिदीत पायी चालत जाणे', pa: 'ਪੈਦਲ ਮਸਜਿਦ ਜਾਣਾ', kn: 'ನಡೆದುಕೊಂಡು ಮಸೀದಿಗೆ ಹೋಗುವುದು',
    or: 'ଚାଲି ଚାଲି ମସଜିଦ ଯିବା', as: 'খোজকাঢ়ি মছজিদলৈ যোৱা', id: 'Berjalan Kaki Menuju Masjid', ms: 'Berjalan Kaki ke Masjid',
    tr: 'Camiye Yürüyerek Gitmek', fa: 'پیاده رفتن به مسجد', ha: 'Tafiya a Kafa Zuwa Masallaci', sw: 'Kutembea kwa Miguu Kwenda Msikitini',
    so: 'U Socodka Masaajidka Cagaha', fr: 'Aller à la mosquée à pied', de: 'Zu Fuß zur Moschee gehen',
    ru: 'Пеший путь в мечеть', zh: '步行前往清真寺'
  }, descs: {
    en: 'For every step, the reward of one year of fasting and standing in prayer.',
    hi: 'हर क़दम पर एक साल के रोज़े और नमाज़-ए-तहज्जुद का सवाब।',
    ur: 'ہر قدم پر ایک سال کے روزوں اور نمازوں کا ثواب۔',
    ar: 'بكل خطوة يخطوها أجر صيام سنة وقيامها.',
    bn: 'প্রতি পদক্ষেপে এক বছরের রোজা ও তাহাজ্জুদের সওয়াব।'
  }},
  // 8. Listening Attentively to Khutbah
  { id: 'khutbah', icon: '🤫', hadiths: {
    en: 'Sahih al-Bukhari 934', hi: 'सहीह अल-बुख़ारी 934', ur: 'صحیح بخاری 934', ar: 'صحيح البخاري ٩٣٤',
    bn: 'সহীহ বুখারী ৯৩৪', ta: 'ஸஹீஹ் புகாரி 934', te: 'సహీహ్ అల్-బుఖారీ 934', ml: 'സ്വഹീഹ് അൽ-ബുഖാരി 934',
    gu: 'સહીહ અલ-બુખારી 934', mr: 'सहीह अल-बुखारी 934', pa: 'ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 934', kn: 'ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 934',
    or: 'ସହିହ ଅଲ-ବୁଖାରୀ 934', as: 'ছহিহ বুখাৰী ৯৩৪', id: 'Shahih al-Bukhari 934', ms: 'Shahih al-Bukhari 934',
    tr: 'Sahih-i Buhari 934', fa: 'صحیح بخاری ۹۳۴', ha: 'Sahih al-Bukhari 934', sw: 'Sahih al-Bukhari 934',
    so: 'Saxiiq al-Bukhaari 934', fr: 'Sahih al-Bukhari 934', de: 'Sahih al-Bukhari 934', ru: 'Сахих аль-Бухари 934', zh: '《布哈里圣训实录》934'
  }, titles: {
    en: 'Listening Attentively to Khutbah', hi: 'ख़ामोशी से ख़ुत्बा सुनना', ur: 'خاموشی سے خطبہ سننا', ar: 'الإنصات التام لخطبة الجمعة',
    bn: 'মনোযোগ সহকারে খুতবা শোনা', ta: 'குத்பாவை மௌனமாக கேட்டல்', te: 'ఖుత్బా శ్రద్ధగా వినడం', ml: 'ഖുതുബ ശ്രദ്ധയോടെ കേൾക്കുക',
    gu: 'શાંતિથી ખુત્બો સાંભળવો', mr: 'शांतपणे खुतबा ऐकणे', pa: 'ਧਿਆਨ ਨਾਲ ਖੁਤਬਾ ਸੁਣਨਾ', kn: 'ಖುತ್ಬಾವನ್ನು ಗಮನವಿಟ್ಟು ಕೇಳುವುದು',
    or: 'ଧ୍ୟାନ ଦେଇ ଖୁତବା ଶୁଣିବା', as: 'মনোযোগেৰে খুতবা শুনা', id: 'Mendengarkan Khutbah dengan Khusyuk', ms: 'Mendengar Khutbah dengan Khusyuk',
    tr: 'Hutbeyi Sessizce ve Dikkatle Dinlemek', fa: 'گوش فرا دادن به خطبه با سکوت کامل', ha: 'Sauraron Huduwa cikin Natsuwa', sw: 'Kusikiliza Khutba kwa Makini',
    so: 'Dhegeysiga Khudbada oo la Aamuso', fr: 'Écouter attentivement le sermon (Khoutba)', de: 'Aufmerksam & schweigend der Chutba zuhören',
    ru: 'Внимательное и безмолвное слушание проповеди (хутбы)', zh: '静听主麻演讲（呼图白）'
  }, descs: {
    en: 'Remaining completely silent and focused while the Imam delivers the sermon.',
    hi: 'ख़ुत्बे के दौरान मुकम्मल ख़ामोशी और तवज्जोह से बयान सुनना।',
    ur: 'خطبے کے دوران مکمل خاموشی اور توجہ سے بیان سننا۔',
    ar: 'السكوت التام والإنصات أثناء إلقاء الإمام للخطبة.',
    bn: 'ইমামের খুতবা প্রদানের সময় সম্পূর্ণ নীরব থেকে মনোযোগ দিয়ে শোনা।'
  }},
  // 9. Reciting Surah Al-Kahf
  { id: 'surah_kahf', icon: '📖', hadiths: {
    en: 'Sahih al-Jami 6470 | Hakim 3392', hi: 'सहीह अल-जामेअ 6470 | हाकिम 3392', ur: 'صحیح الجامع 6470 | حاکم 3392', ar: 'صحيح الجامع ٦٤٧٠ | الحاكم ٣٣٩٢',
    bn: 'সহীহুল জামে ৬৪৭০ | হাকিম ৩৩৯২', ta: 'ஸஹீஹுல் ஜாமி 6470 | ஹாகிம் 3392', te: 'సహీహ్ అల్-జామీ 6470 | హాకిమ్ 3392', ml: 'സ്വഹീഹുൽ ജാമി 6470 | ഹാക്കിം 3392',
    gu: 'સહીહ અલ-જામેઅ 6470 | હાકિમ 3392', mr: 'सहीह अल-जामे 6470 | हाकिम 3392', pa: 'ਸਹੀਹ ਅਲ-ਜਾਮੇ 6470 | ਹਾਕਿਮ 3392', kn: 'ಸಹೀಹ್ ಅಲ್-ಜಾಮಿ 6470 | ಹಾಕಿಮ್ 3392',
    or: 'ସହିହ ଅଲ-ଜାମି 6470 | ହାକିମ 3392', as: 'ছহিহুল জামে ৬৪৭০ | হাকিম ৩৩৯২', id: 'Shahih al-Jami 6470 | Hakim 3392', ms: 'Shahih al-Jami 6470 | Hakim 3392',
    tr: 'Sahihü\'l-Cami 6470 | Hakim 3392', fa: 'صحیح الجامع ۶۴۷۰ | حاکم ۳۳۹۲', ha: 'Sahih al-Jami 6470 | Hakim 3392', sw: 'Sahih al-Jami 6470 | Hakim 3392',
    so: 'Saxiiq al-Jaamic 6470 | Xaakim 3392', fr: 'Sahih al-Jami 6470 | Hakim 3392', de: 'Sahih al-Dschami 6470 | Hakim 3392', ru: 'Сахих аль-Джами 6470 | Хаким 3392', zh: '《圣训全集精选》6470 | 哈基姆3392'
  }, titles: {
    en: 'Reciting Surah Al-Kahf', hi: 'सूरह अल-कहफ़ की तिलावत', ur: 'سورۃ الکہف کی تلاوت', ar: 'قراءة سورة الكهف',
    bn: 'সূরা আল-কাহাফ তিলাওয়াত', ta: 'ஸூரத்துல் கஹ்ஃப் ஓதுதல்', te: 'సూరా అల్-కహఫ్ పఠించడం', ml: 'സൂറത്തുൽ കഹ്ഫ് പാരായണം',
    gu: 'સૂરહ અલ-કહફની તિલાવત', mr: 'सूरह अल-कहफ पठण करणे', pa: 'ਸੂਰਹ ਅਲ-ਕਹਫ ਦੀ ਤਿਲਾਵਤ', kn: 'ಸೂರಾ ಅಲ್-ಕಹ್ಫ್ ಪಠಣ',
    or: 'ସୁରା ଅଲ-କହଫ ପଢିବା', as: 'সূৰা আল-কাহাফ তিলাৱত', id: 'Membaca Surah Al-Kahfi', ms: 'Membaca Surah Al-Kahfi',
    tr: 'Kehf Suresini Okumak', fa: 'تلاوت سوره مبارکه کهف', ha: 'Karatun Suratul Kahfi', sw: 'Kusoma Surah Al-Kahf',
    so: 'Akhrinta Suuradda Al-Kahf', fr: 'Réciter la sourate Al-Kahf', de: 'Die Sure Al-Kahf rezitieren',
    ru: 'Чтение суры «Аль-Кахф» (Пещера)', zh: '诵读《山洞章》（古兰经第18章）'
  }, descs: {
    en: 'A divine light (Noor) illuminates the believer until the next Friday.',
    hi: 'दोनों जुमों के दरमियान नूर चमकता रहेगा और गुनाह माफ़ होंगे।',
    ur: 'دونوں جمعوں کے درمیان نور روشن رہے گا اور گناہ معاف ہوں گے۔',
    ar: 'أضاء له من النور ما بين الجمعتين وعُصم من الدجال.',
    bn: 'দুই জুমার মধ্যবর্তী সময় আল্লাহর পক্ষ থেকে নূরে উদ্ভাসিত থাকবে।'
  }},
  // 10. Abundant Salawat upon Prophet ﷺ
  { id: 'durood', icon: '💚', hadiths: {
    en: 'Sunan Abi Dawood 1047', hi: 'सुनन अबू दाऊद 1047', ur: 'سنن ابوداؤد 1047', ar: 'سنن أبي داود ١٠٤٧',
    bn: 'সুনান আবু দাউদ ১০৪৭', ta: 'ஸுனன் அபூ தாவூத் 1047', te: 'సునన్ అబూ దావూద్ 1047', ml: 'സുനൻ അബൂ ദാവൂദ് 1047',
    gu: 'સુનન અબુ દાઉદ 1047', mr: 'सुनन अबू दाऊद 1047', pa: 'ਸੁਨਨ ਅਬੂ ਦਾਊਦ 1047', kn: 'ಸುನನ್ ಅಬೂ ದಾವೂದ್ 1047',
    or: 'ସୁନନ ଅବୁ ଦାଉଦ 1047', as: 'সুনান আবু দাউদ ১০৪৭', id: 'Sunan Abu Dawud 1047', ms: 'Sunan Abu Dawud 1047',
    tr: 'Sünen-i Ebu Davud 1047', fa: 'سنن ابوداود ۱۰۴۷', ha: 'Sunan Abi Dawud 1047', sw: 'Sunan Abi Dawud 1047',
    so: 'Sunan Abii Daawood 1047', fr: 'Sunan Abi Dawood 1047', de: 'Sunan Abi Dawud 1047', ru: 'Сунан Аби Давуд 1047', zh: '《艾布·达吾德圣训集》1047'
  }, titles: {
    en: 'Abundant Salawat upon Prophet ﷺ', hi: 'हुज़ूर ﷺ पर कसरत से दुरूद भेजना', ur: 'حضور ﷺ پر کثرت سے درود بھیجنا', ar: 'الإكثار من الصلاة والسلام على النبي ﷺ',
    bn: 'নবীজী ﷺ-এর উপর অধিক দরূদ পাঠ', ta: 'நபி ﷺ மீது அதிக ஸலவாத் கூறுதல்', te: 'ప్రవక్త ﷺ పై దరూద్ పంపడం', ml: 'നബി ﷺ മേൽ സ്വലാത്ത് അധികരിപ്പിക്കുക',
    gu: 'હુઝૂર ﷺ પર વધુમાં વધુ દુરૂદ મોકલવા', mr: 'प्रेषित ﷺ यांच्यावर विपुल दुरुद पाठवणे', pa: 'ਹਜ਼ੂਰ ﷺ ਉੱਤੇ ਵੱਧ ਤੋਂ ਵੱਧ ਦਰੂਦ ਭੇਜਣਾ', kn: 'ಪ್ರವಾದಿ ﷺ ಅವರ ಮೇಲೆ ಹೆಚ್ಚಿನ ಸಲವಾತ್ ಸಲ್ಲಿಸುವುದು',
    or: 'ନବୀ ﷺ ଙ୍କ ଉପରେ ଦୁରୁଦ ପଠାଇବା', as: 'নবী ﷺ-ৰ ওপৰত অধিক দৰূদ পঢ়া', id: 'Memperbanyak Shalawat atas Nabi ﷺ', ms: 'Memperbanyakkan Selawat ke atas Nabi ﷺ',
    tr: 'Peygamber Efendimize ﷺ Çokça Salavat Getirmek', fa: 'فرستادن صلوات فراوان بر پیامبر اکرم ﷺ', ha: 'Yawaita Salati ga Manzon Allah ﷺ', sw: 'Kukithirisha Kumswalia Mtume ﷺ',
    so: 'Saliga Nabiga ﷺ oo la Badiyo', fr: 'Multiplier les prières sur le Prophète ﷺ', de: 'Reichlich Segenswünsche (Salawat) auf den Propheten ﷺ senden',
    ru: 'Многократное благословение Пророка ﷺ (Салават)', zh: '多多为先知穆罕默德 ﷺ 祈求祝福（赞圣词）'
  }, descs: {
    en: 'Sending blessings upon Prophet Muhammad ﷺ on Friday is presented directly to him.',
    hi: 'जुमे के दिन कसरत से दुरूद शरीफ़ का विर्द करना सरकार ﷺ तक पहुंचाया जाता है।',
    ur: 'جمعہ کے دن کثرت سے درود شریف پڑھنا حضور ﷺ کی بارگاہ میں پیش ہوتا ہے۔',
    ar: '«أكثروا عليّ من الصلاة فيه فإن صلاتكم معروضة عليّ».',
    bn: 'জুমার দিনে নবীজী ﷺ-এর ওপর পঠিত দরূদ সরাসরি তাঁর সমীপে পেশ করা হয়।'
  }},
  // 11. Supplicating in the Hour of Acceptance
  { id: 'dua_hour', icon: '🤲', hadiths: {
    en: 'Sahih al-Bukhari 935', hi: 'सहीह अल-बुख़ारी 935', ur: 'صحیح بخاری 935', ar: 'صحيح البخاري ٩٣٥',
    bn: 'সহীহ বুখারী ৯৩৫', ta: 'ஸஹீஹ் புகாரி 935', te: 'సహీహ్ అల్-బుఖారీ 935', ml: 'സ്വഹീഹ് അൽ-ബുഖാരി 935',
    gu: 'સહીહ અલ-બુખારી 935', mr: 'सहीह अल-बुखारी 935', pa: 'ਸਹੀਹ ਅਲ-ਬੁਖ਼ਾਰੀ 935', kn: 'ಸಹೀಹ್ ಅಲ್-ಬುಖಾರಿ 935',
    or: 'ସହିହ ଅଲ-ବୁଖାରୀ 935', as: 'ছহিহ বুখাৰী ৯৩৫', id: 'Shahih al-Bukhari 935', ms: 'Shahih al-Bukhari 935',
    tr: 'Sahih-i Buhari 935', fa: 'صحیح بخاری ۹۳۵', ha: 'Sahih al-Bukhari 935', sw: 'Sahih al-Bukhari 935',
    so: 'Saxiiq al-Bukhaari 935', fr: 'Sahih al-Bukhari 935', de: 'Sahih al-Bukhari 935', ru: 'Сахих аль-Бухари 935', zh: '《布哈里圣训实录》935'
  }, titles: {
    en: 'Supplicating in the Hour of Acceptance', hi: 'दुआ-ए-इजाबत में दुआ (असर बाद)', ur: 'ساعتِ اجابت میں دعا (عصر کے بعد)', ar: 'تحري ساعة الإجابة (آخر ساعة بعد العصر)',
    bn: 'দোয়া কবুলের বিশেষ মুহূর্তে প্রার্থনা', ta: 'துஆ ஏற்றுக்கொள்ளப்படும் நேரத்தில் வேண்டுதல்', te: 'దువా అంగీకరించబడే సమయంలో ప్రార్థన', ml: 'ദുആ സ്വീകരിക്കപ്പെടുന്ന സമയത്ത് പ്രാർത്ഥിക്കുക',
    gu: 'દુઆ કબૂલ થવાના ખાસ વક્તમાં દુઆ', mr: 'दुआ स्वीकारल्या जाणाऱ्या वेळेत प्रार्थना', pa: 'ਦੁਆ ਕਬੂਲ ਹੋਣ ਵਾਲੇ ਸਮੇਂ ਦੁਆ ਮੰਗਣਾ', kn: 'ದುವಾ ಸ್ವೀಕರಿಸಲ್ಪಡುವ ಸಮಯದಲ್ಲಿ ಪ್ರಾರ್ಥನೆ',
    or: 'ଦୁଆ କବୁଲ ହେବା ସମୟରେ ପ୍ରାର୍ଥନା', as: 'দোৱা কবুলৰ বিশেষ মুহূৰ্তত প্ৰাৰ্থনা', id: 'Berdoa di Waktu Mustajab (Bada Ashar)', ms: 'Berdoa pada Waktu Mustajab (Selepas Asar)',
    tr: 'İcabet Saatinde (İkindi Sonrası) Dua Etmek', fa: 'دعا در ساعت اجابت (پس از عصر تا مغرب)', ha: 'Yin Addua a Lokacin Karba', sw: 'Kuomba Dua Wakati wa Kukubaliwa',
    so: 'Ducaysiga Waqtiga Ajiibista', fr: 'Invoquer pendant l\'Heure d\'Exaucement', de: 'Bittgebete in der Stunde der Erhörung sprechen',
    ru: 'Мольба (дуа) в час принятия мольбы (между Асром и Магрибом)', zh: '在提求必应的尊贵时刻（晡礼至昏礼之间）祈祷'
  }, descs: {
    en: 'A blessed hour on Friday (between Asr and Maghrib) where no sincere Dua is rejected.',
    hi: 'असर से मग़रिब के बीच एक घड़ी ऐसी है जिसमें मांगी गई कोई भी जायज़ दुआ रद्द नहीं होती।',
    ur: 'عصر سے مغرب کے درمیان ایک ایسی گھڑی ہے جس میں بندہ जो مانگے قبول ہوتا ہے۔',
    ar: 'ساعة لا يوافقها عبد مسلم يسأل الله تعالى شيئاً إلا أعطاه إياه.',
    bn: 'আসর থেকে মাগরিবের মধ্যবর্তী এমন এক বরকতময় মুহূর্ত যখন কোনো নেক দোয়া প্রত্যাখ্যাত হয় না।'
  }},
  // 12. Giving Charity / Sadaqah
  { id: 'charity', icon: '🪙', hadiths: {
    en: 'Musannaf \'Abd al-Razzaq 5558', hi: 'मुसन्नफ़ अब्दुर रज़्ज़ाक़ 5558', ur: 'مصنف عبدالرزاق 5558', ar: 'مصنف عبد الرزاق ٥٥٥٨',
    bn: 'মুসান্নাফ আব্দুর রাজ্জাক ৫৫৫৮', ta: 'முஸன்னஃப் அப்துர் ரஸ்ஸாக் 5558', te: 'ముసన్నఫ్ అబ్దుర్ రజ్జాఖ్ 5558', ml: 'മുസന്നഫ് അബ്ദുർ റസ്സാഖ് 5558',
    gu: 'મુસન્નફ અબ્દુર રઝાક 5558', mr: 'मुसन्नफ अब्दुर रझ्झाक 5558', pa: 'ਮੁਸੰਨਫ਼ ਅਬਦੁਰ ਰਜ਼ਾਕ 5558', kn: 'ಮುಸನ್ನಫ್ ಅಬ್ದುರ್ ರಝಾಕ್ 5558',
    or: 'ମୁସନ୍ନଫ ଅବ୍ଦୁର ରଜ୍ଜାକ 5558', as: 'মুছান্নাফ আব্দুর ৰাজ্জাক ৫৫৫৮', id: 'Mushannaf Abdur Razzaq 5558', ms: 'Mushannaf Abdur Razzaq 5558',
    tr: 'Musannef Abdürrezzak 5558', fa: 'مصنف عبدالرزاق ۵۵۵۸', ha: 'Musannaf Abdur Razzaq 5558', sw: 'Musannaf Abdur Razzaq 5558',
    so: 'Musannaf Cabdir-Razzaaq 5558', fr: 'Musannaf \'Abd al-Razzaq 5558', de: 'Musannaf Abdur Razzaq 5558', ru: 'Мусаннаф Абд ар-Раззак 5558', zh: '《阿卜杜·拉扎格圣训集》5558'
  }, titles: {
    en: 'Giving Charity (Sadaqah)', hi: 'सदक़ा व ख़ैरात देना', ur: 'صدقہ و خیرات کرنا', ar: 'الصدقة يوم الجمعة',
    bn: 'দান-সদকা করা', ta: 'தான தர்மம் (ஸதகா) செய்தல்', te: 'దానం (సదకా) చేయడం', ml: 'ദാനം (സ്വദഖ) ചെയ്യുക',
    gu: 'સદકા અને દાન કરવું', mr: 'दान (सदका) करणे', pa: 'ਦਾਨ (ਸਦਕਾ) ਕਰਨਾ', kn: 'ದಾನ (ಸದಕಾ) ಮಾಡುವುದು',
    or: 'ଦାନ (ସଦକା) କରିବା', as: 'দান-ছদকা কৰা', id: 'Bersedekah di Hari Jumat', ms: 'Bersedekah pada Hari Jumaat',
    tr: 'Cuma Günü Sadaka Vermek', fa: 'صدقه دادن در روز جمعه', ha: 'Yin Sadaka a Ranar Jumaa', sw: 'Kutoa Sadaka Siku ya Ijumaa',
    so: 'Sadaqo Bixinta Jimcaha', fr: 'Faire l\'aumône (Sadaqah) le vendredi', de: 'Almosen (Sadaqah) am Freitag geben',
    ru: 'Раздача милостыни (Садака) в пятницу', zh: '主麻日施舍（撒德盖）'
  }, descs: {
    en: 'Giving charity on Friday is specially blessed, multiplying rewards abundantly.',
    hi: 'जुमे के दिन सदक़ा व ख़ैरात करने का सवाब बाक़ी दिनों से कई गुना बढ़कर मिलता है।',
    ur: 'جمعہ کے دن صدقہ و خیرات کرنا دیگر تمام دنوں کے مقابلے میں زیادہ فضیلت اور اجر رکھتا ہے۔',
    ar: 'الصدقة في يوم الجمعة لها فضل عظيم وأجر مضاعف كما بيّن أهل العلم.',
    bn: 'জুমার দিনে দান-সদকা করার মাধ্যমে অশেষ নেকি ও বরকত অর্জিত হয়।'
  }}
];
const JUMMAH_UI_LANG = {
  hi: {
    badge: 'मुबारक जुमा',
    title: 'जुम्मा मुबारक',
    hadith_hero: '"सूरज निकलने वाले दिनों में सबसे बेहतरीन दिन जुमे का दिन है।" — सहीह मुस्लिम (854)',
    progress_label: 'सुन्नतें मुकम्मल',
    durood_counter_title: 'दरूद शरीफ़ विर्द काउंटर',
    durood_tap_btn: 'दुरूद पढ़ें (+1)',
    durood_target_80: '80 बार',
    durood_target_100: '100 बार',
    durood_target_1000: '1000 बार',
    durood_reset: 'रीसेट',
    read_kahf_btn: '📖 सूरह अल-कहफ़ खोलें',
    mubarak_title: 'माशाअल्लाह! जुमे की सुन्नतें पूरी हुईं',
    mubarak_desc: 'अल्लाह तआला आपकी इबादत और सुन्नतों को क़बूल फ़रमाए।',
    reset_btn: 'चेकलिस्ट रीसेट करें'
  },
  en: {
    badge: 'BLESSED FRIDAY',
    title: 'Jummah Mubarak',
    hadith_hero: '"The best day on which the sun rises is Friday." — Sahih Muslim (854)',
    progress_label: 'Sunnahs Completed',
    durood_counter_title: 'Salawat / Durood Tap Counter',
    durood_tap_btn: 'Tap for Salawat (+1)',
    durood_target_80: '80x Target',
    durood_target_100: '100x Target',
    durood_target_1000: '1000x Target',
    durood_reset: 'Reset',
    read_kahf_btn: '📖 Open Surah Al-Kahf',
    mubarak_title: 'MashaAllah! Friday Sunnahs Completed',
    mubarak_desc: 'May Allah accept all your Friday deeds, prayers, and blessings.',
    reset_btn: 'Reset Checklist'
  },
  ur: {
    badge: 'مبارک جمعہ',
    title: 'جمعہ مبارک',
    hadith_hero: '"تمام دنوں میں سب سے بہترین دن جس پر سورج طلوع ہوتا ہے، جمعہ کا دن ہے۔" — صحیح مسلم (854)',
    progress_label: 'سنتیں مکمل',
    durood_counter_title: 'درود شریف کاؤنٹر',
    durood_tap_btn: 'درود شریف (+1)',
    durood_target_80: '80 مرتبہ',
    durood_target_100: '100 مرتبہ',
    durood_target_1000: '1000 مرتبہ',
    durood_reset: 'ری سیٹ',
    read_kahf_btn: '📖 سورۃ الکہف کھولیں',
    mubarak_title: 'ماشاءاللہ! جمعہ کی تمام سنتیں مکمل ہوئیں',
    mubarak_desc: 'اللہ تعالی آپ کی عبادات اور دعاؤں کو قبول فرمائے۔',
    reset_btn: 'چیک لسٹ ری سیٹ کریں'
  },
  ar: {
    badge: 'يوم الجمعة المبارك',
    title: 'جمعة مباركة',
    hadith_hero: '«خَيْرُ يَوْمٍ طَلَعَتْ عَلَيْهِ الشَّمْسُ يَوْمُ الْجُمُعَةِ» — صحيح مسلم (854)',
    progress_label: 'السنن المكتملة',
    durood_counter_title: 'عداد الصلاة على النبي ﷺ',
    durood_tap_btn: 'اضغط للصلاة (+1)',
    durood_target_80: '80 مرة',
    durood_target_100: '100 مرة',
    durood_target_1000: '1000 مرة',
    durood_reset: 'إعادة ضبط',
    read_kahf_btn: '📖 قراءة سورة الكهف',
    mubarak_title: 'ما شاء الله! اكتملت سنن الجمعة',
    mubarak_desc: 'تقبل الله منا ومنكم صالح الأعمال والطاعات.',
    reset_btn: 'إعادة ضبط القائمة'
  },
  bn: {
    badge: 'বরকতময় শুক্রবার',
    title: 'জুম্মা মোবারক',
    hadith_hero: '"সূর্য উদিত হওয়া দিনগুলির মধ্যে সর্বোত্তম দিন হলো শুক্রবার।" — সহীহ মুসলিম (৮৫৪)',
    progress_label: 'সুন্নাত সম্পন্ন',
    durood_counter_title: 'দরূদ শরীফ কাউন্টার',
    durood_tap_btn: 'দরূদ পাঠ করুন (+১)',
    durood_target_80: '৮০ বার',
    durood_target_100: '১০০ বার',
    durood_target_1000: '১০০০ বার',
    durood_reset: 'রিসেট',
    read_kahf_btn: '📖 সূরা আল-কাহাফ খুলুন',
    mubarak_title: 'মাশাআল্লাহ! জুমার সুন্নাতসমূহ সম্পন্ন হয়েছে',
    mubarak_desc: 'আল্লাহ তায়ালা আপনার সকল ইবাদত ও দোয়া কবুল করুন।',
    reset_btn: 'চেকলিস্ট রিসেট করুন'
  }
};
function getJummahUiText(key){
  const curLang = typeof lang !== 'undefined' ? lang : (localStorage.getItem('dt_lang') || 'en');
  const pack = JUMMAH_UI_LANG[curLang] || JUMMAH_UI_LANG['en'];
  return pack[key] || JUMMAH_UI_LANG['en'][key] || '';
}
function getJummahItemHadith(item){
  const curLang = typeof lang !== 'undefined' ? lang : (localStorage.getItem('dt_lang') || 'en');
  if(item.hadiths && item.hadiths[curLang]) return item.hadiths[curLang];
  if(item.hadiths && item.hadiths['en']) return item.hadiths['en'];
  return item.hadith || '';
}
function getJummahItemTitle(item){
  const curLang = typeof lang !== 'undefined' ? lang : (localStorage.getItem('dt_lang') || 'en');
  return (item.titles && (item.titles[curLang] || item.titles['en'] || item.titles['hi'])) || item.id;
}
function getJummahItemDesc(item){
  const curLang = typeof lang !== 'undefined' ? lang : (localStorage.getItem('dt_lang') || 'en');
  return (item.descs && (item.descs[curLang] || item.descs['en'] || item.descs['hi'])) || '';
}
function jummahDateKey(){
  const now = new Date();
  const y = now.getFullYear();
  const m = String(now.getMonth() + 1).padStart(2, '0');
  const d = String(now.getDate()).padStart(2, '0');
  return `${y}-${m}-${d}`;
}
function loadJummahProgress(){
  try {
    const key = 'dt_jummah_prog_' + jummahDateKey();
    return JSON.parse(localStorage.getItem(key) || '{}');
  } catch(_) {
    return {};
  }
}
function saveJummahProgress(map){
  try {
    const key = 'dt_jummah_prog_' + jummahDateKey();
    localStorage.setItem(key, JSON.stringify(map || {}));
  } catch(_) {}
}
function loadJummahDuroodCount(){
  try {
    const key = 'dt_jummah_durood_' + jummahDateKey();
    return parseInt(localStorage.getItem(key) || '0', 10) || 0;
  } catch(_) {
    return 0;
  }
}
function saveJummahDuroodCount(val){
  try {
    const key = 'dt_jummah_durood_' + jummahDateKey();
    localStorage.setItem(key, String(val || 0));
  } catch(_) {}
}
function getJummahTileIcon(){
  return homeImg('jummah');
}
function isJummahActiveNow(){
  const now = new Date();
  const day = now.getDay(); // 0=Sun, 5=Fri
  const hours = now.getHours();
  // Active on Friday from 4:00 AM until Maghrib (~7:30 PM)
  return day === 5 && hours >= 4 && (hours < 19 || (hours === 19 && now.getMinutes() <= 30));
}
function isJummahSunnahUnread(){
  if(!isJummahActiveNow()) return false;
  try {
    const map = loadJummahProgress();
    const hasMarkedAny = Object.values(map).some(Boolean);
    return !hasMarkedAny; // Shows pulsing red dot until user marks at least one Sunnah
  } catch(_) {
    return false;
  }
}
function openJummahScreen(){
  ['scSalahTracker','scDhikrTracker','scSettingsScreen','scAdhkarScreen'].forEach(function(id){
    const e = document.getElementById(id);
    if(e){ e.classList.remove('open'); e.style.display = 'none'; }
  });
  const el = document.getElementById('scJummahScreen');
  if(el){
    el.classList.add('open');
    el.style.display = 'flex';
    renderJummahLangBar();
    renderJummahScreen();
  }
}
function closeJummahScreen(){
  const el = document.getElementById('scJummahScreen');
  if(el){
    el.classList.remove('open');
    el.style.display = 'none';
  }
}
function renderJummahLangBar(){
  const el = document.getElementById('jummahLangBar');
  if(!el) return;
  const visible = (typeof getVisibleLangBarList === 'function') ? getVisibleLangBarList() : (typeof LANGS !== 'undefined' ? LANGS : []);
  const curLang = (typeof lang !== 'undefined') ? lang : 'en';
  const moreCount = (typeof ALL_LANGS !== 'undefined' ? (ALL_LANGS.length - visible.length > 0 ? ALL_LANGS.length - visible.length : 19) : 19);
  el.className = 'lang-bar st-lang-bar dv-lang-row';
  el.innerHTML =
    visible.map(l => `<div class="dv-lang-pill${l.code === curLang ? ' active' : ''}" data-l="${l.code}" onclick="setLang('${l.code}')">${l.label}</div>`).join('') +
    `<div class="dv-lang-pill dv-lang-more" onclick="toggleMoreLangs()" title="More Languages">🌐 +${moreCount}</div>`;
}
function toggleJummahItem(id){
  const map = loadJummahProgress();
  map[id] = !map[id];
  saveJummahProgress(map);
  if (typeof playTickSound === 'function') playTickSound();
  if (typeof haptic === 'function') haptic('medium');
  renderJummahScreen();
  try { if(typeof renderHomeSection === 'function') renderHomeSection(); } catch(_) {}
}
function incrementJummahDurood(e){
  if(e) e.stopPropagation();
  let count = loadJummahDuroodCount();
  count++;
  saveJummahDuroodCount(count);
  if(typeof playTick === 'function') playTick();
  // Mark durood item as completed if count >= 1
  const map = loadJummahProgress();
  if(!map['durood']){
    map['durood'] = true;
    saveJummahProgress(map);
  }
  if (typeof playTickSound === 'function') playTickSound();
  if (typeof haptic === 'function') haptic('light');
  const valEl = document.getElementById('jummahDuroodVal');
  if(valEl){
    valEl.textContent = count;
    valEl.style.transform = 'scale(1.2)';
    setTimeout(() => { valEl.style.transform = 'scale(1)'; }, 150);
  } else {
    renderJummahScreen();
  }
}
function resetJummahDurood(e){
  if(e) e.stopPropagation();
  saveJummahDuroodCount(0);
  renderJummahScreen();
}
function openSurahKahfFromJummah(e){
  if(e) e.stopPropagation();
  window._QURAN_OPENED_FROM_JUMMAH = true;
  closeJummahScreen();
  if(typeof openQuranHome === 'function'){
    openQuranHome();
  }
  setTimeout(function(){
    if(typeof openQuranReader === 'function'){
      openQuranReader('surah', 18);
    }
  }, 80);
}
// ══════════════════════════════════════════════════════════
// QURAN AUDIO — High Performance Standalone Player Module
// ══════════════════════════════════════════════════════════
const QA_SURAH_AYAH_COUNTS = [7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6];
const QA_SURAH_START_AYAHS = [1, 8, 294, 494, 670, 790, 955, 1161, 1236, 1365, 1474, 1597, 1708, 1751, 1803, 1902, 2030, 2141, 2251, 2349, 2484, 2596, 2674, 2792, 2856, 2933, 3160, 3253, 3341, 3410, 3470, 3504, 3534, 3607, 3661, 3706, 3789, 3971, 4059, 4134, 4219, 4273, 4326, 4415, 4474, 4511, 4546, 4584, 4613, 4631, 4676, 4736, 4785, 4847, 4902, 4980, 5076, 5105, 5127, 5151, 5164, 5178, 5189, 5200, 5218, 5230, 5242, 5272, 5324, 5376, 5420, 5448, 5476, 5496, 5552, 5592, 5623, 5673, 5713, 5759, 5801, 5830, 5849, 5885, 5910, 5932, 5949, 5968, 5994, 6024, 6044, 6059, 6080, 6091, 6099, 6107, 6126, 6131, 6139, 6147, 6158, 6169, 6177, 6180, 6189, 6194, 6198, 6205, 6208, 6214, 6217, 6222, 6226, 6231];
let qaAudioEl = null;
let qaPlayingSurah = null;
let qaAutoList = [];
let qaCurrentGlobal = null;
let qaDisplayLang = null;
let qaSurahList = null;
let qaContinuous = false;
let qaPlayToken = 0;
function buildLocalQuranSurahList(targetLang) {
  const curL = targetLang || (typeof qaDisplayLang !== 'undefined' && qaDisplayLang ? qaDisplayLang : ((typeof lang !== 'undefined' && lang) ? lang : 'en'));
  const res = [];
  for (let i = 1; i <= 114; i++) {
    const meta = (typeof QURAN_SURAH_NAMES !== 'undefined' && QURAN_SURAH_NAMES[String(i)]) ||
                 (typeof SURAH_NAMES !== 'undefined' && SURAH_NAMES[String(i)]) || null;
    let headingName = meta ? meta.en : ('Surah ' + i);
    let nameAr = meta ? meta.ar : '';
    if (meta) {
      if (curL === 'ar') {
        headingName = meta.ar || meta.en;
      } else if (curL === 'en') {
        headingName = meta.en || ('Surah ' + i);
      } else if (curL && meta[curL]) {
        if (typeof meta[curL] === 'string') {
          headingName = meta[curL];
        } else if (meta[curL].name) {
          headingName = meta[curL].name;
        }
      }
    }
    res.push({
      number: i,
      name: nameAr,
      englishName: headingName,
      numberOfAyahs: (typeof QA_SURAH_AYAH_COUNTS !== 'undefined' && QA_SURAH_AYAH_COUNTS[i - 1]) ? QA_SURAH_AYAH_COUNTS[i - 1] : 0
    });
  }
  return res;
}
function getReciterDisplayName(reciterId) {
  const defaultMap = {
    'ar.alafasy': 'Mishary Rashid Alafasy',
    'ar.abdulbasitmurattal': 'Abdul Basit (Murattal)',
    'ar.abdulsamad': 'Abdul Basit (Mujawwad)',
    'ar.sudais': 'Abdur-Rahman As-Sudais',
    'ar.shuraym': 'Saud Ash-Shuraym',
    'ar.minshawi': 'Mohamed Siddiq Al-Minshawi',
    'ar.husary': 'Mahmoud Khalil Al-Husary',
    'ar.ajamy': 'Ahmed ibn Ali al-Ajamy',
    'ar.hudhaify': 'Ali Al-Hudhaify',
    'ar.mahermuaiqly': 'Maher Al-Muaiqly',
    'ar.saadghamidi': 'Saad Al-Ghamdi',
    'ar.ayman': 'Ayman Sowaid'
  };
  const baseName = defaultMap[reciterId] || reciterId || 'Mishary Rashid Alafasy';
  const flag = typeof reciterFlag === 'function' ? reciterFlag(baseName) : '';
  return (flag ? flag + ' ' : '🎙️ ') + baseName;
}
function renderQaLangBar() {
  const el = document.getElementById('qaLangBar');
  if (!el) return;
  const visible = (typeof getVisibleLangBarList === 'function') ? getVisibleLangBarList() : (typeof LANGS !== 'undefined' ? LANGS : [
    { code: 'en', label: 'English' }, { code: 'hi', label: 'हिंदी' }, { code: 'ur', label: 'اردو' }, { code: 'ar', label: 'العربية' }, { code: 'bn', label: 'বাংলা' }
  ]);
  const curLang = qaDisplayLang || (typeof lang !== 'undefined' ? lang : (localStorage.getItem('dt_lang') || 'en'));
  const moreCount = (typeof ALL_LANGS !== 'undefined' ? (ALL_LANGS.length - visible.length > 0 ? ALL_LANGS.length - visible.length : 20) : 20);
  el.className = 'lang-bar dv-lang-row';
  el.innerHTML = visible.map(l => `<div class="dv-lang-pill${l.code === curLang ? ' active' : ''}" data-l="${l.code}" onclick="qaSetLang('${l.code}')">${l.label}</div>`).join('') +
    `<div class="dv-lang-pill dv-lang-more" onclick="toggleMoreLangs()" title="More Languages">🌐 +${moreCount}</div>`;
}
function qaSetLang(l) {
  qaDisplayLang = l;
  try {
    if (typeof setLang === 'function') setLang(l);
    else lang = l;
  } catch(_) { lang = l; }
  renderQaLangBar();
  qaSurahList = buildLocalQuranSurahList(l);
  renderQuranAudioList();
  const titEl = document.getElementById('qa_t_title');
  if (titEl && typeof T === 'function') titEl.textContent = T('home_audio') || 'क़ुरान ऑडियो';
  if (qaPlayingSurah && qaSurahList) {
    const sItem = qaSurahList.find(s => s.number === qaPlayingSurah);
    const nameEl = document.getElementById('qaPlayerName');
    if (nameEl && sItem) nameEl.textContent = sItem.englishName;
  }
}
// ── SACRED ADHANS & QURAN AUDIO ENGINE (SEPARATE SCREENS) ──
const SACRED_ADHANS = [
  {
    id: 'adhan_ahmad_al_nafees',
    rawId: 'adhan_ahmad_al_nafees',
    title: 'Adhan by Sheikh Ahmad Al-Nafees',
    titleAr: 'أذان الشيخ أحمد النفيس',
    muadhin: 'Sheikh Ahmad Al-Nafees',
    country: 'Kuwait 🇰🇼',
    style: 'Heartfelt, Serene & Emotional Melody',
    duration: '03:34',
    durationSec: 214,
    file: 'file:///android_res/raw/adhan_ahmad_al_nafees.mp3'
  },
  {
    id: 'adhan_hafiz_mustafa_ozcan',
    rawId: 'adhan_hafiz_mustafa_ozcan',
    title: 'Adhan by Hafiz Mustafa Özcan from Turkey',
    titleAr: 'أذان الحافظ مصطفى أوزجان',
    muadhin: 'Hafiz Mustafa Özcan',
    country: 'Turkey 🇹🇷',
    style: 'Classical Ottoman Imperial Mosque Style',
    duration: '03:58',
    durationSec: 238,
    file: 'file:///android_res/raw/adhan_hafiz_mustafa_ozcan.mp3'
  },
  {
    id: 'adhan_mansour_al_zahrani',
    rawId: 'adhan_mansour_al_zahrani',
    title: 'Adhan by Mansour Al-Zahrani',
    titleAr: 'أذان الشيخ منصور الزهراني',
    muadhin: 'Mansour Al-Zahrani',
    country: 'Saudi Arabia 🇸🇦',
    style: 'Powerful, Deep & Resonant Sacred Call',
    duration: '03:30',
    durationSec: 210,
    file: 'file:///android_res/raw/adhan_mansour_al_zahrani.mp3'
  },
  {
    id: 'adhan_mishary_dubai_tv',
    rawId: 'adhan_mishary_dubai_tv',
    title: "Adhan from Dubai's One TV by Mishary Rashid Alafasy",
    titleAr: 'أذان قناة دبي للشيخ مشاري راشد العفاسي',
    muadhin: 'Sheikh Mishary Rashid Alafasy',
    country: 'Dubai One TV 🇦🇪',
    style: 'World-Famous Iconic Melodic Adhan',
    duration: '05:36',
    durationSec: 336,
    file: 'file:///android_res/raw/adhan_mishary_dubai_tv.mp3'
  },
  {
    id: 'adhan_karl_jenkins_peace',
    rawId: 'adhan_karl_jenkins_peace',
    title: "Adhan from Karl Jenkins' Mass for Peace",
    titleAr: 'أذان قداس من أجل السلام (كارل جنكينز)',
    muadhin: 'Karl Jenkins (The Armed Man)',
    country: 'Global Harmony 🕊️',
    style: 'Choral Symphony & Universal Call to Peace',
    duration: '03:07',
    durationSec: 187,
    file: 'file:///android_res/raw/adhan_karl_jenkins_peace.mp3'
  },
  {
    id: 'adhan_mishary_alafasy_2',
    rawId: 'adhan_mishary_alafasy_2',
    title: 'Another Adhan by Mishary Rashid Alafasy',
    titleAr: 'أذان آخر للشيخ مشاري العفاسي',
    muadhin: 'Sheikh Mishary Rashid Alafasy (Style 2)',
    country: 'Kuwait / Madinah 🇰🇼',
    style: 'Traditional Hijaz Tone • Soothing & Uplifting',
    duration: '04:03',
    durationSec: 243,
    file: 'file:///android_res/raw/adhan_mishary_alafasy_2.mp3'
  },
  {
    id: 'adhan_mishary_alafasy_3',
    rawId: 'adhan_mishary_alafasy_3',
    title: 'Yet Another Adhan by Mishary Rashid Alafasy (Fajr)',
    titleAr: 'أذان الفجر للشيخ مشاري راشد العفاسي',
    muadhin: 'Sheikh Mishary Rashid Alafasy (Fajr)',
    country: 'Fajr Special 🌅',
    style: 'Includes "As-Salatu Khayrun Minan-Nawm"',
    duration: '04:17',
    durationSec: 257,
    file: 'file:///android_res/raw/adhan_mishary_alafasy_3.mp3'
  }
];
let qaPlayingAdhanId = null;
let adhanAudioEl = null;
// ==========================================
// 1. SACRED ADHAN AUDIO SCREEN CONTROLLER
// ==========================================
function openAdhanAudioScreen() {
  if (window.AndroidBridge && window.AndroidBridge.hideAllNativeAds) {
    try { window.AndroidBridge.hideAllNativeAds(); } catch(e){}
  }
  // Stop Surah audio if playing
  quranAudioStop();
  const sc = document.getElementById('sc-adhan-audio');
  if (sc) sc.classList.add('open');
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  const curLang = (typeof lang !== 'undefined' && lang) ? lang : (localStorage.getItem('dt_lang') || 'en');
  // Title
  const titEl = document.getElementById('adhan_t_title');
  if (titEl) {
    titEl.textContent = (curLang === 'hi') ? 'पवित्र अज़ान (7)' : ((curLang === 'ar') ? 'الأذان الشريف (7)' : ((curLang === 'ur') ? 'مقدس اذان (7)' : 'Sacred Adhan (7)'));
  }
  // Theme button
  const tb = document.getElementById('adhanThemeBtn');
  if (tb && typeof isDayTheme !== 'undefined') {
    tb.textContent = isDayTheme ? '☀️' : '🌙';
  }
  // Language bar
  renderAdhanLangBar();
  // Render the 7 cards
  renderAdhanAudioList();
}
function closeAdhanAudioScreen() {
  stopAdhanAudio();
  const sc = document.getElementById('sc-adhan-audio');
  if (sc) sc.classList.remove('open');
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  const n0 = document.getElementById('nav0');
  if (n0) n0.classList.add('active');
}
function renderAdhanLangBar() {
  const el = document.getElementById('adhanLangBar');
  if (!el) return;
  const visible = (typeof getVisibleLangBarList === 'function') ? getVisibleLangBarList() : (typeof LANGS !== 'undefined' ? LANGS : [
    { code: 'en', label: 'English' }, { code: 'hi', label: 'हिंदी' }, { code: 'ur', label: 'اردو' }, { code: 'ar', label: 'العربية' }, { code: 'bn', label: 'বাংলা' }
  ]);
  const curLang = (typeof lang !== 'undefined' && lang) ? lang : (localStorage.getItem('dt_lang') || 'en');
  const moreCount = (typeof ALL_LANGS !== 'undefined' ? (ALL_LANGS.length - visible.length > 0 ? ALL_LANGS.length - visible.length : 20) : 20);
  el.className = 'lang-bar dv-lang-row';
  el.innerHTML = visible.map(l => `<div class="dv-lang-pill${l.code === curLang ? ' active' : ''}" data-l="${l.code}" onclick="adhanSetLang('${l.code}')">${l.label}</div>`).join('') +
    `<div class="dv-lang-pill dv-lang-more" onclick="toggleMoreLangs()" title="More Languages">🌐 +${moreCount}</div>`;
}
function adhanSetLang(l) {
  try {
    if (typeof setLang === 'function') setLang(l);
    else lang = l;
  } catch(_) { lang = l; }
  renderAdhanLangBar();
  const titEl = document.getElementById('adhan_t_title');
  if (titEl) {
    titEl.textContent = (l === 'hi') ? 'पवित्र अज़ान (7)' : ((l === 'ar') ? 'الأذان الشريف (7)' : ((l === 'ur') ? 'مقدس اذان (7)' : 'Sacred Adhan (7)'));
  }
  renderAdhanAudioList();
}
function renderAdhanAudioList() {
  const c = document.getElementById('adhanListBody');
  if (!c) return;
  c.innerHTML = SACRED_ADHANS.map((item, idx) => {
    const isPlaying = (qaPlayingAdhanId === item.id) && (
      (adhanAudioEl && !adhanAudioEl.paused) ||
      (window.AndroidBridge && window.AndroidBridge.isRawAudioPlaying && window.AndroidBridge.isRawAudioPlaying())
    );
    return `
      <div class="qa-adhan-card ${isPlaying ? 'playing' : ''}" id="qa-adhan-${item.id}" onclick="toggleAdhanPlay('${item.id}')">
        <div class="qa-adhan-left">
          <div class="qa-adhan-rosette">
            <span class="qa-adhan-num">${idx + 1}</span>
            ${isPlaying ? `<div class="qa-adhan-wave"><span></span><span></span><span></span><span></span></div>` : ''}
          </div>
        </div>
        <div class="qa-adhan-mid">
          <div class="qa-adhan-title">${item.title}</div>
          <div class="qa-adhan-meta">
            <span class="qa-adhan-country">${item.country}</span>
            <span class="qa-adhan-dot">•</span>
            <span class="qa-adhan-dur">${item.duration}</span>
          </div>
          <div class="qa-adhan-style">${item.style}</div>
        </div>
        <div class="qa-adhan-actions" onclick="event.stopPropagation()">
          <button class="qa-adhan-play-btn ${isPlaying ? 'playing' : ''}" id="qa-adhan-btn-${item.id}" onclick="toggleAdhanPlay('${item.id}')" title="${isPlaying ? 'Pause' : 'Play'}">
            ${isPlaying ? '⏸' : '▶'}
          </button>
        </div>
      </div>
    `;
  }).join('');
}
function toggleAdhanPlay(adhanId) {
  const item = SACRED_ADHANS.find(a => a.id === adhanId);
  if (!item) return;
  if (qaPlayingAdhanId === adhanId) {
    const isCurrentlyPlaying = (adhanAudioEl && !adhanAudioEl.paused) ||
      (window.AndroidBridge && window.AndroidBridge.isRawAudioPlaying && window.AndroidBridge.isRawAudioPlaying());
    if (isCurrentlyPlaying) {
      if (adhanAudioEl) adhanAudioEl.pause();
      if (window.AndroidBridge && window.AndroidBridge.stopRawAudio) {
        try { window.AndroidBridge.stopRawAudio(); } catch(e){}
      }
      setAdhanPlayingUI(adhanId, false);
    } else {
      resumeAdhanPlay(item);
    }
    return;
  }
  // Stop any Surah audio
  quranAudioStop();
  stopAdhanAudio();
  qaPlayingAdhanId = adhanId;
  const bar = document.getElementById('adhanPlayerBar');
  const nameEl = document.getElementById('adhanPlayerName');
  const subEl = document.getElementById('adhan_t_playing');
  if (nameEl) nameEl.textContent = item.title;
  if (subEl) subEl.textContent = item.muadhin + ' • ' + item.country;
  if (bar) bar.style.display = 'flex';
  // Android Native Playback
  if (window.AndroidBridge && window.AndroidBridge.playRawAudio) {
    try {
      window.AndroidBridge.playRawAudio(item.rawId);
    } catch(e) {
      console.warn('Native playRawAudio failed', e);
    }
  }
  // HTML5 Audio fallback & sync
  try {
    if (!adhanAudioEl) adhanAudioEl = new Audio();
    adhanAudioEl.src = item.file;
    adhanAudioEl.onended = () => {
      stopAdhanAudio();
    };
    adhanAudioEl.onerror = () => {
      console.warn('HTML5 audio error on', item.file);
    };
    adhanAudioEl.play().catch(e => {
      console.warn('Audio playback prevented or handled by native', e);
    });
  } catch(e){}
  setAdhanPlayingUI(adhanId, true);
}
function resumeAdhanPlay(item) {
  if (window.AndroidBridge && window.AndroidBridge.playRawAudio) {
    try { window.AndroidBridge.playRawAudio(item.rawId); } catch(e){}
  }
  if (adhanAudioEl) {
    adhanAudioEl.play().catch(() => {});
  }
  setAdhanPlayingUI(item.id, true);
}
function stopAdhanAudio() {
  if (adhanAudioEl) {
    adhanAudioEl.pause();
    adhanAudioEl.currentTime = 0;
  }
  if (window.AndroidBridge && window.AndroidBridge.stopRawAudio) {
    try { window.AndroidBridge.stopRawAudio(); } catch(e){}
  }
  if (qaPlayingAdhanId) {
    const prevId = qaPlayingAdhanId;
    qaPlayingAdhanId = null;
    setAdhanPlayingUI(prevId, false);
  }
  const bar = document.getElementById('adhanPlayerBar');
  if (bar) bar.style.display = 'none';
}
function toggleAdhanMiniPlay() {
  if (qaPlayingAdhanId) {
    toggleAdhanPlay(qaPlayingAdhanId);
  }
}
function setAdhanPlayingUI(adhanId, isPlaying) {
  const card = document.getElementById('qa-adhan-' + adhanId);
  const btn = document.getElementById('qa-adhan-btn-' + adhanId);
  const playerBtn = document.getElementById('adhanPlayerBtn');
  document.querySelectorAll('#sc-adhan-audio .qa-adhan-card').forEach(c => {
    if (c.id !== 'qa-adhan-' + adhanId) c.classList.remove('playing');
  });
  document.querySelectorAll('#sc-adhan-audio .qa-adhan-play-btn').forEach(b => {
    if (b.id !== 'qa-adhan-btn-' + adhanId) {
      b.classList.remove('playing');
      b.innerHTML = '▶';
    }
  });
  if (card) card.classList.toggle('playing', isPlaying);
  if (btn) {
    btn.classList.toggle('playing', isPlaying);
    btn.innerHTML = isPlaying ? '⏸' : '▶';
  }
  if (playerBtn) playerBtn.textContent = isPlaying ? '⏸' : '▶';
  // Update rosette wave
  const rosette = card ? card.querySelector('.qa-adhan-rosette') : null;
  if (rosette) {
    let wave = rosette.querySelector('.qa-adhan-wave');
    if (isPlaying) {
      if (!wave) {
        wave = document.createElement('div');
        wave.className = 'qa-adhan-wave';
        wave.innerHTML = '<span></span><span></span><span></span><span></span>';
        rosette.appendChild(wave);
      }
    } else {
      if (wave) wave.remove();
    }
  }
}
window.onNativeAudioCompleted = function(resName) {
  stopAdhanAudio();
};
// ==========================================
// 2. QURAN AUDIO SCREEN (114 SURAHS ONLY)
// ==========================================
function openQuranAudioHome() {
  if (window.AndroidBridge && window.AndroidBridge.hideAllNativeAds) {
    try { window.AndroidBridge.hideAllNativeAds(); } catch(e){}
  }
  // Stop Adhan audio if playing
  stopAdhanAudio();
  const sc = document.getElementById('sc-quran-audio');
  if (sc) sc.classList.add('open');
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  const titEl = document.getElementById('qa_t_title');
  if (titEl && typeof T === 'function') titEl.textContent = T('home_audio') || 'Quran Audio';
  const backEl = document.getElementById('qa_t_back');
  if (backEl) {
    const curLang = (typeof lang !== 'undefined' && lang) ? lang : 'en';
    const backText = (curLang === 'hi') ? 'वापस' : ((curLang === 'ur') ? 'پیچھے' : (curLang === 'ar' ? 'رجوع' : (curLang === 'bn' ? 'ফিরে যান' : 'Back')));
    backEl.textContent = backText;
  }
  const qatb = document.getElementById('qaThemeBtn');
  if (qatb && typeof isDayTheme !== 'undefined') {
    qatb.textContent = isDayTheme ? '☀️' : '🌙';
  }
  // Ensure reciter picker is visible
  const recPicker = document.getElementById('qaReciterPicker');
  if (recPicker) recPicker.style.display = 'flex';
  qaDisplayLang = (typeof lang !== 'undefined' && lang) ? lang : (localStorage.getItem('dt_lang') || 'en');
  qaContinuous = !!(typeof quranCacheGet === 'function' && quranCacheGet('quran_audio_continuous_v1'));
  quranAudioUpdateContinuousBtn();
  const nameEl = document.getElementById('qaReciterNameSel');
  if (nameEl) {
    const recId = (typeof quranSettings !== 'undefined' && quranSettings && quranSettings.reciter) || 'ar.alafasy';
    nameEl.innerHTML = getReciterDisplayName(recId);
  }
  renderQaLangBar();
  qaSurahList = buildLocalQuranSurahList(qaDisplayLang);
  renderQuranAudioList();
}
function closeQuranAudioHome() {
  quranAudioStop();
  const sc = document.getElementById('sc-quran-audio');
  if (sc) sc.classList.remove('open');
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  const n0 = document.getElementById('nav0');
  if (n0) n0.classList.add('active');
}
function renderQuranAudioList() {
  const c = document.getElementById('qaListBody');
  const list = qaSurahList || buildLocalQuranSurahList(qaDisplayLang);
  if (!c || !list) return;
  const ayahsLabel = (typeof T === 'function' && T('q_ayahs')) || 'आयतें / Ayahs';
  c.innerHTML = list.map(s => {
    const playing = qaPlayingSurah === s.number;
    return `<div class="qa-row${playing ? ' playing' : ''}" id="qa-row-${s.number}" onclick="quranAudioPlaySurah(${s.number})">
      <div class="qa-num">${s.number}</div>
      <div class="qa-info">
        <div class="qa-name-en">${s.englishName}</div>
        <div class="qa-name-ar">${s.name} • ${s.numberOfAyahs} ${ayahsLabel}</div>
      </div>
      <div class="qa-play-ico" id="qa-ico-${s.number}">${playing && qaAudioEl && !qaAudioEl.paused ? '⏸' : '▶'}</div>
    </div>`;
  }).join('');
}
function quranAudioSurahRange(surahNum) {
  const total = QA_SURAH_AYAH_COUNTS[surahNum - 1] || 0;
  const start = QA_SURAH_START_AYAHS[surahNum - 1] || 1;
  const list = [];
  for (let i = 0; i < total; i++) {
    list.push(start + i);
  }
  return list;
}
function quranAudioStop() {
  qaPlayToken++;
  if (qaAudioEl) {
    qaAudioEl.onended = null;
    qaAudioEl.onerror = null;
    qaAudioEl.pause();
    qaAudioEl = null;
  }
  if (qaPlayingSurah) {
    const prevRow = document.getElementById('qa-row-' + qaPlayingSurah);
    if (prevRow) prevRow.classList.remove('playing');
    const prevIco = document.getElementById('qa-ico-' + qaPlayingSurah);
    if (prevIco) prevIco.textContent = '▶';
  }
  qaPlayingSurah = null;
  qaAutoList = [];
  qaCurrentGlobal = null;
  const bar = document.getElementById('qaPlayerBar');
  if (bar) bar.style.display = 'none';
}
function quranAudioSetPlayingUI(playing) {
  if (qaPlayingSurah) {
    const ico = document.getElementById('qa-ico-' + qaPlayingSurah);
    if (ico) ico.textContent = playing ? '⏸' : '▶';
  }
  const btn = document.getElementById('qaPlayerBtn');
  if (btn) btn.textContent = playing ? '⏸' : '▶';
}
function quranAudioPlayGlobal(globalNum, isFallbackRetry) {
  const reciter = (typeof quranSettings !== 'undefined' && quranSettings && quranSettings.reciter) || 'ar.alafasy';
  let url = '';
  if (typeof QURAN_BACKEND !== 'undefined' && QURAN_BACKEND && /^https?:\/\//i.test(QURAN_BACKEND)) {
    url = `${QURAN_BACKEND}/v1/audio/${reciter}/${globalNum}`;
  } else {
    url = `https://cdn.islamic.network/quran/audio/128/${reciter}/${globalNum}.mp3`;
  }
  qaCurrentGlobal = globalNum;
  const myToken = ++qaPlayToken;
  if (!qaAudioEl) {
    qaAudioEl = new Audio();
  }
  const handleUnplayable = () => {
    if (myToken !== qaPlayToken) return true;
    if (!isFallbackRetry && reciter !== 'ar.alafasy') {
      if (typeof quranSettings !== 'undefined' && quranSettings) {
        quranSettings.reciter = 'ar.alafasy';
        if (typeof quranCacheSet === 'function') quranCacheSet('quran_settings_v1', quranSettings);
      }
      const dispName = getReciterDisplayName('ar.alafasy');
      const n1 = document.getElementById('quranReciterNameSel');
      if (n1) n1.innerHTML = dispName;
      const n2 = document.getElementById('qaReciterNameSel');
      if (n2) n2.innerHTML = dispName;
      if (typeof toast === 'function') toast('Switching audio stream...');
      quranAudioPlayGlobal(globalNum, true);
      return true;
    }
    return false;
  };
  qaAudioEl.onended = () => {
    if (myToken !== qaPlayToken) return;
    const idx = qaAutoList.indexOf(qaCurrentGlobal);
    if (idx > -1 && idx < qaAutoList.length - 1) {
      quranAudioPlayGlobal(qaAutoList[idx + 1]);
    } else if (qaContinuous && qaPlayingSurah && qaPlayingSurah < 114) {
      const nextSurah = qaPlayingSurah + 1;
      const nextList = quranAudioSurahRange(nextSurah);
      if (nextList && nextList.length) {
        quranAudioStartSurahUI(nextSurah, nextList);
        quranAudioPlayGlobal(nextList[0]);
      } else {
        quranAudioStop();
      }
    } else {
      quranAudioStop();
    }
  };
  qaAudioEl.onerror = () => {
    if (myToken !== qaPlayToken) return;
    if (handleUnplayable()) return;
    quranAudioSetPlayingUI(false);
    if (typeof toast === 'function') toast('Audio playback error');
  };
  qaAudioEl.src = url;
  qaAudioEl.load();
  quranAudioSetPlayingUI(true);
  qaAudioEl.play().catch((err) => {
    if (myToken !== qaPlayToken) return;
    if (err && err.name === 'AbortError') return;
    if (handleUnplayable()) return;
    quranAudioSetPlayingUI(false);
  });
}
function quranAudioStartSurahUI(surahNum, list) {
  const prevSurah = qaPlayingSurah;
  if (prevSurah && prevSurah !== surahNum) {
    const prevRow = document.getElementById('qa-row-' + prevSurah);
    if (prevRow) prevRow.classList.remove('playing');
    const prevIco = document.getElementById('qa-ico-' + prevSurah);
    if (prevIco) prevIco.textContent = '▶';
  }
  qaPlayingSurah = surahNum;
  qaAutoList = list;
  document.querySelectorAll('.qa-row').forEach(r => r.classList.remove('playing'));
  const row = document.getElementById('qa-row-' + surahNum);
  if (row) row.classList.add('playing');
  const srcList = qaSurahList || buildLocalQuranSurahList(qaDisplayLang);
  const meta = srcList && srcList.find(s => s.number === surahNum);
  const bar = document.getElementById('qaPlayerBar');
  const nameEl = document.getElementById('qaPlayerName');
  if (nameEl) nameEl.textContent = meta ? meta.englishName : ('Surah ' + surahNum);
  if (bar) bar.style.display = 'flex';
}
function quranAudioPlaySurah(surahNum) {
  stopAdhanAudio();
  if (qaPlayingSurah === surahNum && qaAudioEl) {
    quranAudioToggleMini();
    return;
  }
  quranAudioStop();
  const list = quranAudioSurahRange(surahNum);
  if (!list || !list.length) return;
  quranAudioStartSurahUI(surahNum, list);
  quranAudioPlayGlobal(list[0]);
}
function quranAudioPlayNextSurah() {
  if (!qaPlayingSurah) {
    quranAudioPlaySurah(1);
    return;
  }
  if (qaPlayingSurah >= 114) {
    if (typeof toast === 'function') toast('Last Surah');
    return;
  }
  quranAudioPlaySurah(qaPlayingSurah + 1);
}
function quranAudioUpdateContinuousBtn() {
  const btn = document.getElementById('qaContinuousBtn');
  if (btn) btn.classList.toggle('on', qaContinuous);
}
function quranAudioContinuousToggle() {
  qaContinuous = !qaContinuous;
  if (typeof quranCacheSet === 'function') quranCacheSet('quran_audio_continuous_v1', qaContinuous);
  quranAudioUpdateContinuousBtn();
  if (typeof toast === 'function') toast(qaContinuous ? 'Continuous Play ON' : 'Continuous Play OFF');
}
function quranAudioToggleMini() {
  if (!qaAudioEl) return;
  if (qaAudioEl.paused) {
    const myToken = qaPlayToken;
    qaAudioEl.play().then(() => {
      if (myToken === qaPlayToken) quranAudioSetPlayingUI(true);
    }).catch(() => {});
  } else {
    qaAudioEl.pause();
    quranAudioSetPlayingUI(false);
  }
}
function resetJummahProgressPrompt(){
  if(confirm(getJummahUiText('reset_confirm_title') || 'Reset today\'s Jummah checklist?')){
    saveJummahProgress({});
    saveJummahDuroodCount(0);
    renderJummahScreen();
    if(typeof toast === 'function') toast('🔄 Checklist Reset');
  }
}
const JUMMAH_HADITH_FULL = {
  miswak: {
    ref: 'Sahih Muslim 846 | Sunan an-Nasa\'i 1378',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Sahih Muslim 846 (Sunnah.com)', url: 'https://sunnah.com/muslim:846' },
      { label: 'Sunan an-Nasa\'i 1378 (Sunnah.com)', url: 'https://sunnah.com/nasai:1378' }
    ],
    ar: 'عَنْ أَبِي سَعِيدٍ الْخُدْرِيِّ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «غُسْلُ يَوْمِ الْجُمُعَةِ عَلَى كُلِّ مُحْتَلِمٍ، وَسِوَاكٌ، وَيَمَسُّ مِنَ الطِّيبِ مَا قَدَرَ عَلَيْهِ»',
    en: 'Narrated by Abu Sa\'id Al-Khudri (RA): The Messenger of Allah ﷺ said: "Taking a bath on Friday is essential for every adult, as is using the tooth-stick (Miswak) and applying perfume if available."',
    hi: 'हज़रत अबू सईद ख़ुदरी (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: "जुमे के दिन ग़ुस्ल करना हर बालिग़ पर सुन्नत-ए-मौक़दा है, और मिस्वाक करना और जितनी मुमकिन हो ख़ुशबू लगाना।"',
    ur: 'حضرت ابو سعید خدری رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: "جمعہ کے دن غسل کرنا ہر بالغ پر، اور مسواک کرنا اور جتنی خوشبو میسر ہو لگانا مسنون ہے۔"'
  },
  ghusl: {
    ref: 'Sahih al-Bukhari 877 | Sahih Muslim 844',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Sahih al-Bukhari 877 (Sunnah.com)', url: 'https://sunnah.com/bukhari:877' },
      { label: 'Sahih Muslim 844 (Sunnah.com)', url: 'https://sunnah.com/muslim:844a' }
    ],
    ar: 'عَنِ ابْنِ عُمَرَ رَضِيَ اللَّهُ عَنْهُمَا، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «مَنْ جَاءَ مِنْكُمُ الْجُمُعَةَ فَلْيَغْتَسِلْ»',
    en: 'Narrated by Ibn Umar (RA): The Messenger of Allah ﷺ said: "Anyone of you attending the Friday (Jumu\'ah) prayer should take a bath (Ghusl)."',
    hi: 'हज़रत अब्दुल्लाह बिन उमर (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: "तुम में से जो कोई जुमे की नमाज़ के लिए आए, उसे चाहिए कि वह ग़ुस्ल (स्नान) करे।"',
    ur: 'حضرت عبداللہ بن عمر رضی اللہ عنہما سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: "تم میں سے جو شخص جمعہ کی نماز کے لیے آئے، اسے چاہیے کہ وہ غسل کرے۔"'
  },
  nails: {
    ref: 'Sunan al-Bayhaqi (Shu\'ab al-Iman 2769) | Sahih al-Bukhari 5891',
    grade: 'Sahih (Fitrah Sunnah)',
    links: [
      { label: 'Sahih al-Bukhari 5891 (Sunnah.com)', url: 'https://sunnah.com/bukhari:5891' },
      { label: 'Sahih Muslim 257 (Sunnah.com)', url: 'https://sunnah.com/muslim:257b' }
    ],
    ar: 'عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «الْفِطْرَةُ خَمْسٌ: الْخِتَانُ، وَالِاسْتِحْدَادُ، وَقَصُّ الشَّارِبِ، وَتَقْلِيمُ الْأَظْفَارِ، وَنَتْفُ الْآبَاطِ»',
    en: 'Narrated by Abu Hurairah (RA): The Messenger of Allah ﷺ said: "Five are the acts of natural disposition (Fitrah): circumcision, shaving the pubes, trimming the moustache, clipping the nails, and plucking the armpit hairs."',
    hi: 'हज़रत अबू हुरैरा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: "पाँच चीज़ें फ़ितरत (सुन्नत) में से हैं: ख़तना करना, ज़ेरे नाफ़ के बाल काटना, मूंछें छोटी करना, नाख़ून तराशना और बग़ल के बाल साफ़ करना।"',
    ur: 'حضرت ابوہریرہ رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: "پانچ چیزیں فطرت (سنت) میں سے ہیں: ختنہ کرنا، زیرِ ناف کے بال کاٹنا، مونچھیں چھوٹی کرنا، ناخن تراشنا اور بغل کے بال صاف کرنا۔"'
  },
  clothes: {
    ref: 'Sunan Abi Dawood 343 | Sunan Ibn Majah 1095',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Sunan Abi Dawood 343 (Sunnah.com)', url: 'https://sunnah.com/abudawud:343' },
      { label: 'Sunan Ibn Majah 1095 (Sunnah.com)', url: 'https://sunnah.com/ibnmajah:1095' }
    ],
    ar: 'عَنْ عَائِشَةَ رَضِيَ اللَّهُ عَنْهَا، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «مَا عَلَى أَحَدِكُمْ إِنْ وَجَدَ سَعَةً أَنْ يَتَّخِذَ ثَوْبَيْنِ لِيَوْمِ الْجُمُعَةِ سِوَى ثَوْبَيْ مِهْنَتِهِ»',
    en: 'Narrated by Aisha (RA): The Messenger of Allah ﷺ said: "There is no harm if anyone of you, if he can afford it, keeps two garments for Friday other than his daily working clothes."',
    hi: 'हज़रत आयशा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: "अगर किसी के पास गुंजाइश हो तो इसमें कोई हर्ज़ नहीं कि वह जुमे के दिन के लिए अपने रोज़मर्रा के कामकाजी कपड़ों के अलावा दो कपड़े अलग रखे।"',
    ur: 'ام المومنین حضرت عائشہ رضی اللہ عنہا سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: "اگر کسی کے پاس گنجائش ہو تو اس میں کوئی حرج نہیں کہ وہ روزمرہ کے کام کاج کے کپڑوں کے علاوہ جمعہ کے لیے دو خاص کپڑے رکھے۔"'
  },
  attar: {
    ref: 'Sahih al-Bukhari 880',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Sahih al-Bukhari 880 (Sunnah.com)', url: 'https://sunnah.com/bukhari:880' }
    ],
    ar: 'عَنْ سَلْمَانَ الْفَارِسِيِّ رَضِيَ اللَّهُ عَنْهُ قَالَ: قَالَ النَّبِيُّ ﷺ: «لاَ يَغْتَسِلُ رَجُلٌ يَوْمَ الجُمُعَةِ، وَيَتَطَهَّرُ مَا اسْتَطَاعَ مِنْ طُهْرٍ، وَيَدَّهِنُ مِنْ دُهْنِهِ، أَوْ يَمَسُّ مِنْ طِيبِ بَيْتِهِ، ثُمَّ يَخْرُجُ فَلاَ يُفَرِّقُ بَيْنَ اثْنَيْنِ، ثُمَّ يُصَلِّي مَا كُتِبَ لَهُ، ثُمَّ يُنْصِتُ إِذَا تَكَلَّمَ الإِمَامُ، إِلَّا غُفِرَ لَهُ مَا بَيْنَهُ وَبَيْنَ الجُمُعَةِ الأُخْرَى»',
    en: 'Narrated by Salman Al-Farsi (RA): The Prophet ﷺ said: "Whoever takes a bath on Friday, purifies himself as much as possible, puts on his oil or applies perfume from his house, then proceeds to the mosque... his sins between that Friday and the next will be forgiven."',
    hi: 'हज़रत सलमान फ़ारसी (रज़ि.) से रिवायत है कि नबी करीम ﷺ ने फ़रमाया: "जो शख़्स जुमे के दिन ग़ुस्ल करे, जितनी हो सके पाकीज़गी हासिल करे, तेल या घर में रखी ख़ुशबू लगाए, फिर मस्जिद जाए... तो उसके इस जुमे से अगले जुमे तक के गुनाह बख़्श दिए जाते हैं।"',
    ur: 'حضرت سلمان فارسی رضی اللہ عنہ سے روایت ہے کہ نبی کریم ﷺ نے فرمایا: "جو شخص جمعہ کے دن غسل کرے، جتنی ہو سکے صفائی کرے، تیل یا خوشبو لگائے، پھر مسجد جائے... تو اس کے اس جمعہ سے دوسرے جمعہ تک کے گناہ معاف کر دیے جاتے ہیں۔"'
  },
  early_mosque: {
    ref: 'Sahih al-Bukhari 881 | Sahih Muslim 850',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Sahih al-Bukhari 881 (Sunnah.com)', url: 'https://sunnah.com/bukhari:881' },
      { label: 'Sahih Muslim 850 (Sunnah.com)', url: 'https://sunnah.com/muslim:850' }
    ],
    ar: 'عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «مَنِ اغْتَسَلَ يَوْمَ الجُمُعَةِ غُسْلَ الجَنَابَةِ ثُمَّ رَاحَ، فَكَأَنَّمَا قَرَّبَ بَدَنَةً، وَمَنْ رَاحَ فِي السَّاعَةِ الثَّانِيَةِ، فَكَأَنَّمَا قَرَّبَ بَقَرَةً، وَمَنْ رَاحَ فِي السَّاعَةِ الثَّالِثَةِ، فَكَأَنَّمَا قَرَّبَ كَبْشًا أَقْرَنَ، وَمَنْ رَاحَ فِي السَّاعَةِ الرَّابِعَةِ، فَكَأَنَّمَا قَرَّبَ دَجَاجَةً، وَمَنْ رَاحَ فِي السَّاعَةِ الخَامِسَةِ، فَكَأَنَّمَا قَرَّبَ بَيْضَةً...»',
    en: 'Narrated by Abu Hurairah (RA): The Messenger of Allah ﷺ said: "Whoever takes a bath on Friday and goes to the mosque in the first hour, it is as if he sacrificed a camel; whoever goes in the second hour, it is as if he sacrificed a cow; whoever goes in the third hour, it is as if he sacrificed a horned ram..."',
    hi: 'हज़रत अबू हुरैरा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: "जो शख़्स जुमे के दिन ग़ुस्ल करके पहली घड़ी में मस्जिद जाता है, गोया उसने ऊंट की क़ुर्बानी दी; जो दूसरी घड़ी में जाए गोया उसने गाय की क़ुर्बानी दी; जो तीसरी घड़ी में जाए गोया उसने मेंढे की क़ुर्बानी दी..."',
    ur: 'حضرت ابوہریرہ رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: "جو شخص جمعہ کے دن پہلی گھڑی میں مسجد جائے گویا اس نے اونٹ کی قربانی دی، جو دوسری گھڑی میں جائے گویا اس نے گائے کی قربانی دی، اور جو تیسری گھڑی میں جائے گویا اس نے مینڈھے کی قربانی دی..."'
  },
  walk_mosque: {
    ref: 'Jami at-Tirmidhi 496 | Sunan Abi Dawood 345',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Jami at-Tirmidhi 496 (Sunnah.com)', url: 'https://sunnah.com/tirmidhi:496' },
      { label: 'Sunan Abi Dawood 345 (Sunnah.com)', url: 'https://sunnah.com/abudawud:345' }
    ],
    ar: 'عَنْ أَوْسِ بْنِ أَوْسٍ الثَّقَفِيِّ رَضِيَ اللَّهُ عَنْهُ قَالَ: قَالَ رَسُولُ اللَّهِ ﷺ: «مَنْ غَسَّلَ يَوْمَ الجُمُعَةِ وَاغْتَسَلَ، ثُمَّ بَكَّرَ وَابْتَكَرَ، وَمَشَى وَلَمْ يَرْكَبْ، وَدَنَا مِنَ الإِمَامِ فَاسْتَمَعَ وَلَمْ يَلْغُ، كَانَ لَهُ بِكُلِّ خُطْوَةٍ عَمَلُ سَنَةٍ أَجْرُ صِيَامِهَا وَقِيَامِهَا»',
    en: 'Narrated by Aws ibn Aws (RA): The Prophet ﷺ said: "Whoever takes a bath on Friday, goes early on foot without riding, sits close to the Imam, and listens attentively... for every step he takes, he will have the reward of a whole year of fasting and standing in night prayer."',
    hi: 'हज़रत औस बिन औस (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: "जो शख़्स जुमे के दिन ग़ुस्ल करे, जल्दी मस्जिद जाए, पैदल चले और सवारी न करे, इमाम के क़रीब होकर ख़ामोशी से सुने... तो उसके हर क़दम पर एक साल के रोज़ों और एक साल की रातों की नमाज़ (तहज्जुद) का सवाब मिलता है।"',
    ur: 'حضرت اوس بن اوس رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: "جو شخص جمعہ کے دن غسل کرے، جلدی مسجد جائے، پیدل جائے اور سوار نہ ہو، امام کے قریب بیٹھ کر خاموشی سے سنے... تو اس کے ہر قدم پر پورے ایک سال کے روزوں اور ایک سال کی راتوں کی نماز کا ثواب ملتا ہے۔"'
  },
  khutbah: {
    ref: 'Sahih al-Bukhari 934 | Sahih Muslim 851',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Sahih al-Bukhari 934 (Sunnah.com)', url: 'https://sunnah.com/bukhari:934' },
      { label: 'Sahih Muslim 851 (Sunnah.com)', url: 'https://sunnah.com/muslim:851' }
    ],
    ar: 'عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ قَالَ: «إِذَا قُلْتَ لِصَاحِبِكَ يَوْمَ الجُمُعَةِ: أَنْصِتْ، وَالإِمَامُ يَخْطُبُ، فَقَدْ لَغَوْتَ»',
    en: 'Narrated by Abu Hurairah (RA): The Messenger of Allah ﷺ said: "If you say to your companion, \'Listen quietly,\' on Friday while the Imam is delivering the sermon (Khutbah), you have engaged in idle talk (and nullified your reward)."',
    hi: 'हज़रत अबू हुरैरा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: "अगर तुमने जुमे के दिन ख़ुत्बे के दौरान अपने साथी से इतना भी कहा कि \'ख़ामोश रहो\', तो तुमने भी बेकार बात की (और सवाब गँवा दिया)।"',
    ur: 'حضرت ابوہریرہ رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: "جمعہ کے دن خطبے کے دوران اگر تم نے اپنے ساتھی سے اتنا بھی کہا کہ \'خاموش رہو\'، تو تم نے لغو (بے کار) کام کیا۔"'
  },
  surah_kahf: {
    ref: 'Sahih al-Jami\' 6470 | Al-Mustadrak 3392 (2/399) | Sunan al-Kubra 6063',
    grade: 'Sahih (Authentic — Shaykh al-Albani & Imam al-Hakim)',
    links: [
      { label: '1. Sahih al-Jami\' 6470 (Shaykh al-Albani / Dorar)', url: 'https://dorar.net/hadith/sharh/146476' },
      { label: '2. Al-Mustadrak \'ala al-Sahihayn 3392 (Imam al-Hakim / Dorar)', url: 'https://dorar.net/h/jyf7OzOs?osoul=1' },
      { label: '3. Sunan al-Kubra 6063 (Imam al-Bayhaqi / Dorar)', url: 'https://dorar.net/h/jyf7OzOs?osoul=1' }
    ],
    ar: 'عَنْ أَبِي سَعِيدٍ الْخُدْرِيِّ رَضِيَ اللَّهُ عَنْهُ، أَنَّ النَّبِيَّ ﷺ قَالَ: «مَنْ قَرَأَ سُورَةَ الْكَهْفِ فِي يَوْمِ الْجُمُعَةِ أَضَاءَ لَهُ مِنَ النُّورِ مَا بَيْنَ الْجُمُعَتَيْنِ» [صحيح الجامع الصغير للألباني ٦٤٧٠، المستدرك للحاكم ٣٣٩٢، السنن الكبرى للبيهقي ٦٠٦٣]',
    en: 'Narrated by Abu Sa\'id Al-Khudri (RA): The Prophet ﷺ said: "Whoever recites Surah Al-Kahf on Friday, a light (Noor) will illuminate for him from one Friday to the next." [Sahih al-Jami\' (Shaykh al-Albani 6470), Al-Mustadrak \'ala al-Sahihayn (Imam al-Hakim 3392), Sunan al-Kubra (Imam al-Bayhaqi 6063)]',
    hi: 'हज़रत अबू सईद ख़ुदरी (रज़ि.) से रिवायत है कि नबी करीम ﷺ ने फ़रमाया: "जो शख़्स जुमे के दिन सूरह अल-कहफ़ की तिलावत करेगा, उसके लिए दोनों जुमों के दरमियान नूर चमकता रहेगा।" [सहीह अल-जामेअ (अल्बानी 6470), अल-मुस्तदरक (हाकिम 3392), सुनन अल-कुबरा (बैहक़ी 6063)]',
    ur: 'حضرت ابو سعید خدری رضی اللہ عنہ سے روایت ہے کہ نبی کریم ﷺ نے فرمایا: "جو شخص جمعہ کے دن سورۃ الکہف کی تلاوت کرے گا، اس کے لیے دونوں جمعوں کے درمیان نور روشن رہے گا۔" [صحیح الجامع الصغیر (البانی 6470)، المستدرک للحاکم (3392)، السنن الکبریٰ للبیہقی (6063)]'
  },
  durood: {
    ref: 'Sunan Abi Dawood 1047 | Sunan an-Nasa\'i 1374',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Sunan Abi Dawood 1047 (Sunnah.com)', url: 'https://sunnah.com/abudawud:1047' },
      { label: 'Sunan an-Nasa\'i 1374 (Sunnah.com)', url: 'https://sunnah.com/nasai:1374' }
    ],
    ar: 'عَنْ أَوْسِ بْنِ أَوْسٍ رَضِيَ اللَّهُ عَنْهُ قَالَ: قَالَ رَسُولُ اللَّهِ ﷺ: «إِنَّ مِنْ أَفْضَلِ أَيَّامِكُمْ يَوْمَ الْجُمُعَةِ، فِيهِ خُلِقَ آدَمُ، وَفِيهِ قُبِضَ... فَأَكْثِرُوا عَلَيَّ مِنَ الصَّلَاةِ فِيهِ، فَإِنَّ صَلَاتَكُمْ مَعْرُوضَةٌ عَلَيَّ»',
    en: 'Narrated by Aws ibn Aws (RA): The Messenger of Allah ﷺ said: "Among the most excellent of your days is Friday... therefore, send abundant blessings (Salawat/Durood) upon me on this day, for your blessings are presented to me directly."',
    hi: 'हज़रत औस बिन औस (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने फ़रमाया: "बेशक तुम्हारे दिनों में सबसे अफ़ज़ल दिन जुमे का दिन है... इसलिए इस दिन मुझ पर कसरत से दुरूद भेजा करो, क्योंकि तुम्हारा दुरूद मुझ पर पेश किया जाता है।"',
    ur: 'حضرت اوس بن اوس رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے فرمایا: "تمہارے تمام دنوں میں سب سے افضل دن جمعہ کا دن ہے... لہٰذا اس دن مجھ پر کثرت سے درود بھیجا کرو، کیونکہ تمہارا درود مجھ پر پیش کیا جاتا ہے۔"'
  },
  dua_hour: {
    ref: 'Sahih al-Bukhari 935 | Sunan Abi Dawood 1048',
    grade: 'Sahih (Authentic)',
    links: [
      { label: 'Sahih al-Bukhari 935 (Sunnah.com)', url: 'https://sunnah.com/bukhari:935' },
      { label: 'Sunan Abi Dawood 1048 (Sunnah.com)', url: 'https://sunnah.com/abudawud:1048' }
    ],
    ar: 'عَنْ أَبِي هُرَيْرَةَ رَضِيَ اللَّهُ عَنْهُ، أَنَّ رَسُولَ اللَّهِ ﷺ ذَكَرَ يَوْمَ الْجُمُعَةِ فَقَالَ: «فِيهِ سَاعَةٌ لاَ يُوَافِقُهَا عَبْدٌ مُسْلِمٌ، وَهُوَ قَائِمٌ يُصَلِّي، يَسْأَلُ اللَّهَ تَعَالَى شَيْئًا، إِلَّا أَعْطَاهُ إِيَّاهُ... فَالْتَمِسُوهَا آخِرَ سَاعَةٍ بَعْدَ الْعَصْرِ»',
    en: 'Narrated by Abu Hurairah (RA): The Messenger of Allah ﷺ mentioned Friday and said: "There is an hour on Friday when no Muslim servant stands in prayer asking Allah for anything good but He gives it to him... Seek it in the last hour after Asr prayer."',
    hi: 'हज़रत अबू हुरैरा (रज़ि.) से रिवायत है कि रसूलुल्लाह ﷺ ने जुमे के दिन का ज़िक्र करते हुए फ़रमाया: "जुमे में एक ऐसी घड़ी है जिसमें कोई मुसलमान बन्दा नमाज़ में अल्लाह से जो भी भलाई मांगता है, अल्लाह उसे अता फ़रमाता है... इसे असर के बाद की आख़िरी घड़ी में तलाश करो।"',
    ur: 'حضرت ابوہریرہ رضی اللہ عنہ سے روایت ہے کہ رسول اللہ ﷺ نے جمعہ کے دن کا ذکر کرتے ہوئے فرمایا: "جمعہ کے دن ایک گھڑی ایسی ہے کہ کوئی مسلمان بندہ اس وقت اللہ سے جو بھی دعا مانگتا ہے, اللہ اسے ضرور عطا فرماتا ہے... اسے عصر کے بعد آخری گھڑی میں تلاش کرو۔"'
  },
  charity: {
    ref: 'Musannaf \'Abd al-Razzaq 5558 — Sahih Isnad',
    grade: 'Sahih Isnad (Authentic Chain)',
    links: [
      { label: 'Musannaf \'Abd al-Razzaq 5558 (Islamweb.net)', url: 'https://www.islamweb.net/ar/library/content/73/5302/%D8%A8%D8%A7%D8%A8-%D8%B9%D8%B8%D9%85-%D9%8A%D9%88%D9%85-%D8%A7%D9%84%D8%AC%D9%85%D8%B9%D8%A9' }
    ],
    ar: 'عَنْ كَعْبِ الْأَحْبَارِ رَضِيَ اللَّهُ عَنْهُ قَالَ: «الصَّدَقَةُ يَوْمَ الْجُمُعَةِ أَعْظَمُ مِنْهَا فِي سَائِرِ الْأَيَّامِ» [مصنف عبد الرزاق ٥٥٥٨، إسناده صحيح]',
    en: 'Narrated by Ka\'b al-Ahbar (RA): "Giving charity (Sadaqah) on Friday is greater in reward than on any other day of the week." [Musannaf \'Abd al-Razzaq 5558 — Sahih Isnad]',
    hi: 'हज़रत का\'ब अल-अहबार (रज़ि.) फ़रमाते हैं: "जुमे के दिन सदक़ा व ख़ैरात करने का सवाब बाक़ी तमाम दिनों के मुक़ाबले में सबसे अफ़ज़ल और अज़ीम है।" [मुसन्नफ़ अब्दुर रज़्ज़ाक़ 5558 — सनद सहीह]',
    ur: 'حضرت کعب الاحبار رضی اللہ عنہ فرماتے ہیں: "جمعہ کے دن صدقہ و خیرات کرنا ہفتے کے باقی تمام دنوں کے مقابلے میں سب سے زیادہ اجر و فضیلت رکھتا ہے۔" [مصنف عبدالرزاق 5558 — سند صحیح]'
  }
};
function openJummahHadithModal(id, e){
  if(e) e.stopPropagation();
  const data = JUMMAH_HADITH_FULL[id];
  if(!data) return;
  const curLang = typeof lang !== 'undefined' ? lang : (localStorage.getItem('dt_lang') || 'en');
  const transText = data[curLang] || data['en'] || data['hi'] || '';
  const modal = document.getElementById('jummahHadithModal');
  const contentEl = document.getElementById('jummahHadithModalContent');
  if(!modal || !contentEl) return;
  const linksList = data.links || (data.url ? [{ label: 'View Reference Source', url: data.url }] : []);
  contentEl.innerHTML = `
    <div style="background:#FFFFFF;border-radius:24px;max-width:440px;width:92%;margin:auto;overflow:hidden;box-shadow:0 20px 16px rgba(0,0,0,0.4);border:2px solid #0E5D47;position:relative;animation:jmPopIn .2s ease-out;">
      <!-- Header -->
      <div style="background:linear-gradient(135deg,#0E5D47 0%,#063B2C 100%);padding:16px 18px;display:flex;align-items:center;justify-content:space-between;border-bottom:1.5px solid #F6D365;">
        <div style="display:flex;align-items:center;gap:8px;">
          <span style="font-size:20px;">📜</span>
          <span style="font-family:'Cinzel Decorative','Amiri',serif;font-size:16px;font-weight:900;color:#ffffff;letter-spacing:0.5px;">AUTHENTIC HADITH</span>
        </div>
        <div onclick="closeJummahHadithModal()" style="width:30px;height:30px;border-radius:50%;background:rgba(255,255,255,0.15);display:flex;align-items:center;justify-content:center;color:#FFFFFF;cursor:pointer;font-weight:900;font-size:14px;">✕</div>
      </div>
      <!-- Body -->
      <div style="padding:18px;max-height:75vh;overflow-y:auto;">
        <!-- Reference & Grade Badge -->
        <div style="display:flex;align-items:center;justify-content:space-between;gap:8px;flex-wrap:wrap;margin-bottom:14px;">
          <div style="font-size:12px;font-weight:800;color:#92400E;background:#FEF3C7;border:1px solid #F59E0B;padding:4px 10px;border-radius:8px;">
            📖 ${data.ref}
          </div>
          <div style="font-size:11px;font-weight:800;color:#065F46;background:#D1FAE5;border:1px solid #10B981;padding:4px 8px;border-radius:6px;">
            ✅ ${data.grade}
          </div>
        </div>
        <!-- Arabic Text -->
        <div style="background:#F0FDF4;border-right:4px solid #10B981;border-radius:14px;padding:14px 16px;margin-bottom:14px;font-family:'Amiri','Scheherazade New',serif;font-size:19px;line-height:1.8;color:#064E3B;direction:rtl;text-align:right;">
          ${data.ar}
        </div>
        <!-- Translation in Active Language -->
        <div style="background:#F8FAFC;border:1px solid #E2E8F0;border-radius:14px;padding:14px 16px;font-size:13.5px;line-height:1.6;color:#1E293B;margin-bottom:16px;">
          <div style="font-size:11px;font-weight:800;color:#64748B;text-transform:uppercase;margin-bottom:4px;">Translation (${curLang.toUpperCase()}):</div>
          ${transText}
        </div>
        <!-- Direct Source Links -->
        <div style="margin-bottom:12px;">
          <div style="font-size:11px;font-weight:800;color:#64748B;text-transform:uppercase;margin-bottom:6px;">Authentic References & Source Links:</div>
          ${linksList.map(lnk => `
            <a href="${lnk.url}" target="_blank" rel="noopener noreferrer" style="display:flex;align-items:center;justify-content:space-between;width:100%;box-sizing:border-box;background:linear-gradient(135deg,#0E5D47 0%,#063B2C 100%);color:#FFFFFF !important;border:1.2px solid #F6D365;padding:10px 14px;border-radius:12px;font-size:12px;font-weight:800;text-decoration:none;box-shadow:0 3px 10px rgba(14,93,71,0.25);margin-bottom:8px;cursor:pointer;">
              <span style="display:flex;align-items:center;gap:6px;"><span>🌐</span> <span>${lnk.label}</span></span>
              <span style="font-size:14px;">↗</span>
            </a>
          `).join('')}
        </div>
        <!-- Close Button -->
        <button type="button" onclick="closeJummahHadithModal()" style="width:100%;background:#F1F5F9;color:#334155;border:1px solid #CBD5E1;padding:11px;border-radius:12px;font-size:13.5px;font-weight:800;cursor:pointer;">
          ✕ Close
        </button>
      </div>
    </div>
  `;
  modal.style.display = 'flex';
}
function closeJummahHadithModal(){
  const modal = document.getElementById('jummahHadithModal');
  if(modal) modal.style.display = 'none';
}
function renderJummahScreen(){
  const body = document.getElementById('jummahBody');
  const langBar = document.getElementById('jummahLangBar');
  const titleEl = document.getElementById('jummahHeaderTitle');
  const themeBtn = document.getElementById('jummahThemeBtn');
  if(titleEl) titleEl.textContent = (getJummahUiText('title') || "JUMMAH MUBARAK").toUpperCase();
  if(themeBtn) themeBtn.textContent = isDayTheme ? '☀️' : '🌙';
  if(langBar) renderJummahLangBar();
  if(!body) return;
  const progress = loadJummahProgress();
  const duroodCount = loadJummahDuroodCount();
  const total = JUMMAH_DEFAULT_ITEMS.length;
  const doneCount = JUMMAH_DEFAULT_ITEMS.filter(it => !!progress[it.id]).length;
  const pct = Math.round((doneCount / total) * 100);
  // Render Body Content
  body.innerHTML = `
    <div style="padding:14px 16px 30px;max-width:480px;margin:0 auto;box-sizing:border-box;">
      <!-- Hero Banner Card -->
      <div class="jummah-hero-card">
        <div class="jummah-hero-badge">✨ ${getJummahUiText('badge')}</div>
        <div class="jummah-hero-title">${getJummahUiText('title')}</div>
        <div class="jummah-hero-hadith">${getJummahUiText('hadith_hero')}</div>
        <div class="jummah-progress-wrap">
          <div class="jummah-progress-top">
            <span style="color:#ffffff;">${getJummahUiText('progress_label')}</span>
            <span style="color:var(--jm-gold-lt);font-size:13px;font-weight:900;">${doneCount} / ${total} (${pct}%)</span>
          </div>
          <div class="jummah-progress-bar">
            <div class="jummah-progress-fill" style="width:${pct}%;"></div>
          </div>
        </div>
      </div>
      <!-- Sunnahs & Adhkar Checklist -->
      <div class="jummah-section-title">
        <span>✨</span> ${getJummahUiText('progress_label')}
      </div>
      ${JUMMAH_DEFAULT_ITEMS.map((item, idx) => {
        const isDone = !!progress[item.id];
        const title = getJummahItemTitle(item);
        const desc = getJummahItemDesc(item);
        const hadithText = getJummahItemHadith(item);
        const seq = String(idx + 1).padStart(2, '0');
        let extraAction = '';
        if(item.id === 'surah_kahf'){
          extraAction = `
            <div>
              <button type="button" class="jummah-action-btn" onclick="openSurahKahfFromJummah(event)">
                ${getJummahUiText('read_kahf_btn')} →
              </button>
            </div>
          `;
        }
        return `
          <div class="jummah-item-card ${isDone ? 'is-done' : ''}" onclick="toggleJummahItem('${item.id}')">
            <div class="jummah-item-ico">${item.icon}</div>
            <div class="jummah-item-content">
              <div class="jummah-item-title-row">
                <span class="jummah-item-idx">${seq}</span>
                <span class="jummah-item-title">${title}</span>
              </div>
              <div class="jummah-item-desc">${desc}</div>
              <div class="jummah-item-hadith" onclick="openJummahHadithModal('${item.id}', event)" title="View Authentic Hadith" style="cursor:pointer;">📜 ${hadithText} 🔍</div>
              ${extraAction}
            </div>
            <div class="jummah-checkbox">
              <svg viewBox="0 0 24 24" fill="none"><polyline points="20 6 9 17 4 12"></polyline></svg>
