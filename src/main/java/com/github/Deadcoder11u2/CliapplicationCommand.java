package com.github.Deadcoder11u2;

import com.github.Deadcoder11u2.utils.AddTestCase;
import com.github.Deadcoder11u2.utils.CompileSolution;
import com.github.Deadcoder11u2.utils.FetchProblem;
import com.github.Deadcoder11u2.utils.RunTestcase;
import com.github.Deadcoder11u2.utils.ShowMethods;

import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "leetcode", description = "...",
mixinStandardHelpOptions = true, 
subcommands = {CompileSolution.class, ShowMethods.class, AddTestCase.class, RunTestcase.class, FetchProblem.class}
)
public class CliapplicationCommand implements Runnable {
    final    String HEADER = "\033[95m";
    final    String OKBLUE = "\033[94m";
    final    String OKCYAN = "\033[96m";
    final    String OKGREEN = "\033[92m";
    final    String WARNING = "\033[93m";
    final    String FAIL = "\033[91m";
    final    String ENDC = "\033[0m";
    final    String BOLD = "\033[1m";
    final    String UNDERLINE = "\033[4m";

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    @Option(names = {"-d", "--decoration"}, description = "To disable colors of the output")
    boolean decoration;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(CliapplicationCommand.class, args);
    }

    public void run() {
        System.out.println(OKGREEN + "A cli application made by Srikanth Iyengar( Deadcoder11u2) to fetch the leetcode problem" + ENDC);
        System.out.println(OKGREEN + "Create solution file for the leetcode problem and test against the sample testcase locally" + ENDC);
        System.out.println(OKGREEN + " Currently only java language is supported " + ENDC);
    }
}
