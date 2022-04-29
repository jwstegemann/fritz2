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
    //for loop for 3 buttons that will be clicked respectively
    for (const btn of ["stay", "cancel", "close"]) {
        //description of first test
        test(`by clicking on button-"${btn}" will leave the modal or not`, async ({page}) => {
            //locator for each modal button
            const button = page.locator(`#button-${btn}`);
            //focus on button
            await button.focus();
            //click on button
            await button.click();
            //condition for button-stay
            if(btn[0]){
                //verify if the modal overlay (tag: modalOverlay) is still visible
                await expect(button).toBeVisible();
            //condition for button-cancel and button-close
            } else {
                //verify if modalOverlay is closed
                await expect(button).toBeHidden();
            //end of condition
            }
        //end of first test
        });
    //end of for loop
    }
    //for loop for 3 buttons that will be clicked respectively
    for (const btn of ["stay","cancel", "close"]) {
        //description of second test
        test(`by clicking on button-"${btn}" and clicking again on Open will reopen the modal`, async ({page}) => {
            //locator for each modal button
            const button = page.locator(`#button-${btn}`);
            //focus on button
            await button.focus();
            //click on button
            await button.click();
            //condition for button-stay
            if (btn[0]) {
            //press "Tab" on button-stay
            await button.press('Tab');
            //condition for button-cancel and button-close
            } else {
            //click again on open
            await page.locator('text=Open').click();
            }
        //end of second test
        });
    //end of for loop
    }
    //for loop for 3 buttons that will be pressed respectively
    for (const btn of ["stay","cancel", "close"]) {
        //description of third test
        test(`by pressing Enter on button-"${btn}" will leave the modal or not`, async ({page}) => {
            //locator for each modal button
            const button = page.locator(`#button-${btn}`);
            //press "Enter" on button
            await button.press('Enter');
            //condition for button-stay
            if(btn[0]) {
            //verify if button is visible
            await expect(button).toBeVisible();
            //condition for button-cancel and button-close
            } else {
            //verify if button is hidden
            await expect(button).not.toBeVisible();
            }
        //end of third test
        });
    //end of for loop
    }
    //for loop for 3 buttons that will be pressed respectively
    for (const btn of ["stay","cancel", "close"]) {
        //description of fourth test
        test(`by pressing Enter on button-"${btn}" and pressing again Enter on Open will reopen the modal`, async ({page}) => {
            //locator for each modal button
            const button = page.locator(`#button-${btn}`);
            //focus on button
            await button.focus();
            //press "Enter" on button
            await button.press('Enter');
            //condition for button-stay
            if (btn[0]) {
            await button.press('Enter');
            //condition for button-cancel and button-close
            } else {
            await page.locator('text=Open').press('Enter');
            }
        //end of fourth test
        });
    //end of for loop
    }
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