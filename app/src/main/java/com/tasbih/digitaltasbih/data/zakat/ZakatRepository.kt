package com.tasbih.digitaltasbih.data.zakat

import android.content.Context
import android.content.SharedPreferences

enum class NisabStandard {
    SILVER, // 595 grams of silver (Scholarly majority preference for cash/currency)
    GOLD    // 85 grams of gold (Standard for pure gold holdings)
}

data class ZakatCalculationData(
    val currencySymbol: String = "₹",
    val nisabStandard: NisabStandard = NisabStandard.SILVER,
    val goldRatePerGram: Double = 7500.0,
    val silverRatePerGram: Double = 95.0,
    val customNisabValue: Double? = null,
    val isSolarYear: Boolean = false, // false = 2.5% Lunar (Hijri), true = 2.577% Solar (Gregorian)

    // Assets
    val cashInHand: Double = 0.0,
    val bankSavings: Double = 0.0,
    val goldGrams: Double = 0.0,
    val goldDirectValue: Double = 0.0,
    val silverGrams: Double = 0.0,
    val silverDirectValue: Double = 0.0,
    val businessInventory: Double = 0.0,
    val moneyOwedToYou: Double = 0.0,
    val investmentsAndStocks: Double = 0.0,
    val otherZakatableAssets: Double = 0.0,

    // Liabilities
    val debtsDueNow: Double = 0.0
) {
    val effectiveGoldValue: Double
        get() = if (goldDirectValue > 0.0) goldDirectValue else (goldGrams * goldRatePerGram)

    val effectiveSilverValue: Double
        get() = if (silverDirectValue > 0.0) silverDirectValue else (silverGrams * silverRatePerGram)

    val totalCashAndBank: Double
        get() = cashInHand + bankSavings

    val totalAssets: Double
        get() = totalCashAndBank + effectiveGoldValue + effectiveSilverValue +
                businessInventory + moneyOwedToYou + investmentsAndStocks + otherZakatableAssets

    val totalLiabilities: Double
        get() = debtsDueNow

    val netZakatableWealth: Double
        get() = (totalAssets - totalLiabilities).coerceAtLeast(0.0)

    val nisabThreshold: Double
        get() = customNisabValue ?: when (nisabStandard) {
            NisabStandard.SILVER -> 595.0 * silverRatePerGram
            NisabStandard.GOLD -> 85.0 * goldRatePerGram
        }

    val isObligatory: Boolean
        get() = netZakatableWealth >= nisabThreshold && netZakatableWealth > 0.0

    val zakatRatePercent: Double
        get() = if (isSolarYear) 2.577 else 2.5

    val zakatPayable: Double
        get() = if (isObligatory) (netZakatableWealth * (zakatRatePercent / 100.0)) else 0.0
}

object ZakatRepository {
    private const val PREFS_NAME = "zakat_calculator_prefs"
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    fun loadData(context: Context): ZakatCalculationData {
        init(context)
        val p = prefs ?: return ZakatCalculationData()

        val nisabStd = try {
            NisabStandard.valueOf(p.getString("nisab_standard", NisabStandard.SILVER.name) ?: NisabStandard.SILVER.name)
        } catch (_: Exception) {
            NisabStandard.SILVER
        }

        val customNisab = if (p.contains("custom_nisab")) p.getFloat("custom_nisab", 0f).toDouble().takeIf { it > 0 } else null

        return ZakatCalculationData(
            currencySymbol = p.getString("currency_symbol", "₹") ?: "₹",
            nisabStandard = nisabStd,
            goldRatePerGram = p.getFloat("gold_rate", 7500.0f).toDouble(),
            silverRatePerGram = p.getFloat("silver_rate", 95.0f).toDouble(),
            customNisabValue = customNisab,
            isSolarYear = p.getBoolean("is_solar_year", false),

            cashInHand = p.getFloat("cash_in_hand", 0f).toDouble(),
            bankSavings = p.getFloat("bank_savings", 0f).toDouble(),
            goldGrams = p.getFloat("gold_grams", 0f).toDouble(),
            goldDirectValue = p.getFloat("gold_direct_val", 0f).toDouble(),
            silverGrams = p.getFloat("silver_grams", 0f).toDouble(),
            silverDirectValue = p.getFloat("silver_direct_val", 0f).toDouble(),
            businessInventory = p.getFloat("business_inventory", 0f).toDouble(),
            moneyOwedToYou = p.getFloat("money_owed", 0f).toDouble(),
            investmentsAndStocks = p.getFloat("investments", 0f).toDouble(),
            otherZakatableAssets = p.getFloat("other_assets", 0f).toDouble(),

            debtsDueNow = p.getFloat("debts_due_now", 0f).toDouble()
        )
    }

    fun saveData(context: Context, data: ZakatCalculationData) {
        init(context)
        prefs?.edit()?.apply {
            putString("currency_symbol", data.currencySymbol)
            putString("nisab_standard", data.nisabStandard.name)
            putFloat("gold_rate", data.goldRatePerGram.toFloat())
            putFloat("silver_rate", data.silverRatePerGram.toFloat())
            if (data.customNisabValue != null) {
                putFloat("custom_nisab", data.customNisabValue.toFloat())
            } else {
                remove("custom_nisab")
            }
            putBoolean("is_solar_year", data.isSolarYear)

            putFloat("cash_in_hand", data.cashInHand.toFloat())
            putFloat("bank_savings", data.bankSavings.toFloat())
            putFloat("gold_grams", data.goldGrams.toFloat())
            putFloat("gold_direct_val", data.goldDirectValue.toFloat())
            putFloat("silver_grams", data.silverGrams.toFloat())
            putFloat("silver_direct_val", data.silverDirectValue.toFloat())
            putFloat("business_inventory", data.businessInventory.toFloat())
            putFloat("money_owed", data.moneyOwedToYou.toFloat())
            putFloat("investments", data.investmentsAndStocks.toFloat())
            putFloat("other_assets", data.otherZakatableAssets.toFloat())

            putFloat("debts_due_now", data.debtsDueNow.toFloat())
            apply()
        }
    }

    fun clearAll(context: Context) {
        init(context)
        prefs?.edit()?.clear()?.apply()
    }
}

