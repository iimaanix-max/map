      }).join('')}
      ${doneCount === total ? `
        <div style="background:linear-gradient(135deg,rgba(16,185,129,0.15) 0%,rgba(246,211,101,0.2) 100%);border:2px solid var(--jm-gold);border-radius:18px;padding:18px;text-align:center;margin:20px 0 30px;">
          <div style="font-size:36px;margin-bottom:8px;">🌟</div>
          <div style="font-size:18px;font-weight:900;color:var(--jm-emerald);margin-bottom:4px;">${getJummahUiText('mubarak_title')}</div>
          <div style="font-size:13px;color:var(--jm-muted);line-height:1.5;">${getJummahUiText('mubarak_desc')}</div>
        </div>
      ` : ''}
    </div>
  `;
}
