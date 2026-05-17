package com.example;

public enum PortraitChoice
{
    SANGUINE_TORVA_FULL_HELM("Sanguine torva full helm", 28254),
    HARDCORE_IRONMAN_HELM("Hardcore ironman helm", 20792),
    VIRTUS_MASK("Virtus mask", 26241),
    MASORI_MASK_F("Masori mask (f)", 27235),
    MAX_CAPE("Max cape", 13280),
    INFERNAL_MAX_CAPE("Infernal max cape", 21285);

    private final String name;
    private final int itemId;

    PortraitChoice(String name, int itemId)
    {
        this.name = name;
        this.itemId = itemId;
    }

    public int getItemId()
    {
        return itemId;
    }

    @Override
    public String toString()
    {
        return name;
    }
}