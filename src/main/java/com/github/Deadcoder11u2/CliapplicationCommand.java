package com.github.Deadcoder11u2;

import com.github.Deadcoder11u2.utils.AddTestCase;
import com.github.Deadcoder11u2.utils.CompileSolution;
import com.github.Deadcoder11u2.utils.RunTestcase;
import com.github.Deadcoder11u2.utils.ShowMethods;

import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "leetcode", description = "...",
mixinStandardHelpOptions = true, subcommands = {CompileSolution.class, ShowMethods.class, AddTestCase.class, RunTestcase.class})
public class CliapplicationCommand implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(CliapplicationCommand.class, args);
    }

    public void run() {
        if (verbose) {
            System.out.println("Hi!");
        }
    }
}
