package work.aijiu.onepeace.components.gridview.provider;

import ohos.agp.components.*;
import ohos.app.Context;
import work.aijiu.onepeace.ResourceTable;
import work.aijiu.onepeace.components.gridview.model.GridItemInfo;
import work.aijiu.onepeace.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The GridAdapter
 */
public class GridAdapter {
    private static final int GRID_LAYOUT_BORDER_MARGIN = 10;
    private static final int GRID_ITEM_RIGHT_MARGIN = 8;

    private final List<Component> componentList = new ArrayList<>();

    public GridAdapter(Context context, List<GridItemInfo> itemInfos) {
        int itemPx = getItemWidthByScreen(context);
        for (GridItemInfo item : itemInfos) {
            Component gridItem = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_grid_item, null, false);
            gridItem.setId(item.getIndex());
            if (gridItem.findComponentById(ResourceTable.Id_grid_item_image) instanceof Image) {
                Image imageItem = (Image) gridItem.findComponentById(ResourceTable.Id_grid_item_image);
                imageItem.setPixelMap(item.getIconId());
            }
            if (gridItem.findComponentById(ResourceTable.Id_grid_item_text) instanceof Text) {
                Text textItem = (Text) gridItem.findComponentById(ResourceTable.Id_grid_item_text);
                textItem.setText(item.getItemText());
            }
            gridItem.setWidth(itemPx);
            gridItem.setHeight(itemPx);
            gridItem.setMarginRight(AttrHelper.vp2px(GRID_ITEM_RIGHT_MARGIN, context));
            componentList.add(gridItem);
        }
    }

    /**
     * method for get componentList
     *
     * @return componentList
     */
    public List<Component> getComponentList() {
        return componentList;
    }

    private int getItemWidthByScreen(Context context) {
        int screenWidth = AppUtils.getScreenInfo(context).getPointXToInt();

        return (screenWidth
                - AttrHelper.vp2px(GRID_LAYOUT_BORDER_MARGIN, context) * 2
                - AttrHelper.vp2px(GRID_ITEM_RIGHT_MARGIN, context) * 3)
                / AppUtils.getIntResource(context, 3);
    }
}
