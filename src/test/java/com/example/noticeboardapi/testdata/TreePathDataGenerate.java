package com.example.noticeboardapi.testdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class TreePathDataGenerate {
    public static void main(String[] args) {
        List<Integer> exclude = new ArrayList<>();

        String start = "insert into TREE_PATH (post_id, ancestor, descendant, depth) values ";
        int root = 0;
        start += String.format("(%d, %d, %d, %d),", 1, root, root, 0);
        for (int x = 0; x < 20; x++) {
            for (int i = 0; i < 3; i++) {
                int child = getRandomVariable(exclude);
                start += String.format("(%d, %d, %d, %d),", 1, child, child, 0);
                start += String.format("(%d, %d, %d, %d),", 1, root, child, 1);
                for (int j = 0; j < 3; j++) {
                    int grandSon = getRandomVariable(exclude);
                    start += String.format("(%d, %d, %d, %d),", 1, grandSon, grandSon, 0);
                    start += String.format("(%d, %d, %d, %d),", 1, child, grandSon, 1);
                    start += String.format("(%d, %d, %d, %d),", 1, root, grandSon, 2);
                    for (int k = 0; k < 3; k++) {
                        int grandChildSon = getRandomVariable(exclude);
                        start += String.format("(%d, %d, %d, %d),", 1, grandChildSon, grandChildSon, 0);
                        start += String.format("(%d, %d, %d, %d),", 1, grandSon, grandChildSon, 1);
                        start += String.format("(%d, %d, %d, %d),", 1, child, grandChildSon, 2);
                        start += String.format("(%d, %d, %d, %d),", 1, root, grandChildSon, 3);
                    }
                }
            }
        }

        System.out.println(start);
    }

    private static int getRandomVariable(List<Integer> exclude) {
        while (true) {
            int random = ThreadLocalRandom.current().nextInt(1,1000);
            if (!exclude.contains(random)) {
                exclude.add(random);
                return random;
            }
        }
    }
}
