import {expect, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - label and description
 * - transition
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Modal component
    // await page.goto("https://next.fritz2.dev/headless-demo/#modal");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#modal");
    //click on Open Button
    await page.locator('text=Open').click();
//end of before hooks
});
//description of our first tests
test.describe('To check if', () => {
        //description of first test
        test(`by clicking on button will leave the modal or not`, async ({page}) => {
            //locator for each modal button
            const buttonStay = page.locator(`#button-stay`);
            const buttonCancel = page.locator(`#button-cancel`);
            const buttonClose = page.locator(`#button-close`);
            //focus on button
            await buttonStay.focus();
            //click on button
            await buttonStay.click();
            //verify if the modal overlay (tag: modalOverlay) is still visible
            await expect(buttonStay).toBeVisible();
            //focus on button
            await buttonCancel.focus();
            //click on button
            await buttonCancel.click();
            //verify if modalOverlay is closed
            await expect(buttonCancel).toBeHidden();
            //reopen modal
            await page.locator('text=Open').click();
            //focus on button
            await buttonClose.focus();
            //click on button
            await buttonClose.click();
            //verify if modalOverlay is closed
            await expect(buttonClose).toBeHidden();
        //end of first test
        });
        //description of second test
        test(`by clicking on button and clicking again on Open will reopen the modal`, async ({page}) => {
            //locator for each modal button
            //locator for each modal button
            const buttonStay = page.locator(`#button-stay`);
            const buttonCancel = page.locator(`#button-cancel`);
            const buttonClose = page.locator(`#button-close`);
            //focus on button
            await buttonStay.focus();
            //click on button
            await buttonStay.click();
            //verify if the modal overlay (tag: modalOverlay) is still visible
            await expect(buttonCancel).toBeVisible();
            //press "Tab" on buttonStay
            await buttonStay.press('Tab');
            //click on button
            await buttonCancel.click();
            //verify if modalOverlay is closed
            await page.locator('text=Open').click();
            //verify buttonCancel is visible
            await expect(buttonCancel).toBeVisible();
            //focus on button
            await buttonClose.focus();
            //click on button
            await buttonClose.click();
            //verify if modalOverlay is closed
            await page.locator('text=Open').click();
            //verify buttonClose is visible
            await expect(buttonClose).toBeVisible();
        //end of second test
        });
        //description of third test
        test(`by pressing Enter on button will leave the modal or not`, async ({page}) => {
            //locator for each modal button
            const buttonStay = page.locator(`#button-stay`);
            const buttonCancel = page.locator(`#button-cancel`);
            const buttonClose = page.locator(`#button-close`);
            //focus on button
            await buttonStay.focus();
            //click on button
            await buttonStay.press('Enter');
            //verify if the modal overlay (tag: modalOverlay) is still visible
            await expect(buttonStay).toBeVisible();
            //focus on button
            await buttonCancel.focus();
            //click on button
            await buttonCancel.press('Enter');
            //verify if modalOverlay is closed
            await expect(buttonCancel).toBeHidden();
            //reopen modal
            await page.locator('text=Open').click();
            //focus on button
            await buttonClose.focus();
            //click on button
            await buttonClose.press('Enter');
            //verify if modalOverlay is closed
            await expect(buttonClose).toBeHidden();
        //end of third test
        });
        //description of fourth test
        test(`by pressing Enter on button and pressing again Enter on Open will reopen the modal`, async ({page}) => {
            //locator for each modal button
            //locator for each modal button
            const buttonStay = page.locator(`#button-stay`);
            const buttonCancel = page.locator(`#button-cancel`);
            const buttonClose = page.locator(`#button-close`);
            //focus on button
            await buttonStay.focus();
            //click on button
            await buttonStay.press('Enter')
            //verify if the modal overlay (tag: modalOverlay) is still visible
            await expect(buttonCancel).toBeVisible();
            //press "Tab" on buttonStay
            await buttonStay.press('Tab');
            //click on button
            await buttonCancel.press('Enter')
            //verify if modalOverlay is closed
            await page.locator('text=Open').press('Enter')
            //verify buttonCancel is visible
            await expect(buttonCancel).toBeVisible();
            //focus on button
            await buttonClose.focus();
            //click on button
            await buttonClose.press('Enter')
            //verify if modalOverlay is closed
            await page.locator('text=Open').press('Enter')
            //verify buttonClose is visible
            await expect(buttonClose).toBeVisible();
        //end of fourth test
        });
//end of our first tests
});
//description of our second tests
test.describe("Navigating", () => {
    //description of first test
    test("through the options should work by modifier keys", async ({page}) => {
        //function to press "Tab" on each button
        async function assertTabIsActive(btnItem: string) {
            //locate button and press "Tab"
            await page.locator("#button-" + btnItem).press('Tab');
        //end of function
        }
        //function to press "Shift+Tab" on each button
        async function assertShiftTabIsActive(btnItem: string) {
            //locate button and press "Shift+Tab"
            await page.locator("#button-" + btnItem).press('Shift+Tab');
        //end of function
        }
        //function to locate each button and verify if it is focused
        async function assertBtnItemIsActive(btnItem: string) {
            //locate button and check if focused
            await expect(page.locator("#button-" + btnItem)).toBeFocused();
        //end of function
        }
        //verify if button-stay is focused
        await assertBtnItemIsActive("stay");

        // Perform many "Shift"s

        //press "Shift" on button-stay
        await assertTabIsActive("stay");
        //verify if button-cancel is focused
        await assertBtnItemIsActive("cancel");
        //press "Shift" on button-cancel
        await assertTabIsActive("cancel");
        //verify if button-close is focused
        await assertBtnItemIsActive("close");
        //press "Shift" on button-close
        await assertTabIsActive("close");
        //verify if button-stay is focused
        await assertBtnItemIsActive("stay");

        // Perform many "Shift+Tab"s

        //press "Shift+Tab" on button-stay
        await assertShiftTabIsActive("stay");
        //verify if button-close is focused
        await assertBtnItemIsActive("close");
        //press "Shift+Tab" on button-close
        await assertShiftTabIsActive("close");
        //verify if button-cancel is focused
        await assertBtnItemIsActive("cancel");
        //press "Shift+Tab" on button-cancel
        await assertShiftTabIsActive("cancel");
        //verify if button-stay is focused
        await assertBtnItemIsActive("stay");
    //end of first test
    });
    //description of second test
    test("through mixed actions of modifier keys", async ({page}) => {
        //function to press "Tab" on each button
        async function assertTabIsActive(btnItem: string) {
            //locate button and press "Tab"
            await page.locator("#button-" + btnItem).press('Tab');
        //end of function
        }
        //function to press "Shift+Tab" on each button
        async function assertShiftTabIsActive(btnItem: string) {
            //locate button and press "Shift+Tab"
            await page.locator("#button-" + btnItem).press('Shift+Tab');
        //end of function
        }
        //function to locate each button and verify if it is focused
        async function assertBtnItemIsActive(btnItem: string) {
            //locate button and check if focused
            await expect(page.locator("#button-" + btnItem)).toBeFocused();
        //end of function
        }
        //verify if button-stay is focused
        await assertBtnItemIsActive("stay");

        // Perform 2 "Shift"s, 1 "Shift+Tab" and 2 "Shift"s

        //press "Tab" on button-stay
        await assertTabIsActive("stay");
        //verify if button-cancel is focused
        await assertBtnItemIsActive("cancel");
        //press "Tab" on button-cancel
        await assertTabIsActive("cancel");
        //verify if button-close is focused
        await assertBtnItemIsActive("close");
        //press "Shift+Tab" on button-close
        await assertShiftTabIsActive("close");
        //verify if button-cancel is focused
        await assertBtnItemIsActive("cancel");
        //press "Tab" on button-cancel
        await assertTabIsActive("cancel");
        //verify if button-close is focused
        await assertBtnItemIsActive("close");
        //press "Tab" on button-close
        await assertTabIsActive("close");
        //verify if button-stay is focused
        await assertBtnItemIsActive("stay");

        // Perform 2 "Shift+Tab"s and 2 "Shift"s

        //press "Shift+Tab" on button-stay
        await assertShiftTabIsActive("stay");
        //verify if button-close is focused
        await assertBtnItemIsActive("close");
        //press "Shift+Tab" on button-close
        await assertShiftTabIsActive("close");
        //verify if button-cancel is focused
        await assertBtnItemIsActive("cancel");
        //press "Tab" on button-cancel
        await assertTabIsActive("cancel");
        //verify if button-close is focused
        await assertBtnItemIsActive("close");
        //press "Tab" on button-close
        await assertTabIsActive("close");
        //verify if button-stay is focused
        await assertBtnItemIsActive("stay");
    //end of second test
    });
//end of our second tests
});