import {expect, test} from '@playwright/test';

test.beforeEach(async ({page}) => {
    await page.goto("#focus");

});

test.describe('trap focus', () => {

    async function pressTab(times, page) {
        for (let i = 0; i < times; i++) {
            await page.keyboard.press("Tab");
        }
    }

    for (const {idPrefix, titlePrefix} of [
        {"idPrefix": "trapMountPoint", "titlePrefix": "inside mount-point"},
        {"idPrefix": "trapWhenever", "titlePrefix": "on conditional flow"}]) {
        for (const num of ["1", "2", "3"]) {
            test(`${titlePrefix} should set the initial focus to preselected item-${num} and return focus to activating element`,
                async ({page}) => {
                    const buttonOpen = await page.locator(`#${idPrefix}-open`);
                    const radioInitialFocus = await page.locator(`#${idPrefix}-radio-${num}`);

                    await radioInitialFocus.click();
                    await buttonOpen.click();
                    const buttonClose = await page.locator(`#${idPrefix}-close`);
                    const itemFocused = await page.locator(`#${idPrefix}-Tab-Item-${num}`);

                    await expect(buttonClose).toBeVisible();
                    await expect(itemFocused).toBeFocused();

                    // press 4 times Tab -> without trap focus would be outside the container
                    await pressTab(4, page);

                    // starting item should have the focus again
                    await expect(itemFocused).toBeFocused();

                    await buttonClose.click();

                    // open button should get focus back
                    await expect(buttonOpen).toBeFocused()
                });
        }
    }

    test('nested inside each other should return focus on closing to its activating elements accordingly',
        async ({page}) => {
            const outerActivator = await page.locator('#Open-Below');
            const innerActivator = await page.locator("#InnerToggleActivate");

            await outerActivator.click();
            await expect(innerActivator).toBeVisible();
            await expect(innerActivator).toBeFocused();

            // press 5 times Tab -> without trap focus would be outside the container!
            await pressTab(5, page);

            // check focus is still in trap area
            await expect(innerActivator).toBeFocused();

            await innerActivator.click();

            const firstItemButton = await page.locator("#Tab-Item-1");
            await expect(firstItemButton).toBeFocused();

            // press 7 times Tab -> without trap focus would be outside the container!
            await pressTab(7, page);

            const innerDeactivator = await page.locator("#InnerToggleDeactivate");
            await expect(innerDeactivator).toBeFocused();

            await innerDeactivator.click();

            // inner activator should get the focus back
            await expect(innerActivator).toBeFocused();

            const closeOuterTrap = await page.locator("#CloseOuterTrap");
            await page.keyboard.press("Shift+Tab");
            await closeOuterTrap.click();

            // outer activator should get the focus back
            await expect(outerActivator).toBeFocused();
        });

});