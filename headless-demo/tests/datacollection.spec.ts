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
     await page.goto("https://next.fritz2.dev/headless-demo/#dataCollection");
    //await page.goto("file:///C:/Users/bfong/Downloads/distributions/distributions/index.html#dataCollection");
//end of before hooks
});
//description of our first tests
test.describe('To select', () => {
    //description of the first test
    test('unique element through click', async ({page}) => {
        //locator of for some names from the table
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = page.locator('text=Hansgeorg Kranz').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = page.locator('text=Sigrun Kensy B.Eng.').textContent();
        const nameDiv = page.locator('id=Carl Zänker');
        const nameDivEx = page.locator('text=Carl Zänker');
        const name = page.locator('text=Carl Zänker').textContent();
        const result = page.locator('#result');
        //focus on switch
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([await selectedText1, await selectedText2])
        //verify if the switch is off
        //await nameDiv.dispatchEvent('mouseover')
        const tr = await nameDiv.locator('text=Carl Zänker' ,{hasText: "Carl Zänker"})
        await tr.hover()
        const namebox = await tr.boundingBox()
        await page.mouse.move(namebox.x + namebox.width / 2, namebox.y + namebox.height / 2, {steps: 5});
        //click on switch
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "false")
        //verify if the switch is on
        await nameDiv.dispatchEvent('click')
        //await page.mouse.click(namebox.x + namebox.width / 2, namebox.y + namebox.height / 2);
        //click again on switch
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText([await selectedText1, await selectedText2, await name])
    //end of first test
    });
    //description of the first test
    test('several elements through click', async ({page}) => {
        //locator of for some names from the table
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = page.locator('text=Hansgeorg Kranz').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = page.locator('text=Sigrun Kensy B.Eng.').textContent();
        const nameDiv1 = page.locator('id=Carl Zänker');
        const nameDivEx1 = page.locator('text=Carl Zänker');
        const name1 = page.locator('text=Carl Zänker').textContent();
        const nameDiv2 = page.locator('id=Salih Ernst B.A.');
        const nameDivEx2 = page.locator('text=Salih Ernst B.A.');
        const name2 = page.locator('text=Salih Ernst B.A.').textContent();
        const nameDiv3 = page.locator('id=Orhan Schacht-Gröttner');
        const nameDivEx3 = page.locator('text=Orhan Schacht-Gröttner');
        const name3 = page.locator('text=Orhan Schacht-Gröttner').textContent();
        
        const result = page.locator('#result');
        //first assertions
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([await selectedText1, await selectedText2])
        //verify if the switch is off
        await nameDiv1.first()
        await nameDiv1.hover()
        const namebox1 = await nameDivEx1.boundingBox()
        await page.mouse.move(namebox1.x + namebox1.width / 2, namebox1.y + namebox1.height / 2, {steps: 5});
        // //click on switch
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "false")
        //verify if the switch is on
        await nameDiv1.dispatchEvent('click')
        //click again on switch
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText([await selectedText1, await selectedText2, await name1])
        //verify if the switch is off
        await nameDiv2.first()
        await nameDiv2.hover()
        const namebox2 = await nameDivEx2.boundingBox()
        await page.mouse.move(namebox2.x + namebox2.width / 2, namebox2.y + namebox2.height / 2, {steps: 5});
        //click on switch
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "false")
        //verify if the switch is on
        await nameDiv2.dispatchEvent('click')
        //click again on switch
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText([await selectedText1, await selectedText2, await name1, await name2])
        //verify if the switch is off
        await nameDiv3.first()
        await nameDiv3.hover()
        const namebox3 = await nameDivEx3.boundingBox()
        await page.mouse.move(namebox3.x + namebox3.width / 2, namebox3.y + namebox3.height / 2, {steps: 5});
        //click on switch
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv3).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv3).toHaveAttribute("data-datatable-selected", "false")
        //verify if the switch is on
        await nameDiv3.dispatchEvent('click')
        //click again on switch
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv3).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv3).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText([await selectedText1, await selectedText2, await name1, await name2, await name3])
    //end of second test
    });
//end of our first tests
});
//description of our second tests
test.describe('Navigating', () => {
    //description of the first test
    for (const key of ["Enter", "Space"]) {
    test(`with Arrow keys through the items and selects with ${key}`, async ({page}) => {
        //locator of for some names from the table
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = page.locator('text=Hansgeorg Kranz').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = page.locator('text=Sigrun Kensy B.Eng.').textContent();
        const nameDiv1 = page.locator('id=Prof. Alberto Kraushaar B.A.');
        const name1 = page.locator('text=Prof. Alberto Kraushaar B.A.').textContent();
        const nameDiv2 = page.locator('id=Monique Riehl');
        const name2 = page.locator('text=Monique Riehl').textContent();
        const result = page.locator('#result');
        //first assertions
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([await selectedText1, await selectedText2])

        await selected2.first()
        await selected2.hover()
        const selectedBox2 = await selected2.boundingBox()
        await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2);
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "true")
        await expect(result).toContainText(await selectedText1)
        await selected2.press('ArrowDown')
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "true")
        await nameDiv1.press(key)
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "true")
        await expect(result).toContainText([await selectedText1, await name1])
        await nameDiv1.press('ArrowDown')
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "true")
        await nameDiv2.press(key)
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "true")
        await expect(result).toContainText([await selectedText1, await name1, await name2])
        await nameDiv2.press(key)
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "true")
        await expect(result).toContainText([await selectedText1, await name1])
        await nameDiv2.press('ArrowUp')
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "false")
        await nameDiv1.press(key)
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText(await selectedText1)
        await nameDiv1.press('ArrowUp')
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv1).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv1).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv2).toHaveAttribute("data-datatable-active", "false")

    });
    }

    //description of the second test
    test('through first and last element with keys', async ({page}) => {
        //locator of for some names from the table
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = page.locator('text=Hansgeorg Kranz').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = page.locator('text=Sigrun Kensy B.Eng.').textContent();
        const nameDiv = page.locator('id=Dr. Igor Steckel MBA.');
        const nameDivEx = page.locator('text=Dr. Igor Steckel MBA.');
        const name = page.locator('text=Dr. Igor Steckel MBA.').textContent();
        const result = page.locator('#result');
        //focus on switch
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([await selectedText1, await selectedText2])
        //verify if the switch is off
        //await nameDiv.dispatchEvent('mouseover')
        await selected1.first()
        await selected1.hover()
        const selectedBox1 = await selected1.boundingBox()
        await page.mouse.click(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2);
        //click on switch
        await expect(selected1).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected1).toHaveAttribute("data-datatable-active", "true")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText(await selectedText2)
        //verify if the switch is on
        await selected2.first()
        await selected2.hover()
        const selectedBox2 = await selected2.boundingBox()
        await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2);
        //click on switch
        await expect(selected1).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "true")
        await expect(result).not.toContainText(await selectedText2)
        await selected2.press('Home')
        await selected1.press('Enter')
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "true")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText(await selectedText1)
        await selected1.press('End')
        await nameDiv.press('Space')
        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(result).toContainText([await selectedText1, await name])
    //end of second test
    });
});
//description of our third tests
test.describe('To', () => {
    //description of the first test
    test('filter some elements', async ({page}) => {
        //locator of for some names from the table
        const filterField = page.locator('#dataTable-filter-field');
        const table = page.locator('#dataTable')

        await filterField.fill("Albertine")
        await page.mouse.click(0, 0)
        await expect(table).toContainText("Albertine");

        await filterField.fill("Timm")
        await filterField.fill('Enter')
        await expect(table).toContainText("Timm");
    //end of first test
    });
    //description of the second test
    test('sort some elements', async ({page}) => {
        //locator of for some names from the table
        const sortbtn = page.locator('#datatTable-sort-name');
        const table = page.locator('#dataTable')
        const tableFirst = page.locator('id=dataTable >> tbody >> tr').first()
        const tableLast = page.locator('id=dataTable >> tbody >> tr').last()

        await sortbtn.click()
        await tableFirst.highlight()
        await tableLast.highlight()
        await expect(tableFirst).toContainText("Albertine");
        await expect(tableLast).toContainText("Yasemin");

        await sortbtn.click()
        await table.first().hover()
        await expect(tableFirst).toContainText("Yasemin");
        await expect(tableLast).toContainText("Albertine");
    //end of second test
    });
//end of our third tests
});