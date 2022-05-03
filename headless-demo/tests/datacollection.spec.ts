import {expect, Locator, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - styling
 * - label and description
 * - validation
 * - checkboxGroupValidationMessages
 */

//declare here all our before hooks
test.beforeEach(async ({page}) => {
    //go to the page of CheckBoxGroup component
    // await page.goto("https://next.fritz2.dev/headless-demo/#dataCollection");
    await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#dataCollection");
//end of before hooks
});
//description of our first tests
test.describe('To select', () => {
    //description of the first test
    test('unique element through click', async ({page}) => {
        //locator of for some names from the table
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selected1Text = selected1.locator("xpath=//td[1]").textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selected2Text = selected2.locator("xpath=//td[1]").textContent();
        const name = page.locator('id=Carl Zänker');
        const nameText = name.locator("xpath=//td[1]").textContent();
        const result = page.locator('#result');
        //focus on switch
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([await selected1Text, await selected2Text])
        //verify if the switch is off
        // await name.hover();
        // //click on switch
        // await expect(name).toHaveAttribute("data-datatable-active", "true")
        // await expect(name).toHaveAttribute("data-datatable-selected", "false")
        //verify if the switch is on
        await name.click();
        //click again on switch
        await expect(name).toHaveAttribute("data-datatable-active", "true")
        await expect(name).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText([await selected1Text, await selected2Text, await nameText])
    //end of first test
    });
    //description of the first test
    test('several elements through click', async ({page}) => {
        //locator of for some names from the table
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selected1Text = selected1.locator("xpath=//td[1]").textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selected2Text = selected2.locator("xpath=//td[1]").textContent();
        const name1 = page.locator('id=Carl Zänker');
        const nameText1 = name1.locator("xpath=//td[1]").textContent();
        const name2 = page.locator('id=Salih Ernst B.A.');
        const nameText2 = name2.locator("xpath=//td[1]").textContent();
        const name3 = page.locator('id=Orhan Schacht-Gröttner');
        const nameText3 = name3.locator("xpath=//td[1]").textContent();
        
        const result = page.locator('#result');
        //first assertions
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([await selected1Text, await selected2Text])
        //verify if the switch is off
        // await name1.hover();
        // //click on switch
        // await expect(name1).toHaveAttribute("data-datatable-active", "true")
        // await expect(name1).toHaveAttribute("data-datatable-selected", "false")
        //verify if the switch is on
        await name1.click();
        //click again on switch
        await expect(name1).toHaveAttribute("data-datatable-active", "true")
        await expect(name1).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText([await selected1Text, await selected2Text, await nameText1])
        //verify if the switch is off
        // await name2.hover();
        // //click on switch
        // await expect(name1).toHaveAttribute("data-datatable-active", "false")
        // await expect(name1).toHaveAttribute("data-datatable-selected", "true")
        // await expect(name2).toHaveAttribute("data-datatable-active", "true")
        // await expect(name2).toHaveAttribute("data-datatable-selected", "false")
        //verify if the switch is on
        await name2.click();
        //click again on switch
        await expect(name1).toHaveAttribute("data-datatable-active", "false")
        await expect(name1).toHaveAttribute("data-datatable-selected", "true")
        await expect(name2).toHaveAttribute("data-datatable-active", "true")
        await expect(name2).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText([await selected1Text, await selected2Text, await nameText1, await nameText2])
        //verify if the switch is off
        // await name3.hover();
        // //click on switch
        // await expect(name1).toHaveAttribute("data-datatable-active", "false")
        // await expect(name1).toHaveAttribute("data-datatable-selected", "true")
        // await expect(name2).toHaveAttribute("data-datatable-active", "false")
        // await expect(name2).toHaveAttribute("data-datatable-selected", "true")
        // await expect(name3).toHaveAttribute("data-datatable-active", "true")
        // await expect(name3).toHaveAttribute("data-datatable-selected", "false")
        //verify if the switch is on
        await name3.click();
        //click again on switch
        await expect(name1).toHaveAttribute("data-datatable-active", "false")
        await expect(name1).toHaveAttribute("data-datatable-selected", "true")
        await expect(name2).toHaveAttribute("data-datatable-active", "false")
        await expect(name2).toHaveAttribute("data-datatable-selected", "true")
        await expect(name3).toHaveAttribute("data-datatable-active", "true")
        await expect(name3).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText([await selected1Text, await selected2Text, await nameText1, await nameText2, await nameText3])
    //end of first test
    });
});