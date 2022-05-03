import {expect, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - label
 * - validation
 * - deactivation
 * - textareaValidationMessages
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of Text Area component
    // await page.goto("https://next.fritz2.dev/headless-demo/#textarea");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#textarea");
//end of before hooks
});
//description of our tests
test.describe('To check the', () => {
        //description of the first test
        test('fill of textarea when Tab is pressed', async ({page}) => {
            //locator of textArea (tag: textArea)
            const field = page.locator('textarea');
            //variable for some text to fill into textArea
            const text = "Some new text to write";
            //focus on textArea
            await field.focus();
            //verify is textArea is focused
            await expect(field).toBeFocused();
            //fill the text "text" into textArea
            await field.fill(text);
            //verify is textArea is still focused
            await expect(field).toBeFocused();
            //press "Tab" button
            await field.press('Tab');
            //locator for new text in result
            const fieldfill = await page.locator('#result');
            //verify if result contains the text "text"
            expect(fieldfill).toContainText(text);
        //end of first test
        });
        //description of the second test
        test('fill of textarea when Enter and then Tab is pressed', async ({page}) => {
            //locator of textArea (tag: textArea)
            const field = page.locator('textarea');
            //variable for some text to fill into textArea
            const text = "Some new text to write";
            //another variable for some text to fill into textArea
            const tNext = "A text for this area";
            //focus on textArea
            await field.focus();
            //verify is textArea is focused
            await expect(field).toBeFocused();
            //fill the text "text" into textArea
            await field.fill(text);
            //verify is textArea is still focused
            await expect(field).toBeFocused();
            //press "Enter" button
            await field.press('Enter');
            //fill the text "tNext" into textArea
            await field.type(tNext);
            //verify is textArea is still focused
            await expect(field).toBeFocused();
            //press "Tab" button
            await field.press('Tab');
            //locator for new text in result
            const fieldfill = page.locator('#result');
            //verify if result contains the text "text" and "tNext"
            expect(fieldfill).toContainText([text, tNext]);
        //end of second test
        });
        //description of the third test
        test(`fill of input when textareaLabel is clicked and mouse is clicked away from textarea`, async ({page}) => {
            //locator of textArea
            const field = page.locator('textarea');
            //locator of textareaLabel (tag: textareaLabel)
            const label = page.locator('#textArea-label');
            //variable for some text to fill into textArea
            const text = "Some new text to write";
            //click on textarealabel
            await label.click();
            //verify is textArea is focused
            await expect(field).toBeFocused();
            //fill the text "text" into textArea
            await field.fill(text);
            //verify is textArea is still focused
            await expect(field).toBeFocused();
            //click out of textArea
            await page.mouse.click(0, 0);
            //locator for new text in result
            const fieldfill = page.locator('#result');
            //verify if result contains the text "text"
            expect(fieldfill).toContainText(text);
        });

});