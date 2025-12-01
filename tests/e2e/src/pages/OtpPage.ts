export default class OtpPage {
  get otpInput() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/otpEditText")');
  }
  get btnVerify() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/btnVerify")');
  }
  get progress() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/progressOtp")');
  }

  async verify(code: string) {
    await this.otpInput.waitForDisplayed({timeout: 10000});
    await this.otpInput.setValue(code);
    await browser.waitUntil(async () => await this.btnVerify.isEnabled(), {timeout: 10000});
    await this.btnVerify.click();
  }
}
