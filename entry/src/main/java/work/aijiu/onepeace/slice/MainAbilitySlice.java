package work.aijiu.onepeace.slice;

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.timeutility.Timer;
import work.aijiu.onepeace.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class MainAbilitySlice extends AbilitySlice {

    private static HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0x000110,"MainAbilitySlice");


    private int DELAY = 3000;
    private EventHandler handler;
    private EventRunner runner;



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().setStatusBarColor(getColor(ResourceTable.Color_statusbar));
        super.setUIContent(ResourceTable.Layout_ability_main);
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


    /**
     * 初始化
     */
    public void init(){
        runner = EventRunner.create(true);
        if(runner != null){
            handler = new EventHandler(runner);
            handler.postTask(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    present(new HomeAbilitySlice(),intent);
                    terminate();
                }
            },DELAY);
        }
    }
}
