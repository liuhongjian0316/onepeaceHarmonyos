package work.aijiu.onepeace.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import work.aijiu.onepeace.ResourceTable;
import work.aijiu.onepeace.components.SwitchIconButton.SwitchIconButton;

import java.util.Timer;
import java.util.TimerTask;

public class ButtonDemoAbilitySlice extends AbilitySlice {

    private static HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0x000110,"MainAbilitySlice");
    private Image backhome = null;
    private Button cutdown_btn = null;
    private Timer timer = new Timer();
    private int currentSecond = 60;
    private boolean countDownStatus = true;

    private SwitchIconButton sib = null;
    private Text mac_status = null;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().setStatusBarColor(getColor(ResourceTable.Color_statusbarhome));
        super.setUIContent(ResourceTable.Layout_ability_button_demo);
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
        backhome = (Image) findComponentById(ResourceTable.Id_title_area_home_icon);
        backhome.setClickedListener(component ->  {
            terminateAbility();
        });
        cutdown_btn= (Button) findComponentById(ResourceTable.Id_cutdown_btn);
        cutdown_btn.setClickedListener(component -> {
            if(this.countDownStatus && this.currentSecond > 0){
                // 开始倒计时
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(currentSecond >0){
                            countDownStatus = false;
                            currentSecond--;
                            getUITaskDispatcher().asyncDispatch(new Runnable(){
                                @Override
                                public void run() {
                                    cutdown_btn.setText(currentSecond+"s");
                                }
                            });
                        }else{
                            countDownStatus = true;
                            currentSecond = 60;
                            getUITaskDispatcher().asyncDispatch(new Runnable(){
                                @Override
                                public void run() {
                                    cutdown_btn.setText("结束");
                                }
                            });
                        }
                    }
                },0,1000);
            }
        });

        mac_status = (Text) findComponentById(ResourceTable.Id_mac_status);
        sib = (SwitchIconButton) findComponentById(ResourceTable.Id_sib);
        sib.setClickedListener(component -> {
                sib.changeStatus();
                mac_status.setText(sib.isStatus()+"");
        });
        mac_status.setText(sib.isStatus()+"");

     }
}
