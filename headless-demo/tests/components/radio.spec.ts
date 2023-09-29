import {expect, Page, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - styling
 * - label and description
 * - validation
 * - radioGroupValidationMessages
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Radio Group component */
    await page.goto("#radioGroup");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});

test.describe('Checking', () => {

    const data = [
        {type: "Hobby", expected: ["Startup", "Business", "Enterprise"], id: "0"},
        {type: "Startup", expected: ["Hobby", "Business", "Enterprise"], id: "1"},
        {type: "Business", expected: ["Hobby", "Startup", "Enterprise"], id: "2"},
        {type: "Enterprise", expected: ["Hobby", "Startup", "Business"], id: "3"}
    ]

    async function checkResult(page: Page) {
        return page.locator('#result');
    
    }
    
    async function radioActive(radNum: string, page: Page) {
        const radio = page.locator(`#radioGroup-${data[radNum].type}-toggle`);
        const label = page.locator(`#radioGroup-${data[radNum].type}-label`).textContent();
        const radioOther1 = page.locator(`#radioGroup-${data[radNum].expected[0]}-toggle`);
        const radioOther2 = page.locator(`#radioGroup-${data[radNum].expected[1]}-toggle`);
        const radioOther3 = page.locator(`#radioGroup-${data[radNum].expected[2]}-toggle`);
        await expect(radio).toHaveAttribute("aria-checked", "true");
        await expect(radioOther1).toHaveAttribute("aria-checked", "false");
        await expect(radioOther2).toHaveAttribute("aria-checked", "false");
        await expect(radioOther3).toHaveAttribute("aria-checked", "false");
        await expect(radio).toBeFocused();
        await checkResult(page);
        expect(await checkResult(page)).toContainText(await label);
    
    }

    for (const item of ["Hobby", "Startup", "Business", "Enterprise"]) {

        test(`unique result for radio ${item} through click`, async ({page}) =>{

            async function radioActive(radItem: String) {
                const radio = page.locator("#radioGroup-"+ radItem + "-toggle");
                await expect(page.locator("#radioGroup-"+ radItem + "-toggle")).toHaveAttribute("aria-checked", "true");
                await expect(radio).toBeFocused();
            
            }

            const radToggle = page.locator(`#radioGroup-${item}-toggle`)
            const label = await page.locator(`#radioGroup-${item}-label`).textContent();
            
            await radToggle.click();
            await radioActive(item);
            
            const result = page.locator('#result');
            expect(result).toContainText(label);
        });
    }

    test('unique result and focus on selected radio through click', async ({page}) =>{

        const radioHobby = page.locator("#radioGroup-Hobby-toggle");
        const radioStrUp = page.locator("#radioGroup-Startup-toggle");
        const radioBusin = page.locator("#radioGroup-Business-toggle");
        const radioEnt = page.locator("#radioGroup-Enterprise-toggle");
        
        /* select each radio button step by step through click */
        await radioHobby.click();
        await radioActive("0", page);
        
        await radioStrUp.click();
        await radioActive("1", page);
        
        await radioBusin.click();
        await radioActive("2", page);
        
        await radioEnt.click();
        await radioActive("3", page);

    });

    test('unique result when pressing Up and Down', async ({page}) =>{

        const radioHobby = page.locator("#radioGroup-Hobby-toggle");
        const radioStrUp = page.locator("#radioGroup-Startup-toggle");
        const radioBusin = page.locator("#radioGroup-Business-toggle");
        const radioEnt = page.locator("#radioGroup-Enterprise-toggle");
        
        /* select each radio button step by step through Up and Down */
        await radioHobby.click();
        await radioActive("0", page);
        
        await radioHobby.press('ArrowDown'); 
        await radioActive("1", page);
        
        await radioStrUp.press('ArrowDown'); 
        await radioActive("2", page);
        
        await radioBusin.press('ArrowDown'); 
        await radioActive("3", page);
        
        await radioEnt.press('ArrowUp'); 
        await radioActive("2", page);
        
        await radioBusin.press('ArrowUp'); 
        await radioActive("1", page);
        
        await radioStrUp.press('ArrowUp'); 
        await radioActive("0", page);

    });

});