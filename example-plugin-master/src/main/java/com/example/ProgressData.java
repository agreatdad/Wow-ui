package com.example;

public class ProgressData
{
    public final String text;
    public final int current;
    public final int max;

    public ProgressData(String text, int current, int max)
    {
        this.text = text;
        this.current = current;
        this.max = max;
    }
}