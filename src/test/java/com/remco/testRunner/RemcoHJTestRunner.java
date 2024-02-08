package com.remco.testRunner;

import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class RemcoHJTestRunner  extends AbstractTestNGCucumberTests{
		@AfterSuite(alwaysRun=true)
	    public  void generateHTMLReport() {
	        // Read JSON data from file
	        String jsonData = readJsonFromFile("target\\cucumber-reports\\cucumber.json");
	        // Parse JSON data
	        JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
	        // Process JSON data and generate HTML content
	        String htmlContent = processJson(jsonArray);
	        // Store HTML content into a file
	        storeHtmlToFile(htmlContent, "target\\cucumber-reports\\cucumber.html");
	    }

	    public  String readJsonFromFile(String filePath) {
	        String jsonData = "";
	        try {
	            Path path = Paths.get(filePath);
	            jsonData = new String(Files.readAllBytes(path));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return jsonData;
	    }

	    public String processJson(JsonArray jsonArray) {
	        StringBuilder htmlBuilder = new StringBuilder();

	        // Initialize counts
	        int totalScenarios = 0, totalScenariosPassed = 0, totalScenariosFailed = 0;
	        int totalFeatures = 0, totalFeaturesPassed = 0, totalFeaturesFailed = 0;
	        String scColor = "green";
	        String frColor = "green";

	        // Process each feature
	        for (JsonElement jsonElement : jsonArray) {
	            JsonObject feature = jsonElement.getAsJsonObject();
	            boolean featurePassed = true; // Flag to track if all scenarios in the feature passed

	            String featureName = feature.get("name").getAsString();
	            String featureStatus = "Passed"; // Default feature status

	            // Process each scenario within the feature
	            JsonArray scenarios = feature.getAsJsonArray("elements");

	            // Check if any scenario in the feature failed
	            for (JsonElement scenarioElement : scenarios) {
	                JsonObject scenario = scenarioElement.getAsJsonObject();
	                JsonArray steps = scenario.getAsJsonArray("steps");
	                for (JsonElement stepElement : steps) {
	                    JsonObject step = stepElement.getAsJsonObject();
	                    String status = step.getAsJsonObject("result").get("status").getAsString();
	                    if (!status.equals("passed")) {
	                        featurePassed = false; // Update feature status if any scenario fails
	                        break;
	                    }
	                }
	                if (!featurePassed) {
	                    break;
	                }
	            }

	            // Update feature status based on scenario results
	            totalFeatures++;
	            if (featurePassed) {
	                totalFeaturesPassed++;
	            } else {
	                totalFeaturesFailed++;
	                featureStatus = "Failed";
	                frColor = "red";
	            }

	            // Append feature HTML with appropriate styling
	            htmlBuilder.append("<div class=\"feature\">");
	            htmlBuilder.append("<div class=\"feature-name\">").append("<h4><p style=\"color:" + frColor + ";\">").append("Feature: ").append(featureName).append(" - ").append(featureStatus.toUpperCase()).append("</p></h4> </div>");

	            // Process scenarios
	            for (JsonElement scenarioElement : scenarios) {
	                JsonObject scenario = scenarioElement.getAsJsonObject();
	                String scenarioName = scenario.get("name").getAsString();
	                String scenarioStatus = "Passed"; // Default scenario status

	                // Process each step within the scenario
	                JsonArray steps = scenario.getAsJsonArray("steps");
	                for (JsonElement stepElement : steps) {
	                    JsonObject step = stepElement.getAsJsonObject();
	                    String status = step.getAsJsonObject("result").get("status").getAsString();
	                    if (!status.equals("passed")) {
	                        scenarioStatus = "Failed"; // Update scenario status if any step fails
	                        scColor = "red";
	                    }
	                }

	                // Update scenario counts
	                totalScenarios++;
	                if (scenarioStatus.equals("Passed"))
	                    totalScenariosPassed++;
	                else
	                    totalScenariosFailed++;

	                // Append scenario HTML
	                htmlBuilder.append("<div class=\"scenario\">").append("<p style=\"color:" + scColor + ";\">").append("<b>Scenario</b>:  ").append(scenarioName).append(" - ").append(scenarioStatus.toUpperCase()).append("</p></div>");
	            }

	            // Close feature div
	            htmlBuilder.append("</div>");

	            // Reset color for next feature
	            scColor = "green";
	        }

	        // Generate feature summary table
	        htmlBuilder.append("<h2>Feature Summary</h2>");
	        htmlBuilder.append("<table border=\"1\">");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<th>Total Features</th>");
	        htmlBuilder.append("<th>Passed</th>");
	        htmlBuilder.append("<th>Failed</th>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<td>").append(totalFeatures).append("</td>");
	        htmlBuilder.append("<td>").append(totalFeaturesPassed).append("</td>");
	        htmlBuilder.append("<td>").append(totalFeaturesFailed).append("</td>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("</table>");

	        // Generate scenario summary table
	        htmlBuilder.append("<h2>Scenario Summary</h2>");
	        htmlBuilder.append("<table border=\"1\">");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<th>Total Scenarios</th>");
	        htmlBuilder.append("<th>Passed</th>");
	        htmlBuilder.append("<th>Failed</th>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<td>").append(totalScenarios).append("</td>");
	        htmlBuilder.append("<td>").append(totalScenariosPassed).append("</td>");
	        htmlBuilder.append("<td>").append(totalScenariosFailed).append("</td>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("</table>");

	        return htmlBuilder.toString();
	    }


	    private  void storeHtmlToFile(String htmlContent, String filePath) {
	        try (FileWriter writer = new FileWriter(filePath)) {
	            writer.write(htmlContent);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
