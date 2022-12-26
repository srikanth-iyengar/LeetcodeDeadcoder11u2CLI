package com.github.Deadcoder11u2.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "run", description = "Runs the program against the given testcases")
public class RunTestcase implements Runnable{
    final    String HEADER = "\033[95m";
    final    String OKBLUE = "\033[94m";
    final    String OKCYAN = "\033[96m";
    final    String OKGREEN = "\033[92m";
    final    String WARNING = "\033[93m";
    final    String FAIL = "\033[91m";
    final    String ENDC = "\033[0m";
    final    String BOLD = "\033[1m";
    final    String UNDERLINE = "\033[4m";

    @Option(names = {"-d", "--decoration"}, description = "To disable colors of the output")
    boolean decoration;

    @Override
    public void run() {
        String args[];
        String OPERATING_SYSTEM = System.getProperty("os.name");
        if(OPERATING_SYSTEM.startsWith("Windows")) {
            args = new String[]{"cmd", "/c"};
        }
        else {
            args = new String[]{"bash", "-c"};
        }
        try {
            Process process = new ProcessBuilder(args[0], args[1], "java Main").start();
            process.waitFor(5, TimeUnit.SECONDS);
            if(process.exitValue() != 0) {
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while((line =br.readLine()) != null) {
                    if(!decoration) {
                        System.out.println(WARNING + line + ENDC);
                    }
                    else {
                        System.out.println(line);
                    }
                }
                br.close();
                throw new RuntimeException();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = br.readLine()) != null) {
                if(!decoration) {
                    System.out.println(OKGREEN + line + ENDC);
                }
                else {
                    System.out.println(line);
                }
            }
            br.close();
            if(!decoration) {
                System.out.println(OKGREEN + " Tests run successfully" + ENDC);
            }
            else {
                System.out.println(" Tests run successfully");
            }
        }
        catch(Exception e) {
            if(!decoration) {
                System.out.println(FAIL + "Program took more than 5 seconds to run OR Runtime exception" + ENDC);
            }
            else {
                System.out.println("Program took more than 5 seconds to run OR Runtime exception");
            }
        }
    }
}
