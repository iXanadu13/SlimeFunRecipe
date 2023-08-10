package pers.xanadu.slimefunrecipe.gui;

import pers.xanadu.slimefunrecipe.utils.GuiUtils;

public class GUI3x3 extends GUI{
    public GUI3x3(String title) {
        super(title);
        capacity = 9;
    }
    @Override
    public int getChosen(){
        return chosen;
    }
    @Override
    public boolean setChosen(int slot){
        int j=(index-1)*9+slot-36;
        if(j>=items.size()) return false;
        chosen=j;
        updateChosen();
        return true;
    }
    @Override
    protected void updateChosen(){
        for(int i=0;i<9;i++){
            int j=(index-1)*9+i;
            this.replaceExistingItem(i+45,j==chosen? GuiUtils.getItem_chosen() : GuiUtils.getItem_not_chosen());
        }
    }
    @Override
    protected void fillPagedItems(int page){
        for(int i=0;i<9;i++){
            int j=(page-1)*9+i;
            this.replaceExistingItem(i+36,j<items.size()? items.get(j):GuiUtils.getBackground());
            this.replaceExistingItem(i+45,j==chosen? GuiUtils.getItem_chosen() : GuiUtils.getItem_not_chosen());
        }
    }
    public int getSize(){
        return (items.size()-1)/9+1;
    }
    public int getCancelButtonIndex(){
        return 0;
    }
    public int getVerifyButtonIndex(){
        return 8;
    }
    public int getMachineIndex(){
        return 10;
    }
    public int getItemShowIndex(){
        return 16;
    }
    public int getPrevPageButtonIndex(){
        return 28;
    }
    public int getNextPageButtonIndex(){
        return 34;
    }
}
