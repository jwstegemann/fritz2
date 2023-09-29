import {expect, test} from '@playwright/test';
/*
 * Missing tests due to limited example component:
 * - styling
 * - set and query the active Tab from outside
 * - tabs deactivation
 * - vertical TabGroup
 */

test.beforeEach(async ({page}) => {
    /* go to the page of TabGroup component */
    await page.goto("#tabGroup");
    await expect(page.locator("#portal-root")).toBeAttached();
    await page.waitForTimeout(200);
});

test.describe('Checking', () => {

    for (const num of ["0", "1", "2"]) {

        test(`tab selection for tab ${num} through click`, async ({page}) =>{
            
        async function tabActive(num: String) {
            const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
            await expect(tab).toHaveAttribute("aria-selected", "true");
        
        }
        /* locator for each tab (tag: tab) */
        const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
        /* locator for each tab panel (tag: tabPanel) */
        const panel = page.locator(`#tabGroup-tab-panels-panel-${num}`);

        await tab.click();
        await tabActive(num);
        await expect(panel).toBeVisible();

        });
    }
        test('unique result and focus on selected tab through click', async ({page}) =>{

            const data = [
                {num: "0", expected:["1", "2"]},
                {num: "1", expected:["0", "2"]},
                {num: "2", expected:["0", "1"]}
            ]

            async function tabActive(num: string) {
                const tab = page.locator(`#tabGroup-tab-list-tab-${data[num].num}`);
                const tabOther1 = page.locator(`#tabGroup-tab-list-tab-${data[num].expected[0]}`);
                const tabOther2 = page.locator(`#tabGroup-tab-list-tab-${data[num].expected[1]}`);

                const panel = page.locator(`#tabGroup-tab-panels-panel-${data[num].num}`);
                const panelOther1 = page.locator(`#tabGroup-tab-panels-panel-${data[num].expected[0]}`);
                const panelOther2 = page.locator(`#tabGroup-tab-panels-panel-${data[num].expected[1]}`);

                await expect(tab).toHaveAttribute("aria-selected", "true");
                await expect(tabOther1).toHaveAttribute("aria-selected", "false");
                await expect(tabOther2).toHaveAttribute("aria-selected", "false");
                await expect(panel).toBeVisible();
                await expect(panelOther1).toBeHidden();
                await expect(panelOther2).toBeHidden();
            
            }

            const tab0 = page.locator('#tabGroup-tab-list-tab-0');
            const tab1 = page.locator('#tabGroup-tab-list-tab-1');
            const tab2 = page.locator('#tabGroup-tab-list-tab-2');

            /* click on each tab step by step */
            await tab0.click();
            await tabActive("0");
            
            await tab1.click();
            await tabActive("1");
            
            await tab2.click();
            await tabActive("2");

        });

});

test.describe('Navigating', () => {

    for(const data of [
        {key:"Home", target: "first", id: "0"},
        {key:"PageUp", target: "first", id: "0"},
        {key:"End", target: "last", id: "2"},
        {key:"PageDown", target: "last", id: "2"}
    ]) {

        test(`by pressing "${data.key}" will jump to ${data.target} tab`, async ({page}) => {
            const tab = page.locator(`#tabGroup-tab-list-tab-${data.id}`);
            const panel = page.locator(`#tabGroup-tab-panels-panel-${data.id}`)
            await page.locator('#tabGroup-tab-list-tab-1').click();
            await expect(page.locator('#tabGroup-tab-panels-panel-1')).toBeVisible();

            await tab.press(data.key)
            await expect(tab).toHaveAttribute("aria-selected", "true");
            await expect(panel).toBeVisible();

        });

    }

    test("through the tabs should work by Arrow keys Left and Right", async ({page}) => {
        /* locator for tabGroup */
        const tabGroup = page.locator('#tabGroup-tab-list');

        async function tabActive(num: String) {
            const tab = page.locator(`#tabGroup-tab-list-tab-${num}`);
            await expect(tab).toHaveAttribute("aria-selected", "true");
            await expect(tab).toBeFocused();
            
        }
        
        async function panelActive(num: String) {
            const panel = page.locator(`#tabGroup-tab-panels-panel-${num}`);
            await expect(panel).toBeVisible();
        }

        await tabGroup.press("Tab")

        /* right */
        await tabGroup.press("ArrowRight");
        await tabActive("1");
        await panelActive("1");

        await tabGroup.press("ArrowRight");
        await tabActive("2");
        await panelActive("2");

        await tabGroup.press("ArrowRight");
        await tabActive("0");
        await panelActive("0");

        /* left */
        await tabGroup.press("ArrowLeft");
        await tabActive("2");
        await panelActive("2");

        await tabGroup.press("ArrowLeft");
        await tabActive("1");
        await panelActive("1");

        await tabGroup.press("ArrowLeft");
        await tabActive("0");
        await panelActive("0");

    });

});