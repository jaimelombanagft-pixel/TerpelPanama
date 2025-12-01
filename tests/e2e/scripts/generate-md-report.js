const fs = require("fs");
const path = require("path");

function loadAllureSummary() {
  const resultsDir = path.resolve(__dirname, "..", "allure-results");
  const files = fs.existsSync(resultsDir) ? fs.readdirSync(resultsDir) : [];
  const tests = files
    .filter(f => f.endsWith("-result.json"))
    .map(f => {
      const j = JSON.parse(fs.readFileSync(path.join(resultsDir, f), "utf8"));
      return {name: j.name, status: j.status, start: j.start, stop: j.stop, labels: j.labels};
    });
  const stats = tests.reduce(
    (acc, t) => {
      acc.total++;
      acc[t.status] = (acc[t.status] || 0) + 1;
      return acc;
    },
    {total: 0},
  );
  return {tests, stats};
}

function generateReport() {
  const artifactsDir = path.resolve(__dirname, "..", "artifacts");
  const screenshotsDir = path.join(artifactsDir, "screenshots");
  const videosDir = path.join(artifactsDir, "videos");
  const logsDir = path.join(artifactsDir, "logs");

  const allure = loadAllureSummary();
  const lines = [];
  lines.push("# Reporte Técnico de Pruebas E2E");
  lines.push("");
  lines.push("## Configuración del entorno");
  lines.push("- Appium con UiAutomator2");
  lines.push("- Dispositivo/emulador Android con app `com.terpel.app`");
  lines.push("- Backend en `http://localhost:3000/services/apexrest/` (emulador: `http://10.0.2.2:3000`)");
  lines.push("");
  lines.push("## Casos de prueba ejecutados");
  allure.tests.forEach(t => {
    lines.push(`- ${t.name}: ${t.status}`);
  });
  lines.push("");
  lines.push("## Resultados y estadísticas");
  lines.push(`- Total: ${allure.stats.total}`);
  lines.push(`- Passed: ${allure.stats.passed || 0}`);
  lines.push(`- Failed: ${allure.stats.failed || 0}`);
  lines.push(`- Broken: ${allure.stats.broken || 0}`);
  lines.push(`- Skipped: ${allure.stats.skipped || 0}`);
  lines.push("");
  lines.push("## Evidencias de pantalla");
  if (fs.existsSync(screenshotsDir)) {
    const shots = fs.readdirSync(screenshotsDir);
    shots.forEach(s => {
      lines.push(`- ${path.join("artifacts/screenshots", s)}`);
    });
  }
  lines.push("");
  lines.push("## Videos de ejecución");
  if (fs.existsSync(videosDir)) {
    const vids = fs.readdirSync(videosDir);
    vids.forEach(v => {
      lines.push(`- ${path.join("artifacts/videos", v)}`);
    });
  }
  lines.push("");
  lines.push("## Logs");
  if (fs.existsSync(logsDir)) {
    const logs = fs.readdirSync(logsDir);
    logs.forEach(l => {
      lines.push(`- ${path.join("artifacts/logs", l)}`);
    });
  }

  const outDir = path.resolve(__dirname, "..", "reports");
  fs.mkdirSync(outDir, {recursive: true});
  const outFile = path.join(outDir, "technical-report.md");
  fs.writeFileSync(outFile, lines.join("\n"));
  console.log(`Reporte generado: ${outFile}`);
}

generateReport();
