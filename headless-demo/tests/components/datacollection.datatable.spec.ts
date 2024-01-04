import {expect, Page, test} from '@playwright/test';
/*
 * Missing tests due to limited example component:
 * - styling
 */

test.beforeEach(async ({page}) => {
    /* go to the page of CheckBoxGroup component */
    await page.goto("#dataCollection");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});

test.describe('To select', () => {

    /**
     * Verify the first state of dataTable on page load
     */
    async function dataTableState(page: Page) {
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = await selected1.locator('xpath=//td[1]').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = await selected2.locator('xpath=//td[1]').textContent();
        const result = page.locator('#result li');

        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([selectedText1, selectedText2])
    }

    async function nameMouseOver(name: String, page: Page) {

        const nameDiv = page.locator("id=" + name)

        /* At some places we need some delays to perform tests on all browsers and devices */
        await page.mouse.click(0, 0, {delay: 0})
        await nameDiv.scrollIntoViewIfNeeded()

        const namebox = await nameDiv.boundingBox()
        /* Steps for move are added here to make intermediate moveevent */
        await page.mouse.move(namebox.x + namebox.width / 2, namebox.y + namebox.height / 2, {steps: 100});
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "false")
    }

    async function nameSelect(name: string, page: Page) {

        const result = page.locator('#result');
        const nameDiv = page.locator("id=" + name)

        /* Got some issues with webkit for some tests, need another delay */
        await nameDiv.click({delay: 0})
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText(name)
    }

    test('unique element of Datatable through click', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        await dataTableState(page)
        await nameMouseOver("Carl Zänker", page)

        await nameSelect("Carl Zänker", page)

    });

    test('several elements of Datatable through click', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        await dataTableState(page)

        await nameMouseOver("Carl Zänker", page)
        await nameSelect("Carl Zänker", page)

        await nameMouseOver("Salih Ernst B.A.", page)
        await nameSelect("Salih Ernst B.A.", page)

        await nameMouseOver("Orhan Schacht-Gröttner", page)
        await nameSelect("Orhan Schacht-Gröttner", page)

    });

});

test.describe('Navigating', () => {

    async function dataTableState(page: Page) {
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = await selected1.locator('xpath=//td[1]').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = await selected2.locator('xpath=//td[1]').textContent();
        const result = page.locator('#result li');

        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([selectedText1, selectedText2])
    }

    async function nameActive(name: String, page: Page) {

        const nameDiv = page.locator("id=" + name)

        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "false")
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")

    }

    async function nameSelected(name: string, page: Page) {

        const result = page.locator('#result');
        const nameDiv = page.locator("id=" + name)

        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(result).toContainText(name)
    }

    for (const key of ["Enter", "Space"]) {

        test(`with Arrow keys through the items of Datatable and selects with ${key}`, async ({page, browserName}) => {

            test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

            const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
            const selectedBox2 = await selected2.boundingBox()
            const nameDiv1 = page.locator('id=Prof. Alberto Kraushaar B.A.');
            const nameDiv2 = page.locator('id=Monique Riehl');

            await dataTableState(page)

            /* This test is slow on webkit here a delay to wait for this click*/
            await page.mouse.click(0, 0)

            await page.mouse.move(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2);
            await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2);

            /* We will place delay on all press actions (ArrowUp, ArrowDown, Enter, Space) */
            await selected2.press('ArrowDown')
            await nameActive("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press(key)
            await nameSelected("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press('ArrowDown')
            await nameActive("Monique Riehl", page)

            await nameDiv2.press(key)
            await nameSelected("Monique Riehl", page)

            await nameDiv2.press(key)
            await nameActive("Monique Riehl", page)

            await nameDiv2.press('ArrowUp')
            await nameSelected("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press(key)
            await nameActive("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press('ArrowUp')
            await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
            await expect(selected2).toHaveAttribute("data-datatable-active", "true")

        });

    }

    test('through first and last element of Datatable with keys', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedBox1 = await selected1.boundingBox()
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedBox2 = await selected2.boundingBox()
        const nameDiv = page.locator('id=Dr. Igor Steckel MBA.');

        await dataTableState(page)

        /* Same delays as previous test */
        await page.mouse.click(0, 0)

        await page.mouse.move(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2, {steps: 100});
        await page.mouse.click(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2);

        await page.mouse.move(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {steps: 100});
        await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2);

        await selected2.press('Home')
        await nameActive("Hansgeorg Kranz", page)

        await selected1.press('Enter')
        await nameSelected("Hansgeorg Kranz", page)

        await selected1.press('End')
        await nameActive("Dr. Igor Steckel MBA.", page)

        await nameDiv.press('Enter')
        await nameSelected("Dr. Igor Steckel MBA.", page)

    });

});

test.describe('To', () => {

    for (const data of [
        {expression: "Kranz", filtered: 5, selected: 1},
        {expression: "Timm", filtered: 1, selected: 0}
    ]) {
        test(`filter some elements of Datatable type "${data.expression}" into filter inputfield`, async ({page}) => {

            const filterField = page.locator('#dataTable-filter-field');

            await filterField.fill(data.expression)
            await filterField.press("Enter");
            await page.waitForTimeout(5000)

            const filteredRows = await page.locator('xpath=//tbody//tr').count()
            const selectedRows = parseInt(await page.locator("id=result").getAttribute("data-selected-count"));
            expect(filteredRows).toEqual(data.filtered);
            expect(selectedRows).toEqual(data.selected);
        })
    }

    test('sort some elements of Datatable', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        const sortbtn = page.locator('#dataTable-sort-name');
        const table = page.locator('id=dataTable >> tbody')
        const tableFirst = table.locator('xpath=.//tr').first()
        const tableSecond = table.locator('xpath=.//tr').nth(1)
        const tableLast = table.locator('xpath=.//tr').last()

        /* verify a-z order */
        await sortbtn.click()
        await page.waitForTimeout(5000);
        await expect(tableFirst).toContainText("Albertine");
        await expect(tableLast).toContainText("Yasemin");

        /* verify z-a order */
        await sortbtn.click()
        await page.waitForTimeout(5000);
        await expect(tableFirst).toContainText("Yasemin");
        await expect(tableLast).toContainText("Albertine");

        /* verify original order */
        await sortbtn.click()
        await page.waitForTimeout(5000);
        await expect(tableFirst).toHaveAttribute("data-datatable-selected", "true")
        await expect(tableFirst).toHaveAttribute("data-datatable-active", "false")
        await expect(tableSecond).toHaveAttribute("data-datatable-selected", "true")
        await expect(tableFirst).toHaveAttribute("data-datatable-active", "false")
    });

    /**
     * This test is not working properly on (mobile) Safari and Firefox.
     * The table is not centered as expected on these browsers. Need some improvements.
     */
    test('check if scrollIntoView() of Datatable is respected', async ({page}) => {

        const startingRow = page.locator('id=Caterina Scholtz');
        const currentRow = page.locator('id=Cathrin Schomber B.A.');
        const table = page.locator('#dataTable')

        const selectedBox = await startingRow.boundingBox()
        await page.mouse.move(selectedBox.x + 10, selectedBox.y + 10, {steps: 100})
        await expect(startingRow).toHaveAttribute("data-datatable-active", "true")
        await page.mouse.click(selectedBox.x + 10, selectedBox.y + 10, {delay: 200})
        await expect(startingRow).toHaveAttribute('data-datatable-selected', 'true')

        for (let i = 0; i < 4; i++) {
            await page.keyboard.press("ArrowDown", {delay: 1000})
        }

        /* 
         * We will compare the height of center of table with the height of name 
         * 
         * Also consider that only the "scrollDown" is tested and not the "scrollUp"
         * Need test for "scrollUp"
         */
        const tableBox = await table.boundingBox()
        await expect(currentRow).toHaveAttribute("data-datatable-active", "true");
        const currentRowBox = await currentRow.boundingBox()
        expect(currentRowBox.y).toBeLessThanOrEqual(tableBox.y + tableBox.height / 2)
        expect(currentRowBox.y + currentRowBox.height).toBeGreaterThanOrEqual(tableBox.y + tableBox.height / 2)

    });

});
