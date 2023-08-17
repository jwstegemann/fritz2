package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NullableTests {
    data class Customer(val mail: String?, val name: String)

    private val mailLens: Lens<Customer, String?> = lensOf("mail", Customer::mail) { p, v -> p.copy(mail = v) }

    private val someName = "some name"
    private val someMail = "some mail"
    private val defaultMail = "default mail"

    val customerWithoutMail = Customer(null, someName)
    val customerWithMail = Customer(someMail, someName)

    @Test
    fun nullableStoreWithDefault() = runTest {
        val defaultValue = "fritz2"
        val nonNullValue = "some Value"

        val store = storeOf<String?>(null)
        val defaultStore = store.mapNull(defaultValue)

        val valueWithDefaultId = Id.next()
        val valueWithoutDefaultId = Id.next()

        fun renderedValueWithDefault() = document.getElementById(valueWithDefaultId)?.textContent
        fun renderedValueWithoutDefault() = document.getElementById(valueWithoutDefaultId)?.textContent

        render {
            p(id = valueWithoutDefaultId) { store.data.renderText() }
            p(id = valueWithDefaultId) { defaultStore.data.renderText() }
        }

        delay(100)

        assertEquals("${null}", renderedValueWithoutDefault(), "wrong initial value")
        assertEquals(defaultValue, renderedValueWithDefault(), "default initially not applied")

        defaultStore.update(nonNullValue)
        delay(100)
        assertEquals(nonNullValue, renderedValueWithDefault(), "update does not work")

        defaultStore.update(defaultValue)
        delay(100)
        assertEquals("${null}", renderedValueWithoutDefault(), "default not respected on update")
        assertEquals(defaultValue, renderedValueWithDefault(), "default not respected on update")
    }

    @Test
    fun nullableAttributeWithoutDefaultOnNonNullableStore() = runTest {
        val store = storeOf(customerWithoutMail)
        val subStore = store.map(mailLens)

        val valueId = Id.next()

        fun renderedValue() = document.getElementById(valueId)?.textContent

        render {
            p(id = valueId) { subStore.data.renderText() }
        }

        delay(100)

        assertEquals("${null}", renderedValue(), "wrong initial value")

        subStore.update(someMail)
        delay(100)
        assertEquals(someMail, renderedValue(), "update does not work")

        subStore.update(null)
        delay(100)
        assertEquals("${null}", renderedValue(), "update to null does not work")
    }

    @Test
    fun nullableAttributeWithDefaultOnNonNullableStore() = runTest {
        val store = storeOf(customerWithoutMail)
        val subStore = store.map(mailLens)
        val subStoreWithDefault = subStore.mapNull(defaultMail)

        val valueId = Id.next()
        val valueWithDefaultId = Id.next()

        fun renderedValue() = document.getElementById(valueId)?.textContent
        fun renderedValueWithDefault() = document.getElementById(valueWithDefaultId)?.textContent

        render {
            p(id = valueId) { subStore.data.renderText() }
            p(id = valueWithDefaultId) { subStoreWithDefault.data.renderText() }
        }

        delay(100)

        assertEquals(defaultMail, renderedValueWithDefault(), "wrong initial value with default")
        assertEquals("${null}", renderedValue(), "wrong initial value")

        subStoreWithDefault.update(someMail)
        delay(100)
        assertEquals(someMail, renderedValueWithDefault(), "update does not work with default")
        assertEquals(someMail, renderedValue(), "update does not work")

        subStoreWithDefault.update(defaultMail)
        delay(100)
        assertEquals("${null}", renderedValue(), "update to null does not work")
        assertEquals(defaultMail, renderedValueWithDefault(), "update to default-value does not work")
    }

    @Test
    fun nullableAttributeWithoutDefaultOnNullableStoreWithoutDefault() = runTest {
        val customerStore = storeOf<Customer?>(null)

        val customerValueId = Id.next()
        val mailValueId = Id.next()
        val setMailId = Id.next()
        val removeMailId = Id.next()

        fun renderedCustomerValue() = document.getElementById(customerValueId)?.textContent
        fun renderedMailValue() = document.getElementById(mailValueId)?.textContent

        render {
            p(id = customerValueId) { customerStore.data.renderText() }

            customerStore.data.render {
                if (it != null) {
                    val mailStore = customerStore.map(mailLens)
                    p(id = mailValueId) { mailStore.data.renderText() }

                    button(id = setMailId) {
                        clicks handledBy mailStore.handle { someMail }
                    }

                    button(id = removeMailId) {
                        clicks handledBy mailStore.handle { null }
                    }
                }
            }
        }

        delay(100)

        assertEquals("${null}", renderedCustomerValue(), "wrong initial customer value")
        assertEquals(null, renderedMailValue(), "wrong initial mail value")

        customerStore.update(customerWithMail)
        delay(100)

        assertEquals("$customerWithMail", renderedCustomerValue(), "wrong customer with mail after update")
        assertEquals(someMail, renderedMailValue(), "wrong mail value for customer with mail after update")

        customerStore.update(customerWithoutMail)
        delay(100)

        assertEquals("$customerWithoutMail", renderedCustomerValue(), "wrong customer without mail after update")
        assertEquals("${null}", renderedMailValue(), "wrong mail value for customer without mail after update")

        val setButton = document.getElementById(setMailId)
        assertNotNull(setButton, "customer content not rendered although not null")
        (setButton as HTMLButtonElement).click()

        delay(100)

        assertEquals("$customerWithMail", renderedCustomerValue(), "wrong customer with mail after update")
        assertEquals(someMail, renderedMailValue(), "wrong mail value for customer with mail after update")

        val removeButton = document.getElementById(removeMailId)
        assertNotNull(removeButton, "customer content not rendered although not null")
        (removeButton as HTMLButtonElement).click()

        delay(100)

        assertEquals("$customerWithoutMail", renderedCustomerValue(), "wrong customer without mail after remove")
        assertEquals("${null}", renderedMailValue(), "wrong mail value for customer without mail after remove")
    }

    @Test
    fun nullableAttributeWithoutDefaultOnNullableStoreWithDefault() = runTest {
        val customerStore = storeOf<Customer?>(null)

        val customerValueId = Id.next()
        val mailValueId = Id.next()
        val mailValueWithDefaultId = Id.next()

        val setMailId = Id.next()
        val removeMailId = Id.next()

        fun renderedCustomerValue() = document.getElementById(customerValueId)?.textContent
        fun renderedMailValue() = document.getElementById(mailValueId)?.textContent
        fun renderedMailValueWithDefault() = document.getElementById(mailValueWithDefaultId)?.textContent

        render {
            p(id = customerValueId) { customerStore.data.renderText() }

            customerStore.data.render {
                if (it != null) {
                    val mailStore = customerStore.map(mailLens)
                    p(id = mailValueId) { mailStore.data.renderText() }

                    val mailStoreWithDefault = mailStore.mapNull(defaultMail)
                    p(id = mailValueWithDefaultId) { mailStoreWithDefault.data.renderText() }

                    button(id = setMailId) {
                        clicks handledBy mailStoreWithDefault.handle { someMail }
                    }

                    button(id = removeMailId) {
                        clicks handledBy mailStoreWithDefault.handle { defaultMail }
                    }
                }
            }
        }

        delay(100)

        assertEquals("${null}", renderedCustomerValue(), "wrong initial customer value")
        assertEquals(null, renderedMailValue(), "wrong initial mail value")

        customerStore.update(customerWithMail)
        delay(100)

        assertEquals("$customerWithMail", renderedCustomerValue(), "wrong customer with mail after update")
        assertEquals(someMail, renderedMailValue(), "wrong mail value for customer with mail after update")
        assertEquals(someMail, renderedMailValueWithDefault(), "wrong mail value with default for customer with mail after update")

        customerStore.update(customerWithoutMail)
        delay(100)

        assertEquals("$customerWithoutMail", renderedCustomerValue(), "wrong customer without mail after update")
        assertEquals("${null}", renderedMailValue(), "wrong mail value for customer without mail after update")
        assertEquals(defaultMail, renderedMailValueWithDefault(), "wrong mail value with default for customer without mail after update")

        val setButton = document.getElementById(setMailId)
        assertNotNull(setButton, "customer content not rendered although not null")
        (setButton as HTMLButtonElement).click()

        delay(100)

        assertEquals("$customerWithMail", renderedCustomerValue(), "wrong customer with mail after update")
        assertEquals(someMail, renderedMailValue(), "wrong mail value for customer with mail after update")
        assertEquals(someMail, renderedMailValueWithDefault(), "wrong mail value with default for customer with mail after update")

        val removeButton = document.getElementById(removeMailId)
        assertNotNull(removeButton, "customer content not rendered although not null")
        (removeButton as HTMLButtonElement).click()

        delay(100)

        assertEquals("$customerWithoutMail", renderedCustomerValue(), "wrong customer without mail after remove")
        assertEquals("${null}", renderedMailValue(), "wrong mail value for customer without mail after remove")
        assertEquals(defaultMail, renderedMailValueWithDefault(), "wrong mail value with default or customer without mail after remove")
    }
}
