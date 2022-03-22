package work.aijiu.onepeace.components.SwitchIconButton;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Picker;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Picture;
import ohos.agp.render.PixelMapHolder;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import work.aijiu.onepeace.ResourceTable;

import java.io.IOException;

public class SwitchIconButton extends Component implements Component.DrawTask{

    private static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, "SwitchIconButton");

    // 组件xml属性
    private final static String ATTR_DEFAULT_IMG = "defalut_img" ;  // 默认图标
    private final static String ATTR_CLOSE_IMG = "close_img";       // 关闭图标
    private final static String ATTR_STATUS = "status";       // 关闭图标
    private PixelMap default_img = null;
    private PixelMap close_img = null;
    private boolean status = true;


    private EventHandler handler;


    public SwitchIconButton(Context context) {
        super(context);
        init(context, null, "");
    }

    public SwitchIconButton(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(context, attrSet, "");
    }

    public SwitchIconButton(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(context, attrSet, styleName);
    }


    public void init(Context context, AttrSet attrSet, String styleName){
        handler = new EventHandler(EventRunner.getMainEventRunner());
        if (attrSet != null) {
            initAttrs(context, attrSet, styleName);
        }
        addDrawTask(this::onDraw);
    }

    // init status=> true
    public void initDefalutImg(){
        Resource bgResource = null;
        try {
            bgResource = getResourceManager().getResource(ResourceTable.Media_logo);
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            srcOpts.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(bgResource, srcOpts);
            default_img = imageSource.createPixelmap(null);
        } catch (IOException | NotExistException  e) {
            e.printStackTrace();
        }
        ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
        srcOpts.formatHint = "image/png";
        ImageSource imageSource = ImageSource.create(bgResource, srcOpts);
    }

    // init status=> false
    public void initCloseImg(){
        Resource bgResource = null;
        try {
            bgResource = getResourceManager().getResource(ResourceTable.Media_menu);
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            srcOpts.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(bgResource, srcOpts);
            close_img = imageSource.createPixelmap(null);
        }catch (IOException | NotExistException  e) {
            e.printStackTrace();
        }
    }

    // 初始化组件属性
    public void initAttrs(Context context,AttrSet attrSet,String styleName){
        if (attrSet == null) return;
        // 默认状态的背景图片
        if (attrSet.getAttr(ATTR_DEFAULT_IMG).isPresent()) {
            Element element = attrSet.getAttr(ATTR_DEFAULT_IMG).get().getElement();
            if (element instanceof PixelMapElement) {
                default_img = ((PixelMapElement) element).getPixelMap();
            }
        }else{
            this.initDefalutImg();
        }
        // false状态下的背景图片
        if (attrSet.getAttr(ATTR_CLOSE_IMG).isPresent()) {
            Element element = attrSet.getAttr(ATTR_CLOSE_IMG).get().getElement();
            if (element instanceof PixelMapElement) {
                close_img = ((PixelMapElement) element).getPixelMap();
            }
        }else{
            this.initCloseImg();
        }
        // 获取默认状态
        if (attrSet.getAttr(ATTR_STATUS).isPresent()) {
            status = attrSet.getAttr(ATTR_STATUS).get().getBoolValue();
        }else{
            status = true;
        }
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        PixelMapHolder pixelMapHolder = new PixelMapHolder(this.status ? default_img : close_img);
        canvas.drawPixelMapHolder(pixelMapHolder, 0, 0, new Paint());
    }


    public void refresh(){
        invalidate();
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
        this.refresh();
    }

    public void changeStatus(){
        this.status = !this.status;
        // 切换动画
        this.refresh();
    }
}
