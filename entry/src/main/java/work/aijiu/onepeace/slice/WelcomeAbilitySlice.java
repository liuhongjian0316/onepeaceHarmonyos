package work.aijiu.onepeace.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import work.aijiu.onepeace.ResourceTable;

public class WelcomeAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().setStatusBarColor(getColor(ResourceTable.Color_statusbar));
        super.setUIContent(ResourceTable.Layout_ability_welcome);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
