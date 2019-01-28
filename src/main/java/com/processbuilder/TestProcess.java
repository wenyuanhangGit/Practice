package com.processbuilder;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class TestProcess {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] cmd = new String[]{
                "D:\\Program Files\\nodejs\\npm.cmd",
                "run",
                "debug"
        };
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(new File("D:\\phpstormProjects\\client-caiji"));
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();


        /*String[] cmd = new String[]{
                "D:\\Program Files\\nodejs\\node.exe",
                "standard_input_output.js"
        };
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(new File("F:\\nodejs\\example"));
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();*/



        OutputStream os = p.getOutputStream();
        os.write("这次可以写到文件吗？我收到了!".getBytes());
        os.flush();
        os.close();

    }

}
