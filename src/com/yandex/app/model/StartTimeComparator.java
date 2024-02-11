package com.yandex.app.model;

import java.util.Comparator;

public class StartTimeComparator implements Comparator<Task> {


    @Override
    public int compare(Task o1, Task o2) {
        //Проверка на null
        if(o1.getDuration() < 0) {
            return -1;

        }else if(o2.getDuration() < 0) {
            return -1;
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    }
}
