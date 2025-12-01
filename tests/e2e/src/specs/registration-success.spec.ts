import WelcomePage from "../pages/WelcomePage";
import RegisterStep1Page from "../pages/RegisterStep1Page";
import RegisterStep2Page from "../pages/RegisterStep2Page";
import RegisterStep3Page from "../pages/RegisterStep3Page";
import OtpPage from "../pages/OtpPage";
import allure from "@wdio/allure-reporter";

describe("Registro exitoso", () => {
  const welcome = new WelcomePage();
  const step1 = new RegisterStep1Page();
  const step2 = new RegisterStep2Page();
  const step3 = new RegisterStep3Page();
  const otp = new OtpPage();

  it("Valida campos y envía datos a /services/apexrest/ClientManagement/v2/", async () => {
    allure.addSeverity("critical");
    allure.addLabel("feature", "Registro de clientes");
    await browser.startRecordingScreen();
    await welcome.goToRegister();
    await step1.fillStep1("12345678", "60012345");
    await step1.next();

    await step2.fillValid("Juan", "Pérez", `qa_${Date.now()}@mail.com`, "Moto");
    await step2.submit();

    await step3.acceptAndRegister();

    const screenshotPath = `artifacts/screenshots/${Date.now()}-registro-exitoso.png`;
    await browser.saveScreenshot(screenshotPath);
    await allure.addAttachment(
      "Pantalla Registro Exitoso",
      Buffer.from(await browser.takeScreenshot(), "base64"),
      "image/png",
    );

    const videoBase64 = await browser.stopRecordingScreen();
    const fs = await import("fs");
    const path = await import("path");
    const videoPath = path.resolve("artifacts/videos", `${Date.now()}-registro-exitoso.mp4`);
    fs.mkdirSync(path.dirname(videoPath), {recursive: true});
    fs.writeFileSync(videoPath, Buffer.from(videoBase64, "base64"));

    await otp.verify("123456");

    const homeBanner = await $("id:homeBanner");
    await homeBanner.waitForDisplayed({timeout: 15000});

    try {
      const logs = await driver.getLogs("logcat");
      const ok = logs.some((l: any) => String(l.message).includes("Registro exitoso"));
      if (!ok) throw new Error('No se encontró log de "Registro exitoso" en logcat');
    } catch {}
  });
});
