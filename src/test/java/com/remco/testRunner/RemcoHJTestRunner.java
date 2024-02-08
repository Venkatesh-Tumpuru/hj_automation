package com.remco.testRunner;

import java.io.BufferedReader;
import java.io.FileReader;
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
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
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

	    public  String processJson(JsonArray jsonArray) {
	    	StringBuilder htmlBuilder = new StringBuilder();

	        // Initialize counts
	        int totalScenarios = 0, totalScenariosPassed = 0, totalScenariosFailed = 0;
	        int totalFeatures = 0, totalFeaturesPassed = 0, totalFeaturesFailed = 0;
	        String scColor="green";
	        String frColor="green";

	        // Process each feature
	        for (JsonElement jsonElement : jsonArray) {
	            JsonObject feature = jsonElement.getAsJsonObject();
	            boolean featurePassed = true; // Flag to track if all scenarios in the feature passed
	            
	            String featureName = feature.get("name").getAsString();
	            String featureStatus = "Passed"; // Default feature status

	            // Process each scenario within the feature
	            JsonArray scenarios = feature.getAsJsonArray("elements");
	            
	            // Append feature HTML with appropriate styling
	            htmlBuilder.append("<div class=\"feature\">");
	            htmlBuilder.append("<div class=\"feature-name\">").append("<h4><p style=\"color:"+frColor+";\">").append("Feature: ").append(featureName).append(" - ").append(featureStatus.toUpperCase()).append("</p></h4> </div>");

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
	                        featurePassed = false; // Update feature status if any scenario fails
	                        frColor="red";
	                        scColor="red";
	                        
	                    }
	                }

	                // Update scenario counts
	                totalScenarios++;
	                if (scenarioStatus.equals("Passed"))
	                    totalScenariosPassed++;
	                else
	                    totalScenariosFailed++;

	                // Append scenario HTML
	                htmlBuilder.append("<div class=\"scenario\">").append("<p style=\"color:"+scColor+";\">").append("<b>Scenario</b>:  ").append(scenarioName).append(" - ").append(scenarioStatus.toUpperCase()).append("</p></div>");
	            }

	            // Update feature counts
	            totalFeatures++;
	            if (!featurePassed) {
	                totalFeaturesFailed++;
	                featureStatus = "Failed"; // Update feature status if any scenario fails
	            } else {
	                totalFeaturesPassed++;
	            }

	            htmlBuilder.append("</div>"); // Close feature div
	        }
	        // Generate pictorial representation of counts for scenarios and features
	        htmlBuilder.append("<div class=\"pictorial-representation-container\">");
	        htmlBuilder.append("<div class=\"pictorial-representation\">");
	        htmlBuilder.append("<h2>Features</h2>");
	        htmlBuilder.append("<canvas id=\"featurePieChart\" style=\"width:100px;height:100px\";></canvas>");
	        htmlBuilder.append("</div>");

	        htmlBuilder.append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js\"></script>");
	        htmlBuilder.append("<script>");
	        htmlBuilder.append("document.addEventListener(\"DOMContentLoaded\", function() {");
	        htmlBuilder.append("var featureCtx = document.getElementById('featurePieChart');");
	        htmlBuilder.append("var featurePieChart = new Chart(featureCtx, {");
	        htmlBuilder.append("type: 'pie',");
	        htmlBuilder.append("data: {");
	        htmlBuilder.append("labels: ['Features Passed', 'Features Failed'],");
	        htmlBuilder.append("datasets: [{");
	        htmlBuilder.append("data: ["+totalFeaturesPassed+","+totalFeaturesFailed+"],"); // Example data (replace with actual data;
	        htmlBuilder.append("backgroundColor: ['green', 'red']");
	        htmlBuilder.append("}]");
	        htmlBuilder.append("}");
	        htmlBuilder.append("});");
	        htmlBuilder.append("});");
	        htmlBuilder.append("</script>");
	        // Pictorial representation for scenarios
	        htmlBuilder.append("<div class=\"pictorial-representation-container\">");
	        htmlBuilder.append("<div class=\"pictorial-representation\">");
	        htmlBuilder.append("<h2>Scenarios</h2>");
	        htmlBuilder.append("<canvas id=\"scenarioPieChart\" width=\"200\" height=\"200\"></canvas>");
	        htmlBuilder.append("</div>");
	        htmlBuilder.append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js\"></script>");
	        htmlBuilder.append("<script>");
	        htmlBuilder.append("document.addEventListener(\"DOMContentLoaded\", function() {");
	        htmlBuilder.append("var scenarioCtx = document.getElementById('scenarioPieChart');");
	        htmlBuilder.append("var scenarioPieChart = new Chart(scenarioCtx, {");
	        htmlBuilder.append("type: 'pie',");
	        htmlBuilder.append("data: {");
	        htmlBuilder.append("labels: ['Scenarios Passed', 'Scenarios Failed'],");
	        htmlBuilder.append("datasets: [{");
	        htmlBuilder.append("data: ["+totalScenariosPassed+","+totalScenariosFailed+"],"); // Example data (replace with actual data;
	        htmlBuilder.append("backgroundColor: ['green', 'red']");
	        htmlBuilder.append("}]");
	        htmlBuilder.append("}");
	        htmlBuilder.append("});");
	        htmlBuilder.append("});");
	        htmlBuilder.append("</script>");
	        htmlBuilder.append("</div>");

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
