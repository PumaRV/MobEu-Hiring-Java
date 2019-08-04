package com.mobiquityinc;

import com.mobiquityinc.packer.Packer;

import java.io.InputStream;
import java.util.Properties;

public class PackageApplication {

    public static void main(String[] args){
        try {
            InputStream input = PackageApplication.class.getClassLoader().getResourceAsStream("application.properties");
            final Properties properties = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find application properties");
                return;
            }
            properties.load(input);
            System.out.println(Packer.pack(properties.getProperty("input.file.path")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
