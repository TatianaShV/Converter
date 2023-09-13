package ru.netology.graphics.image;

public class Schema implements TextColorSchema{
    public char[] symbols = {'$', '@', '%', '#',  '*', '+', '^', '-'};
    @Override
    public char convert(int color) {
        int x = color / 32;
        return symbols[x];

    }
}
