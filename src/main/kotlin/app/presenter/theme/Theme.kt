package app.presenter.theme

enum class Theme {
    LIGHT,
    DARK;

    operator fun not(): Theme {
        return when(this) {
            LIGHT -> DARK
            DARK -> LIGHT
        }
    }
}