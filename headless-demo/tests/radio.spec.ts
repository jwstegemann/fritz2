import {expect, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - styling
 * - label and description
 * - validation
 * - radioGroupValidationMessages
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Radio Group component
    await page.goto("https://next.fritz2.dev/headless-demo/#radioGroup");
    // await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#radioGroup");
//end of before hooks
});
//description of our first tests
test.describe('Checking', () => {
    for (const item of ["Hobby", "Startup", "Business", "Enterprise"]) {
    test(`unique result for radio ${item} through click`, async ({page}) =>{
        //function to check the radio
        async function radioActive(radItem: String) {
            const radio = page.locator("#radioGroup-"+ radItem + "-toggle");
            //expect the attribute aria-checked to be true if switched on
            await expect(page.locator("#radioGroup-"+ radItem + "-toggle")).toHaveAttribute("aria-checked", "true");
            await expect(radio).toBeFocused();
        //end of function
        }
        //locator for each radio label (tag: radioGroup)
        const radToggle = page.locator(`#radioGroup-${item}-toggle`)
        //locator for each radio toggle (tag: radioGroupLabel) We will replace all characters with break lines, · and blank space
        const label = await page.locator(`#radioGroup-${item}-label`).textContent();
        //click on radio
        await radToggle.click();
        //verify is radio is checked
        await radioActive(item);
        //locator for expected result. We also remove the <em> from result as we don't need it
        const result = page.locator('#result');
        //verify is label is in result
        expect(result).toContainText(label);
    });
}
    test('unique result and focus on selected radio through click', async ({page}) =>{
        //function to check the content of result
        async function checkResult() {
            //locator for expected result
            const result = await page.locator('#result');
            //return this new value as result
            return result;
        //end of function
    }
        //function to check the content of result
        async function getRadio(radItem: String) {
            //locator for each radio
            const radio = page.locator("#radioGroup-"+ radItem + "-toggle");
            //return the locator
            return radio;
        //end of function
    }
        //function to check the radio
        async function radioActive(radItem: String) {
            await getRadio(radItem);
            //expect the attribute aria-checked to be true if switched on
            await expect(await getRadio(radItem)).toHaveAttribute("aria-checked", "true");
            await expect(await getRadio(radItem)).toBeFocused();
        //end of function
        }
        //function to uncheck the radio
        async function radioNotActive(radItem: String) {
            await getRadio(radItem);
            //expect the attribute aria-checked to be true if switched on
            await expect(await getRadio(radItem)).toHaveAttribute("aria-checked", "false");
            await expect(await getRadio(radItem)).not.toBeFocused();
        //end of function
        }
        //function to uncheck the radio
        async function checkLabel(radItem: String) {
            //expect the attribute aria-checked to be true if switched on
           const label = await page.locator("#radioGroup-" + radItem + "-label").innerText();
           return label;
        //end of function
        }
        //click on radio
        await (await getRadio("Hobby")).click();
        //verify if radio is chosen
        await radioActive("Hobby");
        //verify if other radio and not chosen
        await radioNotActive("Startup");
        await radioNotActive("Business");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Hobby"));
        //click on radio
        await (await getRadio("Startup")).click();
        //verify if radio is chosen
        await radioActive("Startup");
        //verify if other radio and not chosen
        await radioNotActive("Hobby");
        await radioNotActive("Business");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Startup"));
        //click on radio
        await (await getRadio("Business")).click();
        //verify if radio is chosen
        await radioActive("Business");
        //verify if other radio and not chosen
        await radioNotActive("Hobby");
        await radioNotActive("Startup");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Business"));
        //click on radio
        await (await getRadio("Enterprise")).click();
        //verify if radio is chosen
        await radioActive("Enterprise");
        //verify if other radio and not chosen
        await radioNotActive("Hobby");
        await radioNotActive("Startup");
        await radioNotActive("Business");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Enterprise"));
    });
    test('unique result when pressing Up and Down', async ({page}) =>{
        //function to check the content of result
        async function checkResult() {
            //locator for expected result
            const result = page.locator('#result');
            //return this new value as result
            return result;
        //end of function
    }
        //function to check the content of result
        async function getRadio(radItem: String) {
            //locator for each radio
            const radio = page.locator("#radioGroup-"+ radItem + "-toggle");
            //return the locator
            return radio;
        //end of function
    }
        //function to check the radio
        async function radioActive(radItem: String) {
            await getRadio(radItem);
            //expect the attribute aria-checked to be true if switched on
            await expect(await getRadio(radItem)).toHaveAttribute("aria-checked", "true");
            await expect(await getRadio(radItem)).toBeFocused();
        //end of function
        }
        //function to uncheck the radio
        async function radioNotActive(radItem: String) {
            await getRadio(radItem);
            //expect the attribute aria-checked to be true if switched on
            await expect(await getRadio(radItem)).toHaveAttribute("aria-checked", "false");
            await expect(await getRadio(radItem)).not.toBeFocused();
        //end of function
        }
        //function to uncheck the radio
        async function checkLabel(radItem: String) {
            //expect the attribute aria-checked to be true if switched on
           const label = await page.locator("#radioGroup-" + radItem + "-label").innerText();
           return label;
        //end of function
        }
        //click on radio
        await (await getRadio("Hobby")).click();
        //verify if radio is chosen
        await radioActive("Hobby");
        //verify if other radio and not chosen
        await radioNotActive("Startup");
        await radioNotActive("Business");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Hobby"));
        //press "Down" on radio 
        await (await getRadio("Hobby")).press('ArrowDown'); 
        //verify if radio is chosen
        await radioActive("Startup");
        //verify if other radio and not chosen
        await radioNotActive("Hobby");
        await radioNotActive("Business");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Startup"));
        //press "Down" on radio 
        await (await getRadio("Startup")).press('ArrowDown'); 
        //verify if radio is chosen
        await radioActive("Business");
        //verify if other radio and not chosen
        await radioNotActive("Hobby");
        await radioNotActive("Startup");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Business"));
        //press "Down" on radio 
        await (await getRadio("Business")).press('ArrowDown'); 
        //verify if radio is chosen
        await radioActive("Enterprise");
        //verify if other radio and not chosen
        await radioNotActive("Hobby");
        await radioNotActive("Startup");
        await radioNotActive("Business");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Enterprise"));
        //press "Up" on radio 
        await (await getRadio("Enterprise")).press('ArrowUp'); 
        //verify if radio is chosen
        await radioActive("Business");
        //verify if other radio and not chosen
        await radioNotActive("Hobby");
        await radioNotActive("Startup");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Business"));
        //press "Up" on radio 
        await (await getRadio("Business")).press('ArrowUp'); 
        //verify if radio is chosen
        await radioActive("Startup");
        //verify if other radio and not chosen
        await radioNotActive("Hobby");
        await radioNotActive("Business");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Startup"));
        //press "Up" on radio 
        await (await getRadio("Startup")).press('ArrowUp'); 
        //verify if radio is chosen
        await radioActive("Hobby");
        //verify if other radio and not chosen
        await radioNotActive("Startup");
        await radioNotActive("Business");
        await radioNotActive("Enterprise");
        //update the value of result
        await checkResult();
        //verify is label is in result
        expect(await checkResult()).toContainText(await checkLabel("Hobby"));
    });
});