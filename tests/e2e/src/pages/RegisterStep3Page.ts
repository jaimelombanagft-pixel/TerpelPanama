export default class RegisterStep3Page {
  get authCheck() { return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/authorizationCheck")') }
  get btnRegister() { return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/btnNextOtp")') }

  async acceptAndRegister() {
    await this.authCheck.waitForDisplayed({ timeout: 10000 })
    await this.authCheck.click()
    await this.btnRegister.waitForEnabled({ timeout: 5000 })
    await this.btnRegister.click()
  }
}
