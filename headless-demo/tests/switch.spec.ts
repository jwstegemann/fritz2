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
    await page.goto("https://next.fritz2.dev/headless-demo/#switch");
});

test.describe('To switch on and off', () => {

    async function createLocators(page: Page): Promise<[Locator, Locator]> {
        const firstToggle = await page.locator('button[role="switch"]:has-text("Use setting")').first();
        const secondToggle = await page.locator('text=Available to hireNulla amet tempus sit accumsan.Use setting >> button[role="switch"]');
        return [firstToggle, secondToggle]
    }

    async function asserFirstToggleIsSwitchedOn(firstToggle: Locator) {
        await expect(firstToggle).toHaveAttribute("aria-checked", "true");
        await expect(firstToggle).toBeFocused();
    }

    async function asserFirstToggleIsSwitchedOff(firstToggle: Locator) {
        await expect(firstToggle).toHaveAttribute("aria-checked", "false");
        await expect(firstToggle).toBeFocused();
    }

    async function assertSecondToggleIsSwitchedOn(secondToggle: Locator) {
        await expect(secondToggle).toHaveAttribute("aria-checked", "true");
        await expect(secondToggle).toBeFocused();
    }

    async function assertSecondToggleIsSwitchedOff(secondToggle: Locator) {
        await expect(secondToggle).toHaveAttribute("aria-checked", "false");
        await expect(secondToggle).toBeFocused();
    }

    test('Switch on & off principal switch', async ({page}) => {
        const [firstToggle, secondToggle] = await createLocators(page)

        await firstToggle.focus()
        await asserFirstToggleIsSwitchedOff(firstToggle)

        await firstToggle.click();
        await asserFirstToggleIsSwitchedOn(firstToggle)

        await firstToggle.click();
        await asserFirstToggleIsSwitchedOff(firstToggle)
    });

    test('Switch on & off secondary switch', async ({page}) => {
        const [firstToggle, secondToggle] = await createLocators(page)

        await secondToggle.focus()
        await assertSecondToggleIsSwitchedOn(secondToggle)

        await secondToggle.click();
        await assertSecondToggleIsSwitchedOff(secondToggle)

        await secondToggle.click();
        await assertSecondToggleIsSwitchedOn(secondToggle)
    });

});

test.describe("To check the focus", () => {

    async function createLocators(page: Page): Promise<[Locator, Locator, Locator]> {
        const firstToggle = await page.locator('button[role="switch"]:has-text("Use setting")').first();
        const secondToggle = await page.locator('text=Available to hireNulla amet tempus sit accumsan.Use setting >> button[role="switch"]');
        const div = await page.locator('text=Use settingAvailable to hireNulla amet tempus sit accumsan.Use setting');
        return [firstToggle, secondToggle, div]
    }

    test("for first toggle", async ({page}) => {
        const [firstToggle, secondToggle, div] = await createLocators(page)

        await firstToggle.focus()
        await firstToggle.click();
        await div.click();
        await expect(firstToggle).toBeFocused();
    });

    test("for second toggle", async ({page}) => {
        const [firstToggle, secondToggle, div] = await createLocators(page)

        await secondToggle.focus();
        await expect(secondToggle).toBeFocused();
        await secondToggle.click();
        await expect(secondToggle).toBeFocused();
        await div.click();
        await expect(secondToggle).toBeFocused();
    });

});