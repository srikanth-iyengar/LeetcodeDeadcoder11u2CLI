package com.github.Deadcoder11u2.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import picocli.CommandLine.Option;

public class InitializeGenerator implements Runnable{
    String HEADER = "\033[95m";
    String OKBLUE = "\033[94m";
    String OKCYAN = "\033[96m";
    String OKGREEN = "\033[92m";
    String WARNING = "\033[93m";
    String FAIL = "\033[91m";
    String ENDC = "\033[0m";
    String BOLD = "\033[1m";
    String UNDERLINE = "\033[4m";
    final String GENERATOR_CODE = "import java.lang.reflect.Method;"+"import java.lang.reflect.Parameter;"+"import java.util.ArrayList;"+"import java.util.TreeSet;"+"public class Generator {"+"    public final static TreeSet<String> TO_IGNORE;"+""+"    static {"+"        TO_IGNORE = new TreeSet<>();"+"        TO_IGNORE.add(\"wait\");"+"        TO_IGNORE.add(\"equals\");"+"        TO_IGNORE.add(\"toString\");"+"        TO_IGNORE.add(\"hashCode\");"+"        TO_IGNORE.add(\"getClass\");"+"        TO_IGNORE.add(\"notify\");"+"        TO_IGNORE.add(\"notifyAll\");"+"    }"+"    public static void main(String[] args) {"+"        Solution sol = new Solution();"+"        Class cls = sol.getClass();"+"        ArrayList<Method> meths = new ArrayList<>();"+"        for(Method meth: cls.getMethods()) {"+"            if(!TO_IGNORE.contains(meth.getName())) {"+"                meths.add(meth);"+"            }"+"        }"+"        meths.forEach(meth -> {"+"            System.out.print(meth.getName() + \"->{\");"+"            Parameter params[] = meth.getParameters();"+"            for(Parameter param: params) {"+"                System.out.print(param.getType()+\",\");"+"            };"+"            System.out.println(\"}\");"+"        });"+"   }"+"}";


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
        File f = new File("Generator.java");
        try {
            PrintWriter pw = new PrintWriter(f);
            pw.write(GENERATOR_CODE);
            pw.close();
            Process process = new ProcessBuilder(args[0], args[1], "javac Generator.java && java Generator").start();
            process.waitFor();
            if(process.exitValue() != 0) {
                throw new RuntimeException();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            File methodsFile = new File("methods.leetcode");
            pw = new PrintWriter(methodsFile);
            while((line = br.readLine()) != null) {
                pw.write(line);
                pw.write("\n");
            }
            pw.close();
            String output[] = new String[]{" Initialized methods", " HINT: Use the tc command to add the testcases"};
            for(String s : output) {
                if(!decoration) {
                    s = OKGREEN + s + ENDC;
                }
                System.out.println(s);
            }
        }
        catch(Exception e) {
            System.out.println("❌ Error while generating required files ❌");
            System.out.println("💀 Check if you have the Solution.java in the directory 📝");
        }
    }
}
