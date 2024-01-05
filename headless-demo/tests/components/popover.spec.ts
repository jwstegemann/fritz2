import {expect, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - open state
 * - transitions
 * - positioning
 * - arrow
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Pop Over component */
    await page.goto("#popover");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});

test.describe('To open the popover', () => {

    test('and close when clicking on it and also closing when pressing Escape', async ({page}) => {
        /* locator of popOverButton (tag: popOverButton) */
        const popOv = page.locator('#popOver-button');
        /* locator of popOverPanel (tag: popOverPanel) */
        const popOvPanel = page.locator('#popOver-items');

        await popOv.focus();
        await popOv.click();
        await expect(popOvPanel).toBeVisible();

        await popOv.click();
        await expect(popOvPanel).toBeHidden();

        await popOv.click();
        await expect(popOvPanel).toBeVisible();

        await popOv.press('Escape');
        await expect(popOvPanel).toBeHidden();

    });

    for (const key of ["Enter", "Space"]) {

        test(`only with ${key} and close it with Esc`, async ({page}) => {

            const popOv = page.locator('#popOver-button');
            const popOvPanel = page.locator('#popOver-items');

            await popOv.focus();
            await popOv.press(key);
            await expect(popOvPanel).toBeVisible();

            await popOv.press(key);
            await expect(popOvPanel).toBeHidden();

            await popOv.press(key);
            await expect(popOvPanel).toBeVisible();

            await popOv.press('Escape');
            await expect(popOvPanel).toBeHidden();

        });

    }

});

test.describe('Pressing on Tab', () => {

    for (let limit = 1; limit < 6; limit++) {

        test(`${limit} time(s) to check Focus Management on button`, async ({page}) => {

            const popOv = page.locator('#popOver-button');
            const popOvPanel = page.locator('#popOver-items');

            await popOv.press('Enter');
            for (let times = 0; times < limit; times++) {
                await page.keyboard.press('Tab');
                await expect(popOvPanel).toBeVisible();
            }
            await popOv.press('Escape');
            await expect(popOvPanel).not.toBeVisible();

        });

    }

});

test.describe(`Nested popover`, () => {

    test(`Opening the inner popover does not close outer popover`, async ({page}) => {
        const outerBtn = page.locator('#popOver-button');
        const outerItems = page.locator('#popOver-items');
        const innerBtn = page.locator('#innerPopOver-button');
        const innerItems = page.locator('#innerPopOver-items');

        await outerBtn.click();
        await innerBtn.click();

        await expect(outerItems).toBeVisible();
        await expect(innerItems).toBeVisible();
    });

    test(`Clicking inside the inner popover does not close the outer popover`, async ({page}) => {
        const outerBtn = page.locator('#popOver-button');
        const outerItems = page.locator('#popOver-items');
        const innerBtn = page.locator('#innerPopOver-button');
        const innerItems = page.locator('#innerPopOver-items');

        await outerBtn.click();
        await expect(outerItems).toBeVisible();

        await innerBtn.click();
        await expect(innerItems).toBeVisible();

        await innerItems.click();
        await expect(innerItems).toBeVisible();
        await expect(outerItems).toBeVisible();
    });

    test(`Clicking inside the outer popover closes the inner popover`, async ({page}) => {
        const outerBtn = page.locator('#popOver-button');
        const outerItems = page.locator('#popOver-items');
        const innerBtn = page.locator('#innerPopOver-button');
        const innerItems = page.locator('#innerPopOver-items');

        await outerBtn.click();
        await expect(outerItems).toBeVisible();

        await innerBtn.click();
        await expect(innerItems).toBeVisible();

        await outerItems.click();
        await expect(innerItems).not.toBeVisible();
    });

    test(`Closing the outer popover closes the inner popover too`, async ({page}) => {
        const outerBtn = page.locator('#popOver-button');
        const outerItems = page.locator('#popOver-items');
        const innerBtn = page.locator('#innerPopOver-button');
        const innerItems = page.locator('#innerPopOver-items');

        await outerBtn.click();
        await expect(outerItems).toBeVisible();

        await innerBtn.click();
        await expect(innerItems).toBeVisible();

        await outerBtn.click();
        await expect(outerItems).not.toBeVisible();
        await expect(innerItems).not.toBeVisible();
    });

});