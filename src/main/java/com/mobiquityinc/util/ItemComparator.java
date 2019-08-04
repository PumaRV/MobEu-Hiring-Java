package com.mobiquityinc.util;

import com.mobiquityinc.model.Item;

import java.util.Comparator;

public class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2){
        final Integer weightValueRatio1 = item1.getWeight();
        final Integer weightValueRatio2 = item2.getWeight();

        return weightValueRatio1.compareTo(weightValueRatio2);
    }
}
