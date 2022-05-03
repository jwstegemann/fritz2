import {expect, Locator, Page, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - label
 * - validation
 * - positioning
 * - dynamic disabling
 * - external opening or closing
 */

test.beforeEach(async ({page}) => {
    //await page.goto("http://localhost:8080/#listbox");
    await page.goto("https://next.fritz2.dev/headless-demo/#listbox");
});

test.describe('To open and close a listBox', () => {

    async function createLocators(page: Page): Promise<[Locator, Locator, Locator]> {
        const btn = await page.locator("#starwars-button")
        const listBoxItems = await page.locator("#starwars-items")
        const popperDiv = await listBoxItems.locator("xpath=..")
        return [btn, popperDiv, listBoxItems]
    }

    async function assertListBoxIsOpen(btn: Locator, popperDiv: Locator, listBoxItems: Locator) {
        await expect(popperDiv).toBeVisible();
        await expect(btn).toHaveAttribute("aria-expanded", "true")
        await expect(listBoxItems).toBeFocused()
    }

    async function assertListBoxIsClosed(btn: Locator, popperDiv: Locator) {
        await expect(popperDiv).toBeHidden();
        await expect(btn).toHaveAttribute("aria-expanded", "false")
        await expect(btn).toBeFocused()
    }

    test('click twice on the listBoxButton', async ({page}) => {
        const [btn, popperDiv, listBoxItems] = await createLocators(page)

        await btn.focus()
        await assertListBoxIsClosed(btn, popperDiv)

        await btn.click();
        await assertListBoxIsOpen(btn, popperDiv, listBoxItems)

        await btn.click();
        await assertListBoxIsClosed(btn, popperDiv)
    });

    test('click on the listBoxButton first and then click outside of the listBoxItems', async ({page}) => {
        const [btn, popperDiv, listBoxItems] = await createLocators(page)

        await btn.focus()
        await assertListBoxIsClosed(btn, popperDiv)

        await btn.click();
        await assertListBoxIsOpen(btn, popperDiv, listBoxItems)

        await page.mouse.click(0, 0)
        await assertListBoxIsClosed(btn, popperDiv)
    });

    for (const key of ["Enter", "Space"]) {
        test(`focus the listBoxButton and press ${key} then press Escape`, async ({page}) => {
            const [btn, popperDiv, listBoxItems] = await createLocators(page)

            await btn.focus()
            await assertListBoxIsClosed(btn, popperDiv)

            await page.press("#starwars-button", key)
            await assertListBoxIsOpen(btn, popperDiv, listBoxItems)

            await page.press("#starwars-items", "Escape")
            await assertListBoxIsClosed(btn, popperDiv)
        });
    }

});

test.describe("Navigating", () => {
    test("through the items should work by Arrow keys", async ({page}) => {

        const btn = await page.locator("#starwars-button")
        const listBoxItems = await page.locator("#starwars-items")
        await btn.click()
        await expect(listBoxItems).toBeFocused()

        async function assertItemIsActive(itemId: string) {
            await expect(listBoxItems).toHaveAttribute("aria-activedescendant", itemId)
            await expect(page.locator("#" + itemId)).toHaveAttribute("data-listbox-active", "true")
            await expect(page.locator("#" + itemId)).toHaveAttribute("data-listbox-selected", "false")
        }

        // down
        await listBoxItems.press("ArrowDown")
        await assertItemIsActive("starwars-item-1")

        await listBoxItems.press("ArrowDown")
        await assertItemIsActive("starwars-item-2")

        await listBoxItems.press("ArrowDown")
        await assertItemIsActive("starwars-item-3")

        // up
        await listBoxItems.press("ArrowUp")
        await assertItemIsActive("starwars-item-2")

        await listBoxItems.press("ArrowUp")
        await assertItemIsActive("starwars-item-1")
    });

    test("by Arrow Keys will jump over disabled items", async ({page}) => {
        const btn = await page.locator("#starwars-button")
        const listBoxItems = await page.locator("#starwars-items")
        await btn.click()
        await expect(listBoxItems).toBeFocused()

        const startingItem = await page.locator("#starwars-item-4")
        const bypassedItem = await page.locator("#starwars-item-5")
        const targetItem = await page.locator("#starwars-item-6")

        await startingItem.hover()
        await expect(startingItem).toHaveAttribute("data-listbox-selected", "false")

        // down
        await listBoxItems.press("ArrowDown")
        await expect(listBoxItems).toHaveAttribute("aria-activedescendant", "starwars-item-6")
        await expect(startingItem).toHaveAttribute("data-listbox-active", "false")
        await expect(startingItem).toHaveAttribute("data-listbox-selected", "false")
        await expect(bypassedItem).toHaveAttribute("data-listbox-active", "false")
        await expect(bypassedItem).toHaveAttribute("data-listbox-selected", "false")
        await expect(targetItem).toHaveAttribute("data-listbox-active", "true")
        await expect(targetItem).toHaveAttribute("data-listbox-selected", "false")

        // up
        await listBoxItems.press("ArrowUp")
        await expect(listBoxItems).toHaveAttribute("aria-activedescendant", "starwars-item-4")
        await expect(startingItem).toHaveAttribute("data-listbox-active", "true")
        await expect(startingItem).toHaveAttribute("data-listbox-selected", "false")
        await expect(bypassedItem).toHaveAttribute("data-listbox-active", "false")
        await expect(bypassedItem).toHaveAttribute("data-listbox-selected", "false")
        await expect(targetItem).toHaveAttribute("data-listbox-active", "false")
        await expect(targetItem).toHaveAttribute("data-listbox-selected", "false")
    });

    for (const data of [
        {key: "l", expected: "1"},
        {key: "c", expected: "2"},
        {key: "h", expected: "3"},
        {key: "v", expected: "6"},
        {key: "t", expected: "7"}
    ]) {
        test(`by starting character "${data.key}" will jump to first appearance of item`, async ({page}) => {
            const btn = await page.locator("#starwars-button")
            const listBoxItems = await page.locator("#starwars-items")
            const itemId = `starwars-item-${data.expected}`
            await btn.click()
            await expect(listBoxItems).toBeFocused()

            await listBoxItems.press(data.key)
            await expect(listBoxItems).toHaveAttribute("aria-activedescendant", itemId)
            const item = await page.locator("#" + itemId)
            await expect(item).toHaveAttribute("data-listbox-active", "true")
            await expect(item).toHaveAttribute("data-listbox-selected", "false")
        });
    }

    for(const data of [
        {shortcut:"Home", target: "first", id: "1"},
        {shortcut:"End", target: "last", id: "7"}
    ]) {
        test(`by pressing "${data.shortcut}" will jump to ${data.target} item`, async ({page}) => {
            const btn = await page.locator("#starwars-button")
            const listBoxItems = await page.locator("#starwars-items")
            const itemId = `starwars-item-${data.id}`
            await btn.click()
            await expect(listBoxItems).toBeFocused()

            await listBoxItems.press(data.shortcut)
            await expect(listBoxItems).toHaveAttribute("aria-activedescendant", itemId)
            const item = await page.locator("#" + itemId)
            await expect(item).toHaveAttribute("data-listbox-active", "true")
            await expect(item).toHaveAttribute("data-listbox-selected", "false")
        });
    }
});

test.describe("To select an item from a listBox open the listBoxItems", () => {
    test("then click on one item", async ({page}) => {
        const btn = await page.locator("#starwars-button")
        const listBoxItems = await page.locator("#starwars-items")
        const result = page.locator('#result')
        await expect(btn).toHaveText("Luke")

        await btn.click()
        const item = await page.locator("#starwars-item-3")
        await expect(item).toHaveAttribute("data-listbox-selected", "false")
        await item.click()
        await expect(item).toHaveAttribute("data-listbox-selected", "true")
        await expect(result).toContainText(await item.textContent())

        await expect(item).toHaveAttribute("data-listbox-active", "true")
        await expect(btn).toHaveText("Han")
        await expect(listBoxItems).toHaveAttribute("aria-activedescendant", "starwars-item-3")
    });

    for (const key of ["Enter", "Space"]) {
        test(`then press ${key}`, async ({page}) => {
            const btn = await page.locator("#starwars-button")
            const listBoxItems = await page.locator("#starwars-items")
            const result = page.locator('#result')
            await expect(btn).toHaveText("Luke")

            await btn.click()
            const item = page.locator("#starwars-item-3")
            await expect(item).toHaveAttribute("data-listbox-active", "false")
            await item.hover()
            await expect(item).toHaveAttribute("data-listbox-active", "true")
            await expect(item).toHaveAttribute("data-listbox-selected", "false")
            await page.press("#starwars-item-3", key)
            await expect(item).toHaveAttribute("data-listbox-selected", "true")
            await expect(result).toContainText(await item.textContent())

            await expect(btn).toHaveText("Han")
            await expect(listBoxItems).toHaveAttribute("aria-activedescendant", "starwars-item-3")
        });
    }
});