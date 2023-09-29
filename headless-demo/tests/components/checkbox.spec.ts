import {expect, Page, test} from '@playwright/test';
/*
 * Missing tests due to limited example component:
 * - styling
 * - label and description
 * - validation
 * - checkboxGroupValidationMessages
 */

test.beforeEach(async ({page}) => {
    /* go to the page of CheckBoxGroup component */
    await page.goto("#checkboxGroup");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});

test.describe('Checking', () => {

    for (const num of ["1", "2", "3"]) {

        test(`unique result for checkbox ${num} and verify in result when selected through click`, async ({page}) =>{
            /* locator for each checkbox label (tag: checkboxGroupLabel) */
            const label = await page.locator(`#checkboxGroup-${num}-label`).textContent(); 
            /* locator for each checkbox toggle (tag: checkboxGroupOptionToggle) */
            const chToggle = page.locator(`#checkboxGroup-${num}-toggle`);
             
            await chToggle.click();
            await expect(chToggle).toHaveAttribute("aria-checked", "true");
            await expect(chToggle).toBeFocused();

            const result = page.locator('#result');
            await expect(result).toContainText(label);

            await chToggle.click();
            await expect(chToggle).toHaveAttribute("aria-checked", "false");
            await expect(chToggle).toBeFocused();
            await expect(result).not.toContainText(label);

        });
    }

    for (const num of ["1", "2", "3"]) {

        test(`unique result for checkbox ${num} when pressing "Space"`, async ({page}) =>{
            const label = await page.locator(`#checkboxGroup-${num}-label`).textContent(); 
            const chToggle = page.locator(`#checkboxGroup-${num}-toggle`);
            
            await chToggle.press('Space');
            await expect(chToggle).toHaveAttribute("aria-checked", "true");
            await expect(chToggle).toBeFocused();

            const result = page.locator('#result');
            await expect(result).toContainText(label);

            await chToggle.press('Space');
            await expect(chToggle).toHaveAttribute("aria-checked", "false");
            await expect(chToggle).toBeFocused();
            await expect(result).not.toContainText(label);

        });

    }
    
});

test.describe('To check', () => {    

    const data1 = [
        {num: "1", expected: ["2","3"]},
        {num: "2", expected: ["1","3"]},
        {num: "3", expected: ["1","2"]}
    ]

    const data2 = [
        {num: ["1","2"], expected: "3"},
        {num: ["1","3"], expected: "2"},
        {num: ["2","3"], expected: "1"}
    ]

    async function checkResult(page: Page) {
        return page.locator('#result');
    }

    /**
     * Verify if only 1 checkbox is selected and check if its text is the only one in result
     * @param chNum
     * @param page
     */
    async function chBoxActiveSingle(chNum: string, page: Page) {
        await expect(page.locator(`#checkboxGroup-${data1[chNum].num}-toggle`)).toHaveAttribute("aria-checked", "true");
        await expect(page.locator(`#checkboxGroup-${data1[chNum].expected[0]}-toggle`)).toHaveAttribute("aria-checked", "false");
        await expect(page.locator(`#checkboxGroup-${data1[chNum].expected[1]}-toggle`)).toHaveAttribute("aria-checked", "false");
        await checkResult(page);
        await expect(await checkResult(page)).toContainText(await page.locator(`#checkboxGroup-${data1[chNum].num}-label`).textContent());
        await expect(await checkResult(page)).not.toContainText(await page.locator(`#checkboxGroup-${data1[chNum].expected[0]}-label`).textContent());
        await expect(await checkResult(page)).not.toContainText(await page.locator(`#checkboxGroup-${data1[chNum].expected[1]}-label`).textContent());
    }

    /**
     * Verify if only 2 checkboxes are selected and check if text of checkboex are displayed in result
     * @param chNum
     * @param page
     */
    async function chBoxActiveMultiple(chNum: string, page: Page) {
        await expect(page.locator(`#checkboxGroup-${data2[chNum].num[0]}-toggle`)).toHaveAttribute("aria-checked", "true");
        await expect(page.locator(`#checkboxGroup-${data2[chNum].num[1]}-toggle`)).toHaveAttribute("aria-checked", "true");
        await expect(page.locator(`#checkboxGroup-${data2[chNum].expected}-toggle`)).toHaveAttribute("aria-checked", "false");
        await checkResult(page);
        await expect(await checkResult(page)).toContainText(await page.locator(`#checkboxGroup-${data2[chNum].num[0]}-label`).textContent());
        await expect(await checkResult(page)).toContainText(await page.locator(`#checkboxGroup-${data2[chNum].num[1]}-label`).textContent());
        await expect(await checkResult(page)).not.toContainText(await page.locator(`#checkboxGroup-${data2[chNum].expected}-label`).textContent());
    }

    /**
     * Verify if all checkboxes are checked with texts in result
     * @param page
     */
    async function chBoxActiveAll(page: Page) {
        await expect(page.locator(`#checkboxGroup-1-toggle`)).toHaveAttribute("aria-checked", "true");
        await expect(page.locator(`#checkboxGroup-2-toggle`)).toHaveAttribute("aria-checked", "true");
        await expect(page.locator(`#checkboxGroup-3-toggle`)).toHaveAttribute("aria-checked", "true");
        await checkResult(page);
        await expect(await checkResult(page)).toContainText(await page.locator(`#checkboxGroup-1-label`).textContent());
        await expect(await checkResult(page)).toContainText(await page.locator(`#checkboxGroup-2-label`).textContent());
        await expect(await checkResult(page)).toContainText(await page.locator(`#checkboxGroup-3-label`).textContent());
    }

    /**
     * Verify if all checkboxes are unchecked with no texts in result
     * @param page
     */
    async function chBoxNotActiveAll(page: Page) {
        await expect(page.locator(`#checkboxGroup-1-toggle`)).toHaveAttribute("aria-checked", "false");
        await expect(page.locator(`#checkboxGroup-2-toggle`)).toHaveAttribute("aria-checked", "false");
        await expect(page.locator(`#checkboxGroup-3-toggle`)).toHaveAttribute("aria-checked", "false");
        await checkResult(page);
        await expect(await checkResult(page)).not.toContainText(await page.locator(`#checkboxGroup-1-label`).textContent());
        await expect(await checkResult(page)).not.toContainText(await page.locator(`#checkboxGroup-2-label`).textContent());
        await expect(await checkResult(page)).not.toContainText(await page.locator(`#checkboxGroup-3-label`).textContent());
    }

    test(`multiple selection of chechboxes and verify in result when selected`, async ({page}) =>{

        const chToggle1 = page.locator(`#checkboxGroup-1-toggle`);
        const chToggle2 = page.locator(`#checkboxGroup-2-toggle`);
        const chToggle3 = page.locator(`#checkboxGroup-3-toggle`);

        /*check and uncheck every checkbox step by step and verify attributes */
        await chBoxNotActiveAll(page);

        await chToggle1.click();
        await chBoxActiveSingle("0", page);
        
        await chToggle2.click();
        await chBoxActiveMultiple("0", page);
        
        await chToggle3.click();
        await chBoxActiveAll(page);

        await chToggle3.click();
        await chBoxActiveMultiple("0", page);
        
        await chToggle1.click();
        await chBoxActiveSingle("1", page);
        
        await chToggle2.click();
        await chBoxNotActiveAll(page);

    });

    test(`several results with "Tab", "Shift+Tab" and "Space"`, async ({page}) =>{

        const chToggle1 = page.locator(`#checkboxGroup-1-toggle`);
        const chToggle2 = page.locator(`#checkboxGroup-2-toggle`);
        const chToggle3 = page.locator(`#checkboxGroup-3-toggle`);

        
        /* check and uncheck every checkbox step by step and verify attributes */
        await chBoxNotActiveAll(page);

        await chToggle1.focus();
        await chToggle1.press('Space');
        await chBoxActiveSingle("0", page);

        await chToggle1.press('Tab');
        await chToggle2.press('Space');
        await chBoxActiveMultiple("0", page);
        
        await chToggle2.press('Tab');
        await chToggle3.press('Space');
        await chBoxActiveAll(page);

        await chToggle3.press('Space');
        await chBoxActiveMultiple("0", page);

        await chToggle3.press('Shift+Tab');
        await chToggle2.press('Space');
        await chBoxActiveSingle("0", page);

        await chToggle2.press('Shift+Tab');
        await chToggle1.press('Space');
        await chBoxNotActiveAll(page);
    
    });

});