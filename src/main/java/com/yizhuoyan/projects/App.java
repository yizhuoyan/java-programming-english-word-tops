package com.yizhuoyan.projects;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Hello world!
 */
public class App {
    static Map<String, Integer> WORD_MAP = new HashMap<>(0xffff);

    public static void main(String[] args) throws Exception {
        zipFileRead("f:/src.zip");
       //System.out.println(camalSplit("abc"));
    }

    static public void zipFileRead(String file) throws Exception {
        ZipFile zipFile = new ZipFile(file);
        int total=zipFile.size();
        int current=0;
        Enumeration<ZipEntry> enu = (Enumeration<ZipEntry>) zipFile.entries();
        while (enu.hasMoreElements()) {
            ZipEntry zipElement = (ZipEntry) enu.nextElement();
            current++;
            System.out.println(current+"/"+total+"="+zipElement.getName());
            if (zipElement.isDirectory()) {
                continue;
            }

            InputStream read = zipFile.getInputStream(zipElement);
            handlerOne(read, WORD_MAP);
        }
        List<Map.Entry<String, Integer>> list = sortMap(WORD_MAP);
        PrintWriter writer=new PrintWriter("f:/500.ini","utf-8");

        for (Map.Entry<String, Integer> row:list){
            writer.print(row.getKey());
            writer.print("=");
            writer.println(row.getValue());

        }
        writer.close();



    }

    public static void handlerOne(InputStream in, Map<String, Integer> map) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;

        List<String> words;
        Integer count = 0;
        while ((line = br.readLine()) != null) {
            words=splitWords(line);
            for (String word : words) {
                word=word.toLowerCase();
                if (word.length() > 2) {
                    if ((count = map.get(word)) != null) {
                        map.put(word, new Integer(count + 1));
                    } else {
                        map.put(word, new Integer(1));
                    }
                }

            }
        }

    }

    public  static List<String>  splitWords(String line){
        String[] words = line.split("[^a-zA-Z]+");
        List<String> result=new ArrayList<>(words.length<<1);

        for (String word : words) {
            if(word.length()==0){
                continue;
            }

            //驼峰命名分割分割
            result.addAll(camalSplit(word));
        }
        return result;
    }
    private  static List<String> camalSplit(String word){
        int a=0,b=0,max=word.length();
        List<String> result=new LinkedList<>();
        while (b<max){
            if(Character.isUpperCase(word.charAt(b))){
                if(a<b) {
                    result.add(word.substring(a, b));
                }
                a=b;
            }
            b++;
        }
        if(a<b) {
            result.add(word.substring(a, b));
        }
        return  result;
    }


    public static List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map) throws IOException {


        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.size());
        list.addAll(map.entrySet());

        Collections.sort(list, (a, b) -> b.getValue() - a.getValue());


        return list;
    }

}
