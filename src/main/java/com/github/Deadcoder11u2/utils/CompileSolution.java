package com.github.Deadcoder11u2.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import picocli.CommandLine.Command;

@Command(name = "compile", description = "Compile the solution file with testcases",
mixinStandardHelpOptions = true)
final public class CompileSolution implements Runnable {

    String HEADER = "\033[95m";
    String OKBLUE = "\033[94m";
    String OKCYAN = "\033[96m";
    String OKGREEN = "\033[92m";
    String WARNING = "\033[93m";
    String FAIL = "\033[91m";
    String ENDC = "\033[0m";
    String BOLD = "\033[1m";
    String UNDERLINE = "\033[4m";
    @Override
    public void run() {
        try {
            String args[];
            String OPERATING_SYSTEM = System.getProperty("os.name");
            if(OPERATING_SYSTEM.startsWith("Windows")) {
                args = new String[]{"cmd", "/c"};
            }
            else {
                args = new String[]{"bash", "-c"};
            }
            try {
                Process process = new ProcessBuilder(args[0], args[1], "javac Solution.java").start();
                process.waitFor();
                if(process.exitValue() != 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line ;
                    while((line = br.readLine()) != null) {
                        System.out.println(FAIL + line + ENDC);
                    }
                    throw new RuntimeException("Compilation error");
                }
                System.out.println(OKGREEN + " Compilation Successfull" + ENDC);
                Thread t1 = new Thread(new InitializeGenerator());
                t1.start();
            }
            catch(Exception e) {
                System.out.println("💀 Something went wrong 💀");
                System.out.println("See the above trace for debugging 📝");
            }
        }
        catch(Exception e ) {

        }
    }
}
