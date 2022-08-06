import repo.ReAdRepository

class AdRepoInMemoryUpdateTest: RepoAdUpdateTest() {
    override val repo: ReAdRepository = ReAdRepoInMemory(
        initObjects = initObjects, randomUuid = { newLock.asString() }
    )
}
