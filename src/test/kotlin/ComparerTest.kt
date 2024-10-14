import app.data.fileManager.*
import app.domain.umlDiagram.comparing.*
import kotlinx.serialization.json.*
import org.junit.Test
import java.io.*
import kotlin.test.*

class ComparerTest {

    companion object {
        const val MAX_MARK = 100f
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
        explicitNulls = true
    }

    private val comparer = UMLDiagramComparer()

    @Test
    fun `Two identical diagrams should return same mark`() {
        val testSaveData = readTestFile()

        val mark = comparer.compare(testSaveData.toCompareData(), testSaveData.toCompareData()).apply {
            println("${value(MAX_MARK)}: $this")
        }.value(MAX_MARK)

        assertEquals(mark, MAX_MARK)
    }

    @Test
    fun `Two different diagrams should return different mark`() {
        val testSaveData = readTestFile()
        val changedTestSaveData = readChangedTestFile()

        val mark = comparer.compare(testSaveData.toCompareData(), changedTestSaveData.toCompareData()).apply {
            println("${value(MAX_MARK)}: $this")
        }.value(MAX_MARK)

        assertNotEquals(mark, MAX_MARK)
    }

    private fun readTestFile(): SaveData {
        val file = File("C:\\class_diagrams\\class_diagram.json")
        val saveDataJson = file.readText()

        return json.decodeFromString<SaveData>(saveDataJson)
    }

    private fun readChangedTestFile(): SaveData {
        val file = File("C:\\class_diagrams\\class_diagram_changed.json")
        val saveDataJson = file.readText()

        return json.decodeFromString<SaveData>(saveDataJson)
    }
}