import {expect, Locator, Page, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - styling
 * - transitions
 * - positioning
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Menu component
    // await page.goto("https://next.fritz2.dev/headless-demo/#menu");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#menu");
//end of before hooks
});

test.describe('To open and close a menu', () => {

    async function createLocators(page: Page): Promise<[Locator, Locator, Locator]> {
        const btn = page.locator('#menu-button');
        const menuItems = page.locator('#menu-items');
        const popperDiv = menuItems.locator("xpath=..");
        return [btn, popperDiv, menuItems]
    }

    async function asserMenuIsOpen(btn: Locator, popperDiv: Locator, menuItems: Locator) {
        await expect(popperDiv).toBeVisible();
        await expect(btn).toHaveAttribute("aria-expanded", "true")
        await expect(menuItems).toBeFocused();
    }

    async function assertMenuIsClosed(btn: Locator, popperDiv: Locator) {
        await expect(popperDiv).toBeHidden();
        await expect(btn).toHaveAttribute("aria-expanded", "false")
    }

    test('click twice on the menuButton', async ({page}) => {
        const [btn, popperDiv, menuItems] = await createLocators(page)

        await btn.focus()
        await assertMenuIsClosed(btn, popperDiv)

        await btn.click();
        await asserMenuIsOpen(btn, popperDiv, menuItems)

        await btn.click();
        await assertMenuIsClosed(btn, popperDiv)
    });

    test('click on the menuButton first and then click outside of the menuItems', async ({page}) => {
        const [btn, popperDiv, menuItems] = await createLocators(page)

        await btn.focus()
        await assertMenuIsClosed(btn, popperDiv)

        await btn.click();
        await asserMenuIsOpen(btn, popperDiv, menuItems)

        await page.mouse.click(0, 0)
        await assertMenuIsClosed(btn, popperDiv)
    });

    for (const key of ["Enter", "Space"]) {
        test(`focus the menuButton and press ${key} then press Escape`, async ({page}) => {
            const [btn, popperDiv, menuItems] = await createLocators(page)

            await btn.focus()
            await assertMenuIsClosed(btn, popperDiv)

            await page.press('#menu-button', key)
            await asserMenuIsOpen(btn, popperDiv, menuItems)

            await page.press('#menu-items', "Escape")
            await assertMenuIsClosed(btn, popperDiv)
        });
    }

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

        // down
        await menuItems.press("ArrowDown")
        await assertItemIsActive("menu-item-2")

        await menuItems.press("ArrowDown")
        await assertItemIsActive("menu-item-3")

        await menuItems.press("ArrowDown")
        await assertItemIsActive("menu-item-4")

        // up
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

        // down
        await menuItems.press("ArrowDown")
        await expect(startingItem).toHaveAttribute("data-menu-active", "false");
        await expect(bypassedItem).toHaveAttribute("data-menu-active", "false");
        await expect(targetItem).toHaveAttribute("data-menu-active", "true");

        // up
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

    for(const data of [
        {shortcut:"Home", target: "first", id: "1"},
        {shortcut:"End", target: "last", id: "6"}
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
    
    test("then click on one item", async ({page}) => {
        async function getMenuItem(itemName: String) {
            const menuItem = page.locator("#" + itemName);
            return menuItem;
        }
        async function getMenuItemText(itemName: String) {
            const menuItemText = page.locator("#" + itemName).textContent();
            return menuItemText;
        }

        const btn = page.locator('#menu-button');
        const result = page.locator('#result');

        await btn.click();
        await (await getMenuItem("menu-item-1")).click();
        await expect(result).toContainText(await getMenuItemText("menu-item-1"));

        await btn.click();
        await (await getMenuItem("menu-item-2")).click();
        await expect(result).toContainText(await getMenuItemText("menu-item-2"));

        await btn.click();
        await (await getMenuItem("menu-item-3")).click();
        await expect(result).toContainText(await getMenuItemText("menu-item-3"));

        await btn.click();
        await (await getMenuItem("menu-item-4")).click();
        await expect(result).toContainText(await getMenuItemText("menu-item-4"));

        await btn.click();
        await (await getMenuItem("menu-item-6")).click();
        await expect(result).toContainText(await getMenuItemText("menu-item-6"));
    });

    for (const key of ["Enter", "Space"]) {

        test(`then press ${key} on item`, async ({page}) => {

            const menuItem1 = page.locator('#menu-item-1');
            const menuItem2 = page.locator('#menu-item-2');
            const menuItem3 = page.locator('#menu-item-3');
            const menuItem4 = page.locator('#menu-item-4');
            const menuItem6 = page.locator('#menu-item-6');
    
            const btn = page.locator('#menu-button');
            const result = page.locator('#result');
            
             await btn.click();
             await (menuItem1).press(key);
             await expect(result).toContainText(await menuItem1.textContent());
    
             await btn.click();
             await (menuItem1).press("ArrowDown");
             await (menuItem2).press(key);
             await expect(result).toContainText(await menuItem2.textContent());
    
             await btn.click();
             await (menuItem1).press("ArrowDown");
             await (menuItem2).press("ArrowDown");
             await (menuItem3).press(key);
             await expect(result).toContainText(await menuItem3.textContent());
    
             await btn.click();
             await (menuItem1).press("ArrowDown");
             await (menuItem2).press("ArrowDown");
             await (menuItem3).press("ArrowDown");
             await (menuItem4).press(key);
             await expect(result).toContainText(await menuItem4.textContent());
    
             await btn.click();
             await (menuItem1).press("ArrowDown");
             await (menuItem2).press("ArrowDown");
             await (menuItem3).press("ArrowDown");
             await (menuItem4).press("ArrowDown");
             await (menuItem6).press(key);
             await expect(result).toContainText(await menuItem6.textContent());
        });
    }
});