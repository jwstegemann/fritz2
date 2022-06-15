import type { PlaywrightTestConfig } from '@playwright/test';
import { devices } from '@playwright/test';

/**
 * Read environment variables from file.
 * https://github.com/motdotla/dotenv
 */
// require('dotenv').config();

/**
 * See https://playwright.dev/docs/test-configuration.
 */
const config: PlaywrightTestConfig = {
  testDir: './tests',
  /* Maximum time one test can run for. */
  timeout: 45 * 1000,
  expect: {
    /**
     * Maximum time expect() should wait for the condition to be met.
     * For example in `await expect(locator).toHaveText();`
     */
    timeout: 5000
  },
  /* Fail the build on CI if you accidentally left test.only in the source code. */
  forbidOnly: !!process.env.CI,
  /* Retry on CI only */
  retries: process.env.CI ? 2 : 2,
  /* Opt out of parallel tests on CI. */
  workers: process.env.CI ? 1 : undefined,
  /* Reporter to use. See https://playwright.dev/docs/test-reporters */
  reporter: [['list'],['html']],
  /* Shared settings for all the projects below. See https://playwright.dev/docs/api/class-testoptions. */
  use: {
    /* Maximum time each action such as `click()` can take. Defaults to 0 (no limit). */
    actionTimeout: 0,
    /* Base URL to use in actions like `await page.goto('/')`. */
    //baseURL: 'https://next.fritz2.dev/headless-demo/',
    //baseURL: 'C:\\Users\\DE-P0029\\IdeaProjects\\fritz2\\headless-demo\\build\\distributions\\index.html',
    baseURL: 'http://localhost:8081/',

    /* Collect trace when retrying the failed test. See https://playwright.dev/docs/trace-viewer */
    trace: 'on-first-retry',
    /* Headed tests or headless tests. See https://playwright.dev/docs/api/class-testoptions#test-options-headless */
    headless: true,
    /* Take screenshots only on failure. See https://playwright.dev/docs/screenshots */
    screenshot: 'only-on-failure',
    /* Take videos only on failure. See https://playwright.dev/docs/videos */
    video: 'retain-on-failure'
  },

  /* Configure projects for major browsers */
  projects: [
    {
      name: 'chromium',
      use: {
        ...devices['Desktop Chrome'],
      },
    },

    {
      name: 'firefox',
      use: {
        ...devices['Desktop Firefox'],
      },
    },

    {
      name: 'webkit',
      use: {
        ...devices['Desktop Safari'],
        launchOptions: {
          slowMo: 1000
        }
      },
    },

    /* Test against mobile viewports. */
     {
       name: 'Mobile Chrome',
       use: {
         ...devices['Pixel 5'],
       },
     },
     {
      name: 'Mobile Chrome Landscape',
      use: {
        ...devices['Pixel 5 landscape'],
      },
    },
  {
    name: 'Mobile Safari',
    use: {
      ...devices['iPhone 12'],
      launchOptions: {
        slowMo: 1000
      }
    },
  },
     {
      name: 'Mobile Safari Landscape',
      use: {
        ...devices['iPhone 12 landscape'],
        launchOptions: {
          slowMo: 1000
        }
      },
    },

    /* Test against branded browsers. */
    {
      name: 'Microsoft Edge',
      use: {
        channel: 'msedge',
      },
    },
    {
      name: 'Google Chrome',
      use: {
        channel: 'chrome',
      },
    },
  ],

  /* Folder for test artifacts such as screenshots, videos, traces, etc. */
  // outputDir: 'test-results/',

  /* Run your local dev server before starting the tests */
  // webServer: {
  //   command: 'npm run start',
  //   port: 3000,
  // },
};

export default config;