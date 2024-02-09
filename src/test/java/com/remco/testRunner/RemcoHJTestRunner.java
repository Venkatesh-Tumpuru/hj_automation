package com.remco.testRunner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.testng.annotations.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

//@RunWith(Cucumber.class)
@CucumberOptions(
		features	=	"src/test/java/com/remco/features/",
		glue		=	{"com/remco/stepDefinitions"},
		plugin		=	{"pretty",
						 "json:target/cucumber-reports/Cucumber.json"},
		tags		=	"",
		monochrome	= 	true
		)
@Test
public class RemcoHJTestRunner extends AbstractTestNGCucumberTests {
    static String JSON_FILE_PATH = "target/cucumber-reports/cucumber.json";
    static String HTML_FILE_PATH = "target/cucumber-reports/cucumber.html";
@AfterSuite(alwaysRun=true)
public void generateHTMLReport() {
    try {
        String filePath = JSON_FILE_PATH;
        // Read JSON data from file
        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();

        // Parse JSON data
        String jsonData = jsonBuilder.toString();
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonData);

        // Convert JSON element to JSON array
        JsonArray features = jsonElement.getAsJsonArray();
        StringBuilder htmlBuilder = new StringBuilder();

        int totalFeatures = features.size();
        int totalPassedFeatures = 0;
        int totalFailedFeatures = 0;
        int totalScenarios = 0;
        int totalPassedScenarios = 0;
        int totalFailedScenarios = 0;
        int totalSteps = 0;
        int totalPassedSteps = 0;
        int totalFailedSteps = 0;

        for (JsonElement featureElement : features) {
            JsonObject feature = featureElement.getAsJsonObject();
            String featureName = feature.get("name").getAsString();
            JsonArray scenarios = feature.getAsJsonArray("elements");
            boolean hasFailedScenario = false;

            htmlBuilder.append("<table border='1' style='border-collapse: collapse;'>\n");
            htmlBuilder.append("<thead><tr>");
            boolean hasFailedStepInFeature = false; // Flag to check if any step has failed in the feature
            for (JsonElement scenarioElement : scenarios) {
                JsonObject scenario = scenarioElement.getAsJsonObject();
                JsonArray steps = scenario.getAsJsonArray("steps");
                for (JsonElement stepElement : steps) {
                    JsonObject step = stepElement.getAsJsonObject();
                    JsonObject result = step.getAsJsonObject("result");
                    totalSteps++;
                    String status = result.get("status").getAsString();
                    if (status.equalsIgnoreCase("failed")) {
                        hasFailedStepInFeature = true;
                        totalFailedSteps++;
                        break; // No need to check further steps if one has already failed
                    }
                }
                if (hasFailedStepInFeature) {
                    break; // No need to check further scenarios if one has already failed
                }
            }
            // Set background color of feature header based on whether any scenario has failed
            String featureHeaderColor = hasFailedStepInFeature ? "#F9502B" : "#2BF96D";
            htmlBuilder.append("<th colspan='2' style='background-color: ").append(featureHeaderColor).append(";'>").append(featureName).append("</th>");
            htmlBuilder.append("</tr></thead>\n");
            htmlBuilder.append("<tbody>\n");

            for (JsonElement scenarioElement : scenarios) {
                JsonObject scenario = scenarioElement.getAsJsonObject();
                String scenarioName = scenario.get("name").getAsString();
                JsonArray steps = scenario.getAsJsonArray("steps");
                boolean hasFailedStep = false;

                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td>").append(scenarioName).append("</td>");

                for (JsonElement stepElement : steps) {
                    JsonObject step = stepElement.getAsJsonObject();
                    JsonObject result = step.getAsJsonObject("result");
                    String status = result.get("status").getAsString();
                    if (status.equalsIgnoreCase("failed")) {
                        hasFailedStep = true;
                        totalFailedSteps++;
                    } else {
                        totalPassedSteps++;
                    }
                }

                if (hasFailedStep) {
                    totalFailedScenarios++;
                    hasFailedScenario = true;
                    // Set background color of scenario cell and status cell based on whether any step has failed
                    htmlBuilder.append("<td style='background-color: #F9502B;'>FAILED</td>");
                } else {
                    totalPassedScenarios++;
                    htmlBuilder.append("<td style='background-color: #2BF96D;'>PASSED</td>");
                }

                htmlBuilder.append("</tr>\n");
            }

            if (hasFailedScenario) {
                totalFailedFeatures++;
            } else {
                totalPassedFeatures++;
            }

            htmlBuilder.append("</tbody></table>\n");
            htmlBuilder.append("</br>");
            // Increment total scenarios count
            totalScenarios += scenarios.size();
        }

        // Generate test summary table
        htmlBuilder.append("<h2>Test Summary</h2>\n");
        htmlBuilder.append("<table border='1' style='border-collapse: collapse;'>\n");
        htmlBuilder.append("<thead><tr><th>Type</th><th>Total Count</th><th>Total Passed</th><th>Total Failed</th></tr></thead>\n");
        htmlBuilder.append("<tbody><tr><td>Features</td><td>").append(totalFeatures).append("</td><td>").append(totalPassedFeatures).append("</td><td>").append(totalFailedFeatures).append("</td></tr></tbody>\n");
        htmlBuilder.append("<tbody><tr><td>Scenarios</td><td>").append(totalScenarios).append("</td><td>").append(totalPassedScenarios).append("</td><td>").append(totalFailedScenarios).append("</td></tr></tbody>\n");
        htmlBuilder.append("<tbody><tr><td>Steps</td><td>").append(totalSteps).append("</td><td>").append(totalPassedSteps).append("</td><td>").append(totalFailedSteps).append("</td></tr></tbody>\n");
        htmlBuilder.append("</table>\n");

        // Save HTML report to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(HTML_FILE_PATH));
        writer.write(htmlBuilder.toString());
        writer.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
