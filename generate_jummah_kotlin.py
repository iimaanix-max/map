import json
import os

with open('jummah_data.json', 'r', encoding='utf-8') as f:
    d = json.load(f)

items = d['items']
hadiths = d['hadiths']
ui_langs = d['ui']

def escape_str(s):
    if s is None:
        return '""'
    return json.dumps(s, ensure_ascii=False)

def format_map(m):
    pairs = [f'{escape_str(k)} to {escape_str(v)}' for k, v in m.items()]
    return 'mapOf(' + ', '.join(pairs) + ')'

out_dir = r'c:\Users\Lenovo\Documents\index\The Native\app\src\main\java\com\tasbih\digitaltasbih\data\jummah'
os.makedirs(out_dir, exist_ok=True)
out_file = os.path.join(out_dir, 'JummahData.kt')

with open(out_file, 'w', encoding='utf-8') as f:
    f.write('package com.tasbih.digitaltasbih.data.jummah\n\n')
    f.write('data class HadithSourceLink(\n    val label: String,\n    val url: String\n)\n\n')
    f.write('data class JummahHadithDetail(\n    val ref: String,\n    val grade: String,\n    val links: List<HadithSourceLink>,\n    val ar: String,\n    val en: String,\n    val hi: String = "",\n    val ur: String = ""\n)\n\n')
    f.write('data class JummahSunnahItem(\n    val id: String,\n    val sequence: String,\n    val icon: String,\n    val hadiths: Map<String, String>,\n    val titles: Map<String, String>,\n    val descs: Map<String, String>\n)\n\n')
    f.write('data class JummahUiPack(\n    val badge: String,\n    val title: String,\n    val hadithHero: String,\n    val progressLabel: String,\n    val duroodCounterTitle: String,\n    val duroodTapBtn: String,\n    val duroodReset: String,\n    val readKahfBtn: String,\n    val mubarakTitle: String,\n    val mubarakDesc: String,\n    val resetBtn: String\n)\n\n')
    f.write('object JummahData {\n\n')

    # Sunnah items
    f.write('    val items: List<JummahSunnahItem> = listOf(\n')
    for idx, it in enumerate(items):
        seq = f'{idx + 1:02d}'
        icon_str = it.get('icon', '')
        if '<svg' in icon_str:
            icon_str = '✂️' # Clean nail clipper emoji for native rendering
        hadiths_map = format_map(it.get('hadiths', {}))
        titles_map = format_map(it.get('titles', {}))
        descs_map = format_map(it.get('descs', {}))
        f.write(f'        JummahSunnahItem(\n            id = {escape_str(it["id"])},\n            sequence = "{seq}",\n            icon = {escape_str(icon_str)},\n            hadiths = {hadiths_map},\n            titles = {titles_map},\n            descs = {descs_map}\n        ),\n')
    f.write('    )\n\n')

    # Hadith full map
    f.write('    val hadithDetails: Map<String, JummahHadithDetail> = mapOf(\n')
    for k, h in hadiths.items():
        links_code = 'listOf(' + ', '.join([f'HadithSourceLink({escape_str(l.get("label", ""))}, {escape_str(l.get("url", ""))})' for l in h.get('links', [])]) + ')'
        f.write(f'        {escape_str(k)} to JummahHadithDetail(\n')
        f.write(f'            ref = {escape_str(h.get("ref", ""))},\n')
        f.write(f'            grade = {escape_str(h.get("grade", ""))},\n')
        f.write(f'            links = {links_code},\n')
        f.write(f'            ar = {escape_str(h.get("ar", ""))},\n')
        f.write(f'            en = {escape_str(h.get("en", ""))},\n')
        f.write(f'            hi = {escape_str(h.get("hi", ""))},\n')
        f.write(f'            ur = {escape_str(h.get("ur", ""))}\n')
        f.write('        ),\n')
    f.write('    )\n\n')

    # UI packs
    f.write('    val uiPacks: Map<String, JummahUiPack> = mapOf(\n')
    for lang_code, ui in ui_langs.items():
        f.write(f'        {escape_str(lang_code)} to JummahUiPack(\n')
        f.write(f'            badge = {escape_str(ui.get("badge", ""))},\n')
        f.write(f'            title = {escape_str(ui.get("title", ""))},\n')
        f.write(f'            hadithHero = {escape_str(ui.get("hadith_hero", ""))},\n')
        f.write(f'            progressLabel = {escape_str(ui.get("progress_label", ""))},\n')
        f.write(f'            duroodCounterTitle = {escape_str(ui.get("durood_counter_title", ""))},\n')
        f.write(f'            duroodTapBtn = {escape_str(ui.get("durood_tap_btn", ""))},\n')
        f.write(f'            duroodReset = {escape_str(ui.get("durood_reset", ""))},\n')
        f.write(f'            readKahfBtn = {escape_str(ui.get("read_kahf_btn", ""))},\n')
        f.write(f'            mubarakTitle = {escape_str(ui.get("mubarak_title", ""))},\n')
        f.write(f'            mubarakDesc = {escape_str(ui.get("mubarak_desc", ""))},\n')
        f.write(f'            resetBtn = {escape_str(ui.get("reset_btn", ""))}\n')
        f.write('        ),\n')
    f.write('    )\n\n')

    # Helper functions
    f.write('''    fun getUiPack(lang: String): JummahUiPack {
        return uiPacks[lang] ?: uiPacks["en"] ?: JummahUiPack(
            badge = "BLESSED FRIDAY",
            title = "Jummah Mubarak",
            hadithHero = "\\"The best day on which the sun rises is Friday.\\" — Sahih Muslim (854)",
            progressLabel = "Sunnahs Completed",
            duroodCounterTitle = "Salawat / Durood Tap Counter",
            duroodTapBtn = "Tap for Salawat (+1)",
            duroodReset = "Reset",
            readKahfBtn = "📖 Open Surah Al-Kahf",
            mubarakTitle = "MashaAllah! Friday Sunnahs Completed",
            mubarakDesc = "May Allah accept all your Friday deeds, prayers, and blessings.",
            resetBtn = "Reset Checklist"
        )
    }

    fun getItemTitle(item: JummahSunnahItem, lang: String): String {
        return item.titles[lang] ?: item.titles["en"] ?: item.titles["hi"] ?: item.id
    }

    fun getItemDesc(item: JummahSunnahItem, lang: String): String {
        return item.descs[lang] ?: item.descs["en"] ?: item.descs["hi"] ?: ""
    }

    fun getItemHadithRef(item: JummahSunnahItem, lang: String): String {
        return item.hadiths[lang] ?: item.hadiths["en"] ?: item.hadiths["hi"] ?: ""
    }
}
''')

print('JummahData.kt generated successfully!')
