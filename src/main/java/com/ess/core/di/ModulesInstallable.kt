package com.ess.core.di

import toothpick.Scope

interface ModulesInstallable {
    fun installModules(scope: Scope) = Unit
}