# UI Tests

We use [Playwright](https://playwright.dev/) as UI testing tool.

To run the tests just type:

```text
npx playwright test    
```

## General Design Principles

- Write one test for all platforms! (Headless components are meant to work the same on all platforms after all!)
- Write one test for one behaviour; do not test a bunch of functions within one test!
- Prefer parameterized tests over "numbering" steps!
- Never use loops *inside* a test case!

## General Design Principles for dealing with Browser Trouble

Sometimes there are errors during creation of tests, We have identified three different problem classes:

 1. Issue with Playwright itself
 2. Different behaviour in browsers or even issues in some browser
 3. Component might not work (for enduser) by design on some platforms (think of *Tooltip* on mobile devices)

If such an incident occurs, please apply the following rules to deal with it:

 1. Document the issue in Playwright next ot the test, but do not implement a workaround! If the issue gets solved, the
    test will continue to work again.
 2. Invest time to really check, if the component itself might have an issue left (after all this is one of the main 
    purposes to have automatic tests!). Although this might be an issue with Playwright itself. Create an issue there
    and then mark this next to the currently failing test.
 3. If a component is not designed to work on some platform, but Playwright is able to run the test nevertheless, then
    simply let the test run also on such platforms and do not implement a workaround!
 4. If there is absolutely no other way to make a test pass with the general test implementation on some platform,
    do not solve this inside the test by using logic (some `if - else` blocks or alike)! Instead, create two tests for
    one use case and adapt the run configuration for both, so that the defined set of platforms are used to run the
    two tests in a *disjoint* manner! So one set of platforms run the "common" test and only the edge case platforms
    run the "special" test case.
 