import {expect, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - label and description
 * - transition
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Modal component */
    await page.goto("#modal");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
    await page.locator('text=Open').click();
});

test.describe('To check if', () => {
    
    test(`by clicking on button will leave the modal or not`, async ({page}) => {
        /* locator for each modal button */
        const buttonStay = page.locator(`#button-stay`);
        const buttonCancel = page.locator(`#button-cancel`);
        const buttonClose = page.locator(`#button-close`);
        
        await buttonStay.focus();
        await buttonStay.click();
        await expect(buttonStay).toBeVisible();
        
        await buttonCancel.focus();
        await buttonCancel.click();
        await expect(buttonCancel).toBeHidden();
        
        await page.locator('text=Open').click();
        await buttonClose.focus();
        await buttonClose.click();
        await expect(buttonClose).toBeHidden();
    
    });
    
    test(`by clicking on button and clicking again on Open will reopen the modal`, async ({page}) => {
        const buttonStay = page.locator(`#button-stay`);
        const buttonCancel = page.locator(`#button-cancel`);
        const buttonClose = page.locator(`#button-close`);
        
        await buttonStay.focus();
        await buttonStay.click();
        await expect(buttonCancel).toBeVisible();
        
        await buttonStay.press('Tab');
        await buttonCancel.click();
        await page.locator('text=Open').click();
        await expect(buttonCancel).toBeVisible();
        
        await buttonClose.focus();
        await buttonClose.click();
        await page.locator('text=Open').click();
        await expect(buttonClose).toBeVisible();
    
    });
    
    test(`by pressing Enter on button will leave the modal or not`, async ({page}) => {

        const buttonStay = page.locator(`#button-stay`);
        const buttonCancel = page.locator(`#button-cancel`);
        const buttonClose = page.locator(`#button-close`);
        
        await buttonStay.focus();
        await buttonStay.press('Enter');
        await expect(buttonStay).toBeVisible();
        
        await buttonCancel.focus();
        await buttonCancel.press('Enter');
        await expect(buttonCancel).toBeHidden();
        
        await page.locator('text=Open').click();
        await buttonClose.focus();
        await buttonClose.press('Enter');
        await expect(buttonClose).toBeHidden();
    
    });
    
    test(`by pressing Enter on button and pressing again Enter on Open will reopen the modal`, async ({page}) => {
        const buttonStay = page.locator(`#button-stay`);
        const buttonCancel = page.locator(`#button-cancel`);
        const buttonClose = page.locator(`#button-close`);
        
        await buttonStay.focus();
        await buttonStay.press('Enter');
        await expect(buttonStay).toBeVisible();
        
        await buttonStay.press('Tab');
        await buttonCancel.press('Enter');
        await page.locator('text=Open').press('Enter');
        await page.waitForTimeout(500); // Not sure why this is needed, but locator will fail else!
        await expect(buttonCancel).toBeVisible();
        
        await buttonClose.focus();
        await buttonClose.press('Enter');
        await page.locator('text=Back').press('Enter');
        await expect(buttonClose).toBeVisible();
    
    });

});

test.describe("Navigating", () => {
    
    test("through the options should work by modifier keys", async ({page}) => {
        
        async function pressTab(btnItem: string) {
            await page.locator("#button-" + btnItem).press('Tab');
    
        }

        async function pressShiftTab(btnItem: string) {
            await page.locator("#button-" + btnItem).press('Shift+Tab');
        
        }
        
        async function assertButtonIsFocused(btnItem: string) {
            await expect(page.locator("#button-" + btnItem)).toBeFocused();
        
        }
       
        await assertButtonIsFocused("stay");

        /* Perform many "Tab"s */
        await pressTab("stay");
        await assertButtonIsFocused("cancel");

        await pressTab("cancel");
        await assertButtonIsFocused("close");

        await pressTab("close");
        await assertButtonIsFocused("stay");

        /* Perform many "Shift+Tab"s */
        await pressShiftTab("stay");
        await assertButtonIsFocused("close");

        await pressShiftTab("close");
        await assertButtonIsFocused("cancel");

        await pressShiftTab("cancel");
        await assertButtonIsFocused("stay");
    
    });
   
    test("through mixed actions of modifier keys", async ({page}) => {
        async function pressTab(btnItem: string) {
            await page.locator("#button-" + btnItem).press('Tab');
        
        }
        
        async function pressShiftTab(btnItem: string) {
            await page.locator("#button-" + btnItem).press('Shift+Tab');
        
        }
        
        async function assertButtonIsFocused(btnItem: string) {
            await expect(page.locator("#button-" + btnItem)).toBeFocused();
        
        }
        
        await assertButtonIsFocused("stay");

        /* Perform 2 "Shift"s, 1 "Shift+Tab" and 2 "Shift"s */
        await pressTab("stay");
        await assertButtonIsFocused("cancel");

        await pressTab("cancel");
        await assertButtonIsFocused("close");

        await pressShiftTab("close");
        await assertButtonIsFocused("cancel");

        await pressTab("cancel");
        await assertButtonIsFocused("close");

        await pressTab("close");
        await assertButtonIsFocused("stay");

        /* Perform 2 "Shift+Tab"s and 2 "Shift"s */
        await pressShiftTab("stay");
        await assertButtonIsFocused("close");

        await pressShiftTab("close");
        await assertButtonIsFocused("cancel");

        await pressTab("cancel");
        await assertButtonIsFocused("close");

        await pressTab("close");
        await assertButtonIsFocused("stay");
    
    });

});