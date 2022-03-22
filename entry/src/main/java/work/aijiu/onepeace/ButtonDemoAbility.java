package work.aijiu.onepeace;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import work.aijiu.onepeace.slice.ButtonDemoAbilitySlice;

public class ButtonDemoAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ButtonDemoAbilitySlice.class.getName());
    }
}
