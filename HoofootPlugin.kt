package com.hoofoot

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.MainAPI

class HoofootPlugin : MainAPI() {
    override var mainUrl = "https://www.hoofoot.com"
    override var name = "Hoofoot"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Movie)

    override suspend fun getMainPage(): HomePageResponse {
        // Dummy implementation
        return newHomePageResponse(arrayOf()) {}
    }
}
