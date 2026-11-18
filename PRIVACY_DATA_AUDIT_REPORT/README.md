# Digital Tasbih - Privacy & Data-Flow Audit Package

This directory contains the complete, evidence-based privacy and data-flow audit for the **Digital Tasbih** Android application (`com.tasbih.digitaltasbih`) and its backend cloud infrastructure (Cloudflare Workers, D1 SQL, Cloud Firestore, Firebase Auth, Resend, Brevo).

---

## 📁 Directory Structure & Deliverables

```
PRIVACY_DATA_AUDIT_REPORT/
├── README.md                                  <-- This guide
│
├── docs/                                      <-- Full written documentation
│   ├── DIGITAL_TASBIH_PRIVACY_AUDIT.md        <-- Complete Markdown report (all 10 deliverables, line numbers, snippets)
│   ├── EXECUTIVE_SUMMARY.md                   <-- Concise 2-page brief for founders & legal counsel
│   └── AUDIT_REPORT_WEB_VERSION.html          <-- Standalone styled HTML report viewable in any browser
│
├── diagrams/                                  <-- High-resolution visual diagrams
│   ├── diagrams_preview.html                  <-- Interactive gallery showing all diagrams with descriptions
│   ├── 01_system_architecture_data_flow.svg   <-- System topology, client boundaries & external cloud APIs
│   ├── 02_backend_storage_pii_map.svg         <-- Plain-text PII vs HMAC hashes across D1 & Firestore
│   ├── 03_permissions_and_boundaries.svg      <-- Android permissions matrix & hardware triggers
│   └── 04_consent_and_age_gate_flow.svg       <-- DPDP Act 18+ age gate & Account deletion remediation flow
│
└── pdf/                                       <-- Publication-grade executive PDF
    └── Digital_Tasbih_Privacy_and_Data_Flow_Audit_Report.pdf (9 Pages, Vector diagrams, ready to print/share)
```

---

## 🔒 Verification & Rules Adherence
- **Tasbih Module Frozen:** In strict compliance with the permanent user rule in `GEMINI.md`, the Tasbih module (`TasbihScreen.kt`, beads canvas math, counter logic, lifetime stats) was inspected in **READ-ONLY** mode. Zero lines of app code or Tasbih code were modified.
- **Evidence-Backed:** Every claim in the documentation cites exact file paths and line numbers or code snippets.
- **Secrets Protected:** No private API keys or database secrets are included in the documentation.

