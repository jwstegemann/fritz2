import {expect, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - label and description
 * - validation
 * - deactivation
 * - inputValidationMessages
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Input Field component */
    await page.goto("#inputfield");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});

test.describe('To check the', () => {
    
    for (const key of ["Enter", "Tab"]) {
        
        test(`fill of input when ${key} is pressed`, async ({page}) => {
            /* locator of inputField (tag: inputField) */
            const field = page.locator('#inputField-field');
            /* variable for some text to fill into inputField */
            const text = "Some new text to write";

            await field.focus();
            await expect(field).toBeFocused();

            await field.fill(text);
            await expect(field).toBeFocused();

            await page.press("#inputField-field", key);
            const fieldfill = page.locator('#result');
            await expect(fieldfill).toContainText(text);
        });
    
    }
  
    test(`fill of input when inputLabel is clicked and mouse is clicked away from inputField`, async ({page}) => {
        
        const field = page.locator('#inputField-field');
        /* locator of inputLabel (tag: inputLabel) */
        const label = page.locator('#inputField-label');
        const text = "Some new text to write";

        await label.click();
        await expect(field).toBeFocused();

        await field.fill(text);
        await expect(field).toBeFocused();

        await page.mouse.click(0, 0);
        const fieldfill = page.locator('#result');
        expect(fieldfill).toContainText(text);
    
    });

});