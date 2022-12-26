package com.github.Deadcoder11u2.utils;

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "fetch", description = "Command to generate the solution file from leetcode", mixinStandardHelpOptions = true)
public class FetchProblem implements Runnable {
    final    String HEADER = "\033[95m";
    final    String OKBLUE = "\033[94m";
    final    String OKCYAN = "\033[96m";
    final    String OKGREEN = "\033[92m";
    final    String WARNING = "\033[93m";
    final    String FAIL = "\033[91m";
    final    String ENDC = "\033[0m";
    final    String BOLD = "\033[1m";
    final    String UNDERLINE = "\033[4m";

    @Option(names = {"-u", "--url"}, description = "URL for the leetcode problem to fetch")
    String url;

    @Option(names = {"-v", "--verbose"}, description = "verbose")
    boolean verbose;

    @Option(names = {"-d", "--decoration"}, description = "To disable colors of the output")
    boolean decoration;

    @Override
    public void run() {
        try {
            if(verbose) {
                String output = "API call initiated" ;
                if(!decoration)
                    output = WARNING + output + ENDC;
                System.out.println(output);
            }
            HashMap<String, String> data = fetchProblem(url);
            if(verbose){
                String output = "Problem Fetched" ;
                if(!decoration) {
                    output = WARNING + output + ENDC;
                }
                System.out.println(output);
            }
            File solutionFile = new File("Solution.java");
            PrintWriter pw = new PrintWriter(solutionFile);
            pw.write(data.get("code"));
            pw.close();
            File testFile = new File("methods1.test");
            pw = new PrintWriter(testFile);
            pw.write(data.get("testCase"));
            pw.close();
            if(verbose) {
                String output = "All files created" ;
                if(!decoration) {
                    output = WARNING + output + ENDC;
                }
                System.out.println(output);
            }
            String output = " Fetched the problem Solution file created" ;
            if(!decoration) {
                output = OKGREEN + output + ENDC;
            }
            System.out.println(output);
        }
        catch(Exception e) {
             System.out.println("Failed to fetch the problem check the url again");
        }
    }

    final String QUERY = "{\n\t\"variables\": {\n\t\t\"titleSlug\": \"%s\"\n\t},\n\t\"query\": \"query questionData($titleSlug: String!) { question(titleSlug: $titleSlug) { codeSnippets { lang langSlug code __typename } hints exampleTestcases sampleTestCase } }\"\n}";

    public  String getBody(String url) throws Exception {
        Pattern pat = Pattern.compile("problems\\/.*");
        Matcher match = pat.matcher(url);
        String problemSlug = "";
        match.find();
        problemSlug = match.group(0);
        return String.format(QUERY, problemSlug.substring(9, problemSlug.length() - 1));
    }

    public  HashMap<String, String> fetchProblem(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://leetcode.com/graphql"))
            .header("Content-Type", "application/json")
            .method("GET", HttpRequest.BodyPublishers.ofString(getBody(url))).build();
        HttpClient response = HttpClient.newBuilder().followRedirects(Redirect.ALWAYS).build();
        HttpResponse<String> res = response.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(res.body());
        JSONObject questionData = json.getJSONObject("data").getJSONObject("question");
        HashMap<String, String> data = new HashMap<>();
        data.put("testCase", questionData.getString("exampleTestcases"));
        data.put("code", questionData.getJSONArray("codeSnippets").getJSONObject(1).getString("code"));
        return data;
    }
}
