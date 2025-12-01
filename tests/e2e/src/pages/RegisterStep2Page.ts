export default class RegisterStep2Page {
  get nameInput() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/nameEditText")');
  }
  get lastNameInput() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/lastNameEditText")');
  }
  get emailInput() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/emailEditText")');
  }
  get termsCheck() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/termsCheck")');
  }
  get vehicleMoto() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/checkMoto")');
  }
  get vehicleParticular() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/checkParticular")');
  }
  get btnNext() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/btnRegisterFinal")');
  }
  get nameLayout() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/nameInputLayout")');
  }
  get lastNameLayout() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/lastNameInputLayout")');
  }
  get emailLayout() {
    return $('-android uiautomator:new UiSelector().resourceId("com.terpel.app:id/emailInputLayout")');
  }

  async fillValid(name: string, lastName: string, email: string, vehicle: "Moto" | "Particular" = "Moto") {
    await this.nameInput.waitForDisplayed({timeout: 15000});
    await this.nameInput.click();
    await this.nameInput.clearValue();
    await this.nameInput.setValue(name);
    await browser.pause(300);
    await this.lastNameInput.click();
    await this.lastNameInput.clearValue();
    await this.lastNameInput.setValue(lastName);
    await browser.pause(300);
    await this.emailInput.click();
    await this.emailInput.clearValue();
    await this.emailInput.setValue(email);
    try {
      await browser.hideKeyboard();
    } catch {}
    await browser.pause(300);
    if (vehicle === "Moto") {
      try {
        await $(
          '-android uiautomator:new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceId("com.terpel.app:id/checkMoto"))',
        );
      } catch {}
      await this.vehicleMoto.click();
    } else {
      try {
        await $(
          '-android uiautomator:new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceId("com.terpel.app:id/checkParticular"))',
        );
      } catch {}
      await this.vehicleParticular.click();
    }
    try {
      await $(
        '-android uiautomator:new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceId("com.terpel.app:id/termsCheck"))',
      );
    } catch {}
    await this.termsCheck.click();
  }

  async submit() {
    try {
      await $(
        '-android uiautomator:new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceId("com.terpel.app:id/btnRegisterFinal"))',
      );
    } catch {}
    await browser.waitUntil(async () => await this.btnNext.isEnabled(), {timeout: 20000});
    await this.btnNext.click();
  }
}
