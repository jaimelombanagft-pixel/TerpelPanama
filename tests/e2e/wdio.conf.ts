import type {Options} from "@wdio/types";

export const config: Options.Testrunner = {
  runner: "local",
  specs: ["./src/specs/**/*.spec.ts"],
  maxInstances: 1,
  specFileRetries: 1,
  hostname: "127.0.0.1",
  port: 4725,
  path: "/",
  capabilities: [
    {
      platformName: "Android",
      "appium:automationName": "UiAutomator2",
      "appium:deviceName": process.env.ANDROID_DEVICE_NAME || "Android Emulator",
      "appium:appPackage": "com.terpel.app",
      "appium:appActivity": "com.terpel.app.MainActivity",
      "appium:noReset": false,
      "appium:newCommandTimeout": 180,
      "appium:autoGrantPermissions": true,
    },
  ],
  logLevel: "info",
  services: [],
  framework: "mocha",
  mochaOpts: {ui: "bdd", timeout: 120000, retries: 1},
  reporters: [
    "spec",
    [
      "allure",
      {
        outputDir: "allure-results",
        disableWebdriverStepsReporting: false,
        disableWebdriverScreenshotsReporting: false,
        useCucumberStepReporter: false,
      },
    ],
  ],
  beforeTest: async () => {
    try {
      await browser.startRecordingScreen();
    } catch {}
    try {
      await driver.terminateApp("com.terpel.app");
      await driver.activateApp("com.terpel.app");
      await browser.pause(500);
    } catch {}
  },
  before: async () => {
    await browser.setImplicitTimeout(5000);
  },
  afterTest: async (test, context, {error}) => {
    const safeTitle = test.title.replace(/[^a-zA-Z0-9_-]+/g, "_");
    const shotsDir = "artifacts/screenshots";
    const logsDir = "artifacts/logs";
    const videosDir = "artifacts/videos";
    const fs = await import("fs");
    const path = await import("path");
    fs.mkdirSync(shotsDir, {recursive: true});
    fs.mkdirSync(logsDir, {recursive: true});
    fs.mkdirSync(videosDir, {recursive: true});
    if (error) {
      const file = `${shotsDir}/${Date.now()}-${safeTitle}.png`;
      await browser.saveScreenshot(file);
    }
    try {
      const logs = await driver.getLogs("logcat");
      const logFile = path.resolve(logsDir, `${Date.now()}-${safeTitle}.log`);
      fs.writeFileSync(logFile, logs.map((l: any) => `[${l.timestamp}] ${l.level}: ${l.message}`).join("\n"));
    } catch {}
    try {
      const base64 = await browser.stopRecordingScreen();
      const videoPath = path.resolve(videosDir, `${Date.now()}-${safeTitle}.mp4`);
      fs.writeFileSync(videoPath, Buffer.from(base64, "base64"));
    } catch {}
  },
};
