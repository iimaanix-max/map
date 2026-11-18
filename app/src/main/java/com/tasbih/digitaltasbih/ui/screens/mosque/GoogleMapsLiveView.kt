package com.tasbih.digitaltasbih.ui.screens.mosque

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.net.URLEncoder

/**
 * 100% Free Live Google Maps Engine tailored for Mosques & Halal Food worldwide.
 * - Suppresses search bar and junk POIs (hospitals, bars, garages).
 * - Styles Mosque pins in Emerald Green with Mosque Name labels.
 * - Styles Halal Food pins in Crimson Red with Restaurant Name labels.
 * - Supports turn-by-turn routing and multiple routes.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GoogleMapsLiveView(
    userLat: Double,
    userLon: Double,
    searchQuery: String = "mosque",
    radiusKm: Double = 5.0,
    refreshTrigger: Long = 0L,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoadingPage by remember { mutableStateOf(true) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    val isHalal = searchQuery.contains("halal", ignoreCase = true)

    val zoomLevel = when {
        radiusKm <= 2.0 -> 16
        radiusKm <= 5.0 -> 14
        radiusKm <= 10.0 -> 13
        else -> 12
    }

    val targetUrl = remember(userLat, userLon, searchQuery, zoomLevel) {
        val encoded = URLEncoder.encode(searchQuery, "UTF-8")
        "https://www.google.com/maps/search/$encoded/@$userLat,$userLon,${zoomLevel}z?hl=en"
    }

    LaunchedEffect(targetUrl, refreshTrigger) {
        webViewRef?.loadUrl(targetUrl)
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
            webViewRef = null
        }
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Box(modifier = modifier.clipToBounds()) {
        AndroidView(
            factory = { ctx ->
                WebView.setWebContentsDebuggingEnabled(true)
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        setGeolocationEnabled(true)
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        userAgentString = "Mozilla/5.0 (Linux; Android 14; Nexus 9) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onGeolocationPermissionsShowPrompt(
                            origin: String?,
                            callback: GeolocationPermissions.Callback?
                        ) {
                            // Allow geolocation so Google Maps renders its genuine coordinate-anchored blue dot,
                            // powered by our injected coordinates (userLat, userLon)
                            callback?.invoke(origin, true, false)
                        }

                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            if (newProgress >= 70) {
                                isLoadingPage = false
                            }
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            isLoadingPage = true
                            // 1. Mock Geolocation immediately so Google Maps never defaults to California/USA
                            val mockGeoScript = """
                                (function() {
                                    window.__dt_recentered = false;
                                    var fakeCoords = {
                                        latitude: $userLat,
                                        longitude: $userLon,
                                        accuracy: 10,
                                        altitude: null,
                                        altitudeAccuracy: null,
                                        heading: null,
                                        speed: null
                                    };
                                    var fakePos = {
                                        coords: fakeCoords,
                                        timestamp: Date.now()
                                    };
                                    if (navigator.geolocation) {
                                        navigator.geolocation.getCurrentPosition = function(s, e, o) { s(fakePos); };
                                        navigator.geolocation.watchPosition = function(s, e, o) { s(fakePos); return 1; };
                                    }
                                })();
                            """.trimIndent()
                            view?.evaluateJavascript(mockGeoScript, null)
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoadingPage = false

                            val pinFilter = if (isHalal) {
                                "hue-rotate(0deg) saturate(2.0) brightness(1.0)"
                            } else {
                                "hue-rotate(130deg) saturate(2.6) brightness(0.9)"
                            }

                            // CSS: Clean search bar, hide non-mosque POIs, style pins
                            val css = """
                                (function() {
                                    // 1. Re-mock geolocation in page context
                                    var fakeCoords = {
                                        latitude: $userLat,
                                        longitude: $userLon,
                                        accuracy: 10,
                                        altitude: null,
                                        altitudeAccuracy: null,
                                        heading: null,
                                        speed: null
                                    };
                                    var fakePos = { coords: fakeCoords, timestamp: Date.now() };
                                    if (navigator.geolocation) {
                                        navigator.geolocation.getCurrentPosition = function(s, e, o) { s(fakePos); };
                                        navigator.geolocation.watchPosition = function(s, e, o) { s(fakePos); return 1; };
                                    }

                                    var style = document.getElementById('custom-dt-style');
                                    if (!style) {
                                        style = document.createElement('style');
                                        style.id = 'custom-dt-style';
                                        document.head.appendChild(style);
                                    }
                                    style.innerHTML = `
                                        /* 1. HIDE Google Search Bar & Google Branding without touching map canvas */
                                        .bVzuaf, .QU7iXc, .JdG3E, .NtcBjb, .R30LOe, .eD6Rpc, .GnJVlc,
                                        #header, .ml-header, form, [role="search"],
                                        .searchbox, .searchbox-container, .omnibox-container,
                                        div[class*="ml-header"], div[class*="searchbox-shadow"],
                                        .app-banner, .ml-banner, [aria-label*="Get the app" i],
                                        .watermark, img[src*="googlelogo"], [aria-label*="Google" i][class*="logo"],
                                        div[class*="watermark"], .ml-header-branding, #titlecard, .widget-titlecard {
                                            display: none !important;
                                            visibility: hidden !important;
                                            height: 0 !important;
                                            min-height: 0 !important;
                                            pointer-events: none !important;
                                        }

                                        /* 2. HIDE Irrelevant POIs (Hospitals, Bars, Garages, Lodges, ATMs) */
                                        [aria-label*="Hospital"], [aria-label*="Nursing Home"], [aria-label*="Clinic"],
                                        [aria-label*="Bar"], [aria-label*="Wine"], [aria-label*="Liquor"],
                                        [aria-label*="Garage"], [aria-label*="Workshop"], [aria-label*="Repair"],
                                        [aria-label*="Hotel"], [aria-label*="Lodge"], [aria-label*="Bank"], [aria-label*="ATM"] {
                                            display: none !important;
                                            opacity: 0 !important;
                                            pointer-events: none !important;
                                        }

                                        /* 3. Style Pin Markers (Green for Mosques, Red for Halal) */
                                        img[src*="spotlight"], img[src*="marker"], img[src*="pin"], .tactile-marker-icon, [data-marker-id], .ml-marker {
                                            filter: $pinFilter !important;
                                        }
                                    `;

                                    // 4. Debug inspector for search bar element
                                    var all = document.querySelectorAll('*');
                                    for (var i = 0; i < all.length; i++) {
                                        var el = all[i];
                                        if (el.children.length === 0 && (el.textContent || '').includes('Rajnandgaon')) {
                                            console.log('DT_INSPECT: tag=' + el.tagName + ' id=' + el.id + ' class=' + el.className + ' parentTag=' + el.parentElement?.tagName + ' parentClass=' + el.parentElement?.className + ' grandParentClass=' + el.parentElement?.parentElement?.className);
                                        }
                                    }
                                    var sweep = function() {
                                        var selectors = [
                                            '.bVzuaf', '.QU7iXc', '.JdG3E', '.NtcBjb',
                                            '#header', '.ml-header', 'form', '[role="search"]',
                                            '.searchbox-container', '.omnibox-container',
                                            '#titlecard', '.widget-titlecard',
                                            'div[class*="ml-header"]', 'div[class*="searchbox-shadow"]',
                                            'div[jsaction*="omnibox"]', 'div[class*="omnibox-container"]',
                                            '.watermark', 'img[src*="googlelogo"]'
                                        ];
                                        selectors.forEach(function(s) {
                                            try {
                                                var els = document.querySelectorAll(s);
                                                els.forEach(function(el) {
                                                    el.style.setProperty('display', 'none', 'important');
                                                    el.style.setProperty('visibility', 'hidden', 'important');
                                                    el.style.setProperty('height', '0px', 'important');
                                                    el.style.setProperty('min-height', '0px', 'important');
                                                });
                                            } catch(e) {}
                                        });

                                        // Auto-dismiss 'Keep using web' prompt
                                        var buttons = document.querySelectorAll('button');
                                        for (var i = 0; i < buttons.length; i++) {
                                            var text = (buttons[i].textContent || '').toLowerCase();
                                            if (text.includes('keep using web') || text.includes('stay on web') || text.includes('dismiss') || text.includes('no thanks')) {
                                                buttons[i].click();
                                            }
                                        }

                                        // Auto-trigger native recenter so Google Maps locks onto user coordinates immediately
                                        if (!window.__dt_recentered) {
                                            var recenterBtn = document.querySelector('button[aria-label*="Re-center" i], button[aria-label*="location" i]');
                                            if (recenterBtn) {
                                                window.__dt_recentered = true;
                                                recenterBtn.click();
                                            }
                                        }
                                    };
                                    sweep();
                                    setInterval(sweep, 250);

                                    // 5. Attach Name Badges to Pins
                                    var attachBadges = function() {
                                        var isHalalMode = $isHalal;
                                        var elements = document.querySelectorAll('[aria-label]');
                                        elements.forEach(function(el) {
                                            var label = (el.getAttribute('aria-label') || '').trim();
                                            if (!label) return;
                                            var lLower = label.toLowerCase();

                                            // Hide junk POIs dynamically
                                            if (lLower.includes('hospital') || lLower.includes('nursing') || lLower.includes('bar') || lLower.includes('wine')) {
                                                el.style.display = 'none';
                                                return;
                                            }

                                            var isMatch = isHalalMode
                                                ? (lLower.includes('halal') || lLower.includes('restaurant') || lLower.includes('biryani') || lLower.includes('food') || lLower.includes('cafe'))
                                                : (lLower.includes('masjid') || lLower.includes('mosque') || lLower.includes('madarsa') || lLower.includes('idgah') || lLower.includes('dargah') || lLower.includes('jama'));

                                            if (!isMatch) return;

                                            if (!el.querySelector('.dt-place-badge')) {
                                                var cleanName = label.split('·')[0].split(',')[0].trim();
                                                var badge = document.createElement('div');
                                                badge.className = 'dt-place-badge';
                                                var bg = isHalalMode ? '#DC2626' : '#0D4A3E';
                                                var border = isHalalMode ? '#FEF08A' : '#F3D48A';
                                                var icon = isHalalMode ? '🍴' : '🕌';
                                                badge.innerHTML = '<span style="font-size:11px;margin-right:3px;">' + icon + '</span><span>' + cleanName + '</span>';
                                                badge.style.cssText = 'position: absolute; bottom: -22px; left: 50%; transform: translateX(-50%); background: ' + bg + '; color: #FFFFFF; font-size: 10px; font-weight: bold; padding: 2px 7px; border-radius: 9px; border: 1.2px solid ' + border + '; white-space: nowrap; pointer-events: none; z-index: 999999; box-shadow: 0 2px 6px rgba(0,0,0,0.4); text-shadow: 0 1px 2px rgba(0,0,0,0.5); font-family: sans-serif;';
                                                el.style.position = 'relative';
                                                el.appendChild(badge);
                                            }
                                        });
                                    };
                                    attachBadges();
                                    setInterval(attachBadges, 800);
                                })();
                            """.trimIndent()
                            view?.evaluateJavascript(css, null)
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val uri = request?.url ?: return false
                            val scheme = uri.scheme?.lowercase() ?: ""

                            // Launch native turn-by-turn navigation if user clicks Directions in Google Maps
                            if (scheme == "intent" || scheme == "geo" || scheme == "google.navigation") {
                                try {
                                    val intent = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                    return true
                                } catch (_: Exception) {}
                                return true
                            }

                            return false
                        }
                    }

                    loadUrl(targetUrl)
                    webViewRef = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-58).dp)
                .height(screenHeight + 85.dp)
        )

        AnimatedVisibility(
            visible = isLoadingPage,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Color.White.copy(alpha = 0.9f), androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF0D4A3E),
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
