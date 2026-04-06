import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('https://litres.com/');
  await page.getByTestId('search__input').click();
  await page.getByTestId('search__input').fill('j');
  await page.getByTestId('search__input').fill('playwright java');
  await page.getByRole('link', { name: 'Beginning Java Programming.' }).click();
  await expect(page.getByTestId('book__name--wrapper').getByRole('heading')).toContainText('Beginning Java Programming. The Object-Oriented Approach');
});