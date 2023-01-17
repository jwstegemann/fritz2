import {expect, test} from '@playwright/test';
/*
 * Missing tests due to limited example component:
 * - label
 * - validation
 * - deactivation
 * - textareaValidationMessages
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    /* go to the page of Text Area component */
    await page.goto("#textarea");

});

test.describe('To check the', () => {
        
        test('fill of textarea when Tab is pressed', async ({page}) => {
            /* locator of textArea (tag: textArea) */
            const field = page.locator('textarea');
            const text = "Some new text to write";
            
            await field.focus();
            await expect(field).toBeFocused();
            
            await field.fill(text);
            await expect(field).toBeFocused();
            
            await field.press('Tab');
            const fieldfill = page.locator('#result');
            expect(fieldfill).toContainText(text);
        
        });
         
        test('fill of textarea when Enter and then Tab is pressed', async ({page}) => {

            const field = page.locator('textarea');
            const text = "Some new text to write";
            const tNext = "A text for this area";
            
            await field.focus();
            await expect(field).toBeFocused();

            await field.fill(text);
            await expect(field).toBeFocused();
            
            await field.press('Enter');
            await field.type(tNext);
            await expect(field).toBeFocused();
            
            await field.press('Tab');
            const fieldfill = page.locator('#result');
            expect(fieldfill).toContainText(text);
            expect(fieldfill).toContainText(tNext);
        });
        
        test(`fill of input when textareaLabel is clicked and mouse is clicked away from textarea`, async ({page}) => {
            
            const field = page.locator('textarea');
            /* locator of textareaLabel (tag: textareaLabel) */
            const label = page.locator('#textArea-label');
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