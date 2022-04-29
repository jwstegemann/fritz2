import {expect, Locator, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - styling
 * - label and description
 * - validation
 * - checkboxGroupValidationMessages
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of CheckBoxGroup component
    // await page.goto("https://next.fritz2.dev/headless-demo/#checkboxGroup");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#checkboxGroup");
//end of before hooks
});
//description of our first tests
test.describe('Checking through click', () => {
    //function to check the box
    async function checkBoxActive(chToggle: Locator) {
        //expect the attribute aria-checked to be true if switched on
        await expect(chToggle).toHaveAttribute("aria-checked", "true");
        //expect the checkbox to be focused
        await expect(chToggle).toBeFocused();
    //end of function
    }
    //function to uncheck the box
    async function checkBoxNotActive(chToggle: Locator) {
        //expect the attribute aria-checked to be true if switched on
        await expect(chToggle).toHaveAttribute("aria-checked", "false");
        //expect the checkbox to be focused
        await expect(chToggle).toBeFocused();
    //end of function
    }
    for (const num of ["1", "2", "3"]) {
    test(`unique result for checkbox ${num}`, async ({page}) =>{
        //locator for each checkbox label (tag: checkboxGroupLabel)
        const label = await page.locator(`#checkboxGroup-${num}-label`).textContent(); 
        //locator for each checkbox toggle (tag: checkboxGroupOptionToggle)
        const chToggle = await page.locator(`#checkboxGroup-${num}-toggle`);
        //click on checkbox
        await chToggle.click();
        //verify is checkbox is checked
        await checkBoxActive(chToggle);
        //locator for expected result
        const result = await page.evaluate(el => el.textContent, await (await page.$('#result')));
        //remove the not needed <em> from #result
        const expResult = result.replace('Selected: ','');
        //verify is label is in result
        await expect(expResult).toContain("(" + num + ") " + label);
        //click on checkbox
        await chToggle.click();
        //verify is checkbox is unchecked
        await checkBoxNotActive(chToggle);
        //verify is label is in result
        await expect(expResult).toContain("");
    });
}
    test(`several results`, async ({page}) =>{
        //function to uncheck the box
        async function checkResult() {
            //locator for all texts from #result
            const result = await page.evaluate(el => el.textContent, await (await page.$('#result')));
            //remove the not needed <em> from #result
            const allResult = result.replace('Selected: ','');
            //return this new value as result
            return allResult;
        //end of function
    }
        //variable for each checkbox/label/toggle 
        const num = ["1", "2", "3"];
        //locator for each checkbox label 
        const label1 = await page.locator(`#checkboxGroup-${num[0]}-label`).textContent(); 
        const label2 = await page.locator(`#checkboxGroup-${num[1]}-label`).textContent(); 
        const label3 = await page.locator(`#checkboxGroup-${num[2]}-label`).textContent(); 
        //locator for each checkbox toggle
        const chToggle1 = await page.locator(`#checkboxGroup-${num[0]}-toggle`);
        const chToggle2 = await page.locator(`#checkboxGroup-${num[1]}-toggle`);
        const chToggle3 = await page.locator(`#checkboxGroup-${num[2]}-toggle`);
        //result of each checkbox in result
        const result1 = "(" + num[0] + ") " + label1;
        const result2 = "(" + num[1] + ") " + label2;
        const result3 = "(" + num[2] + ") " + label3;
        //click on checkbox
        await chToggle1.click();
        //verify if checkbox 1 is checked
        await checkBoxActive(chToggle1);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect((await checkResult())).toContain(result1);
        //click on checkbox
        await chToggle2.click();
        //verify if checkbox 2 is checked
        await checkBoxActive(chToggle2);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain(result1 + result2);
        //click on checkbox
        await chToggle3.click();
        //verify if checkbox 3 is checked
        await checkBoxActive(chToggle3);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain(result1 + result2 + result3);
        //click on checkbox
        await chToggle1.click();
        //verify if checkbox 1 is unchecked
        await checkBoxNotActive(chToggle1);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain(result2 + result3);
        //click on checkbox
        await chToggle2.click();
        //verify if checkbox 2 is unchecked
        await checkBoxNotActive(chToggle2);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain(result3);
        //click on checkbox
        await chToggle3.click();
        //verify if checkbox 3 is unchecked
        await checkBoxNotActive(chToggle3);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain("");
    //end of second test
    });
//end of our first tests
});
//description of our second tests
test.describe('Checking with keys', () => {
    //function to check the box
    async function checkBoxActive(chToggle: Locator) {
        //expect the attribute aria-checked to be true if switched on
        await expect(chToggle).toHaveAttribute("aria-checked", "true");
        //expect the checkbox to be focused
        await expect(chToggle).toBeFocused();
    //end of function
    }
    //function to uncheck the box
    async function checkBoxNotActive(chToggle: Locator) {
        //expect the attribute aria-checked to be true if switched on
        await expect(chToggle).toHaveAttribute("aria-checked", "false");
        //expect the checkbox to be focused
        await expect(chToggle).toBeFocused();
    //end of function
    }
    for (const num of ["1", "2", "3"]) {
    test(`unique result for checkbox ${num} when pressing "Space"`, async ({page}) =>{
        //locator for each checkbox label (tag: checkboxGroupLabel)
        const label = await page.locator(`#checkboxGroup-${num}-label`).textContent(); 
        //locator for each checkbox toggle (tag: checkboxGroupOptionToggle)
        const chToggle = await page.locator(`#checkboxGroup-${num}-toggle`);
        //press "Space" on checkbox
        await chToggle.press('Space');
        //verify is checkbox is checked
        await checkBoxActive(chToggle);
        //locator for expected result
        const result = await page.evaluate(el => el.textContent, await (await page.$('#result')));
        //remove the not needed <em> from #result
        const expResult = result.replace('Selected: ','');
        //verify is label is in result
        await expect(expResult).toContain("(" + num + ") " + label);
        //press "Space" on checkbox
        await chToggle.press('Space');
        //verify is checkbox is unchecked
        await checkBoxNotActive(chToggle);
        //verify is label is in result
        await expect(expResult).toContain("");
    });
}
    test(`several results with "Tab", "Shift+Tab" and "Space"`, async ({page}) =>{
        //function to uncheck the box
        async function checkResult() {
            //locator for all texts from #result
            const result = await page.evaluate(el => el.textContent, await (await page.$('#result')));
            //remove the not needed <em> from #result
            const allResult = result.replace('Selected: ','');
            //return this new value as result
            return allResult;
        //end of function
    }
        //variable for each checkbox/label/toggle 
        const num = ["1", "2", "3"];
        //locator for each checkbox label 
        const label1 = await page.locator(`#checkboxGroup-${num[0]}-label`).textContent(); 
        const label2 = await page.locator(`#checkboxGroup-${num[1]}-label`).textContent(); 
        const label3 = await page.locator(`#checkboxGroup-${num[2]}-label`).textContent(); 
        //locator for each checkbox toggle
        const chToggle1 = await page.locator(`#checkboxGroup-${num[0]}-toggle`);
        const chToggle2 = await page.locator(`#checkboxGroup-${num[1]}-toggle`);
        const chToggle3 = await page.locator(`#checkboxGroup-${num[2]}-toggle`);
        //result of each checkbox in result
        const result1 = "(" + num[0] + ") " + label1;
        const result2 = "(" + num[1] + ") " + label2;
        const result3 = "(" + num[2] + ") " + label3;
        //focus on checkbox 1
        await chToggle1.focus();
        //press "Space" on checkbox
        await chToggle1.press('Space');
        //verify if checkbox 1 is checked
        await checkBoxActive(chToggle1);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect((await checkResult())).toContain(result1);
        //press "Tab" on checkbox
        await chToggle1.press('Tab');
        //press "Space" on checkbox
        await chToggle2.press('Space');
        //verify if checkbox 2 is checked
        await checkBoxActive(chToggle2);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain(result1 + result2);
        //press "Tab" on checkbox
        await chToggle2.press('Tab');
        //press "Space" on checkbox
        await chToggle3.press('Space');
        //verify if checkbox 3 is checked
        await checkBoxActive(chToggle3);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain(result1 + result2 + result3);
        //press "Space" on checkbox
        await chToggle3.press('Space');
        //verify if checkbox 1 is unchecked
        await checkBoxNotActive(chToggle3);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain(result1 + result2);
        //press "Shift+Tab" on checkbox
        await chToggle3.press('Shift+Tab');
        //press "Space" on checkbox
        await chToggle2.press('Space');
        //verify if checkbox 2 is unchecked
        await checkBoxNotActive(chToggle2);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain(result1);
        //press "Shift+Tab" on checkbox
        await chToggle2.press('Shift+Tab');
        //press "Space" on checkbox
        await chToggle1.press('Space');
        //verify if checkbox 3 is unchecked
        await checkBoxNotActive(chToggle1);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContain("");
    //end of second test
});
//end of our second tests
});