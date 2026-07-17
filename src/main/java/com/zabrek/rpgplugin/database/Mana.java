package com.zabrek.rpgplugin.database;

public class Mana {
    private int maxMana = 100;
    private int mana;

    public Mana() {
        this.mana = maxMana;
    }

    public int getMaxMana() { return maxMana; }
    public void setMaxMana(int newQuantity) { maxMana = newQuantity; }

    public int getMana() { return mana; }
    private void setMana(int newQuantity) { mana = newQuantity; }

    public void addMana(int addMana) {
        int res = mana + addMana;
        setMana(Math.min(maxMana, res));
    }

    public void subMana(int subMana) {
        int res = mana - subMana;
        setMana(Math.max(res, 0));
    }
}
