import repo.ReAdRepository

class AdRepoInMemorySearchTest: RepoAdSearchTest() {
    override val repo: ReAdRepository = ReAdRepoInMemory(
        initObjects = initObjects
    )
}
