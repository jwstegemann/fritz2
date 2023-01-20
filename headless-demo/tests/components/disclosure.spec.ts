import {expect, Page, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - state depending styling
 * - open and close button in disclosure panel
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Disclosure component */
    await page.goto("#disclosure");

});
test.describe('Checking', () => {

    for (const data of [
        {num: "0", expected: ["1","2","3"]},
        {num: "1", expected: ["0","2","3"]},
        {num: "2", expected: ["0","1","3"]},
        {num: "3", expected: ["0","1","2"]}
        ]) {

        test(`expansion of disclosure button ${data.num} when clicking`, async ({page}) =>{

            /**
             * Verify if the actual disclosure button is expanded and the other ones not
             * @param num
             */
            async function dsBtnExpanded(num: string) {
                /* locator for actual disclosure button (tag: disclosureButton) */
                const dsBtn = page.locator(`#disclosure-${num}-button`);
                /* locator for other disclosure buttons */
                const dsBtn1 = page.locator(`#disclosure-${data.expected[0]}-button`);
                const dsBtn2 = page.locator(`#disclosure-${data.expected[1]}-button`);
                const dsBtn3 = page.locator(`#disclosure-${data.expected[2]}-button`);
                /* locator for actual disclosure button (tag: disclosureButton) */
                const dsPanel = page.locator(`#disclosure-${num}-panel`);
                /* locator for other disclosure buttons */
                const dsPanel1 = page.locator(`#disclosure-${data.expected[0]}-panel`);
                const dsPanel2 = page.locator(`#disclosure-${data.expected[1]}-panel`);
                const dsPanel3 = page.locator(`#disclosure-${data.expected[2]}-panel`);
                /* verify attributes */
                await expect(dsBtn).toHaveAttribute("aria-expanded", "true");
                await expect(dsBtn1).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn2).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn3).toHaveAttribute("aria-expanded", "false");
                await expect(dsPanel).toBeVisible();
                await expect(dsPanel1).toBeHidden();
                await expect(dsPanel2).toBeHidden();
                await expect(dsPanel3).toBeHidden();
            }

            /**
             * Verify if the actual disclosure button is not expanded and also the other ones
             * @param num
             */
            async function dsBtnNotExpanded(num: string) {
                const dsBtn = page.locator(`#disclosure-${num}-button`);
                const dsBtn1 = page.locator(`#disclosure-${data.expected[0]}-button`);
                const dsBtn2 = page.locator(`#disclosure-${data.expected[1]}-button`);
                const dsBtn3 = page.locator(`#disclosure-${data.expected[2]}-button`);

                const dsPanel = page.locator(`#disclosure-${num}-panel`);
                const dsPanel1 = page.locator(`#disclosure-${data.expected[0]}-panel`);
                const dsPanel2 = page.locator(`#disclosure-${data.expected[1]}-panel`);
                const dsPanel3 = page.locator(`#disclosure-${data.expected[2]}-panel`);

                await expect(dsBtn).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn1).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn2).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn3).toHaveAttribute("aria-expanded", "false");
                await expect(dsPanel).toBeHidden();
                await expect(dsPanel1).toBeHidden();
                await expect(dsPanel2).toBeHidden();
                await expect(dsPanel3).toBeHidden();
            } 

            const dsBtn = page.locator(`#disclosure-${data.num}-button`);
            
            await dsBtn.click()
            await dsBtnExpanded(data.num);

            await dsBtn.click()
            await dsBtnNotExpanded(data.num);

        });

    }

    for (const data of [
        {num: "0", expected: ["1","2","3"]},
        {num: "1", expected: ["0","2","3"]},
        {num: "2", expected: ["0","1","3"]},
        {num: "3", expected: ["0","1","2"]}
        ]) {

        test(`expansion of disclosure button ${data.num} by pressing "Space"`, async ({page}) =>{

            async function dsBtnExpanded(num: string) {
                const dsBtn = page.locator(`#disclosure-${num}-button`);
                const dsBtn1 = page.locator(`#disclosure-${data.expected[0]}-button`);
                const dsBtn2 = page.locator(`#disclosure-${data.expected[1]}-button`);
                const dsBtn3 = page.locator(`#disclosure-${data.expected[2]}-button`);

                const dsPanel = page.locator(`#disclosure-${num}-panel`);
                const dsPanel1 = page.locator(`#disclosure-${data.expected[0]}-panel`);
                const dsPanel2 = page.locator(`#disclosure-${data.expected[1]}-panel`);
                const dsPanel3 = page.locator(`#disclosure-${data.expected[2]}-panel`);

                await expect(dsBtn).toHaveAttribute("aria-expanded", "true");
                await expect(dsBtn1).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn2).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn3).toHaveAttribute("aria-expanded", "false");
                await expect(dsPanel).toBeVisible();
                await expect(dsPanel1).toBeHidden();
                await expect(dsPanel2).toBeHidden();
                await expect(dsPanel3).toBeHidden();
            }

            async function dsBtnNotExpanded(num: string) {
                const dsBtn = page.locator(`#disclosure-${num}-button`);
                const dsBtn1 = page.locator(`#disclosure-${data.expected[0]}-button`);
                const dsBtn2 = page.locator(`#disclosure-${data.expected[1]}-button`);
                const dsBtn3 = page.locator(`#disclosure-${data.expected[2]}-button`);

                const dsPanel = page.locator(`#disclosure-${num}-panel`);
                const dsPanel1 = page.locator(`#disclosure-${data.expected[0]}-panel`);
                const dsPanel2 = page.locator(`#disclosure-${data.expected[1]}-panel`);
                const dsPanel3 = page.locator(`#disclosure-${data.expected[2]}-panel`);
                
                await expect(dsBtn).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn1).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn2).toHaveAttribute("aria-expanded", "false");
                await expect(dsBtn3).toHaveAttribute("aria-expanded", "false");
                await expect(dsPanel).toBeHidden();
                await expect(dsPanel1).toBeHidden();
                await expect(dsPanel2).toBeHidden();
                await expect(dsPanel3).toBeHidden();
            } 

            const dsBtn = page.locator(`#disclosure-${data.num}-button`);
            await dsBtn.press("Space");
            await dsBtnExpanded(data.num)

            await dsBtn.press("Space");
            await dsBtnNotExpanded(data.num);

        });

    }

});
test.describe('Checking with keys', () => {

    const data1 = [
        {num: "0", expected: ["1","2","3"]},
        {num: "1", expected: ["0","2","3"]},
        {num: "2", expected: ["0","1","3"]},
        {num: "3", expected: ["0","1","2"]}
    ]

    const data2 = [
        {numGroup: ["0","1"], expected: ["2","3"]},
        {numGroup: ["2","3"], expected: ["0","1"]}
    ]

    /**
     * Verify if only 1 disclosure button is expanded and the others not
     * @param numOne
     * @param page
     */
    async function dsBtnExpandedOnlyOne(numOne: string, page: Page) {
        const dsBtn = page.locator(`#disclosure-${numOne}-button`);
        const dsBtnOther1 = page.locator(`#disclosure-${data1[numOne].expected[0]}-button`);
        const dsBtnOther2 = page.locator(`#disclosure-${data1[numOne].expected[1]}-button`);
        const dsBtnOther3 = page.locator(`#disclosure-${data1[numOne].expected[2]}-button`);

        const dsPanel = page.locator(`#disclosure-${numOne}-panel`);
        const dsPanelOther1 = page.locator(`#disclosure-${data1[numOne].expected[0]}-panel`);
        const dsPanelOther2 = page.locator(`#disclosure-${data1[numOne].expected[1]}-panel`);
        const dsPanelOther3 = page.locator(`#disclosure-${data1[numOne].expected[2]}-panel`);
        
        await expect(dsBtn).toHaveAttribute("aria-expanded", "true");
        await expect(dsBtnOther1).toHaveAttribute("aria-expanded", "false");
        await expect(dsBtnOther2).toHaveAttribute("aria-expanded", "false");
        await expect(dsBtnOther3).toHaveAttribute("aria-expanded", "false");
        await expect(dsPanel).toBeVisible();
        await expect(dsPanelOther1).toBeHidden();
        await expect(dsPanelOther2).toBeHidden();
        await expect(dsPanelOther3).toBeHidden();
    }

    /**
     * Verify if only 1 disclosure button is not expanded and also the others
     * @param numOne
     * @param page
     */
    async function dsBtnNotExpandedOnlyOne(numOne: string, page: Page) {
        const dsBtn = page.locator(`#disclosure-${numOne}-button`);
        const dsBtnOther1 = page.locator(`#disclosure-${data1[numOne].expected[0]}-button`);
        const dsBtnOther2 = page.locator(`#disclosure-${data1[numOne].expected[1]}-button`);
        const dsBtnOther3 = page.locator(`#disclosure-${data1[numOne].expected[2]}-button`);
        
        const dsPanel = page.locator(`#disclosure-${numOne}-panel`);
        const dsPanelOther1 = page.locator(`#disclosure-${data1[numOne].expected[0]}-panel`);
        const dsPanelOther2 = page.locator(`#disclosure-${data1[numOne].expected[1]}-panel`);
        const dsPanelOther3 = page.locator(`#disclosure-${data1[numOne].expected[2]}-panel`);
        
        await expect(dsBtn).toHaveAttribute("aria-expanded", "false");
        await expect(dsBtnOther1).toHaveAttribute("aria-expanded", "true");
        await expect(dsBtnOther2).toHaveAttribute("aria-expanded", "true");
        await expect(dsBtnOther3).toHaveAttribute("aria-expanded", "true");
        await expect(dsPanel).toBeHidden();
        await expect(dsPanelOther1).toBeVisible();
        await expect(dsPanelOther2).toBeVisible();
        await expect(dsPanelOther3).toBeVisible();
    }

    /**
     * Verify if 2 disclosure buttons are expanded and the 2 others not
     * @param num
     * @param page
     */
    async function dsBtnExpandedOnlyTwo(num: string, page: Page) {
        const dsBtn1 = page.locator(`#disclosure-${data2[num].numGroup[0]}-button`);
        const dsBtn2 = page.locator(`#disclosure-${data2[num].numGroup[1]}-button`);
        const dsBtnOther1 = page.locator(`#disclosure-${data2[num].expected[0]}-button`);
        const dsBtnOther2 = page.locator(`#disclosure-${data2[num].expected[1]}-button`);
        
        const dsPanel1 = page.locator(`#disclosure-${data2[num].numGroup[0]}-panel`);
        const dsPanel2 = page.locator(`#disclosure-${data2[num].numGroup[1]}-panel`);
        const dsPanelOther1 = page.locator(`#disclosure-${data2[num].expected[0]}-panel`);
        const dsPanelOther2 = page.locator(`#disclosure-${data2[num].expected[1]}-panel`);
        
        await expect(dsBtn1).toHaveAttribute("aria-expanded", "true");
        await expect(dsBtn2).toHaveAttribute("aria-expanded", "true");
        await expect(dsBtnOther1).toHaveAttribute("aria-expanded", "false");
        await expect(dsBtnOther2).toHaveAttribute("aria-expanded", "false");
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanelOther1).toBeHidden();
        await expect(dsPanelOther2).toBeHidden();
    } 

    /**
     * Verify if all disclosure buttons are expanded
     * @param page
     */
    async function dsBtnAllExpanded(page: Page) {
        await expect(page.locator(`#disclosure-0-button`)).toHaveAttribute("aria-expanded", "true");
        await expect(page.locator(`#disclosure-1-button`)).toHaveAttribute("aria-expanded", "true");
        await expect(page.locator(`#disclosure-2-button`)).toHaveAttribute("aria-expanded", "true");
        await expect(page.locator(`#disclosure-3-button`)).toHaveAttribute("aria-expanded", "true");
        await expect(page.locator(`#disclosure-0-panel`)).toBeVisible();
        await expect(page.locator(`#disclosure-1-panel`)).toBeVisible();
        await expect(page.locator(`#disclosure-2-panel`)).toBeVisible();
        await expect(page.locator(`#disclosure-3-panel`)).toBeVisible();
    }

    /**
     * Verify if all disclosure buttons are not expanded
     * @param page
     */
    async function dsBtnAllNotExpanded(page: Page) {
        await expect(page.locator(`#disclosure-0-button`)).toHaveAttribute("aria-expanded", "false");
        await expect(page.locator(`#disclosure-1-button`)).toHaveAttribute("aria-expanded", "false");
        await expect(page.locator(`#disclosure-2-button`)).toHaveAttribute("aria-expanded", "false");
        await expect(page.locator(`#disclosure-3-button`)).toHaveAttribute("aria-expanded", "false");
        await expect(page.locator(`#disclosure-0-panel`)).toBeHidden();
        await expect(page.locator(`#disclosure-1-panel`)).toBeHidden();
        await expect(page.locator(`#disclosure-2-panel`)).toBeHidden();
        await expect(page.locator(`#disclosure-3-panel`)).toBeHidden();
    }

    test('multiple expansion for all disclosure buttons', async ({page}) =>{
        const dsBtn1 = page.locator(`#disclosure-0-button`);
        const dsBtn2 = page.locator(`#disclosure-1-button`);
        const dsBtn3 = page.locator(`#disclosure-2-button`);
        const dsBtn4 = page.locator(`#disclosure-3-button`);
        
        /* verify first if all disclosure panels are not expanded */
        await dsBtnAllNotExpanded(page);

        /* open each disclosure button step by step */
        await dsBtn1.click();
        await dsBtnExpandedOnlyOne("0", page);

        await dsBtn2.click();
        await dsBtnExpandedOnlyTwo("0", page);

        await dsBtn3.click();
        await dsBtnNotExpandedOnlyOne("3", page);

        await dsBtn4.click();
        await dsBtnAllExpanded(page);

        /* close each disclosure button step by step */
        await dsBtn4.click();
        await dsBtnNotExpandedOnlyOne("3", page);

        await dsBtn3.click();
        await dsBtnExpandedOnlyTwo("0", page);

        await dsBtn2.click();
        await dsBtnExpandedOnlyOne("0", page);

        await dsBtn1.click();
        await dsBtnAllNotExpanded(page);

    });

    test('expansion of several disclosure buttons by pressing "Tab" and "Space"', async ({page}) =>{

        const dsBtn1 = page.locator(`#disclosure-0-button`);
        const dsBtn2 = page.locator(`#disclosure-1-button`);
        const dsBtn3 = page.locator(`#disclosure-2-button`);
        const dsBtn4 = page.locator(`#disclosure-3-button`);

        /* verify first if all disclosure panels are not expanded */
        await dsBtnAllNotExpanded(page);

        /* open each disclosure button step by step */
        await dsBtn1.press("Space");
        await dsBtnExpandedOnlyOne("0", page);

        await dsBtn1.press("Tab");
        await dsBtn2.press("Space");
        await dsBtnExpandedOnlyTwo("0", page);

        await dsBtn2.press("Tab");
        await dsBtn3.press("Space");
        await dsBtnNotExpandedOnlyOne("3", page);

        await dsBtn3.press("Tab");
        await dsBtn4.press("Space");
        await dsBtnAllExpanded(page);

        /* close each disclosure button step by step */
        await dsBtn1.press("Space");
        await dsBtnNotExpandedOnlyOne("0", page);

        await dsBtn1.press("Tab");
        await dsBtn2.press("Space");
        await dsBtnExpandedOnlyTwo("1", page);

        await dsBtn2.press("Tab");
        await dsBtn3.press("Space");
        await dsBtnExpandedOnlyOne("3", page);

        await dsBtn3.press("Tab");
        await dsBtn4.press("Space");
        await dsBtnAllNotExpanded(page);

    });

});