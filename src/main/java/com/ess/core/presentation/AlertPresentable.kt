package com.ess.core.presentation

data class Action(
        val title: String,
        val action: (() -> Unit)? = null
)

interface AlertPresentable {
    fun showAlert(
            title: String?,
            message: String?,
            positive: Action? = null,
            negative: Action? = null,
            neutral: Action? = null
    )

    fun hideAlert()
}