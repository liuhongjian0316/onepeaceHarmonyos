package work.aijiu.onepeace;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import work.aijiu.onepeace.slice.MainAbilitySlice;

public class WelcomeAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(WelcomeAbility.class.getName());
    }
}
