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

    async function dataTableState(page: Page) {
        const selected1 = page.locator('id=Hansgeorg Kranz');
        const selectedText1 = await selected1.locator('xpath=//h3[1]').textContent();
        const selected2 = page.locator('id=Sigrun Kensy B.Eng.');
        const selectedText2 = await selected2.locator('xpath=//h3[1]').textContent();
        const result = page.locator('#result li');

        await expect(selected1).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected1).toHaveAttribute("data-datatable-active", "false")
        await expect(selected2).toHaveAttribute("data-datatable-selected", "true")
        await expect(selected2).toHaveAttribute("data-datatable-active", "false")
        await expect(result).toContainText([selectedText1, selectedText2])
    }

    async function nameMouseOver(name: String, page: Page) {

        const nameDiv = page.locator("id=" + name)

        await page.mouse.click(0, 0, {delay: 0})
        await nameDiv.scrollIntoViewIfNeeded()

        const namebox = await nameDiv.boundingBox()
        await page.mouse.move(namebox.x + namebox.width / 2, namebox.y + namebox.height / 2, {steps: 100});
        await expect(nameDiv).toHaveAttribute("data-datatable-active", "true")
        await expect(nameDiv).toHaveAttribute("data-datatable-selected", "false")
    }

    async function nameSelect(name: string, page: Page) {

        const result = page.locator('#result');
        const nameDiv = page.locator("id=" + name)

        await nameDiv.click({delay: 0})
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
        const result = page.locator('#result li');

        /* Delays for GridList are more important because the page needs too much time to load */
        await page.mouse.click(0, 0, {delay: 2000})

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
            const nameDiv1 = page.locator('id=Prof. Alberto Kraushaar B.A.');
            const nameDiv2 = page.locator('id=Monique Riehl');
            await dataTableState(page)

            await page.mouse.click(0, 0)

            const selectedBox2 = await selected2.boundingBox()
            await page.mouse.move(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {steps: 100});
            await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2);

            await selected2.press('ArrowDown')
            await nameActive("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press(key)
            await nameSelect("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press('ArrowDown')
            await nameActive("Monique Riehl", page)

            await nameDiv2.press(key)
            await nameSelect("Monique Riehl", page)

            await nameDiv2.press(key)
            await nameActive("Monique Riehl", page)

            await nameDiv2.press('ArrowUp')
            await nameSelect("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press(key)
            await nameActive("Prof. Alberto Kraushaar B.A.", page)

            await nameDiv1.press('ArrowUp')
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
        await page.mouse.move(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2, {steps: 100});
        await page.mouse.click(selectedBox1.x + selectedBox1.width / 2, selectedBox1.y + selectedBox1.height / 2, {delay: 1000});

        const selectedBox2 = await selected2.boundingBox()
        await page.mouse.move(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {steps: 100});
        await page.mouse.click(selectedBox2.x + selectedBox2.width / 2, selectedBox2.y + selectedBox2.height / 2, {delay: 1000});

        await selected2.press('Home', {delay: 1000})
        await nameActive("Hansgeorg Kranz", page)

        await selected1.press('Enter', {delay: 1000})
        await nameSelect("Hansgeorg Kranz", page)

        await selected1.press('End', {delay: 1000})
        await selected1.press('End', {delay: 1000})
        await nameActive("Dr. Igor Steckel MBA.", page)

        await nameDiv.press('Enter')
        await nameSelect("Dr. Igor Steckel MBA.", page)

    });

});

test.describe('To', () => {

    for (const data of [
        {expression: "Kranz", filtered: 4, selected: 1},
        {expression: "Timm", filtered: 1, selected: 0}
    ]) {
        test(`filter some elements of Gridlist type "${data.expression}" into filter inputfield`, async ({page}) => {

            await page.locator('#tabGroup-tab-list-tab-1').click();
            await page.mouse.click(0, 0, {delay: 2000})

            const filterField = page.locator('#gridList-filter-field');
            await filterField.fill(data.expression)
            await filterField.press("Enter");
            await page.waitForTimeout(4000)

            const filteredRows = await page.locator("id=gridList >> li").count()
            const selectedRows = parseInt(await page.locator("id=result").getAttribute("data-selected-count"));
            expect(filteredRows).toEqual(data.filtered);
            expect(selectedRows).toEqual(data.selected);
        })
    }

    test('sort some elements of Gridlist', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        await page.locator('#tabGroup-tab-list-tab-1').click();

        await page.mouse.click(0, 0, {delay: 4000})

        const sortbtn = page.locator('#gridList-sort-name');
        const table = page.locator('id=gridList >> ul')
        const tableFirst = table.locator('xpath=.//li').first()
        const tableSecond = table.locator('xpath=.//li').nth(1)
        const tableLast = table.locator('xpath=.//li').last()

        await sortbtn.click()
        await page.waitForTimeout(5000);
        await expect(tableFirst).toContainText("Albertine");
        await expect(tableLast).toContainText("Yasemin");

        await sortbtn.click()
        await page.waitForTimeout(5000);
        await expect(tableFirst).toContainText("Yasemin");
        await expect(tableLast).toContainText("Albertine");

        await sortbtn.click()
        await page.waitForTimeout(5000);
        await expect(tableFirst).toHaveAttribute("data-datatable-selected", "true")
        await expect(tableFirst).toHaveAttribute("data-datatable-active", "false")
        await expect(tableSecond).toHaveAttribute("data-datatable-selected", "true")
        await expect(tableFirst).toHaveAttribute("data-datatable-active", "false")

    });

    test('check if scrollIntoView() of Gridlist is respected', async ({page, browserName}) => {

        test.slow(browserName === 'webkit', 'This test for DataCollection is slow on Mac');

        await page.locator('#tabGroup-tab-list-tab-1').click();

        await page.mouse.click(0, 0, {delay: 1500})

        const startingCard = page.locator('id=Caterina Scholtz');
        const selectedBox = await startingCard.boundingBox()
        await page.mouse.move(selectedBox.x + 10, selectedBox.y + 10, {steps: 100})
        await expect(startingCard).toHaveAttribute("data-datatable-active", "true")
        await page.mouse.click(selectedBox.x + 10, selectedBox.y + 10, {delay: 200})

        let activeLocator = page.locator("[data-datatable-active=true]")

        for (let i = 0; i < 9; i++) {
            let activeText = await activeLocator.textContent()
            await page.keyboard.press("ArrowDown")
            await expect(activeLocator).not.toHaveText(activeText)
        }

        await expect(page.getByText("Geza Stey")).toBeInViewport()
        await expect(activeLocator).toContainText("Geza Stey")
        await expect(startingCard).not.toBeInViewport()

    });

});