package com.remco.testRunner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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
    
    LocalDateTime currentTimeEST 	= LocalDateTime.now(ZoneId.of("America/New_York"));
    DateTimeFormatter formatterEST 	= DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mma", Locale.ENGLISH);
    String formattedTimeEST 		= currentTimeEST.format(formatterEST);
    
    LocalDateTime currentTimeIST 	= LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    DateTimeFormatter formatterIST 	= DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mma", Locale.ENGLISH);
    String formattedTimeIST 		= currentTimeIST.format(formatterIST);

@AfterSuite(alwaysRun=true)
public  void generateHTMLReport() {
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

        int totalFeatures = 0;
        int totalPassedFeatures = 0;
        int totalFailedFeatures = 0;
        int totalScenarios = 0;
        int totalPassedScenarios = 0;
        int totalFailedScenarios = 0;
        int totalSteps = 0;
        int totalPassedSteps = 0;
        int totalFailedSteps = 0;
        int totalSkippedSteps = 0;

        htmlBuilder.append("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>HJ Remo Automation Test Report</title>");
        htmlBuilder.append("<style>table {border-collapse: collapse;width: 80%;max-width: 600px;margin: 0 auto;} th, td {border: 1px solid #bcbcbc;text-align: left;padding: 8px;}th {background-color: #f2f2f2;}</style>");
        htmlBuilder.append("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css\">");
        htmlBuilder.append("</head><body>");

        // Header
        htmlBuilder.append("<header style='border-collapse: collapse;width: 80%;max-width: 600px;margin: 0 auto;'><p>Hi Team,</br>Greetings for the day.</br></br>Please find the below Automation status report </p></header>");
        htmlBuilder.append("<p style='border-collapse: collapse;width: 80%;max-width: 600px;margin: 0 auto;'><b>Report Date EST: </b>").append(formattedTimeEST).append("</p>");
        htmlBuilder.append("<p style='border-collapse: collapse;width: 80%;max-width: 600px;margin: 0 auto;'><b>Report Date IST: </b>").append(" "+formattedTimeIST).append("</p></br>");
        htmlBuilder.append("<h2 style='margin-bottom:20px;border-collapse: collapse;width: 80%;max-width: 600px;margin: 0 auto;'>Automation Test Report </h2>");

        for (JsonElement featureElement : features) {
            totalFeatures++; // Increment total features count
            JsonObject feature = featureElement.getAsJsonObject();
            String featureName = feature.get("name").getAsString();
            JsonArray scenarios = feature.getAsJsonArray("elements");
            String iconColor = "#2BF96D";
            String iconType	 = "\"fas fa-check-circle\"";	
            boolean hasFailedScenario = false;

            htmlBuilder.append("<table>");
            

            for (JsonElement scenarioElement : scenarios) {
                totalScenarios++; // Increment total scenarios count
                JsonObject scenario = scenarioElement.getAsJsonObject();
                String scenarioName = scenario.get("name").getAsString();
                JsonArray steps = scenario.getAsJsonArray("steps");
                boolean hasFailedStep = false;

                

                for (JsonElement stepElement : steps) {
                    totalSteps++; // Increment total steps count
                    JsonObject step = stepElement.getAsJsonObject();
                    JsonObject result = step.getAsJsonObject("result");
                    String status = result.get("status").getAsString();
                    if (status.equalsIgnoreCase("failed")) {
                        hasFailedStep = true;
                        iconColor="#F9502B";
                        iconType="\"fas fa-times-circle\"";
 
                        totalFailedSteps++; // Increment total failed steps count
                    } else if (status.equalsIgnoreCase("passed")) {
                        totalPassedSteps++; // Increment total passed steps count
                    }
                    else if (status.equalsIgnoreCase("skipped")) {
                        totalSkippedSteps++; // Increment total passed steps count
                    }
                }
                htmlBuilder.append("<tr><th colspan='2'>").append("<i class="+iconType+" style='color:"+iconColor+";'").append("></i>").append(" <b style='color:"+iconColor+";'>Feature: </b>").append(featureName).append("</th></tr>");
                htmlBuilder.append("<tr>");
                htmlBuilder.append("<td> <b>Scenario: </b>").append(scenarioName).append("</td>");
                if (hasFailedStep) {
                    totalFailedScenarios++;
                    hasFailedScenario = true;
                    htmlBuilder.append("<td style='background-color: #F9502B;'>FAILED</td>");
                } else {
                    totalPassedScenarios++;
                    htmlBuilder.append("<td style='background-color: #2BF96D;'>PASSED</td>");
                }

                htmlBuilder.append("</tr>");
            }

            if (hasFailedScenario) {
                totalFailedFeatures++;
            } else {
                totalPassedFeatures++;
            }

            htmlBuilder.append("</table>");
        }
        // Generate test summary table
        htmlBuilder.append("</br><h3 style='margin-bottom:20px;border-collapse: collapse;width: 80%;max-width: 600px;margin: 0 auto;'>Test Summary</h3>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>Type</th><th>Total Count</th><th style='background-color: #2BF96D;'>Total Passed</th><th style='background-color: #F9502B;'>Total Failed</th><th style='background-color: #03ADFC;'>Total Skipped</th></tr>");
        htmlBuilder.append("<tr><td>Features</td><td>").append(totalFeatures).append("</td><td>").append(totalPassedFeatures).append("</td><td>").append(totalFailedFeatures).append("</td><td>-").append("</td></tr>");
        htmlBuilder.append("<tr><td>Scenarios</td><td>").append(totalScenarios).append("</td><td>").append(totalPassedScenarios).append("</td><td>").append(totalFailedScenarios).append("</td><td>-").append("</td></tr>");
        htmlBuilder.append("<tr><td>Steps</td><td>").append(totalSteps).append("</td><td>").append(totalPassedSteps).append("</td><td>").append(totalFailedSteps).append("</td><td>").append(totalSkippedSteps).append("</td></tr>");
        htmlBuilder.append("</table>");

        // Footer
        htmlBuilder.append("<div style='width: 80%; max-width: 600px; margin: 0 auto;'>");
        htmlBuilder.append("<p style='background-color: #ffcccc; color: #ff0000; text-align: center; border-radius: 5px; padding: 10px;'><strong>Note:</strong> This is an autogenerated mail. Please do not reply to this mail.</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<footer style='border-collapse: collapse;width: 80%;max-width: 600px;margin: 0 auto;'><p>Thanks & Regards,</br>Automation Team.</p></footer>");
        
        htmlBuilder.append("</body></html>");

        // Save HTML report to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(HTML_FILE_PATH));
        writer.write(htmlBuilder.toString());
        writer.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
