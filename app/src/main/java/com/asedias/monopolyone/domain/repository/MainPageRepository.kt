package com.asedias.monopolyone.domain.repository

import com.asedias.monopolyone.domain.model.ResponseState
import com.asedias.monopolyone.domain.model.main_page.MainPageData

interface MainPageRepository: AwaitSessionRepository {
    suspend fun getMainPageData(): ResponseState<MainPageData>
}