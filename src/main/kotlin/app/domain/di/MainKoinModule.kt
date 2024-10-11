package app.domain.di

import app.domain.viewModels.designing.*
import moe.tlaster.precompose.koin.*
import moe.tlaster.precompose.viewmodel.*
import org.koin.dsl.*

val mainKoinModule = module {
    factory { DesigningViewModel() }
}