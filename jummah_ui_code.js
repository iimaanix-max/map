






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






