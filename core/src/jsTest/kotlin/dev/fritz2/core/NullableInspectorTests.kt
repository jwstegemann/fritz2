package dev.fritz2.core

import kotlinx.coroutines.Job
import kotlin.test.Test
import kotlin.test.assertEquals

class NullableInspectorTests {

    @Test
    fun testMapNullResultsInSpecifiedDefaultValueWhenCalledOnNullContainingInspector() {
        val rootInspector = inspectorOf<String?>(null)
        val expected = "Foo"

        val resultInspector = rootInspector.mapNull(expected)
        val resultValue = resultInspector.data

        assertEquals(
            expected,
            resultValue,
            "Data of the derived Inspector must equal the expected value."
        )
    }

    @Test
    fun testPathsAreEqualForStoresAndInspectorsWhenDerivedViaMapNull() {
        val rootInspector = inspectorOf<String?>(null)
        val rootStore = storeOf<String?>(null, job = Job())

        val nonNullableInspector = rootInspector.mapNull("Test")
        val nonNullableStore = rootStore.mapNull("Test")

        assertEquals(
            nonNullableStore.path,
            nonNullableInspector.path,
            "Sub inspector and sub Store paths must be the same."
        )
    }
}