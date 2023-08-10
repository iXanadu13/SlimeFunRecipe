package pers.xanadu.slimefunrecipe.gui;

import java.lang.reflect.Field;

public enum GUISize {
    size_1x4,
    size_3x3,
    size_6x6,
    unknown;
    public static GUISize of(String str){
        String name = "size_"+str.toLowerCase();
        try{
            Field field = GUISize.class.getField(name);
            return (GUISize) field.get(null);
        } catch (ReflectiveOperationException e) {
            return unknown;
        }
    }
}
