package work.aijiu.onepeace.components.gridview.model;

/**
 * Grid item model
 */
public class GridItemInfo {
    private final String itemText;
    private final int iconId;
    private final int index;

    /**
     * Item data model Constructor
     *
     * @param itemText item text
     * @param iconId   image resource ID
     * @param index      int index
     */
    public GridItemInfo(String itemText, int iconId, int index) {
        this.itemText = itemText;
        this.iconId = iconId;
        this.index = index;
    }

    public String getItemText() {
        return itemText;
    }

    public int getIconId() {
        return iconId;
    }

    public int getIndex() {
        return index;
    }
}
