import {expect, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - label and description
 * - validation
 * - deactivation
 * - inputValidationMessages
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of TabGroup component
    // await page.goto("https://next.fritz2.dev/headless-demo/#tabGroup");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#tabGroup");
//end of before hooks
});
//description of our first tests
test.describe('Checking', () => {
    for (const num of ["0", "1", "2"]) {
    test(`tab selection for tab ${num} through click`, async ({page}) =>{
        //function to check the tab
        async function tabActive(num: String) {
            const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
            //expect the attribute aria-checked to be true if tab is selected
            await expect(tab).toHaveAttribute("aria-selected", "true");
            await expect(tab).toBeFocused();
        //end of function
        }
        //locator for each tab (tag: tab)
        const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
        //locator for each tab panel (tag: tabPanel)
        const panel = page.locator(`#tabGroup-tab-panels-panel-${num}`)
        //click on radio
        await tab.click();
        //verify is radio is checked
        await tabActive(num);
        //verify if panel is visible
        await expect(panel).toBeVisible();
    });
}
test('unique result and focus on selected radio through click', async ({page}) =>{
    //function to check the content of result
    async function getTab(num: String) {
        //locator for each radio
        const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
        //return the locator
        return tab;
    //end of function
}
    //function to check the content of result
    async function getPanel(num: String) {
        //locator for each radio
        const panel = page.locator(`#tabGroup-tab-panels-panel-${num}`);
        //return the locator
        return panel;
    //end of function
}
    //function to check the radio
    async function tabActive(num: String) {
        await getTab(num);
        //expect the attribute aria-checked to be true if switched on
        await expect(await getTab(num)).toHaveAttribute("aria-selected", "true");
        await expect(await getTab(num)).toBeFocused();
    //end of function
    }
    //function to uncheck the radio
    async function tabNotActive(num: String) {
        await getTab(num);
        //expect the attribute aria-checked to be true if switched on
        await expect(await getTab(num)).toHaveAttribute("aria-selected", "false");
        await expect(await getTab(num)).not.toBeFocused();
    //end of function
    }
    //click on tab
    await (await getTab("0")).click();
    //verify if tab 0 is chosen
    await tabActive("0");
    //verify if other tabs and not chosen
    await tabNotActive("1");
    await tabNotActive("2");
    //verify if panel 0 is visible and panel 1 and panel 2 are hidden
    await expect(await getPanel("0")).toBeVisible();
    await expect(await getPanel("1")).toBeHidden();
    await expect(await getPanel("2")).toBeHidden();
    //click on tab
    await (await getTab("1")).click();
    //verify if tab 1 is chosen
    await tabActive("1");
    ////verify if other tabs and not chosen
    await tabNotActive("0");
    await tabNotActive("2");
    //verify if panel 1 is visible and panel 0 and panel 2 are hidden
    await expect(await getPanel("0")).toBeHidden();
    await expect(await getPanel("1")).toBeVisible();
    await expect(await getPanel("2")).toBeHidden();
    //click on tab
    await (await getTab("2")).click();
    //verify if tab 2 is chosen
    await tabActive("2");
    ////verify if other tabs and not chosen
    await tabNotActive("0");
    await tabNotActive("1");
    //verify if panel 2 is visible and panel 1 and panel 2 are hidden
    await expect(await getPanel("0")).toBeHidden();
    await expect(await getPanel("1")).toBeHidden();
    await expect(await getPanel("2")).toBeVisible();
});
});
//description of our second tests
// test.describe('Checking', () => {
//     for (const key of ["Home", "PageUp"]) {
//     test(`tab selection for tab ${key} through click`, async ({page}) =>{
//         //function to check the tab
//         async function tabActive(num: String) {
//             const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
//             //expect the attribute aria-checked to be true if tab is selected
//             await expect(tab).toHaveAttribute("aria-selected", "true");
//             await expect(tab).toBeFocused();
//         //end of function
//         }
//         //locator for each tab (tag: tab)
//         const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
//         //locator for each tab panel (tag: tabPanel)
//         const panel = page.locator(`#tabGroup-tab-panels-panel-${num}`)
//         //click on radio
//         await tab.click();
//         //verify is radio is checked
//         await tabActive(num);
//         //verify if panel is visible
//         await expect(panel).toBeVisible();
//     });
// }
// test('unique result and focus on selected radio through click', async ({page}) =>{
//     //function to check the content of result
//     async function getTab(num: String) {
//         //locator for each radio
//         const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
//         //return the locator
//         return tab;
//     //end of function
// }
//     //function to check the content of result
//     async function getPanel(num: String) {
//         //locator for each radio
//         const panel = page.locator(`#tabGroup-tab-panels-panel-${num}`);
//         //return the locator
//         return panel;
//     //end of function
// }
//     //function to check the radio
//     async function tabActive(num: String) {
//         await getTab(num);
//         //expect the attribute aria-checked to be true if switched on
//         await expect(await getTab(num)).toHaveAttribute("aria-selected", "true");
//         await expect(await getTab(num)).toBeFocused();
//     //end of function
//     }
//     //function to uncheck the radio
//     async function tabNotActive(num: String) {
//         await getTab(num);
//         //expect the attribute aria-checked to be true if switched on
//         await expect(await getTab(num)).toHaveAttribute("aria-selected", "false");
//         await expect(await getTab(num)).not.toBeFocused();
//     //end of function
//     }
//     //first assertions
//     await expect(await getPanel("0")).toBeHidden();
//     await expect(await getPanel("1")).toBeHidden();
//     await expect(await getPanel("2")).toBeHidden();
//     //click on tab
//     await (await getTab("0")).click();
//     //verify if tab 0 is chosen
//     await tabActive("0");
//     //verify if other tabs and not chosen
//     await tabNotActive("1");
//     await tabNotActive("2");
//     //verify if panel 0 is visible and panel 1 and panel 2 are hidden
//     await expect(await getPanel("0")).toBeVisible();
//     await expect(await getPanel("1")).toBeHidden();
//     await expect(await getPanel("2")).toBeHidden();
//     //click on tab
//     await (await getTab("1")).click();
//     //verify if tab 1 is chosen
//     await tabActive("1");
//     ////verify if other tabs and not chosen
//     await tabNotActive("0");
//     await tabNotActive("2");
//     //verify if panel 1 is visible and panel 0 and panel 2 are hidden
//     await expect(await getPanel("0")).toBeHidden();
//     await expect(await getPanel("1")).toBeVisible();
//     await expect(await getPanel("2")).toBeHidden();
//     //click on tab
//     await (await getTab("2")).click();
//     //verify if tab 2 is chosen
//     await tabActive("2");
//     ////verify if other tabs and not chosen
//     await tabNotActive("0");
//     await tabNotActive("1");
//     //verify if panel 2 is visible and panel 1 and panel 2 are hidden
//     await expect(await getPanel("0")).toBeHidden();
//     await expect(await getPanel("1")).toBeHidden();
//     await expect(await getPanel("2")).toBeVisible();
// });
// });