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
    //go to the page of Input Field component
    // await page.goto("https://next.fritz2.dev/headless-demo/#inputfield");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#inputfield");
//end of before hooks
});
//description of our tests
test.describe('To check the', () => {
    //for loop for 2 keys that will be pressed respectively
    for (const key of ["Enter", "Tab"]) {
        //description of the first test
        test(`fill of input when ${key} is pressed`, async ({page}) => {
            //locator of inputField (tag: inputField)
            const field = page.locator('#inputField-field');
            //variable for some text to fill into inputField
            const text = "Some new text to write";
            //focus on inputField
            await field.focus();
            //verify if inputField is focused
            await expect(field).toBeFocused();
            //fill the text "text" into inputField
            await field.fill(text);
            //verify if inputField is focused
            await expect(field).toBeFocused();
            //press "Enter"/"Tab" button
            await page.press("#inputField-field", key);
            //locator for new text in result
            const fieldfill = await page.evaluate(el => el.textContent, (await page.$('#result')));
            //remove the not needed <em> from result
            fieldfill.replace('Selected: ', '');
            //verify if result contains the text "text"
            expect(fieldfill).toContain(text);
        //end of first test
        });
    //end of for loop
    }
    //description of the second test
    test(`fill of input when inputLabel is clicked and mouse is clicked away from inputField`, async ({page}) => {
        //locator of inputField
        const field = page.locator('#inputField-field');
        //locator of inputLabel (tag: inputLabel)
        const label = page.locator('#inputField-label');
        //variable for some text to fill into inputField
        const text = "Some new text to write";
        //click on label
        await label.click();
        //verify if inputField is focused
        await expect(field).toBeFocused();
        //fill the text "text" into inputField
        await field.fill(text);
        //verify if inputField is focused
        await expect(field).toBeFocused();
        //click out of inputField
        await page.mouse.click(0, 0);
        //locator for new text in result
        const fieldfill = await page.evaluate(el => el.textContent, (await page.$('#result')));
        //remove the not needed <em> from result
        fieldfill.replace('Selected: ', '');
        //verify if result contains the text "text"
        expect(fieldfill).toContain(text);
    //end of second test
    });
//end of our tests
});