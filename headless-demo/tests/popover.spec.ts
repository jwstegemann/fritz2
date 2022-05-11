import {expect, test} from '@playwright/test';

/*
 * Missing tests due to limited example component:
 * - open state
 * - transitions
 * - positioning
 * - arrow
 */

test.beforeEach(async ({page}) => {
    /* go to the page of Pop Over component */
    await page.goto("#popover");

});

test.describe('To open the popover', () => {
    
        test('and close when clicking on it and also closing when pressing Escape', async ({page}) =>{
            /* locator of popOverButton (tag: popOverButton) */
            const popOv = page.locator('#popOver-button');
            /* locator of popOverPanel (tag: popOverPanel) */
            const popOvPanel = page.locator('#popOver-items');

            await popOv.focus();
            await popOv.click();
            await expect(popOvPanel).toBeVisible();
            
            await popOv.click();
            await expect(popOvPanel).toBeHidden();

            await popOv.click();
            await expect(popOvPanel).toBeVisible();
            
            await popOv.press('Escape');
            await expect(popOvPanel).toBeHidden();
        
        });
    
    for (const key of ["Enter", "Space"]){
        
        test(`only with ${key} and close it with Esc`, async ({page}) =>{

            const popOv = page.locator('#popOver-button');
            const popOvPanel = page.locator('#popOver-items');
            
            await popOv.focus();
            await popOv.press(key);
            await expect(popOvPanel).toBeVisible();
            
            await popOv.press(key);
            await expect(popOvPanel).toBeHidden();
            
            await popOv.press(key);
            await expect(popOvPanel).toBeVisible();
            
            await popOv.press('Escape');
            await expect(popOvPanel).toBeHidden();
        
        });

    }

});

test.describe('Pressing on', () => {
    
    for(let i = 1; i < 5; i++){
        
        test(`Tab ${i} time(s) to check Focus Management on button`, async ({page}) =>{

            const popOv = page.locator('#popOver-button');
            const popOvPanel = page.locator('#popOver-items');
            
            for (let j = 1; j < i; j++)
            await popOv.press('Tab');
            await popOv.press('Enter');
            await expect(popOvPanel).toBeVisible();
            await popOv.press('Escape');
            await expect(popOvPanel).not.toBeVisible();
        
        }); 

    }

});