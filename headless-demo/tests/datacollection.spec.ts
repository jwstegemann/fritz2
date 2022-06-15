import {expect, Page, test} from '@playwright/test';
/*
 * Missing tests due to limited example component:
 * - styling
 */

test.beforeEach(async ({page}) => {
    /* go to the page of CheckBoxGroup component */
    await page.goto("#dataCollection");

});

/* 
 * TESTS FOR DATATABLE 
 */
test.describe('To select', () => {

    /**
     * Verify the first state of dataTable on page load
     */
    async function dataTableState(page: Page) {
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = await selected1.locator('xpath=//td[1]').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = await selected2.locator('xpath=//td[1]').textContent();
        const result = page.locator('#result');

        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([selectedText1, selectedText2])
    }

    async function nameMouseOver(name: String, page: Page) {

        const nameDiv = page.locator("id=" + name)

        /* At some places we need some delays to perform tests on all browsers and devices */
        await page.mouse.click(0, 0, {delay: 2000})
        await nameDiv.scrollIntoViewIfNeeded()

        const namebox = await nameDiv.boundingBox()
        /* Steps for move are added here to make intermediate moveevent */
        await page.mouse.move(namebox.x + namebox.width / 2, namebox.y + namebox.height / 2, {steps: 5});
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "false")
    }

    async function nameSelect(name: string, page: Page) {

        const result = page.locator('#result');
        const nameDiv = page.locator("id=" + name)

        /* Got some issues with webkit for some tests, need another delay */
        await nameDiv.click({delay: 2000})
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
        const result = page.locator('#result');

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

    async function nameSelect(name: string, page: Page) {

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
            await page.mouse.click(0, 0, {delay: 2000})

            await page.mouse.move(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {steps: 5});
            await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {delay: 2000});

            /* We will place delay on all press actions (ArrowUp, ArrowDown, Enter, Space) */
            await selected2.press('ArrowDown', {delay: 2000})
            await nameActive("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press(key, {delay: 2000})
            await nameSelect("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press('ArrowDown', {delay: 2000})
            await nameActive("Monique Riehl", page)

            await nameDiv2.press(key, {delay: 2000})
            await nameSelect("Monique Riehl", page)

            await nameDiv2.press(key, {delay: 2000})
            await nameActive("Monique Riehl", page)

            await nameDiv2.press('ArrowUp', {delay: 2000})
            await nameSelect("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press(key, {delay: 2000})
            await nameActive("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press('ArrowUp', {delay: 2000})
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
        await page.mouse.click(0, 0, {delay: 2000})

        await page.mouse.move(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2, {steps: 5});
        await page.mouse.click(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2, {delay: 2500});

        await page.mouse.move(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {steps: 5});
        await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {delay: 2500});

        await selected2.press('Home', {delay: 2000})
        await nameActive("Hansgeorg Kranz", page)

        await selected1.press('Enter', {delay: 2000})
        await nameSelect("Hansgeorg Kranz", page)

        await selected1.press('End', {delay: 2000})
        await nameActive("Dr. Igor Steckel MBA.", page)

        await nameDiv.press('Enter', {delay: 2000})
        await nameSelect("Dr. Igor Steckel MBA.", page)

    });

});

test.describe('To', () => {

    for (const data of [
        {expression: "Kranz", filtered: 5, selected: 2},
        {expression: "Timm", filtered: 1, selected: 2}
    ]) {
        test(`filter some elements of Datatable type "${data.expression}" into filter inputfield`, async ({page}) => {

            const filterField = page.locator('#dataTable-filter-field');

            await filterField.fill(data.expression)
            await page.mouse.click(0, 0, {delay: 4000})

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
        const tableFirst = table.locator('xpath=//tr').first()
        const tableSecond = table.locator('xpath=//tr').nth(1)
        const tableLast = table.locator('xpath=//tr').last()

        /* verify a-z order */
        await sortbtn.click({delay: 4000})
        await expect(tableFirst).toContainText("Albertine");
        await expect(tableLast).toContainText("Yasemin");

        /* verify z-a order */
        await sortbtn.click({delay: 4000})
        await expect(tableFirst).toContainText("Yasemin");
        await expect(tableLast).toContainText("Albertine");

        /* verify original order */
        await sortbtn.click({delay: 4000})
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
        const currentRowBox = await currentRow.boundingBox()
        expect(currentRowBox.y).toBeLessThanOrEqual(tableBox.y + tableBox.height / 2)
        expect(currentRowBox.y + currentRowBox.height).toBeGreaterThanOrEqual(tableBox.y + tableBox.height / 2)

    });

});

/* 
 * TESTS FOR GRIDLIST 
 */
test.describe('To select', () => {

    async function dataTableState(page: Page) {
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = await selected1.locator('xpath=//h3[1]').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = await selected2.locator('xpath=//h3[1]').textContent();
        const result = page.locator('#result');

        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([selectedText1, selectedText2])
    }

    async function nameMouseOver(name: String, page: Page) {

        const nameDiv = page.locator("id=" + name)

        await page.mouse.click(0, 0, {delay: 2000})
        await nameDiv.scrollIntoViewIfNeeded()

        const namebox = await nameDiv.boundingBox()
        await page.mouse.move(namebox.x + namebox.width / 2, namebox.y + namebox.height / 2, {steps: 5});
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "false")
    }

    async function nameSelect(name: string, page: Page) {

        const result = page.locator('#result');
        const nameDiv = page.locator("id=" + name)

        await nameDiv.click({delay: 2000})
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "true")
        await expect(result).toContainText(name)
    }

    test('unique element of Gridlist through click', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        await page.locator('#tabGroup-tab-list-tab-1').click();

        await dataTableState(page)
        await nameMouseOver("Carl Zänker", page)

        await nameSelect("Carl Zänker", page)

    });

    test('several elements of Gridlist through click', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        await page.locator('#tabGroup-tab-list-tab-1').click();

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
        const selectedText1 = await selected1.locator('xpath=//h3[1]').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = await selected2.locator('xpath=//h3[1]').textContent();
        const result = page.locator('#result');

        /* Delays for GridList are more important because the page needs too much time to load */
        await page.mouse.click(0, 0, {delay: 5000})

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

    async function nameSelect(name: string, page: Page) {

        const result = page.locator('#result');
        const nameDiv = page.locator("id=" + name)

        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(result).toContainText(name)
    }

    for (const key of ["Enter", "Space"]) {

        /**
         * This test is not working properly on (mobile) Safari and Firefox.
         * The second name is not clicked as expected and takes too long. Need some improvements.
         */
        test(`with Arrow keys through the items of Gridlist and selects with ${key}`, async ({page, browserName}) => {

            test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');
            test.slow(browserName === 'firefox', 'This test for DataCollection is slow on Firefox');

            await page.locator('#tabGroup-tab-list-tab-1').click();

            const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
            const selectedBox2 = await selected2.boundingBox()
            const nameDiv1 = page.locator('id=Prof. Alberto Kraushaar B.A.');
            const nameDiv2 = page.locator('id=Monique Riehl');

            await dataTableState(page)

            await page.mouse.click(0, 0, {delay: 3000})

            await page.mouse.move(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {steps: 5});
            await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {delay: 3000});

            await selected2.press('ArrowDown', {delay: 3000})
            await nameActive("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press(key, {delay: 3000})
            await nameSelect("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press('ArrowDown', {delay: 3000})
            await nameActive("Monique Riehl", page)

            await nameDiv2.press(key, {delay: 3000})
            await nameSelect("Monique Riehl", page)

            await nameDiv2.press(key, {delay: 3000})
            await nameActive("Monique Riehl", page)

            await nameDiv2.press('ArrowUp', {delay: 3000})
            await nameSelect("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press(key, {delay: 3000})
            await nameActive("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press('ArrowUp', {delay: 3000})
            await expect(selected2).toHaveAttribute("data-datatable-selected", "false")
            await expect(selected2).toHaveAttribute("data-datatable-active", "true")

        });

    }

    test('through first and last element of Gridlist with keys', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');
        test.slow(browserName === 'firefox', 'This test for DataCollection is slow on Firefox');

        await page.locator('#tabGroup-tab-list-tab-1').click();

        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const nameDiv = page.locator('id=Dr. Igor Steckel MBA.');

        await dataTableState(page)

        const selectedBox1 = await selected1.boundingBox()
        await page.mouse.move(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2, {steps: 5});
        await page.mouse.click(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2, {delay: 2000});

        const selectedBox2 = await selected2.boundingBox()
        await page.mouse.move(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {steps: 5});
        await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {delay: 2000});

        await selected2.press('Home', {delay: 2000})
        await nameActive("Hansgeorg Kranz", page)

        await selected1.press('Enter', {delay: 2000})
        await nameSelect("Hansgeorg Kranz", page)

        await selected1.press('End', {delay: 2000})
        await selected1.press('End', {delay: 2000})
        await nameActive("Dr. Igor Steckel MBA.", page)

        await nameDiv.press('Enter')
        await nameSelect("Dr. Igor Steckel MBA.", page)

    });

});

test.describe('To', () => {

    for (const data of [
        {expression: "Kranz", filtered: 4, selected: 2},
        {expression: "Timm", filtered: 1, selected: 2}
    ]) {
        test(`filter some elements of Gridlist type "${data.expression}" into filter inputfield`, async ({page}) => {

            await page.locator('#tabGroup-tab-list-tab-1').click();
            await page.mouse.click(0, 0, {delay: 5000})

            const filterField = page.locator('#gridList-filter-field');
            await filterField.fill(data.expression)
            await page.mouse.click(0, 0, {delay: 4000})

            const filteredRows = await page.locator("id=gridList >> li").count()
            const selectedRows = parseInt(await page.locator("id=result").getAttribute("data-selected-count"));
            expect(filteredRows).toEqual(data.filtered);
            expect(selectedRows).toEqual(data.selected);
        })
    }

    test('sort some elements of Gridlist', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        await page.locator('#tabGroup-tab-list-tab-1').click();

        await page.mouse.click(0, 0, {delay: 5000})

        const sortbtn = page.locator('#gridList-sort-name');
        const table = page.locator('id=gridList >> ul')
        const tableFirst = table.locator('xpath=//li').first()
        const tableSecond = table.locator('xpath=//li').nth(1)
        const tableLast = table.locator('xpath=//li').last()

        await sortbtn.click({delay: 5000})
        await expect(tableFirst).toContainText("Albertine");
        await expect(tableLast).toContainText("Yasemin");

        await sortbtn.click({delay: 5000})
        await expect(tableFirst).toContainText("Yasemin");
        await expect(tableLast).toContainText("Albertine");

        await sortbtn.click({delay: 5000})
        await expect(tableFirst).toHaveAttribute("data-datatable-selected", "true")
        await expect(tableFirst).toHaveAttribute("data-datatable-active", "false")
        await expect(tableSecond).toHaveAttribute("data-datatable-selected", "true")
        await expect(tableFirst).toHaveAttribute("data-datatable-active", "false")

    });

    test('check if scrollIntoView() of Gridlist is respected', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        await page.locator('#tabGroup-tab-list-tab-1').click();

        await page.mouse.click(0, 0, {delay: 5000})

        const selected = page.locator('id=Caterina Scholtz');
        const nameDiv1 = page.locator('id=Isabelle Fliegner-Köhler');
        const table = page.locator('#gridList')

        await selected.click({delay: 5000})
        await selected.press('ArrowDown', {delay: 5000})

        /* We will compare the height of the table with the height of bottom of card name 
         *
         * Also consider that only the "scrollDown" is tested and not the "scrollUp"
         * Need test for "scrollUp"
         */
        const tableBox = await table.boundingBox()
        const namebox1 = await nameDiv1.boundingBox()
        expect(tableBox.y + tableBox.height).toBeCloseTo(namebox1.y + namebox1.height)

    });

});