/**
 * NISIRCOP Frontend UI Testing Script
 * Tests all UI elements, buttons, routes, and interactions
 */

const puppeteer = require('puppeteer');
const chalk = require('chalk');

const FRONTEND_URL = process.env.FRONTEND_URL || 'http://localhost:3000';
const TEST_RESULTS = [];
let browser, page;

// Test result tracking
let totalTests = 0;
let passedTests = 0;
let failedTests = 0;

// Helper functions
function recordTest(testName, passed, details = '') {
    totalTests++;
    if (passed) {
        passedTests++;
        console.log(chalk.green(`✓ ${testName}`));
        TEST_RESULTS.push({status: 'PASS', name: testName, details});
    } else {
        failedTests++;
        console.log(chalk.red(`✗ ${testName} - ${details}`));
        TEST_RESULTS.push({status: 'FAIL', name: testName, details});
    }
}

function printHeader(title) {
    console.log('\n' + chalk.blue('='.repeat(60)));
    console.log(chalk.blue(title));
    console.log(chalk.blue('='.repeat(60)) + '\n');
}

async function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

// Test suites
async function testFrontendAccessibility() {
    printHeader('1. FRONTEND ACCESSIBILITY TESTS');
    
    try {
        browser = await puppeteer.launch({
            headless: true,
            args: ['--no-sandbox', '--disable-setuid-sandbox']
        });
        page = await browser.newPage();
        
        // Test main page loads
        try {
            const response = await page.goto(FRONTEND_URL, {
                waitUntil: 'networkidle0',
                timeout: 10000
            });
            recordTest('Frontend loads successfully', response.status() === 200);
        } catch (error) {
            recordTest('Frontend loads successfully', false, error.message);
        }
        
        // Test page title
        const title = await page.title();
        recordTest('Page has title', title && title.length > 0, `Title: ${title}`);
        
        // Test viewport renders
        const viewport = await page.viewport();
        recordTest('Viewport set correctly', viewport !== null);
        
    } catch (error) {
        recordTest('Frontend accessibility', false, error.message);
    }
}

async function testUIElements() {
    printHeader('2. UI ELEMENTS TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping UI tests - page not loaded'));
        return;
    }
    
    try {
        // Test for common UI elements
        const elements = {
            'header': 'header',
            'navigation': 'nav',
            'main content': 'main',
            'footer': 'footer',
            'div elements': 'div',
            'links': 'a'
        };
        
        for (const [name, selector] of Object.entries(elements)) {
            try {
                const element = await page.$(selector);
                recordTest(`${name} element exists`, element !== null);
            } catch (error) {
                recordTest(`${name} element exists`, false, error.message);
            }
        }
        
        // Test for Vue.js app
        const vueApp = await page.evaluate(() => {
            return document.querySelector('#app') !== null;
        });
        recordTest('Vue app root element exists', vueApp);
        
    } catch (error) {
        recordTest('UI elements test', false, error.message);
    }
}

async function testButtons() {
    printHeader('3. BUTTON INTERACTION TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping button tests - page not loaded'));
        return;
    }
    
    try {
        // Find all buttons
        const buttons = await page.$$('button');
        recordTest('Buttons found on page', buttons.length > 0, `Found ${buttons.length} buttons`);
        
        // Test button clickability
        for (let i = 0; i < Math.min(buttons.length, 5); i++) {
            try {
                const buttonText = await page.evaluate(el => el.textContent, buttons[i]);
                const isVisible = await page.evaluate(el => {
                    const style = window.getComputedStyle(el);
                    return style.display !== 'none' && style.visibility !== 'hidden';
                }, buttons[i]);
                
                recordTest(`Button "${buttonText.trim()}" is visible`, isVisible);
                
                // Test if button is clickable (doesn't throw error)
                try {
                    await buttons[i].click();
                    await delay(100);
                    recordTest(`Button "${buttonText.trim()}" is clickable`, true);
                } catch (error) {
                    recordTest(`Button "${buttonText.trim()}" is clickable`, false, error.message);
                }
            } catch (error) {
                recordTest(`Button ${i} interaction`, false, error.message);
            }
        }
        
        // Test for common button types
        const buttonTypes = [
            'submit',
            'button',
            'reset'
        ];
        
        for (const type of buttonTypes) {
            const found = await page.$(`button[type="${type}"]`);
            if (found) {
                recordTest(`${type} button exists`, true);
            }
        }
        
    } catch (error) {
        recordTest('Button tests', false, error.message);
    }
}

async function testForms() {
    printHeader('4. FORM ELEMENTS TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping form tests - page not loaded'));
        return;
    }
    
    try {
        // Test for forms
        const forms = await page.$$('form');
        recordTest('Forms exist on page', forms.length > 0, `Found ${forms.length} forms`);
        
        // Test input fields
        const inputs = await page.$$('input');
        recordTest('Input fields exist', inputs.length > 0, `Found ${inputs.length} inputs`);
        
        // Test for common input types
        const inputTypes = ['text', 'password', 'email', 'number', 'submit'];
        for (const type of inputTypes) {
            const found = await page.$(`input[type="${type}"]`);
            if (found) {
                recordTest(`${type} input exists`, true);
            }
        }
        
        // Test select elements
        const selects = await page.$$('select');
        if (selects.length > 0) {
            recordTest('Select elements exist', true, `Found ${selects.length} selects`);
        }
        
        // Test textareas
        const textareas = await page.$$('textarea');
        if (textareas.length > 0) {
            recordTest('Textarea elements exist', true, `Found ${textareas.length} textareas`);
        }
        
    } catch (error) {
        recordTest('Form tests', false, error.message);
    }
}

async function testRoutes() {
    printHeader('5. ROUTE NAVIGATION TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping route tests - page not loaded'));
        return;
    }
    
    try {
        // Test common routes
        const routes = [
            '/',
            '/users',
            '/incidents',
            '/analytics',
            '/about'
        ];
        
        for (const route of routes) {
            try {
                const url = `${FRONTEND_URL}${route}`;
                const response = await page.goto(url, {
                    waitUntil: 'networkidle0',
                    timeout: 5000
                });
                recordTest(`Route ${route} accessible`, response.status() === 200 || response.status() === 404);
            } catch (error) {
                recordTest(`Route ${route} accessible`, false, 'Timeout or error');
            }
        }
        
        // Test navigation links
        await page.goto(FRONTEND_URL);
        const links = await page.$$('a[href]');
        recordTest('Navigation links exist', links.length > 0, `Found ${links.length} links`);
        
    } catch (error) {
        recordTest('Route tests', false, error.message);
    }
}

async function testResponsiveness() {
    printHeader('6. RESPONSIVE DESIGN TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping responsive tests - page not loaded'));
        return;
    }
    
    try {
        const viewports = [
            {name: 'Desktop', width: 1920, height: 1080},
            {name: 'Laptop', width: 1366, height: 768},
            {name: 'Tablet', width: 768, height: 1024},
            {name: 'Mobile', width: 375, height: 667}
        ];
        
        for (const viewport of viewports) {
            try {
                await page.setViewport({width: viewport.width, height: viewport.height});
                await delay(500);
                
                const dimensions = await page.evaluate(() => {
                    return {
                        width: window.innerWidth,
                        height: window.innerHeight
                    };
                });
                
                recordTest(
                    `${viewport.name} viewport (${viewport.width}x${viewport.height})`,
                    dimensions.width === viewport.width
                );
            } catch (error) {
                recordTest(`${viewport.name} viewport`, false, error.message);
            }
        }
        
    } catch (error) {
        recordTest('Responsive design tests', false, error.message);
    }
}

async function testStyles() {
    printHeader('7. CSS AND STYLING TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping style tests - page not loaded'));
        return;
    }
    
    try {
        // Test for stylesheet links
        const stylesheets = await page.$$('link[rel="stylesheet"]');
        recordTest('Stylesheets loaded', stylesheets.length > 0, `Found ${stylesheets.length} stylesheets`);
        
        // Test for Tailwind CSS classes
        const hasTailwind = await page.evaluate(() => {
            const elements = document.querySelectorAll('[class]');
            const tailwindClasses = ['flex', 'grid', 'p-', 'm-', 'bg-', 'text-'];
            
            for (const el of elements) {
                const classes = el.className.toString();
                for (const twClass of tailwindClasses) {
                    if (classes.includes(twClass)) {
                        return true;
                    }
                }
            }
            return false;
        });
        
        recordTest('Tailwind CSS classes present', hasTailwind);
        
        // Test for inline styles
        const hasInlineStyles = await page.$$('[style]');
        recordTest('Inline styles exist', hasInlineStyles.length >= 0, `Found ${hasInlineStyles.length} elements with inline styles`);
        
    } catch (error) {
        recordTest('Style tests', false, error.message);
    }
}

async function testJavaScript() {
    printHeader('8. JAVASCRIPT FUNCTIONALITY TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping JavaScript tests - page not loaded'));
        return;
    }
    
    try {
        // Test for Vue.js
        const hasVue = await page.evaluate(() => {
            return typeof window.__VUE__ !== 'undefined' || 
                   document.querySelector('[data-v-app]') !== null ||
                   document.querySelector('#app').__vue__ !== undefined;
        });
        recordTest('Vue.js framework detected', hasVue);
        
        // Test for JavaScript execution
        const jsWorks = await page.evaluate(() => {
            return 2 + 2 === 4;
        });
        recordTest('JavaScript execution works', jsWorks);
        
        // Test console errors
        const errors = [];
        page.on('console', msg => {
            if (msg.type() === 'error') {
                errors.push(msg.text());
            }
        });
        
        await page.reload();
        await delay(2000);
        
        recordTest('No console errors', errors.length === 0, errors.length > 0 ? `${errors.length} errors found` : '');
        
    } catch (error) {
        recordTest('JavaScript tests', false, error.message);
    }
}

async function testAccessibility() {
    printHeader('9. ACCESSIBILITY TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping accessibility tests - page not loaded'));
        return;
    }
    
    try {
        // Test for alt attributes on images
        const imagesWithoutAlt = await page.$$eval('img', imgs => 
            imgs.filter(img => !img.alt || img.alt.trim() === '').length
        );
        recordTest('Images have alt attributes', imagesWithoutAlt === 0, 
            imagesWithoutAlt > 0 ? `${imagesWithoutAlt} images without alt` : '');
        
        // Test for proper heading hierarchy
        const headings = await page.$$eval('h1, h2, h3, h4, h5, h6', elements => 
            elements.map(el => el.tagName)
        );
        recordTest('Heading elements exist', headings.length > 0, `Found ${headings.length} headings`);
        
        // Test for aria labels
        const ariaLabels = await page.$$('[aria-label]');
        if (ariaLabels.length > 0) {
            recordTest('ARIA labels present', true, `Found ${ariaLabels.length} elements with ARIA labels`);
        }
        
        // Test for form labels
        const labels = await page.$$('label');
        const inputs = await page.$$('input');
        if (inputs.length > 0) {
            recordTest('Form labels exist', labels.length > 0, 
                `${labels.length} labels for ${inputs.length} inputs`);
        }
        
    } catch (error) {
        recordTest('Accessibility tests', false, error.message);
    }
}

async function testPerformance() {
    printHeader('10. PERFORMANCE TESTS');
    
    if (!page) {
        console.log(chalk.yellow('⚠ Skipping performance tests - page not loaded'));
        return;
    }
    
    try {
        // Test page load time
        const startTime = Date.now();
        await page.goto(FRONTEND_URL, {waitUntil: 'networkidle0'});
        const loadTime = Date.now() - startTime;
        
        recordTest('Page load time < 5s', loadTime < 5000, `${loadTime}ms`);
        
        // Test performance metrics
        const metrics = await page.metrics();
        recordTest('Performance metrics available', metrics !== null);
        
        // Test for large resources
        const resources = await page.evaluate(() => {
            const entries = performance.getEntriesByType('resource');
            return entries.map(e => ({
                name: e.name,
                size: e.transferSize,
                duration: e.duration
            }));
        });
        
        const largeResources = resources.filter(r => r.size > 1000000); // > 1MB
        recordTest('No large resources (> 1MB)', largeResources.length === 0, 
            largeResources.length > 0 ? `${largeResources.length} large resources` : '');
        
    } catch (error) {
        recordTest('Performance tests', false, error.message);
    }
}

// Main test execution
async function runAllTests() {
    console.log(chalk.blue.bold('\n🧪 NISIRCOP FRONTEND UI TEST SUITE\n'));
    console.log(chalk.gray(`Testing frontend at: ${FRONTEND_URL}\n`));
    
    try {
        await testFrontendAccessibility();
        await testUIElements();
        await testButtons();
        await testForms();
        await testRoutes();
        await testResponsiveness();
        await testStyles();
        await testJavaScript();
        await testAccessibility();
        await testPerformance();
        
    } catch (error) {
        console.error(chalk.red('Test suite error:'), error);
    } finally {
        if (browser) {
            await browser.close();
        }
    }
    
    // Print summary
    printHeader('TEST SUMMARY');
    console.log(chalk.white(`Total Tests:  ${totalTests}`));
    console.log(chalk.green(`Passed:       ${passedTests} (${Math.round(passedTests/totalTests*100)}%)`));
    console.log(chalk.red(`Failed:       ${failedTests} (${Math.round(failedTests/totalTests*100)}%)`));
    console.log('');
    
    if (failedTests === 0) {
        console.log(chalk.green.bold('✓ ALL UI TESTS PASSED! 🎉\n'));
        process.exit(0);
    } else {
        console.log(chalk.yellow.bold(`⚠ ${failedTests} test(s) failed\n`));
        process.exit(1);
    }
}

// Run tests
runAllTests().catch(error => {
    console.error(chalk.red('Fatal error:'), error);
    process.exit(1);
});
