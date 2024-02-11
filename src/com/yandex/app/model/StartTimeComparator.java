package com.yandex.app.model;

import java.util.Comparator;

public class StartTimeComparator implements Comparator<Task> {


        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getStartTime() != null) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            } else {
                return 1;
            }
        }
}
