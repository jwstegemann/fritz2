import {expect, Locator, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - open state
 * - transitions
 * - positioning
 * - arrow
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Pop Over component
    // await page.goto("https://next.fritz2.dev/headless-demo/#popover");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#popover");
//end of before hooks
});
//description of our first tests
test.describe('To open the popover', () => {
    //function to verify if panel is open
    async function popOvPanelOpen(popOvPanel:Locator){
        //verify if panel is visible
        await expect(popOvPanel).toBeVisible();
    //end of function
    }
    //function to verify if panel is closed
    async function popOvPanelClose(popOvPanel:Locator){
        //verify if panel is hidden
        await expect(popOvPanel).not.toBeVisible();
    //end of function
    }
    //description of first test
    test('and close when clicking on it and also closing when pressing Escape', async ({page}) =>{
        //locator of popOverButton (tag: popOverButton)
        const popOv = page.locator('#popOver-button');
        //locator of popOverPanel (tag: popOverPanel)
        const popOvPanel = page.locator('#popOver-items');
        //focus on popOverButton
        await popOv.focus();
        //click on popOverButton
        await popOv.click();
        //verify if popOverPanel is open
        await popOvPanelOpen(popOvPanel);
        //click on popOverButton
        await popOv.click();
        //verify if popOverPanel is closed
        await popOvPanelClose(popOvPanel);
        //click on popOverButton
        await popOv.click();
        //verify if popOverPanel is open
        await popOvPanelOpen(popOvPanel);
        //press "Escape" on popOverButton
        await popOv.press('Escape');
        //verify if popOverPanel is closed
        await popOvPanelClose(popOvPanel);
    //end of first test
    });
    //for loop for 2 keys that will be pressed respectively
    for (const key of ["Enter", "Space"]){
    //description of second test
    test(`only with ${key} and close it with Esc`, async ({page}) =>{
        //locator of popOverButton
        const popOv = page.locator('#popOver-button');
        //locator of popOverPanel
        const popOvPanel = page.locator('#popOver-items');
        //focus on popOverButton
        await popOv.focus();
        //press "Enter"/"Space" on popOverButton
        await popOv.press(key);
        //verify if popOverPanel is open
        await popOvPanelOpen(popOvPanel);
        //press "Enter"/"Space" on popOverButton
        await popOv.press(key);
        //verify if popOverPanel is closed
        await popOvPanelClose(popOvPanel);
        //press "Enter"/"Space" on popOverButton
        await popOv.press(key);
        //verify if popOverPanel is open
        await popOvPanelOpen(popOvPanel);
        //press "Escape" on popOverButton
        await popOv.press('Escape');
        //verify if popOverPanel is closed
        await popOvPanelClose(popOvPanel);
    //end of second test
    });
//end of for loop
}
//end of our first tests
});
//description of our second tests
test.describe('Pressing on', () => {
    //function to verify if panel is open
    async function popOvPanelOpen(popOvPanel:Locator){
        //verify if panel is visible
        await expect(popOvPanel).toBeVisible();
    //end of function
    }
    //function to verify if panel is closed
    async function popOvPanelClose(popOvPanel:Locator){
        //verify if panel is hidden
        await expect(popOvPanel).not.toBeVisible();
    //end of function
    }
    //for loop to repeat the test i times
    for(let i = 1; i < 5; i++){
    //description of test
    test(`Tab ${i} time(s) will always focus the Popover`, async ({page}) =>{
        //locator of popOverButton
        const popOv = page.locator('#popOver-button');
        //locator of popOverPanel
        const popOvPanel = page.locator('#popOver-items');
        //for loop to press "Tab" i times
        for (let j = 1; j < i; j++)
            //press "Tab" on popOverButton
            await popOv.press('Tab');
            //press "Enter" on popOverButton
            await popOv.press('Enter');
            //verify if popOverPanel is open
            await popOvPanelOpen(popOvPanel);
            //press "Escape" on popOverButton
            await popOv.press('Escape');
            //verify if popOverPanel is closed
            await popOvPanelClose(popOvPanel);
    //end of test
    }); 
//end of for loop
}
//end of our second tests 
});