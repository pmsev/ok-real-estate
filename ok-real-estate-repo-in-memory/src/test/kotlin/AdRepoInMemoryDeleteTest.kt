import repo.ReAdRepository

class AdRepoInMemoryDeleteTest: RepoAdDeleteTest() {
    override val repo: ReAdRepository = ReAdRepoInMemory(
        initObjects = initObjects
    )
}
