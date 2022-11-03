import {expect, test} from "@playwright/test";

test.beforeEach(async ({page}) => {
    await page.goto('#toast')
});

test.describe('Toast', () => {
    const ids = [
        {toggleId: 'btn-toast-default', containerId: 'toast-container-default'},
        {toggleId: 'btn-toast-important', containerId: 'toast-container-important'}
    ];

    ids.forEach(({containerId}) => {
        test(`Container is present (${containerId})`, async ({page}) => {
            const container = page.locator(`#${containerId}`);
            await expect(container).toHaveCount(1);
        });
    });

    ids.forEach(({toggleId, containerId}) => {
        test(`Toast visible after creation (toggle: ${toggleId})`, async ({page}) => {
            const toasts = page.locator(`#${containerId} > *`);
            await expect(toasts).toHaveCount(0);

            const toggle = page.locator(`#${toggleId}`);
            await toggle.click();
            await expect(toasts).toHaveCount(1);
        });
    });

    ids.forEach(({toggleId, containerId}) => {
        test(`Toast closed after duration (toggle: ${toggleId})`, async ({page}) => {
            const toasts = page.locator(`#${containerId} > *`);
            await expect(toasts).toHaveCount(0);

            const toggle = page.locator(`#${toggleId}`);
            await toggle.click();
            await expect(toasts).toHaveCount(1);

            // Wait for toast to be closed (6000ms + small buffer)
            await page.waitForTimeout(6500);

            await expect(toasts).toHaveCount(0);
        });
    });

    ids.forEach(({toggleId, containerId}) => {
        test(`Toast closed after pressing close button (toggle: ${toggleId})`, async ({page}) => {
            const toasts = page.locator(`#${containerId} > *`);
            await expect(toasts).toHaveCount(0);

            const toggle = page.locator(`#${toggleId}`);
            await toggle.click();
            await expect(toasts).toHaveCount(1);

            const closeButton = page.locator(`#${containerId} > li:first-child > button`);
            await closeButton.click();

            await expect(toasts).toHaveCount(0);
        });
    });

    test('Most recent toast closed after pressing escape', async ({page}) => {
        const toasts = page.locator(`#${ids[0].containerId} > *`);
        await expect(toasts).toHaveCount(0);

        const toggle = page.locator(`#${ids[0].toggleId}`);
        await toggle.click();
        await toggle.click();
        await expect(toasts).toHaveCount(2);

        const firstToast = page.locator(`#toast-0`);
        await expect(firstToast).toBeVisible();

        const secondToast = page.locator(`#toast-1`);
        await expect(secondToast).toBeVisible();

        await page.keyboard.down('Escape');
        await expect(toasts).toHaveCount(1);
        await expect(firstToast).toBeVisible();

        await page.keyboard.down('Escape');
        await expect(toasts).toHaveCount(0);
    });
});