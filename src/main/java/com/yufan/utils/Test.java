package com.yufan.utils;

import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/19 21:00
 * 功能介绍:
 */
public class Test {


    public static void main(String[] args) throws Exception {
        String root = "C:\\Users\\admin\\Desktop\\备份web\\品类";
        String filePath = root + "\\集合.txt";
        File file = new File(filePath);
        FileReader reader = new FileReader(file);
        BufferedReader bf = new BufferedReader(reader);
        String line = "";
        Map<String, String> maps = new HashMap<>();
        int s = 0;
        while (line != null) {
            line = bf.readLine();
            maps.put(line, line);
            s++;
        }
        System.out.println("-------------" + s);
        bf.close();
        reader.close();
        File files = new File(root + "\\goods");
        String[] list = files.list();
        System.out.println(list.length);
        for (int i = 0; i < list.length; i++) {
            String name = list[i];
            maps.remove(name);
        }

        for (Map.Entry<String, String> m : maps.entrySet()) {
            System.out.println(m.getKey());
        }
    }
//    public static void main(String[] args) throws Exception {
//        String root = "C:\\Users\\admin\\Desktop\\备份web\\品类";
//        String filePath = root + "\\集合.txt";
//        File file = new File(filePath);
//        FileReader reader = new FileReader(file);
//        BufferedReader bf = new BufferedReader(reader);
//        String line = "";
//        int count = 0;
//        while (line != null) {
//            line = bf.readLine();
//            if (!StringUtils.isEmpty(line)) {
//                count++;
////                System.out.println(line);
//                String outPath = root + "\\goods\\" + line;
//                File fileOut = new File(outPath);
//                fileOut.mkdir();
//            }
//        }
//        bf.close();
//        reader.close();
//        System.out.println(count);
//    }

}
