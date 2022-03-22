package work.aijiu.onepeace.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.utils.MimeData;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import work.aijiu.onepeace.ResourceTable;
import work.aijiu.onepeace.components.gridview.GridView;
import work.aijiu.onepeace.components.gridview.model.GridItemInfo;
import work.aijiu.onepeace.components.gridview.provider.GridAdapter;
import work.aijiu.onepeace.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeAbilitySlice extends AbilitySlice {

    private static HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0x000110,"HomeAbilitySlice");

    private GridView gridView = null;
    private final Component.LongClickedListener longClickListener =
            component -> {
                // 长按事件
            };
    private final Component.ClickedListener clickedListener =
            component -> {
                int id = component.getId();
                if (id == 0){
                    // 取消进出场动画
                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("work.aijiu.onepeace")
                            .withAbilityName("work.aijiu.onepeace.ButtonDemoAbility")
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
                }
            };



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().setStatusBarColor(getColor(ResourceTable.Color_statusbarhome));
        super.setUIContent(ResourceTable.Layout_ability_home);
        init();


    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    public void init(){
        gridView = (GridView) findComponentById(ResourceTable.Id_grid_view_up);
        List<GridItemInfo> upperItemList = new ArrayList<>();
        upperItemList.add(new GridItemInfo("按钮",ResourceTable.Media_logo,0));
        upperItemList.add(new GridItemInfo("弹出框",ResourceTable.Media_logo,1));
        upperItemList.add(new GridItemInfo("布局",ResourceTable.Media_logo,2));
        upperItemList.add(new GridItemInfo("空页面",ResourceTable.Media_logo,3));
        GridAdapter adapter = new GridAdapter(this.getContext(), upperItemList);
        gridView.setAdapter(adapter, longClickListener,clickedListener);
    }
}
