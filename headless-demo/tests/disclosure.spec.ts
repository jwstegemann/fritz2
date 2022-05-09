import {expect, Locator, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - state depending styling
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Disclosure component
    await page.goto("https://next.fritz2.dev/headless-demo/#disclosure");
    //await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#disclosure");
//end of before hooks
});
test.describe('Checking through click', () => {
    //function to check the expansion of disclosure button
    async function dsBtnExpanded(dsBtn: Locator) {
        //expect the attribute aria-expanded to be true if disclosure is expanded
        await expect(dsBtn).toHaveAttribute("aria-expanded", "true");
    //end of function
    }
    //function to uncheck the expansion of disclosure button
    async function dsBtnNotExpanded(dsBtn: Locator) {
        //expect the attribute aria-expanded to be true if disclosure is not expanded
        await expect(dsBtn).toHaveAttribute("aria-expanded", "false");
    //end of function
    }
    for (const num of ["0", "1", "2", "3"]) {
    test(`expansion of disclosure button ${num}`, async ({page}) =>{
        //locator for each disclosure button (tag: disclosureButton)
        const dsBtn = page.locator(`#disclosure-${num}-button`);
        //locator for each disclosure panel (tag: disclosurePanel)
        const dsPanel = page.locator(`#disclosure-${num}-panel`);
        //click on disclosure button
        await dsBtn.click();
        //verify is disclosure is expanded
        await dsBtnExpanded(dsBtn);
        //verify is disclosure panel is expanded
        await expect(dsPanel).toBeVisible();
        //click on disclosure button
        await dsBtn.click();
        //verify is disclosure is expanded
        await dsBtnNotExpanded(dsBtn);
        //verify is disclosure panel is expanded
        await expect(dsPanel).toBeHidden();
    });
}
    test('expansion of several disclosure buttons', async ({page}) =>{
        //variable for each disclosure button/panel 
        const num = ["0", "1", "2", "3"];
        //locator for each disclosure button
        const dsBtn1 = page.locator(`#disclosure-${num[0]}-button`);
        const dsBtn2 = page.locator(`#disclosure-${num[1]}-button`);
        const dsBtn3 = page.locator(`#disclosure-${num[2]}-button`);
        const dsBtn4 = page.locator(`#disclosure-${num[3]}-button`);
        //locator for each disclosure panel
        const dsPanel1 = page.locator(`#disclosure-${num[0]}-panel`);
        const dsPanel2 = page.locator(`#disclosure-${num[1]}-panel`);
        const dsPanel3 = page.locator(`#disclosure-${num[2]}-panel`);
        const dsPanel4 = page.locator(`#disclosure-${num[3]}-panel`);
        //first assertions of disclosure panels
        await expect(dsPanel1).toBeHidden();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
        //first assertions of disclosure button expansion
        expect(dsBtnNotExpanded(dsBtn1));
        expect(dsBtnNotExpanded(dsBtn2));
        expect(dsBtnNotExpanded(dsBtn3));
        expect(dsBtnNotExpanded(dsBtn4));

        //click on disclosure button
        await dsBtn1.click();
        //verify if aria-expanded is true for disclosure button 1 and aria-expanded of others is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnNotExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1 is expanded and others not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
        //click on disclosure button
        await dsBtn2.click();
        //verify if aria-expanded is true for disclosure button 1 and disclosure button 2 and aria-expanded of others is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1 and disclosure panel 2 are expanded and others not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
        //click on disclosure button
        await dsBtn3.click();
        //verify if aria-expanded is true for disclosure button 1, disclosure button 2 and disclosure button 3 and aria-expanded of disclosure button 4 is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1, disclosure panel 2 and disclosure panel 3 are expanded and disclosure panel 4 not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeVisible();
        await expect(dsPanel4).toBeHidden();
        //click on disclosure button
        await dsBtn4.click();
        //verify if aria-expanded is true for all disclosure buttons
        await dsBtnExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnExpanded(dsBtn3);
        await dsBtnExpanded(dsBtn4);
        //verify if all disclosure panel are expanded
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeVisible();
        await expect(dsPanel4).toBeVisible();
        //click on disclosure button
        await dsBtn4.click();
        //verify if aria-expanded is true for disclosure button 1, disclosure button 2 and disclosure button 3 and aria-expanded of disclosure button 4 is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1, disclosure panel 2 and disclosure panel 3 are expanded and disclosure panel 4 not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeVisible();
        await expect(dsPanel4).toBeHidden();
        //click on disclosure button
        await dsBtn3.click();
        //verify if aria-expanded is true for disclosure button 1 and disclosure button 2 and aria-expanded of others is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1 and disclosure panel 2 are expanded and others not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
        //click on disclosure button
        await dsBtn2.click();
        //verify if aria-expanded is true for disclosure button 1 and aria-expanded of others is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnNotExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1 is expanded and others not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
        //click on disclosure button
        await dsBtn1.click();
        //verify if aria-expanded is false for all disclosure buttons
        await dsBtnNotExpanded(dsBtn1);
        await dsBtnNotExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if all disclosure panels are not expanded
        await expect(dsPanel1).toBeHidden();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
    });
});
test.describe('Checking with keys', () => {
    //function to check the expansion of disclosure button
    async function dsBtnExpanded(dsBtn: Locator) {
        //expect the attribute aria-expanded to be true if disclosure is expanded
        await expect(dsBtn).toHaveAttribute("aria-expanded", "true");
    //end of function
    }
    //function to uncheck the expansion of disclosure button
    async function dsBtnNotExpanded(dsBtn: Locator) {
        //expect the attribute aria-expanded to be true if disclosure is not expanded
        await expect(dsBtn).toHaveAttribute("aria-expanded", "false");
    //end of function
    }
    for (const num of ["0", "1", "2", "3"]) {
    test(`expansion of disclosure button ${num} by pressing "Space"`, async ({page}) =>{
        //locator for each disclosure button
        const dsBtn = page.locator(`#disclosure-${num}-button`);
        //locator for each disclosure panel
        const dsPanel = page.locator(`#disclosure-${num}-panel`);
        //click on disclosure button
        await dsBtn.press("Space");
        //verify is disclosure is expanded
        await dsBtnExpanded(dsBtn);
        //verify is disclosure panel is expanded
        await expect(dsPanel).toBeVisible();
        //click on disclosure button
        await dsBtn.click();
        //verify is disclosure is expanded
        await dsBtnNotExpanded(dsBtn);
        //verify is disclosure panel is expanded
        await expect(dsPanel).toBeHidden();
    });
}
    test('expansion of several disclosure buttons by pressing "Tab" and "Space"', async ({page}) =>{
        //variable for each disclosure button/panel 
        const num = ["0", "1", "2", "3"];
        //locator for each disclosure button
        const dsBtn1 = page.locator(`#disclosure-${num[0]}-button`);
        const dsBtn2 = page.locator(`#disclosure-${num[1]}-button`);
        const dsBtn3 = page.locator(`#disclosure-${num[2]}-button`);
        const dsBtn4 = page.locator(`#disclosure-${num[3]}-button`);
        //locator for each disclosure panel
        const dsPanel1 = page.locator(`#disclosure-${num[0]}-panel`);
        const dsPanel2 = page.locator(`#disclosure-${num[1]}-panel`);
        const dsPanel3 = page.locator(`#disclosure-${num[2]}-panel`);
        const dsPanel4 = page.locator(`#disclosure-${num[3]}-panel`);
        //first assertions of disclosure panels
        await expect(dsPanel1).toBeHidden();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
        //first assertions of disclosure button expansion
        expect(dsBtnNotExpanded(dsBtn1));
        expect(dsBtnNotExpanded(dsBtn2));
        expect(dsBtnNotExpanded(dsBtn3));
        expect(dsBtnNotExpanded(dsBtn4));

        //press "Space" on disclosure button
        await dsBtn1.press("Space");
        //verify if aria-expanded is true for disclosure button 1 and aria-expanded of others is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnNotExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1 is expanded and others not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
        //press "Tab" on disclosure button
        await dsBtn1.press("Tab");
        //press "Space" on disclosure button
        await dsBtn2.press("Space");
        //verify if aria-expanded is true for disclosure button 1 and disclosure button 2 and aria-expanded of others is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1 and disclosure panel 2 are expanded and others not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
        //press "Tab" on disclosure button
        await dsBtn2.press("Tab");
        //press "Space" on disclosure button
        await dsBtn3.press("Space");
        //verify if aria-expanded is true for disclosure button 1, disclosure button 2 and disclosure button 3 and aria-expanded of disclosure button 4 is false
        await dsBtnExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if disclosure panel 1, disclosure panel 2 and disclosure panel 3 are expanded and disclosure panel 4 not
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeVisible();
        await expect(dsPanel4).toBeHidden();
        //press "Tab" on disclosure button
        await dsBtn3.press("Tab");
        //press "Space" on disclosure button
        await dsBtn4.press("Space");
        //verify if aria-expanded of all disclosure buttons is true
        await dsBtnExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnExpanded(dsBtn3);
        await dsBtnExpanded(dsBtn4);
        //verify if all disclosure panels are expanded
        await expect(dsPanel1).toBeVisible();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeVisible();
        await expect(dsPanel4).toBeVisible();
        //press "Space" on disclosure button
        await dsBtn1.press("Space");
        //verify if aria-expanded is true for disclosure button 2, disclosure button 3 and disclosure button 4 and aria-expanded of disclosure button 1 is false
        await dsBtnNotExpanded(dsBtn1);
        await dsBtnExpanded(dsBtn2);
        await dsBtnExpanded(dsBtn3);
        await dsBtnExpanded(dsBtn4);
        //verify if disclosure panel 2, disclosure panel 3 and disclosure panel 4 are expanded and disclosure panel 1 not
        await expect(dsPanel1).toBeHidden();
        await expect(dsPanel2).toBeVisible();
        await expect(dsPanel3).toBeVisible();
        await expect(dsPanel4).toBeVisible();
        //press "Tab" on disclosure button
        await dsBtn1.press("Tab");
        //press "Space" on disclosure button
        await dsBtn2.press("Space");
        //verify if aria-expanded is true for disclosure button 3 and disclosure button 4 and aria-expanded of others is false
        await dsBtnNotExpanded(dsBtn1);
        await dsBtnNotExpanded(dsBtn2);
        await dsBtnExpanded(dsBtn3);
        await dsBtnExpanded(dsBtn4);
        //verify if disclosure panel 3 and disclosure panel 4 are expanded and others not
        await expect(dsPanel1).toBeHidden();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeVisible();
        await expect(dsPanel4).toBeVisible();
        //press "Tab" on disclosure button
        await dsBtn2.press("Tab");
        //press "Space" on disclosure button
        await dsBtn3.press("Space");
        //verify if aria-expanded is true for disclosure button 4 and aria-expanded of others is false
        await dsBtnNotExpanded(dsBtn1);
        await dsBtnNotExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnExpanded(dsBtn4);
        //verify if disclosure panel 4 is expanded and others not
        await expect(dsPanel1).toBeHidden();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeVisible();
        //press "Tab" on disclosure button
        await dsBtn3.press("Tab");
        //press "Space" on disclosure button
        await dsBtn4.press("Space");
        //verify if aria-expanded of all disclosure buttons is false
        await dsBtnNotExpanded(dsBtn1);
        await dsBtnNotExpanded(dsBtn2);
        await dsBtnNotExpanded(dsBtn3);
        await dsBtnNotExpanded(dsBtn4);
        //verify if all disclosure panels are not expanded
        await expect(dsPanel1).toBeHidden();
        await expect(dsPanel2).toBeHidden();
        await expect(dsPanel3).toBeHidden();
        await expect(dsPanel4).toBeHidden();
    });
});