package ru.otus;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {

        List<String> words = new ArrayList<>();

        words.add("a");
        words.add("a");
        words.add("b");
        words.add("c");
        words.add("d");
        words.add("e");
        words.add("c");
        words.add("e");

        Multiset<String> wordsMultiset = HashMultiset.create();
        wordsMultiset.addAll(words);

        System.out.println(wordsMultiset);

    }
}
