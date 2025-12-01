export default class RegisterStep1Page {
  get documentInput() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/documentEditText")');
  }
  get phoneInput() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/phoneEditText")');
  }
  get btnNext() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/btnSubmit")');
  }

  async fillStep1(document: string, phone: string) {
    await this.documentInput.waitForDisplayed({timeout: 15000});
    await this.documentInput.click();
    await browser.pause(200);
    await this.documentInput.clearValue();
    await this.documentInput.setValue(document);
    await this.phoneInput.click();
    await browser.pause(200);
    await this.phoneInput.clearValue();
    await this.phoneInput.setValue(phone);
  }

  async next() {
    await browser.waitUntil(async () => await this.btnNext.isEnabled(), {timeout: 15000});
    await this.btnNext.click();
  }
}
