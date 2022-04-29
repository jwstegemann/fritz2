import {expect, test} from '@playwright/test';
const uaParser = require('ua-parser-js');
/*
 * Missing tests due to limited example component:
 * - open state
 * - transitions
 * - positioning
 * - arrow
 */

//let us declare some variables that we will use later
let browserDevice;
//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Tooltip component
    // await page.goto("https://next.fritz2.dev/headless-demo/#tooltip");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#tooltip");
    //use of "ua.parser-js" to detect the browser or device type
    const getUA = await page.evaluate(() => navigator.userAgent);
    //now we give our previous variable the uaParser element
    browserDevice = uaParser(getUA);
//end of before hooks
});
//description of our tests
test.describe('To check the display of tooltip', () => {
    test('when mouse over input and it is filled with something', async ({page}) =>{
        //get the browser OS
        const browserOS = browserDevice.os.name;
        //locator for first button on top (tag: tooltip)
        const btnF = page.locator('text=Some Button').first();
        //and locator for his tooltip
        const toolTbtnF = page.locator('[data-popper-placement="bottom"]');
        //locator for last button on the right corner
        const btnL = page.locator('text=Some Button').last();
        //and locator for his tooltip
        const toolTbtnL = page.locator('[data-popper-placement="left"]').first();
        //locator for button on the center
        const btnM = page.locator('text=Some Button').nth(1);
        //and locator for his tooltip
        const toolTbtnM = page.locator('[data-popper-placement="right"]').last();
        //locator for input on the left corner
        const inputF = page.locator('[placeholder="some input"]').first();
        //and locator for his tooltip
        const toolTinpF = page.locator('[data-popper-placement="right"]').first();
        //locator for input at the bottom
        const inputL = page.locator('[placeholder="some input"]').last();
        //and locator for his tooltip
        const toolTinpL = page.locator('[data-popper-placement="top"]');

        //start of test
        //let us exclude the mobile devices from this test as the tooltips are not good on these types of devices
        if (browserOS === "Android" || browserOS === "iOS"){
            //No test needed for Android or iOS
            console.log("No test needed for " + browserOS);
        //then let us check with all desktop browsers
        } else {
        //hover the button on top
        await btnF.hover();
        //verify if its tooltip is visible
        await expect(toolTbtnF).toBeVisible();
        //hover the input on the left corner
        await inputF.hover();
        //verify if its tooltip is visible
        await expect(toolTinpF).toBeVisible();
        //hover the button on the center
        await btnM.hover();
        //verify if its tooltip is visible
        await expect(toolTbtnM).toBeVisible();
        //hover the button on the right corner
        await btnL.hover();
        //verify if its tooltip is visible
        await expect(toolTbtnL).toBeVisible();
        //hover the input at the bottom
        await inputL.hover();
        //verify if its tooltip is visible
        await expect(toolTinpL).toBeVisible();
        //end of condition
        }
    });

});