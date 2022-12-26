package com.github.Deadcoder11u2.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(name="methods", description = "Lists all the methods in the Solution.java file", mixinStandardHelpOptions = true)
public class ShowMethods implements Runnable {

    String HEADER = "\033[95m";
    String OKBLUE = "\033[94m";
    String OKCYAN = "\033[96m";
    String OKGREEN = "\033[92m";
    String WARNING = "\033[93m";
    String FAIL = "\033[91m";
    String ENDC = "\033[0m";
    String BOLD = "\033[1m";
    String UNDERLINE = "\033[4m";

    @Option(names = {"-d", "--decoration"}, description = "To disable colors of the output")
    boolean decoration;

    @Override
    public void run() {
        File methods = new File("methods.leetcode");
        if(methods.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(methods));
                String line;
                int cnt = 1;
                while((line = br.readLine()) != null) {
                    if(!decoration) {
                        System.out.println(cnt + " -> "+ line);
                    }
                    else {
                        System.out.println(OKCYAN + cnt + " -> "+ line + ENDC);
                    }
                    cnt++;
                }
                br.close();
            }
            catch(Exception e) {
                String output = "Error while reading the file delete the methods file and generate it again" ;
                if(!decoration) {
                    output = FAIL + output + ENDC;
                }
                System.out.println(output);
            }
        }
        else {
            String output = "Use the init command to generate methods" ;
            if(!decoration) {
                output = FAIL + output + ENDC;
            }
            System.out.println(output);
        }
    }
}
