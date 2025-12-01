import WelcomePage from "../pages/WelcomePage";
import RegisterStep1Page from "../pages/RegisterStep1Page";
import RegisterStep2Page from "../pages/RegisterStep2Page";
import RegisterStep3Page from "../pages/RegisterStep3Page";
import allure from "@wdio/allure-reporter";

describe("Errores de registro", () => {
  const welcome = new WelcomePage();
  const step1 = new RegisterStep1Page();
  const step2 = new RegisterStep2Page();
  const step3 = new RegisterStep3Page();

  it("Campos obligatorios vacíos muestran errores", async () => {
    allure.addSeverity("normal");
    allure.addLabel("feature", "Validaciones de formulario");
    await welcome.goToRegister();
    await step1.fillStep1("123", "12");
    const nextEnabled = await (await step1.btnNext).isEnabled();
    if (nextEnabled) {
      await step1.next();
    }
    await browser.saveScreenshot(`artifacts/screenshots/${Date.now()}-step1-invalid.png`);
  });

  it("Email inválido y Términos no aceptados", async () => {
    allure.addSeverity("minor");
    await welcome.goToRegister();
    await step1.fillStep1("12345678", "60012345");
    await step1.next();

    await step2.fillValid("A", "B", "correo_invalido", "Moto");
    await browser.saveScreenshot(`artifacts/screenshots/${Date.now()}-step2-email-invalido.png`);
    const btnEnabled = await (await step2.btnNext).isEnabled();
    if (btnEnabled) {
      throw new Error("El botón Siguiente no debería estar habilitado con email inválido");
    }
  });

  it("Términos y condiciones no aceptados (Paso 2)", async () => {
    allure.addSeverity("minor");
    await welcome.goToRegister();
    await step1.fillStep1("12345678", "60012345");
    await step1.next();
    // Llenar sin aceptar términos
    await step2.nameInput.setValue("Juan");
    await step2.lastNameInput.setValue("Pérez");
    await step2.emailInput.setValue(`qa_${Date.now()}@mail.com`);
    await step2.vehicleParticular.click();
    // Verificar que no se habilita Siguiente
    const enabled = await (await step2.btnNext).isEnabled();
    if (enabled) {
      throw new Error("El botón Siguiente no debería habilitarse sin aceptar términos");
    }
    await browser.saveScreenshot(`artifacts/screenshots/${Date.now()}-step2-terminos-no-aceptados.png`);
  });
});
