package com.mariabuliga.softpaws.domain.usecases

import com.mariabuliga.softpaws.data.model.CatDataItem
import com.mariabuliga.softpaws.domain.repository.CatRepository
import javax.inject.Inject

class GetCatsUseCase @Inject constructor(
    private val catRepository: CatRepository
) {

    suspend fun execute(): List<CatDataItem>? = catRepository.getCats()

    suspend fun loadNext(): List<CatDataItem> = catRepository.loadNextCats()

    suspend fun searchCatsByName(query: String?): List<CatDataItem>? = catRepository.getCatsByName(query)

}