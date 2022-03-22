package work.aijiu.onepeace.components.countdown;

import ohos.agp.components.*;
import ohos.agp.render.Canvas;
import ohos.app.Context;
import ohos.global.systemres.ResourceTable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReadWriteLock;

public class CountDownButton extends Component implements Component.ClickedListener {

    private Context mcontext = null;
    private Text text = null;
    private Timer timer = new Timer();
    private String prefix = "";
    private String suffix = "s";
    private int cur = 0;
    private int total = 60000;
    private byte status = 0x00; // 0x00 默认 0*01进行中

    public CountDownButton(Context context,String prefix,String suffix){
        super(context);
        this.mcontext = context;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public CountDownButton(Context context) {
        super(context);
        this.mcontext = context;
    }

    public CountDownButton(Context context, AttrSet attrSet) {
        super(context, attrSet);
        this.mcontext = context;
    }

    public CountDownButton(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        this.mcontext = context;
    }




    @Override
    public void onClick(Component component) {

    }

    public void start(){
        if(timer == null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cur++;
            }
        },1000);

    }

    public void destroy(){
        timer = null;
    }


}
