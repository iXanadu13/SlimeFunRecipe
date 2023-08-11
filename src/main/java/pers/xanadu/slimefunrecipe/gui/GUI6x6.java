package pers.xanadu.slimefunrecipe.gui;

import pers.xanadu.slimefunrecipe.utils.GuiUtils;

public class GUI6x6 extends GUI{
    public GUI6x6(String title,GUIType type) {
        super(title,type);
        capacity = 36;
    }
    public GUI6x6(String title) {
        super(title);
        capacity = 36;
    }
    @Override
    public int getChosen(){
        return chosen;
    }
    @Override
    public boolean setChosen(int slot){
        int j=(index-1)*6+slot/9;
        if(j>=items.size()) return false;
        chosen=j;
        updateChosen();
        return true;
    }
    @Override
    protected void updateChosen(){
        for(int i=0;i<6;i++){
            int j=(index-1)*6+i;
            this.replaceExistingItem(i*9+6,j==chosen? GuiUtils.getItem_chosen() : GuiUtils.getItem_not_chosen());
        }
    }
    @Override
    protected void fillPagedItems(int page){
        for(int i=0;i<6;i++){
            int j=(page-1)*6+i;
            this.replaceExistingItem(i*9+7,j<items.size()? items.get(j):GuiUtils.getBackground());
            this.replaceExistingItem(i*9+6,j==chosen? GuiUtils.getItem_chosen() : GuiUtils.getItem_not_chosen());
        }
    }
    public int getSize(){
        return (items.size()-1)/6+1;
    }
    public int getCancelButtonIndex(){
        return 8;
    }
    public int getVerifyButtonIndex(){
        return 53;
    }
    public int getMachineIndex(){
        return 35;
    }
    public int getItemShowIndex(){
        return 26;
    }
    public int getPrevPageButtonIndex(){
        return 17;
    }
    public int getNextPageButtonIndex(){
        return 44;
    }
    public int[] getBlack_border(){
        return GuiUtils.black_border_6x6;
    }
    public int[] getGray_border(){
        return GuiUtils.gray_border_6x6;
    }
    public int[] getItemSlots(){
        return GuiUtils.item_slot_6x6;
    }
}
