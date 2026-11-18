
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
