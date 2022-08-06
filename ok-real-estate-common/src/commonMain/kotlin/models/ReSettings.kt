package models

import repo.ReAdRepository

data class ReSettings(
    val repoStub: ReAdRepository = ReAdRepository.NONE,
    val repoTest: ReAdRepository = ReAdRepository.NONE,
    val repoProd: ReAdRepository = ReAdRepository.NONE,
)
