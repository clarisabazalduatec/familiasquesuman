
package com.example.familiasquesuman.data.local

import android.content.Context

class OnboardingPreferences(context: Context) {

    private val preferencias = context.getSharedPreferences(
        "familias_preferencias",
        Context.MODE_PRIVATE
    )

    fun yaCompletoOnboarding(): Boolean {
        return preferencias.getBoolean(
            "onboarding_completado",
            false
        )
    }

    fun marcarComoCompletado() {
        preferencias.edit()
            .putBoolean("onboarding_completado", true)
            .apply()
    }
}
