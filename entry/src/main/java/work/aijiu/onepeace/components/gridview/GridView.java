package work.aijiu.onepeace.components.gridview;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.TableLayout;
import ohos.app.Context;
import work.aijiu.onepeace.components.gridview.provider.GridAdapter;

import java.util.ArrayList;
import java.util.List;

public class GridView extends TableLayout {

    private List<Component> list = new ArrayList<>();

    public GridView(Context context) {
        super(context);
    }

    public GridView(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public GridView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }




    /**
     * The setAdapter
     *
     * @param adapter             adapter
     * @param longClickedListener longClickedListener
     */
    public void setAdapter(GridAdapter adapter, LongClickedListener longClickedListener, ClickedListener clickedListener) {
        removeAllComponents();
        this.list = adapter.getComponentList();
        for (int i = 0; i < adapter.getComponentList().size(); i++) {
            adapter.getComponentList().get(i).setLongClickedListener(longClickedListener);
            adapter.getComponentList().get(i).setClickedListener(clickedListener);
            addComponent(adapter.getComponentList().get(i));
        }
    }
}
