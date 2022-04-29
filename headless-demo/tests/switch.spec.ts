import {expect, Locator, Page, test} from '@playwright/test';
const uaParser = require('ua-parser-js');
/*
 * Missing tests due to limited example component:
 * - label and description
 * - validation
 * - switchValidationMessages
 */

//let us declare some variables that we will use later
let browserDevice: { browser: { name: any; }; };
//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Switch component
    // await page.goto("https://next.fritz2.dev/headless-demo/#switch");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#switch");
    //use of "ua.parser-js" to detect the browser or device type
    const getUA = await page.evaluate(() => navigator.userAgent);
    //now we give our previous variable the uaParser element
    browserDevice = uaParser(getUA);
//end of before hooks
});

//description of our first tests
test.describe('To switch on and off', () => {
    //fonction to switch on the switch
    async function swSwitchOn(sw: Locator) {
        //get the browser name
        const browserName = browserDevice.browser.name;
        //expect the attribute aria-checked to be true if switched on
        await expect(sw).toHaveAttribute("aria-checked", "true");
        //condition only for all browsers and devices except Safari and Mobile Safari
        if (browserName.indexOf("Safari") === -1){
            //the focus works only on all browsers except Safari and Mobile Safari
            await expect(sw).toBeFocused();
        //end of condition
    }
    //end of function
    }
    //fonction to switch off the switch
    async function swSwitchOff(sw: Locator) {
         //get the browser name
        const browserName = browserDevice.browser.name;
        //expect the attribute aria-checked to be false if switched off
        await expect(sw).toHaveAttribute("aria-checked", "false");
        //condition only for all browsers and devices except Safari and Mobile Safari
        if (browserName.indexOf("Safari") === -1){
            //the focus works only on all browsers except Safari and Mobile Safari
            await expect(sw).toBeFocused();
        //end of condition
    }
    //end of function
    }
    //fonction to switch on the switch with label
    async function swToggleSwitchOn(swToggle: Locator) {
         //get the browser name
        const browserName = browserDevice.browser.name;
        //expect the attribute aria-checked to be true if switched on
        await expect(swToggle).toHaveAttribute("aria-checked", "true");
        //condition only for all browsers and devices except Safari and Mobile Safari
        if (browserName.indexOf("Safari") === -1){
            //the focus works only on all browsers except Safari and Mobile Safari
            await expect(swToggle).toBeFocused();
        //end of condition
    }
    //end of function
    }
    //fonction to switch off the switch with label
    async function swToggleSwitchOff(swToggle: Locator) {
         //get the browser name
        const browserName = browserDevice.browser.name;
        //expect the attribute aria-checked to be false if switched off
        await expect(swToggle).toHaveAttribute("aria-checked", "false");
        //condition only for all browsers and devices except Safari and Mobile Safari
        if (browserName.indexOf("Safari") === -1){
            //the focus works only on all browsers except Safari and Mobile Safari
            await expect(swToggle).toBeFocused();
        //end of condition
        }
    //end of function
    }
    async function swLabelSwitchOn(swToggle: Locator) {
        //get the browser name
       const browserName = browserDevice.browser.name;
       //expect the attribute aria-checked to be false if switched off
       await expect(swToggle).toHaveAttribute("aria-checked", "true");
       //condition only for all browsers and devices except Safari and Mobile Safari
       if (browserName.indexOf("Safari") === -1){
           //the focus works only on all browsers except Safari and Mobile Safari
           await expect(swToggle).not.toBeFocused();
       //end of condition
       }
   //end of function
   }
   async function swLabelSwitchOff(swToggle: Locator) {
    //get the browser name
   const browserName = browserDevice.browser.name;
   //expect the attribute aria-checked to be false if switched off
   await expect(swToggle).toHaveAttribute("aria-checked", "false");
   //condition only for all browsers and devices except Safari and Mobile Safari
   if (browserName.indexOf("Safari") === -1){
       //the focus works only on all browsers except Safari and Mobile Safari
       await expect(swToggle).not.toBeFocused();
   //end of condition
   }
//end of function
}
    //description of the first test
    test('Switch on & off principal switch', async ({page}) => {
        //locator of switch (tag: switch)
        const sw = page.locator('id=switch');
        //focus on switch
        await sw.focus()
        //verify if the switch is off
        await swSwitchOff(sw)
        //click on switch
        await sw.click();
        //verify if the switch is on
        await swSwitchOn(sw)
        //click again on switch
        await sw.click();
        //verify if the switch is off
        await swSwitchOff(sw)
    //end of first test
    });
    //description of the second test
    test('Switch on & off secondary switch', async ({page}) => {
        //locator of switchToggle (tag: switchToggle)
        const swToggle = page.locator('id=switchWithLabel-toggle');
        //focus on switchToggle
        await swToggle.focus()
        //verify if switchToggle is on
        await swToggleSwitchOn(swToggle)
        //click on the switchToggle
        await swToggle.click();
        //verify if switchToggle is off
        await swToggleSwitchOff(swToggle)
        //click again on switchToggle
        await swToggle.click();
        //verify if switchToggle is on
        await swToggleSwitchOn(swToggle)
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
        await swLabelSwitchOff(swToggle)
        //click again on switchToggle
        await swLabel.click();
        //verify if switchToggle is on
        await swLabelSwitchOn(swToggle)
    //end of third test
    });
    //description of the fourth test
    test("focus the switchbutton and press Space", async ({page}) => {
        //locator of switch
        const sw = page.locator('id=switch');
        //locator of switchToggle
        const swToggle = page.locator('id=switchWithLabel-toggle');
        //get the browser name
        // const browserName = browserDevice.browser.name;
        //focus on switch
        await sw.focus();
        //verify if switch is off
        await swSwitchOff(sw);
        //conditions for all browsers and devices except Firefox
        // if (browserName.indexOf("Firefox") === -1){
        //press "Space" button
        await sw.press(' ');
        //condition for Firefox only
        // } else {
        // //press "Space" button (Firefox version)
        // await sw.press("Space");
        // }
        //verify if switch is on
        await swSwitchOn(sw);
        //focus on switchToggle
        await swToggle.focus();
        //verify if switchToggle is on
        await swToggleSwitchOn(swToggle);
        //conditions for all browsers and devices except Firefox
        // if (browserName.indexOf("Firefox") === -1){
            //press "Space" button
            await swToggle.press('Space');
            //condition for Firefox only
            // } else {
            // //press "Space" button (Firefox version)
            // await swToggle.press("Space");
            // }
        //verify if switchToggle is off
        await swToggleSwitchOff(swToggle);
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
        //get the browser name
        const browserName = browserDevice.browser.name;
        //focus on switch
        await sw.focus();
        //verify if switch is focused
        await expect(sw).toBeFocused();
        //click on switch
        await sw.click();
        //condition only for all browsers and devices except Safari and Mobile Safari
        if (browserName.indexOf("Safari") === -1){
        //the focus works only on all browsers except Safari and Mobile Safari
        await expect(sw).toBeFocused();
    //end of condition
    }
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
        //get the browser name
        const browserName = browserDevice.browser.name;
        //focus on switchToggle
        await swToggle.focus();
        //verify if switchToggle is focused
        await expect(swToggle).toBeFocused();
        //click on switchToggle
        await swToggle.click();
         //condition only for all browsers and devices except Safari and Mobile Safari
        if (browserName.indexOf("Safari") === -1){
        //the focus works only on all browsers except Safari and Mobile Safari
        await expect(swToggle).toBeFocused();
        //end of condition
        }
        //click on switch with label
        await swLabel.click();
        //verify if switchToggle is not focused
        await expect(swToggle).not.toBeFocused();
    //end of second test
    });
//end of our second tests
});