import {expect, test} from '@playwright/test';
/*
 * Missing tests due to limited example component:
 * - open state
 * - transitions
 * - positioning
 * - arrow
 * 
 * Also for this headless component we will only check 3 elements here:
 * - The button at the top
 * - The button on the center
 * - The input field at the bottom
 * 
 * Because for all our borwsers and mobile devices these 3 elements are always shown.
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Tooltip component */
    await page.goto("#tooltip");

});

test.describe('To check the display of tooltip', () => {

    test('when mouse over button on top', async ({page}) =>{
        /* locator for first button on top (tag: tooltip) */
        const btnTop = page.locator('#top-reference');
        /* and locator for its tooltip */
        const toolTbtnTop = page.locator('#top-tooltip');

        await btnTop.hover();
        await expect(toolTbtnTop).toBeVisible();
    });

    test('when mouse over button on center', async ({page}) =>{
        //locator for last button on the right corner
        const btnCenter = page.locator('#center-reference');
        /* and locator for its tooltip */
        const toolTbtnCenter = page.locator('#center-tooltip');
        
        await btnCenter.hover();
        await expect(toolTbtnCenter).toBeVisible();
    });

    test('when mouse over input field at the bottom', async ({page}) =>{
        /* locator for button on the center */
        const inputBottom = page.locator('#bottom-reference');
        /* and locator for its tooltip */
        const toolTiptBottom = page.locator('#bottom-tooltip');
    
        await inputBottom.hover();
        await expect(toolTiptBottom).toBeVisible();
    });

});