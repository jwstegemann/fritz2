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
        //locator for first button on top (tag: tooltip)
        const btnTop = page.locator('#top-reference');
        //and locator for his tooltip
        const toolTbtnTop = page.locator('#top-tooltip');
        //locator for last button on the right corner
        const btnCenter = page.locator('#center-reference');
        //and locator for his tooltip
        const toolTbtnCenter = page.locator('#center-tooltip');
        //locator for button on the center
        const btnBottom = page.locator('#bottom-reference');
        //and locator for his tooltip
        const toolTbtnBottom = page.locator('#bottom-tooltip');

        //start of test
        //hover the button on top
        await btnTop.hover();
        //verify if its tooltip is visible
        await expect(toolTbtnTop).toBeVisible();
        //hover the input on the left corner
        await btnCenter.hover();
        //verify if its tooltip is visible
        await expect(toolTbtnCenter).toBeVisible();
        //hover the button on the center
        await btnBottom.hover();
        //verify if its tooltip is visible
        await expect(toolTbtnBottom).toBeVisible();
    });

});