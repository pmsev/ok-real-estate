import repo.ReAdRepository

class AdRepoInMemoryReadTest: RepoAdReadTest() {
    override val repo: ReAdRepository = ReAdRepoInMemory(
        initObjects = initObjects
    )
}
