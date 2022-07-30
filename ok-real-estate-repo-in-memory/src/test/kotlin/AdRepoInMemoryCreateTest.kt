import repo.ReAdRepository

class AdRepoInMemoryCreateTest: RepoAdCreateTest() {
    override val repo: ReAdRepository = ReAdRepoInMemory(
        initObjects = initObjects
    )
}
