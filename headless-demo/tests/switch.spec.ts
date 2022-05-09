import {expect, Locator, Page, test} from '@playwright/test';
//const uaParser = require('ua-parser-js');
/*
 * Missing tests due to limited example component:
 * - label and description
 * - validation
 * - switchValidationMessages
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Switch component
    await page.goto("https://next.fritz2.dev/headless-demo/#switch");
    // await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#switch");
//end of before hooks
});

//description of our first tests
test.describe('To switch on and off', () => {
    //description of the first test
    test('Switch on & off principal switch', async ({page}) => {
        //locator of switch (tag: switch)
        const sw = page.locator('id=switch');
        //focus on switch
        await sw.focus();
        //verify if the switch is off
        await expect(sw).toHaveAttribute("aria-checked", "false");
        //click on switch
        await sw.click();
        //verify if the switch is on
        await expect(sw).toHaveAttribute("aria-checked", "true");
        //click again on switch
        await sw.click();
        //verify if the switch is off
        await expect(sw).toHaveAttribute("aria-checked", "false");
    //end of first test
    });
    //description of the second test
    test('Switch on & off secondary switch', async ({page}) => {
        //locator of switchToggle (tag: switchToggle)
        const swToggle = page.locator('id=switchWithLabel-toggle');
        //focus on switchToggle
        await swToggle.focus()
        //verify if switchToggle is on
        await expect(swToggle).toHaveAttribute("aria-checked", "true");
        //click on the switchToggle
        await swToggle.click();
        //verify if switchToggle is off
        await expect(swToggle).toHaveAttribute("aria-checked", "false");
        //click again on switchToggle
        await swToggle.click();
        //verify if switchToggle is on
        await expect(swToggle).toHaveAttribute("aria-checked", "true");
    //end of second test
    });
    //description of the third test
    test('Switch on & off secondary switch with switchLabel', async ({page}) => {
        //locator of switchToggle
        const swToggle = page.locator('id=switchWithLabel-toggle');
        //locator of switchLabel (tag: switchLabel)
        const swLabel = page.locator('id=switchWithLabel-label');
        //focus on switchLabel
        await swLabel.focus();
        //click on the switchToggle
        await swLabel.click();
        //verify if switchToggle is off
        await expect(swToggle).toHaveAttribute("aria-checked", "false");
        //click again on switchToggle
        await swLabel.click();
        //verify if switchToggle is on
        await expect(swToggle).toHaveAttribute("aria-checked", "true");
    //end of third test
    });
    //description of the fourth test
    test("focus the switchbutton and press Space", async ({page}) => {

        //locator of switch
        const sw = page.locator('id=switch');
        //locator of switchToggle
        const swToggle = page.locator('id=switchWithLabel-toggle');
        //focus on switch
        await sw.focus();
        //verify if switch is off
        await expect(sw).toHaveAttribute("aria-checked", "false");
        //press "Space" button
        //await page.press('id=switch', "Space", {delay: 1000})
        await page.keyboard.down('Space')
        //await sw.press('Space');
        //verify if switch is on
        await expect(sw).toHaveAttribute("aria-checked", "true");
        //focus on switchToggle
        await swToggle.focus();
        //verify if switchToggle is on
        await expect(swToggle).toHaveAttribute("aria-checked", "true");
        //press "Space" button
        //await page.press('id=switchWithLabel-toggle', "Space", {delay: 1000})
        await page.keyboard.down('Space')
        //await swToggle.press('Space');
        //verify if switchToggle is off
        await expect(swToggle).toHaveAttribute("aria-checked", "false");
    //end of fourth test
    });
    
//end of our first tests
});
//description of our second tests
test.describe("To check the focus", () => {
    //function to save the elements of switch and switchLabel
    async function locatorsSw(page: Page): Promise<[Locator, Locator]> {
        //locator of switch
        const sw = page.locator('id=switch');
        //locator of switch with label
        const swLabel = page.locator('id=switchWithLabel');
        //return of the two elements
        return [sw, swLabel]
    //end of function
    }
    //function to save the elements of switchToggle and the div of switchLabel
    async function locatorsSwLabel(page: Page): Promise<[Locator, Locator]> {
        //locator of switchToggle
        const swToggle = page.locator('id=switchWithLabel-toggle');
        //locator of switch with label
        const swLabel = page.locator('id=switchWithLabel');
        //return of the two elements
        return [swToggle, swLabel]
    //end of function
    }
    //description of the first test
    test("for first toggle", async ({page}) => {
        //get of both elements
        const [sw, swLabel] = await locatorsSw(page);
        //focus on switch
        await sw.focus();
        //verify if switch is focused
        await expect(sw).toBeFocused();
        //click on switch
        await sw.click();
        //click on the div of switch with label
        await swLabel.click();
        //verify if switch is not focused
        await expect(sw).not.toBeFocused();
    //end of first test
    });
    //description of the second test
    test("for second toggle", async ({page}) => {
        //get of both elements
        const [swToggle, swLabel] = await locatorsSwLabel(page);
        //focus on switchToggle
        await swToggle.focus();
        //verify if switchToggle is focused
        await expect(swToggle).toBeFocused();
        //click on switchToggle
        await swToggle.click();
        //click on switch with label
        await swLabel.click();
        //verify if switchToggle is not focused
        await expect(swToggle).not.toBeFocused();
    //end of second test
    });
//end of our second tests
});