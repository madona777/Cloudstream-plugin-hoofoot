package com.hoofoot

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.extractors.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

class HoofootPlugin : MainAPI() {
    override var mainUrl = "https://www.hoofoot.com"
    override var name = "Hoofoot"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Movie)

    override val mainPage = mainPageOf(
        "$mainUrl/" to "Highlights"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "$mainUrl/page$page.html"
        val document = app.get(url).document
        val home = document.select("div.videodiv").mapNotNull {
            it.toSearchResult()
        }
        return HomePageResponse(
            listOf(HomePageList(request.name, home))
        )
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val link = selectFirst("a")?.attr("href") ?: return null
        val title = selectFirst("a")?.attr("title") ?: return null
        val poster = selectFirst("img")?.attr("src")
        return newMovieSearchResponse(title, link) {
            this.posterUrl = poster
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document
        val title = document.selectFirst("meta[property=og:title]")?.attr("content") ?: "Video"
        val videoUrl = document.selectFirst("meta[property=og:video]")?.attr("content") ?: throw ErrorLoadingException("No video found")

        return newMovieLoadResponse(title, url, TvType.Movie, videoUrl) {
            this.posterUrl = document.selectFirst("meta[property=og:image]")?.attr("content")
        }
    }
}
