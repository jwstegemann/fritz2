import {expect, Locator, Page, test} from '@playwright/test';
/*
 * Missing tests due to limited example component:
 * - label and description
 * - validation
 * - switchValidationMessages
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Switch component */
    await page.goto("#switch");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});

test.describe('To switch on and off', () => {
    
    test('Switch on & off switch', async ({page}) => {
        /* locator of switch (tag: switch) */
        const sw = page.locator('id=switch');
        
        await sw.focus();
        await expect(sw).toHaveAttribute("aria-checked", "false");

        await sw.click();
        await expect(sw).toHaveAttribute("aria-checked", "true");

        await sw.click();
        await expect(sw).toHaveAttribute("aria-checked", "false");
    
    });
    
    test('Switch on & off switchToggle', async ({page}) => {
        /* locator of switchToggle (tag: switchToggle) */
        const swToggle = page.locator('id=switchWithLabel-toggle');
        
        await swToggle.focus();
        await expect(swToggle).toHaveAttribute("aria-checked", "true");
        
        await swToggle.click();
        await expect(swToggle).toHaveAttribute("aria-checked", "false");
        
        await swToggle.click();
        await expect(swToggle).toHaveAttribute("aria-checked", "true");
    
    });
    
    test('Switch on & off switchLabel', async ({page}) => {

        const swToggle = page.locator('id=switchWithLabel-toggle');
        /* locator of switchLabel (tag: switchLabel) */
        const swLabel = page.locator('id=switchWithLabel-label');
        
        await swLabel.focus();
        await swLabel.click();
        await expect(swToggle).toHaveAttribute("aria-checked", "false");
        
        await swLabel.click();
        await expect(swToggle).toHaveAttribute("aria-checked", "true");
    
    });
    
    test("focus switch and switchToggle then press Space", async ({page}) => {

        const sw = page.locator('id=switch');
        const swToggle = page.locator('id=switchWithLabel-toggle');
        
        await sw.focus();
        await expect(sw).toHaveAttribute("aria-checked", "false");
        
        await page.keyboard.down('Space')
        await expect(sw).toHaveAttribute("aria-checked", "true");
        
        await swToggle.focus();
        await expect(swToggle).toHaveAttribute("aria-checked", "true");

        await page.keyboard.down('Space')
        await expect(swToggle).toHaveAttribute("aria-checked", "false");
    
    });
    
});

test.describe("To check the focus", () => {
    
    async function locatorsSw(page: Page): Promise<[Locator, Locator]> {
        const sw = page.locator('id=switch');
        const swLabel = page.locator('id=switchWithLabel');
        return [sw, swLabel];
    
    }
    
    async function locatorsSwLabel(page: Page): Promise<[Locator, Locator]> {
        const swToggle = page.locator('id=switchWithLabel-toggle');
        const swLabel = page.locator('id=switchWithLabel');
        return [swToggle, swLabel]
    
    }
    
    test("for switch", async ({page}) => {

        const [sw, swLabel] = await locatorsSw(page);
        await sw.focus();
        await expect(sw).toBeFocused();
        
        await sw.click();
        await swLabel.click();
        await expect(sw).not.toBeFocused();
    
    });

    test("for switchToggle", async ({page}) => {
        
        const [swToggle, swLabel] = await locatorsSwLabel(page);
        await swToggle.focus();
        await expect(swToggle).toBeFocused();
        
        await swToggle.click();
        await swLabel.click();
        await expect(swToggle).not.toBeFocused();
    
    });

});