import {expect, Locator, Page, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - styling
 * - transitions
 * - positioning
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Menu component */
    await page.goto("#menu");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});

test.describe('To open and close a menu', () => {

    async function createLocators(page: Page): Promise<[Locator, Locator, Locator]> {
        const btn = page.locator('#menu-button');
        const menuItems = page.locator('#menu-items');
        const popupDiv = menuItems.locator("xpath=..");
        return [btn, popupDiv, menuItems]
    }

    async function assertMenuIsOpen(btn: Locator, popupDiv: Locator, menuItems: Locator) {
        await expect(popupDiv).toBeVisible();
        await expect(btn).toHaveAttribute("aria-expanded", "true")
        await expect(menuItems).toBeFocused();
    }

    async function assertMenuIsClosed(btn: Locator, popupDiv: Locator) {
        await expect(popupDiv).toBeHidden();
        await expect(btn).toHaveAttribute("aria-expanded", "false")
    }

    test('click twice on the menuButton', async ({page}) => {
        const [btn, popupDiv, menuItems] = await createLocators(page)

        await btn.focus()
        await assertMenuIsClosed(btn, popupDiv)

        await btn.click();
        await assertMenuIsOpen(btn, popupDiv, menuItems)

        await btn.click();
        await assertMenuIsClosed(btn, popupDiv)
    });

    test('click on the menuButton first and then click outside of the menuItems', async ({page}) => {
        const [btn, popupDiv, menuItems] = await createLocators(page)

        await btn.focus()
        await assertMenuIsClosed(btn, popupDiv)

        await btn.click();
        await assertMenuIsOpen(btn, popupDiv, menuItems)

        await page.mouse.click(0, 0)
        await assertMenuIsClosed(btn, popupDiv)
    });

    for (const key of ["Enter", "Space"]) {
        test(`focus the menuButton and press ${key} then press Escape`, async ({page}) => {
            const [btn, popupDiv, menuItems] = await createLocators(page)

            /* Need some delay actions because it was not performed correctly */
            await page.mouse.click(0, 0, {delay: 1000});
            await btn.focus()
            await assertMenuIsClosed(btn, popupDiv)

            await page.press('#menu-button', key)
            await assertMenuIsOpen(btn, popupDiv, menuItems)

            await page.press('#menu-items', "Escape")
            await assertMenuIsClosed(btn, popupDiv)
        });
    }

    test('and check if data-listbox-active of item is true', async ({page}) => {
        async function assertItemIsActive(itemId: string) {
            await expect(page.locator("#" + itemId)).toHaveAttribute("data-menu-active", "true")
        }

        const [btn, popupDiv, listBoxItems] = await createLocators(page)

        await btn.focus()
        await assertMenuIsClosed(btn, popupDiv)

        await btn.click();
        await assertMenuIsOpen(btn, popupDiv, listBoxItems)

        const item1 = page.locator("#menu-item-1")
        const itemM1 = await item1.boundingBox()
        const item2 = page.locator("#menu-item-2")
        const itemM2 = await item2.boundingBox()
        const item3 = page.locator("#menu-item-3")
        const itemM3 = await item3.boundingBox()
        const item4 = page.locator("#menu-item-4")
        const itemM4 = await item4.boundingBox()

        await page.mouse.move(itemM1.x + itemM1.width / 2, itemM1.y + itemM1.height / 2)
        await assertItemIsActive("menu-item-1")
        await page.mouse.move(itemM2.x + itemM2.width / 2, itemM2.y + itemM2.height / 2)
        await assertItemIsActive("menu-item-2")
        await page.mouse.move(itemM3.x + itemM3.width / 2, itemM3.y + itemM3.height / 2)
        await assertItemIsActive("menu-item-3")
        await page.mouse.move(itemM4.x + itemM4.width / 2, itemM4.y + itemM4.height / 2)
        await assertItemIsActive("menu-item-4")
    });

});

test.describe("Navigating", () => {

    test("through the items should work by Arrow keys", async ({page}) => {

        const btn = page.locator('#menu-button');
        const menuItems = page.locator('#menu-items');
        await btn.click()
        await expect(menuItems).toBeFocused()

        async function assertItemIsActive(itemId: string) {
            await expect(page.locator("#" + itemId)).toHaveAttribute("data-menu-active", "true");
        }

        /* down */
        await menuItems.press("ArrowDown")
        await assertItemIsActive("menu-item-2")

        await menuItems.press("ArrowDown")
        await assertItemIsActive("menu-item-3")

        await menuItems.press("ArrowDown")
        await assertItemIsActive("menu-item-4")

        /* up */
        await menuItems.press("ArrowUp")
        await assertItemIsActive("menu-item-3")

        await menuItems.press("ArrowUp")
        await assertItemIsActive("menu-item-2")
    });

    test("by Arrow Keys will jump over disabled items", async ({page}) => {
        const btn = page.locator('#menu-button');
        const menuItems = page.locator('#menu-items');
        await btn.click()
        await expect(menuItems).toBeFocused()

        const startingItem = page.locator("#menu-item-4")
        const bypassedItem = page.locator("#menu-item-5")
        const targetItem = page.locator("#menu-item-6")

        await startingItem.hover()
        await expect(startingItem).toHaveAttribute("data-menu-active", "true");
        await expect(bypassedItem).toHaveAttribute("data-menu-active", "false");
        await expect(targetItem).toHaveAttribute("data-menu-active", "false");

        /* down */
        await menuItems.press("ArrowDown")
        await expect(startingItem).toHaveAttribute("data-menu-active", "false");
        await expect(bypassedItem).toHaveAttribute("data-menu-active", "false");
        await expect(targetItem).toHaveAttribute("data-menu-active", "true");

        /* up */
        await menuItems.press("ArrowUp")
        await expect(startingItem).toHaveAttribute("data-menu-active", "true");
        await expect(bypassedItem).toHaveAttribute("data-menu-active", "false");
        await expect(targetItem).toHaveAttribute("data-menu-active", "false");
    });

    for (const data of [
        {key: "a", expected: "1"},
        {key: "m", expected: "2"},
        {key: "d", expected: "3"},
        {key: "e", expected: "4"}
    ]) {
        test(`by starting character "${data.key}" will jump to first appearance of item`, async ({page}) => {
            const btn = page.locator('#menu-button');
            const menuItems = page.locator('#menu-items');
            const itemId = `menu-item-${data.expected}`
            await btn.click()
            await expect(menuItems).toBeFocused()

            await menuItems.press(data.key)
            const item = page.locator("#" + itemId)
            await expect(item).toHaveAttribute("data-menu-active", "true");
        });
    }

    for (const data of [
        {shortcut: "Home", target: "first", id: "1"},
        {shortcut: "End", target: "last", id: "6"}
    ]) {
        test(`by pressing "${data.shortcut}" will jump to ${data.target} item`, async ({page}) => {
            const btn = page.locator('#menu-button');
            const menuItems = page.locator('#menu-items');
            const itemId = `menu-item-${data.id}`
            await btn.click()
            await expect(menuItems).toBeFocused()

            await menuItems.press(data.shortcut)
            const item = page.locator("#" + itemId)
            await expect(item).toHaveAttribute("data-menu-active", "true");
        });
    }
});

test.describe("To select an item from a menu open the menuItems", () => {

    test("click on one item", async ({page}) => {
        const btn = page.getByRole('button', {name: 'Open Menu'});
        const result = page.locator("#result");

        await btn.click();
        await expect(page.locator('#menu-items')).toBeVisible();
        await page.locator("#menu-item-1").click()
        await expect(result).toContainText("Archive");

        await btn.click();
        await expect(page.locator('#menu-items')).toBeVisible();
        await page.locator("#menu-item-4").click();
        await expect(result).toContainText("Edit");
    });

    for (const key of ["Enter", "Space"]) {
        for (const spec of [
            {id: "#menu-item-1", arrowDowns: 0, text: "Archive"},
            {id: "#menu-item-2", arrowDowns: 1, text: "Move"},
            {id: "#menu-item-6", arrowDowns: 4, text: "Encrypt"},
        ]) {
            test(`press ${spec.arrowDowns} times down and then press ${key} on item`, async ({page, browserName}) => {
                test.slow(browserName === 'webkit', 'This test for Menu is slow on Mac');

                const btn = page.getByRole('button', {name: 'Open Menu'});
                const result = page.locator("#result");
                const targetItem = page.locator(spec.id);

                await btn.click();
                await expect(page.locator('#menu-items')).toBeVisible();
                await page.waitForTimeout(200);
                for (let times = 0; times < spec.arrowDowns; times++) {
                    await page.keyboard.press("ArrowDown", {delay: 100});
                }

                await expect(targetItem).toHaveAttribute("data-menu-active", "true");
                await targetItem.press(key);
                await expect(result).toContainText(spec.text);
            });
        }
    }
});