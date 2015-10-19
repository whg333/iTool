package com.why.tool.nativeCollection;

import org.apache.commons.lang.ArrayUtils;

public class HArrayUtil {

    @SuppressWarnings("unchecked")
    public static <T> T[] merge(T[] ...arrays) {
        T[] mergeArrays = null;
        if(arrays==null){
            return null;
        }
        for(int i=0;i<arrays.length;i++){
            mergeArrays = (T[])ArrayUtils.addAll(mergeArrays, arrays[i]);
        }
        return mergeArrays;
    }

}
