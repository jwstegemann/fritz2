import { expect, Locator, Page, test } from "@playwright/test";


test.beforeEach(async ({ page }) => {
    await page.goto("#combobox");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});


async function createLocators(page: Page): Promise<[Locator, Locator]> {
    const btn = page.locator("#countries-input")
    const listBoxItems = page.locator("#countries-items")
    return [btn, listBoxItems]
}

async function assertIsOpen(input: Locator, popupRoot: Locator) {
    await expect(popupRoot).toBeVisible();
    await expect(input).toHaveAttribute("aria-expanded", "true")
}

async function assertIsClosed(input: Locator, popupRoot: Locator) {
    await expect(popupRoot).toBeHidden();
    await expect(input).toHaveAttribute("aria-expanded", "false")
}


const clickInput = (page: Page) => page.locator("#countries-input").click();

const clickOutside = (page: Page) => page.mouse.click(0, 0);

const pressTab = (page: Page) => page.keyboard.down("Tab");

const pressEscape = (page: Page) => page.keyboard.down("Escape");

const openCloseArgs = [
    {
        desc: "clicking into the input field, then clicking outside",
        open: clickInput,
        close: clickOutside,
    },
    {
        desc: "tabbing into the input field, then pressing tab again",
        open: pressTab,
        close: pressTab,
    },
    {
        desc: "clicking into the input field, then pressing tab",
        open: clickInput,
        close: pressTab,
    },
    {
        desc: "tabbing into the input field, then clicking outside",
        open: pressTab,
        close: clickOutside,
    },
    {
        desc: "clicking into the input field, then pressing escape",
        open: clickInput,
        close: pressEscape,
    },
    {
        desc: "tabbing into the input field, then pressing escape",
        open: pressTab,
        close: pressEscape,
    },
];


test("Dropdown is initially closed", async ({ page }) => {
    const [input, items] = await createLocators(page);
    await assertIsClosed(input, items);
});


test.describe("Trying to open and close a combobox", () => {

    test.describe("with the dropdown opening eagerly", () => {
        for (const { desc, open, close } of openCloseArgs) {
            test(`by ${desc} opens and closes the dropdown`, async ({ page }) => {
                const [input, items] = await createLocators(page);

                await open(page);
                await assertIsOpen(input, items);

                await close(page);
                await assertIsClosed(input, items);
            });
        }
    });

    test.describe("with the dropdown opening lazily", () => {

        for (const { desc, open, close } of openCloseArgs) {
            test(`by ${desc} does not open the dropdown`, async ({ page }) => {
                const [input, items] = await createLocators(page);

                await page.locator("#checkbox-enable-lazy-opening").check();

                await open(page);
                await assertIsClosed(input, items);

                await close(page);
                await assertIsClosed(input, items);
            });
        }

        test("by typing a query opens the dropdown", async ({ page }) => {
            const [input, items] = await createLocators(page);

            await page.locator("#checkbox-enable-lazy-opening").check();

            await input.click();
            await expect(items).not.toBeVisible();

            await input.pressSequentially("ger");
            await expect(items).toBeVisible();
        })
    });
});

test.describe("Entering an leaving the input", () => {

    for (const { desc, open, close } of openCloseArgs) {
        test(`by ${desc} resets the input`, async ({ page }) => {
            const [input, items] = await createLocators(page);

            await open(page);

            await input.pressSequentially("xxx");
            await expect(input).toHaveValue("xxx");

            await close(page);
            await expect(input).toBeEmpty();
        });
    }
});

test("With a default value of 'null', the input is empty", async ({ page }) => {
    const [input, _] = await createLocators(page);

    const selection = page.locator("#countries-selection");
    await expect(selection).toContainText("null");
        
    await expect(input).toHaveValue("");
});

test.describe("When updating the value via the data-binding", () => {

    test("non-null values are reflected in the input element", async ({ page }) => {
        const [input, _] = await createLocators(page);

        const selection = page.locator("#countries-selection");
        await expect(selection).toContainText("null");

        const selectionButton = await page.locator('#btn-select-2');
        const expectedValue = await selectionButton.textContent();

        await selectionButton.click();

        await expect(input).toHaveValue(expectedValue!);
    });

    test("null values are reflected in the input element", async ({ page }) => {
        const [input, _] = await createLocators(page);

        const selectionButton = await page.locator('#btn-select-2');
        await selectionButton.click();

        const resetButton = await page.locator('#btn-select-0');
        await resetButton.click();

        await expect(input).toHaveValue("");
    });
})

test.describe("When the input is read-only", () => {

    const selectInputText = async (page: Page) => page.locator("#countries-input").selectText();

    for (const { desc, action } of [
        {
            desc: "focusing the input does not open the dropdown",
            action: clickInput
        },
        {
            desc: "selecting text does not open the dropdown",
            action: selectInputText
        },
    ]) {
        test(desc, async ({ page }) => {
            const [input, items] = await createLocators(page);

            await page.locator('#checkbox-enable-readonly').check();

            await action(page);
            await expect(items).not.toBeVisible();
        })
    }
});

test.describe("Activating an item", () => {

    for (const { desc, activate } of [
        {
            desc: "via mouse hover",
            activate: (page: Page) => page.locator("#countries-item-0").hover()
        },
        {
            desc: "via keyboard",
            activate: (page: Page) => page.keyboard.down("ArrowDown")
        },
    ]) {
        test(`${desc} sets 'aria-activedescendant'`, async ({ page }) => {
            const [input, items] = await createLocators(page);
            await input.click();

            await expect(items).not.toHaveAttribute("aria-activedescendant");

            const firstItem = page.locator("#countries-item-0");
            await expect(firstItem).toBeVisible();

            await activate(page);
            await expect(items).toHaveAttribute("aria-activedescendant", "countries-item-0");
        })
    }
});

test("Changing the active item updates 'aria-activedescendant'", async ({ page }) => {
    const [input, items] = await createLocators(page);
    await input.click();

    await expect(items).not.toHaveAttribute("aria-activedescendant");

    const firstItem = page.locator("#countries-item-0");
    await expect(firstItem).toBeVisible();

    const secondItem = page.locator("#countries-item-1");
    await expect(secondItem).toBeVisible();

    await firstItem.hover();
    await expect(items).toHaveAttribute("aria-activedescendant", "countries-item-0");

    await secondItem.hover();
    await expect(items).toHaveAttribute("aria-activedescendant", "countries-item-1");
});

test.describe("When activating an item via keyboard", () => {

    for (const { key, index } of [
        { key: "Home", index: 0 },
        { key: "End", index: 19 }
    ]) {
        test(`'pressing ${{ key }}' jumps to the ${index}th item`, async ({ page }) => {
            const [input, items] = await createLocators(page);
            await input.click();

            const item = page.locator("#countries-item-0");
            await expect(item).toBeVisible();

            await page.keyboard.down(key);
            await expect(items).toHaveAttribute("aria-activedescendant", `countries-item-${index}`);
        });
    }

    for (const { setupKey, testKey, index, indexName } of [
        { setupKey: "Home", testKey: "ArrowUp", index: 0, indexName: "first" },
        { setupKey: "End", testKey: "ArrowDown", index: 19, indexName: "last" }
    ]) {
        test(`'pressing ${testKey}' on the ${indexName} item does nothing`, async ({ page }) => {
            const [input, items] = await createLocators(page);
            await input.click();

            await expect(page.locator("#countries-item-0")).toBeVisible();

            await page.keyboard.down(setupKey);
            await expect(items).toHaveAttribute("aria-activedescendant", `countries-item-${index}`);

            await page.keyboard.down(testKey);
            await expect(items).toHaveAttribute("aria-activedescendant", `countries-item-${index}`);
        });
    }
});

test.describe("Select an item", () => {

    test("by clicking on it", async ({ page }) => {
        const [input, items] = await createLocators(page);
        const selection = page.locator("#countries-selection");

        await expect(selection).toContainText("null");

        await input.click();

        const firstItem = page.locator("#countries-item-0");
        await expect(firstItem).toBeVisible();

        await firstItem.click();
        await expect(selection).toContainText((await firstItem.textContent())!);
    });

    test("by activating it via the keyboard and pressing Enter", async ({ page }) => {
        const [input, items] = await createLocators(page);
        const selection = page.locator("#countries-selection");

        await expect(selection).toContainText("null");

        await page.keyboard.down("Tab");

        const firstItem = page.locator("#countries-item-0");
        await expect(firstItem).toBeVisible();

        await page.keyboard.down("ArrowDown");
        await expect(items).toHaveAttribute("aria-activedescendant", "countries-item-0");

        await page.keyboard.down("Enter");
        await expect(selection).toContainText((await firstItem.textContent())!);
    });
});

test.describe("When typing a query", () => {

    test("the result dropdown is updated", async ({ page }) => {
        const [input, items] = await createLocators(page);

        await input.click();
        await expect(page.locator("#countries-item-0")).toBeVisible();

        await expect(page.locator("#countries-items > [data-combobox-item]")).toHaveCount(20);

        await input.fill("oma");

        await expect(page.locator("#countries-items > [data-combobox-item]")).toHaveCount(3);
        await expect(page.locator("#countries-item-0")).toContainText("Oman");
        await expect(page.locator("#countries-item-1")).toContainText("Romania");
        await expect(page.locator("#countries-item-2")).toContainText("Somalia");
    });

    test("closing the dropdown resets the input field", async ({ page }) => {
        const [input, items] = await createLocators(page);

        await input.click();
        await expect(page.locator("#countries-item-0")).toBeVisible();

        await input.fill("oma");

        await page.mouse.click(0, 0);

        const selection = page.locator("#countries-selection");
        await expect(selection).toContainText("null");
    });

    test("by default, exact matches are not auto-selected", async ({ page }) => {
        const [input, items] = await createLocators(page);

        await input.click();
        await expect(page.locator("#countries-item-0")).toBeVisible();

        await input.fill("Oman");

        await expect(page.locator("#countries-items > [data-combobox-item]")).toHaveCount(2);
        await expect(items).toBeVisible();
        const selection = page.locator("#countries-selection");
        await expect(selection).toContainText("null");
    });

    test("exact matches are auto-selected if configured", async ({ page }) => {
        const [input, items] = await createLocators(page);

        await page.locator("#checkbox-enable-autoselect").check();

        await input.click();
        await expect(page.locator("#countries-item-0")).toBeVisible();

        await input.fill("Oman");

        const selection = page.locator("#countries-selection");
        await expect(items).not.toBeVisible();
        await expect(selection).toContainText("Oman");
    });

    test("in lazy dropdown opening mode, the dropdown is first hidden and then opened", async ({ page }) => {
        const [input, items] = await createLocators(page);

        await page.locator("#checkbox-enable-lazy-opening").check();

        await input.click();
        await expect(items).not.toBeVisible();

        await input.pressSequentially("ger");
        await expect(items).toBeVisible();
    })
});

/*
 * Regression test based on a niche bug encountered during development
 *
 * Bug:
 *
 * 1) Enable lazy dropdown mode
 * 2) Type a query that yields results
 * 3) Close the dropdown
 * 4) Re-enter input
 * 5) Select invisible items via keyboard
 * 6) Selection is updated - BUG
 *
 * Keyboard selections should not be possible when the dropdown is closed.
 */
test("Keyboard selection in lazy mode does nothing when the dropdown is closed", async ({ page }) => {
    const [input, items] = await createLocators(page);

    await page.locator("#checkbox-enable-lazy-opening").check();

    await input.click();
    await input.pressSequentially("oma");
    await expect(items).toBeVisible();

    await page.mouse.click(0, 0);
    await expect(items).toBeHidden();

    await input.click();
    await expect(input).toBeFocused();
    await expect(input).toBeEmpty();

    await page.waitForTimeout(100);

    await page.keyboard.press("ArrowDown");
    await page.waitForTimeout(100);
    await page.keyboard.press("Enter");
    await page.waitForTimeout(100);

    await expect(input).toBeEmpty();
    await expect(page.locator("#countries-selection")).toContainText("null");
});