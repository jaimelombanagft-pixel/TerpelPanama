export default class WelcomePage {
  get btnRegister() {
    return $('-android uiautomator:new UiSelector().text("Registrarme")');
  }
  async goToRegister() {
    try {
      await this.btnRegister.waitForDisplayed({timeout: 15000});
    } catch {
      const scrollTarget = await $(
        '-android uiautomator:new UiScrollable(new UiSelector().scrollable(true)).scrollTextIntoView("Registrarme")',
      );
      await scrollTarget.waitForDisplayed({timeout: 10000});
    }
    await this.btnRegister.click();
  }
}
