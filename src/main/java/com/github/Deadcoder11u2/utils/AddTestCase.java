package com.github.Deadcoder11u2.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.annotation.processing.SupportedOptions;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "tc", description = "Adding testcase for the problem")
public class AddTestCase implements Runnable{
    static HashMap<String, String> paramMapper;
    static {
        paramMapper = new HashMap<>();
        paramMapper.put("int", "int");
        paramMapper.put("I", "int");
        paramMapper.put("J", "long");
        paramMapper.put("F", "float");
        paramMapper.put("S", "short");
        paramMapper.put("Z", "boolean");
        paramMapper.put("C", "char");
        paramMapper.put("B", "byte");
        paramMapper.put("String", "String");
        paramMapper.put("Long", "Long");
        paramMapper.put("Integer", "Integer");
        paramMapper.put("Double", "Double");
        paramMapper.put("Character", "Character");
        paramMapper.put("Byte", "Byte");
    }
    final    String HEADER = "\033[95m";
    final    String OKBLUE = "\033[94m";
    final    String OKCYAN = "\033[96m";
    final    String OKGREEN = "\033[92m";
    final    String WARNING = "\033[93m";
    final    String FAIL = "\033[91m";
    final    String ENDC = "\033[0m";
    final    String BOLD = "\033[1m";
    final    String UNDERLINE = "\033[4m";
    final    String code =
        "import java.util.*;"+
        ""+
        "public class Main {" +
        "public static void main(String args[]) {"+
        "   Solution sol = new Solution();"+
        "   %s"+
        "}"+
        "}";
    int method = 1;

    int arguments = 1;

    @Option(names = {"-v", "--verbose"}, description = "Show all the scripts")
    boolean verbose;

    @Option(names = {"-d", "--decoration"}, description = "To disable colors of the output")
    boolean decoration;

    int tc = 1;

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
        File testFile = new File("methods" + method + ".test");
        File mainFile = new File("Main.java");
        Path path = Paths.get(testFile.getName());
        int lines = 1;
        try {
            lines = (int)Files.lines(path).count();
        }
        catch(Exception e) {
            System.out.println("Line number exception");
            throw new RuntimeException();
        }
        try {
            mainFile.createNewFile();
            StringBuilder sb = new StringBuilder();
            HashMap<String, String> arg = fetchMethod();
            for(String key: arg.keySet()) {
                if(key.equals("methodName")) continue;
                sb.append(arg.get(key) + key + ";");
            }
            tc = lines/arguments;
            BufferedReader br = new BufferedReader(new FileReader(testFile));
            for(int test= 0 ; test < tc ; test++) {
                for(int i = 0; i < arguments ; i++) {
                    String s = br.readLine().replace("[", "{").replace("]", "}");
                    sb.append("sol=new Solution();");
                    if(isArray.get("arg"+i)) {
                        sb.append("arg" + i  + "="+ "new " + arg.get("arg" + i) +s + ";");
                    }else {
                        sb.append("arg" + i + "=" + s + ";") ;
                    }
                }
                String call = "System.out.println(sol." + arg.get("methodName") + "(";
                for(int i = 0 ; i < arguments ; i++) {
                    call += "arg" + i;
                    if(i != arguments - 1) {
                        call += ",";
                    }
                }
                call += "));";
                sb.append(call);
                String code_final = code;
                code_final = String.format(code_final, sb.toString());
                PrintWriter pw = new PrintWriter(mainFile);
                pw.write(code_final);
                pw.close();
            }
            br.close();
            Process process = new ProcessBuilder(args[0], args[1], "javac Main.java").start();
            process.waitFor();
            if(process.exitValue() != 0) throw new RuntimeException();
            String output = "Use the run to test solution against the testcases" ;
            if(!decoration) {
                output = OKBLUE + output + ENDC;
            }
            System.out.println(output);
        }
        catch(Exception e) {
            String output = "Something went wrong" ;
            if(!decoration) {
                output = FAIL + output + ENDC;
            }
            System.out.println(output);
        }
    }

    HashMap<String, Boolean> isArray = new HashMap<>();

    HashMap<String, String> fetchMethod() {
        HashMap<String, String> mp = new HashMap<>();
        File f = new File("methods.leetcode");
        String str = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            for(int i = 0 ; i < method; i++) {
                str = br.readLine();
            }
            br.close();
            String splits[] = str.split("->");
            mp.put("methodName", splits[0]);
            String param = splits[1];
            param = param.replace("{", "").replace("}", "").replace("Ljava.lang.", "").replace("class ", "").replace(";", "").replace("java.lang.", "");
            String params[] = param.split(",");
            int paramCount = 0;
            for(String par: params) {
                int dimension = 0;
                int i;
                for(i = 0 ; i < par.length() ; i++) {
                    if(par.charAt(i) == '[') dimension++;
                    else break;
                }
                par = par.substring(i);
                String dataType = paramMapper.get(par);
                dataType += " ";
                dataType += "[]".repeat(dimension);
                mp.put("arg"+paramCount, dataType);
                if(dimension > 0) {
                    isArray.put("arg" + paramCount, true);
                }
                else {
                    isArray.put("arg" + paramCount, false);
                }
                paramCount++;
            }
            arguments = paramCount;
        }
        catch(Exception e) {
            String output = "Method doesn't exist" ;
            if(!decoration) {
                output = FAIL + output + ENDC;
            }
            System.out.println(output);
            System.exit(-1);
        }
        return mp;
    }
}
