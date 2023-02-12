import java.io.File

class TestHelper {
    companion object {
        private const val TEST_EMPTY_DIRECTORY_NAME = "test_empty_directory"
        private const val TEST_HOME_DIRECTORY_NAME = "test_home_directory"
        val TEST_EMPTY_DIRECTORY = File(this::class.java.classLoader.getResource(TEST_EMPTY_DIRECTORY_NAME)!!.file)
        val TEST_HOME_DIRECTORY = File(this::class.java.classLoader.getResource(TEST_HOME_DIRECTORY_NAME)!!.file)
        val TEST_BDS_DIRECTORY = File(TEST_HOME_DIRECTORY, AppConstants.APP_DIRECTORY)

        fun resetTestEmptyDirectory() {
            val children = TEST_EMPTY_DIRECTORY.listFiles()

            if (children == null || children.isEmpty()) {
                return
            }

            for (child in children) {
                child.deleteRecursively()
            }
        }
    }
}
