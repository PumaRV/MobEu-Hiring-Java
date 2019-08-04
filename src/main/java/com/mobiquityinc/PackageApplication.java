package com.mobiquityinc;

import com.mobiquityinc.packer.Packer;

public class PackageApplication {

    public static void main(String[] args){
        try {
            Packer.pack("C:\\Users\\Puma\\Documents\\Mobiquity\\correct.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
