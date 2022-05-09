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
    await page.goto("https://next.fritz2.dev/headless-demo/#checkboxGroup");
   // await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#checkboxGroup");
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
        const chToggle = page.locator(`#checkboxGroup-${num}-toggle`);
        //click on checkbox
        await chToggle.click();
        //verify is checkbox is checked
        await checkBoxActive(chToggle);
        //locator for expected result
        const result = page.locator('#result');
        //verify is label is in result
        await expect(result).toContainText(label);
        //click on checkbox
        await chToggle.click();
        //verify is checkbox is unchecked
        await checkBoxNotActive(chToggle);
        //verify is label is in result
        await expect(result).not.toContainText(label);
    });
}
    test(`several results`, async ({page}) =>{
        //function to uncheck the box
        async function checkResult() {
            //locator for all texts from #result
            const result = page.locator('#result');
            //return this new value as result
            return result;
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
        //click on checkbox
        await chToggle1.click();
        //verify if checkbox 1 is checked
        await checkBoxActive(chToggle1);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        expect((await checkResult())).toContainText(label1);
        //click on checkbox
        await chToggle2.click();
        //verify if checkbox 2 is checked
        await checkBoxActive(chToggle2);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContainText([label1, label2]);
        //click on checkbox
        await chToggle3.click();
        //verify if checkbox 3 is checked
        await checkBoxActive(chToggle3);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContainText([label1, label2, label3]);
        //click on checkbox
        await chToggle1.click();
        //verify if checkbox 1 is unchecked
        await checkBoxNotActive(chToggle1);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContainText([label2, label3]);
        //click on checkbox
        await chToggle2.click();
        //verify if checkbox 2 is unchecked
        await checkBoxNotActive(chToggle2);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContainText(label3);
        //click on checkbox
        await chToggle3.click();
        //verify if checkbox 3 is unchecked
        await checkBoxNotActive(chToggle3);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).not.toContainText(label3);
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
        const result = page.locator('#result');
        //verify is label is in result
        await expect(result).toContainText(label);
        //press "Space" on checkbox
        await chToggle.press('Space');
        //verify is checkbox is unchecked
        await checkBoxNotActive(chToggle);
        //verify is label is in result
        await expect(result).not.toContainText(label);
    });
}
    test(`several results with "Tab", "Shift+Tab" and "Space"`, async ({page}) =>{
        //function to uncheck the box
        async function checkResult() {
            //locator for all texts from #result
            const result = page.locator('#result');
            //return this new value as result
            return result;
        //end of function
    }
        //variable for each checkbox/label/toggle 
        const num = ["1", "2", "3"];
        //locator for each checkbox label 
        const label1 = await page.locator(`#checkboxGroup-${num[0]}-label`).textContent(); 
        const label2 = await page.locator(`#checkboxGroup-${num[1]}-label`).textContent(); 
        const label3 = await page.locator(`#checkboxGroup-${num[2]}-label`).textContent(); 
        //locator for each checkbox toggle
        const chToggle1 = page.locator(`#checkboxGroup-${num[0]}-toggle`);
        const chToggle2 = page.locator(`#checkboxGroup-${num[1]}-toggle`);
        const chToggle3 = page.locator(`#checkboxGroup-${num[2]}-toggle`);
        //focus on checkbox 1
        await chToggle1.focus();
        //press "Space" on checkbox
        await chToggle1.press('Space');
        //verify if checkbox 1 is checked
        await checkBoxActive(chToggle1);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect((await checkResult())).toContainText(label1);
        //press "Tab" on checkbox
        await chToggle1.press('Tab');
        //press "Space" on checkbox
        await chToggle2.press('Space');
        //verify if checkbox 2 is checked
        await checkBoxActive(chToggle2);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContainText([label1, label2]);
        //press "Tab" on checkbox
        await chToggle2.press('Tab');
        //press "Space" on checkbox
        await chToggle3.press('Space');
        //verify if checkbox 3 is checked
        await checkBoxActive(chToggle3);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContainText([label1, label2, label3]);
        //press "Space" on checkbox
        await chToggle3.press('Space');
        //verify if checkbox 1 is unchecked
        await checkBoxNotActive(chToggle3);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContainText([label1, label2]);
        //press "Shift+Tab" on checkbox
        await chToggle3.press('Shift+Tab');
        //press "Space" on checkbox
        await chToggle2.press('Space');
        //verify if checkbox 2 is unchecked
        await checkBoxNotActive(chToggle2);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).toContainText(label1);
        //press "Shift+Tab" on checkbox
        await chToggle2.press('Shift+Tab');
        //press "Space" on checkbox
        await chToggle1.press('Space');
        //verify if checkbox 3 is unchecked
        await checkBoxNotActive(chToggle1);
        //update the value of result
        await checkResult();
        //verify if the result is as expected
        await expect(await checkResult()).not.toContainText(label1);
    //end of second test
});
//end of our second tests
});