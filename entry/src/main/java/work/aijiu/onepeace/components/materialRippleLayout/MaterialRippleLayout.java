package work.aijiu.onepeace.components.materialRippleLayout;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

/**
 * 仿照 android
 * 自定义 水波纹组件
 */
public class MaterialRippleLayout extends StackLayout implements Component.TouchEventListener, Component.DrawTask,Component.ClickedListener {

    private static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, "RippleView");
    private static final boolean DEFAULT_DELAY_CLICK = true;
    private static final float DEFAULT_ALPHA = 0.2f;
    private static final int DEFAULT_DURATION = 350;
    private static final float DEFAULT_DIAMETER_DP = 80;
    private static final int HOVER_DURATION = 1500;
    private static final int ADD_RADIUS_LENGTH = 20;
    private static final boolean DEFAULT_RIPPLE_OVERLAY = false;
    private static final int DEFAULT_RIPPLE_COLOR = 0x80000000;
    private static final int DEFAULT_OUTER_LINE_COLOR = 0x1A000000;
    private Component childView;
    private Color rippleColor = Color.BLACK;
    private float rippleAlpha = DEFAULT_ALPHA;
    private int rippleDuration;
    private float rippleDiameter = DEFAULT_DIAMETER_DP;
    private boolean rippleOverlay;
    private boolean rippleInAdapter;
    private MmiPoint pointerPosition;
    private DelayRunAnimator delayRunAnimator = new DelayRunAnimator();
    private EventHandler handler;
    private AnimatorValue animatorValue;
    private ListContainer parentAdapter;
    private ShapeElement mOuterLineDrawable;
    private boolean isLongClick = false;
    private float maxRadius;
    private float mRadiusRipple;
    private boolean mFingerUp = true;
    private float x;
    private float y;
    private final Paint mPaint = new Paint();


    private final AnimatorValue.ValueUpdateListener mAnimatorUpdateListener = new AnimatorValue.ValueUpdateListener() {
        @Override
        public void onUpdate(AnimatorValue animatorValue, float v) {
            if (v <= 0) {
                return;
            }
            float currentRadius = NumCalcUtil.add(rippleDiameter, maxRadius * v);
            if (currentRadius < rippleDiameter) {
                setRadius(rippleDiameter);
            } else {
                setRadius(currentRadius);
            }
            invalidate();
        }
    };

    /**
     * constructor
     *
     * @param context
     */
    public MaterialRippleLayout(Context context) {
        super(context);
        init(context, null, "");
    }

    /**
     * constructor
     *
     * @param context
     * @param attrSet
     */
    public MaterialRippleLayout(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(context, attrSet, "");
    }

    /**
     * constructor
     *
     * @param context
     * @param attrSet
     * @param styleName
     */
    public MaterialRippleLayout(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(context, attrSet, styleName);
    }

    /**
     * connect component
     *
     * @param view
     * @return RippleBuilder
     */
    public static RippleBuilder on(Component view) {
        return new RippleBuilder(view);
    }

    private void init(Context context, AttrSet attrs, String defStyleAttr) {

        handler = new EventHandler(EventRunner.getMainEventRunner());

        if (attrs != null) {
            initAttrs(context, attrs, defStyleAttr);
        }
        setupPaint();

        addDrawTask(this::onDraw);
        setTouchEventListener(this::onTouchEvent);
    }

    private void initAttrs(Context context, AttrSet attrSet, String defStyleAttr) {
        rippleColor = TypedAttrUtils.getColor(attrSet, "mrl_rippleColor", new Color(DEFAULT_RIPPLE_COLOR));
        rippleAlpha = TypedAttrUtils.getFloat(attrSet, "mrl_rippleAlpha", DEFAULT_ALPHA);
        rippleDuration = TypedAttrUtils.getInteger(attrSet, "mrl_rippleDuration", DEFAULT_DURATION);
        rippleDiameter = TypedAttrUtils.getFloat(attrSet, "mrl_rippleDiameterDp", DEFAULT_DIAMETER_DP);
        rippleOverlay = TypedAttrUtils.getBoolean(attrSet, "mrl_rippleOverlay", DEFAULT_RIPPLE_OVERLAY);
        HiLog.info(label, "initAttrs rippleDiameter = " + rippleDiameter);
        HiLog.info(label, "initAttrs rippleDuration = " + rippleDuration);
    }

    /**
     * 初始化画笔
     */
    private void setupPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_STYLE);
        mPaint.setColor(rippleColor);
        mPaint.setAlpha(rippleAlpha);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (mOuterLineDrawable != null) {
            mOuterLineDrawable.drawToCanvas(canvas);
        }

        canvas.drawCircle(getX(), getY(), getRadius(), mPaint);
    }

    private ShapeElement generateBackgroundDrawable() {
        ShapeElement element = new ShapeElement();
        element.setRgbColor(RgbColor.fromArgbInt(DEFAULT_OUTER_LINE_COLOR));
        return element;
    }

    private ShapeElement generateTransparentBackgroundDrawable() {
        ShapeElement element = new ShapeElement();
        element.setRgbColor(RgbColor.fromArgbInt(Color.TRANSPARENT.getValue()));
        return element;
    }

    private void initAnimator() {
        animatorValue = new AnimatorValue();
        animatorValue.setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
        animatorValue.setDuration(rippleDuration);
        animatorValue.setValueUpdateListener(mAnimatorUpdateListener);
        animatorValue.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {
            }

            @Override
            public void onStop(Animator animator) {
            }

            @Override
            public void onCancel(Animator animator) {
            }

            @Override
            public void onEnd(Animator animator) {
                mOuterLineDrawable = generateTransparentBackgroundDrawable();
                setRadius(0);
                fingerUp();
            }

            @Override
            public void onPause(Animator animator) {
            }

            @Override
            public void onResume(Animator animator) {
            }
        });
        animatorValue.start();
    }

    /**
     * 长按事件动画
     */
    private void initLongClickAnimator() {
        animatorValue = new AnimatorValue();
        animatorValue.setCurveType(Animator.CurveType.LINEAR);
        animatorValue.setDuration(HOVER_DURATION);
        animatorValue.setValueUpdateListener(mAnimatorUpdateListener);
        animatorValue.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {
            }

            @Override
            public void onStop(Animator animator) {
            }

            @Override
            public void onCancel(Animator animator) {
            }

            @Override
            public void onEnd(Animator animator) {
                mOuterLineDrawable = generateTransparentBackgroundDrawable();
                if (rippleOverlay && mFingerUp) {
                    setRadius(0);
                    invalidate();
                }
                if (!rippleOverlay) {
                    setRadius(0);
                    invalidate();
                }
            }

            @Override
            public void onPause(Animator animator) {
            }

            @Override
            public void onResume(Animator animator) {
            }
        });
        animatorValue.start();
    }

    /**
     * getChildView
     *
     * @param <T>
     * @return T extends Component
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getChildView() {
        return (T) childView;
    }

    @Override
    public void addComponent(Component childComponent) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("MaterialRippleLayout can host only one child");
        }
        childView = childComponent;
        super.addComponent(childComponent);
    }

    @Override
    public void setClickedListener(ClickedListener listener) {
        if (childView == null) {
            throw new IllegalStateException("MaterialRippleLayout must have a child view to handle clicks");
        }
        childView.setClickedListener(listener);
    }

    @Override
    public void setLongClickedListener(LongClickedListener listener) {
        if (childView == null) {
            throw new IllegalStateException("MaterialRippleLayout must have a child view to handle clicks");
        }
        childView.setLongClickedListener(listener);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * getRadius
     *
     * @return float
     */
    public float getRadius() {
        return mRadiusRipple;
    }

    /**
     * setRadius
     *
     * @param radius
     */
    public void setRadius(float radius) {
        this.mRadiusRipple = radius;
    }

    @Override
    public void onClick(Component component) {
        isLongClick = false;
    }

    public void setRipplePersistent(boolean ripplePersistent) {

    }

    public void performRipple() {

    }

    public void performRipple(Point anchor) {

    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        touchEvent.getRadius(0);
        pointerPosition = touchEvent.getPointerPosition(0);

        float x = pointerPosition.getX();
        float y = pointerPosition.getY();

        int[] parentLocationOnScreen = getLocationOnScreen();

        setX(NumCalcUtil.subtract(x, (float) parentLocationOnScreen[0]));
        setY(((float) component.getHeight()) / 2);
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                mOuterLineDrawable = generateBackgroundDrawable();
                mFingerUp = false;
                handler.postTask(delayRunAnimator, 200);
                setRippleDuration(rippleDuration);
                if (Math.abs(NumCalcUtil.subtract(x, (float) parentLocationOnScreen[0]))
                        > Math.abs(NumCalcUtil.subtract(x, (parentLocationOnScreen[0] + getWidth())))) {
                    maxRadius = NumCalcUtil.add(Math.abs(NumCalcUtil.subtract(x, parentLocationOnScreen[0])),
                            ADD_RADIUS_LENGTH);
                } else {
                    maxRadius = NumCalcUtil.add(Math.abs(NumCalcUtil.subtract(x, (parentLocationOnScreen[0] + getWidth()))),
                            ADD_RADIUS_LENGTH);
                }
                mRadiusRipple = maxRadius;
                resetConfig();
                return true;
            case TouchEvent.POINT_MOVE:
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                mFingerUp = true;
                if (!isLongClick) {
                    handler.removeTask(delayRunAnimator);
                    if (rippleDuration <= 0) {
                        rippleDuration = DEFAULT_DURATION;
                    }
                    setRippleDuration(rippleDuration);
                    initAnimator();
                }
                if (isLongClick) {
                    fingerUp();
                }
                break;
            default:
                break;
        }
        return false;
    }

    private class DelayRunAnimator implements Runnable {
        @Override
        public void run() {
            isLongClick = true;
            initLongClickAnimator();
        }
    }

    /**
     * 点击操作
     */
    private void performalViewClick() {
        if (getComponentParent() instanceof ListContainer) {
            if (!childView.callOnClick()) {
                clickAdapterView((ListContainer) getComponentParent());
            }
        } else if (rippleInAdapter) {
            clickAdapterView(findParentAdapterView());
        } else {
            if (childView != null) {
                childView.callOnClick();
            }
        }
    }

    private void clickAdapterView(ListContainer parent) {
        final int position = parent.getIndexForComponent(MaterialRippleLayout.this);
        final long itemId = parent.getItemProvider() != null
                ? parent.getItemProvider().getItemId(position)
                : 0;
        if (position != ListContainer.INVALID_INDEX) {
            parent.executeItemClick(MaterialRippleLayout.this, position, itemId);
        }
    }

    private class PerformClickEvent implements Runnable {
        @Override
        public void run() {
            if (getComponentParent() instanceof ListContainer) {
                if (!childView.callOnClick()) {
                    clickAdapterView((ListContainer) getComponentParent());
                }
            } else if (rippleInAdapter) {
                clickAdapterView(findParentAdapterView());
            } else {
                if (childView != null) {
                    childView.callOnClick();
                }
                callOnClick();
            }
        }

        private void clickAdapterView(ListContainer parent) {
            final int position = parent.getIndexForComponent(MaterialRippleLayout.this);
            final long itemId = parent.getItemProvider() != null
                    ? parent.getItemProvider().getItemId(position)
                    : 0;
            if (position != ListContainer.INVALID_INDEX) {
                parent.executeItemClick(MaterialRippleLayout.this, position, itemId);
            }
        }
    }

    private ListContainer findParentAdapterView() {
        if (parentAdapter != null) {
            return parentAdapter;
        }
        ComponentParent current = getComponentParent();
        while (true) {
            if (current instanceof ListContainer) {
                parentAdapter = (ListContainer) current;
                return parentAdapter;
            } else {
                if (current != null) {
                    current = current.getComponentParent();
                }
            }
        }
    }

    private void resetConfig() {
        setRadius(getRadius());
    }

    /**
     * 抬起
     */
    private void fingerUp() {
        if (rippleOverlay && mFingerUp) {
            setRadius(0);
            invalidate();
        }
        isLongClick = false;
    }

    /**
     * set Ripple color
     *
     * @param rippleColor color
     */
    public void setRippleColor(int rippleColor) {
        this.rippleColor = new Color(rippleColor);
        mPaint.setColor(new Color(rippleColor));
        mPaint.setAlpha(rippleAlpha);
        invalidate();
    }

    /**
     * 设置透明度
     *
     * @param alpha 透明度
     */
    public void setDefaultRippleAlpha(float alpha) {
        this.rippleAlpha = alpha;
        mPaint.setAlpha(rippleAlpha);
        invalidate();
    }

    public void setRippleDelayClick(boolean rippleDelayClick) {

    }

    public void setRippleDiameter(int rippleDiameter) {
        this.rippleDiameter = rippleDiameter;
    }

    public void setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public void setRippleOverlay(boolean overlay) {
        this.rippleOverlay = overlay;
    }

    /**
     * setRippleBackground
     *
     * @param color
     */
    public void setRippleBackground(RgbColor color) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(color);
        setBackground(shapeElement);
    }

    public static class RippleBuilder {
        private final Context context;
        private final Component child;
        private int rippleColor = Color.rgb(0, 0, 0);
        private boolean rippleOverlay = DEFAULT_RIPPLE_OVERLAY;
        private float rippleDiameter = DEFAULT_DIAMETER_DP;
        private int rippleDuration = DEFAULT_DURATION;
        private float rippleAlpha = DEFAULT_ALPHA;
        private boolean rippleDelayClick = DEFAULT_DELAY_CLICK;
        private int rippleBackground = Color.rgb(255, 255, 255);

        /**
         * constructor
         *
         * @param child
         */
        public RippleBuilder(Component child) {
            this.child = child;
            this.context = child.getContext();
        }

        /**
         * rippleColor
         *
         * @param color
         * @return RippleBuilder
         */
        public RippleBuilder rippleColor(int color) {
            this.rippleColor = color;
            return this;
        }

        /**
         * rippleOverlay
         *
         * @param overlay
         * @return RippleBuilder
         */
        public RippleBuilder rippleOverlay(boolean overlay) {
            this.rippleOverlay = overlay;
            return this;
        }

        /**
         * rippleDiameterDp
         *
         * @param diameterDp
         * @return RippleBuilder
         */
        public RippleBuilder rippleDiameterDp(int diameterDp) {
            this.rippleDiameter = diameterDp;
            return this;
        }

        /**
         * rippleDuration
         *
         * @param duration
         * @return RippleBuilder
         */
        public RippleBuilder rippleDuration(int duration) {
            this.rippleDuration = duration;
            return this;
        }

        /**
         * rippleAlpha
         *
         * @param alpha
         * @return RippleBuilder
         */
        public RippleBuilder rippleAlpha(float alpha) {
            this.rippleAlpha = alpha;
            return this;
        }

        /**
         * rippleDelayClick
         *
         * @param delayClick
         * @return RippleBuilder
         */
        public RippleBuilder rippleDelayClick(boolean delayClick) {
            this.rippleDelayClick = delayClick;
            return this;
        }

        /**
         * rippleBackground
         *
         * @param color
         * @return RippleBuilder
         */
        public RippleBuilder rippleBackground(int color) {
            this.rippleBackground = color;
            return this;
        }

        /**
         * create
         *
         * @return MaterialRippleLayout
         * @throws IllegalStateException
         */
        public MaterialRippleLayout create() {
            MaterialRippleLayout layout = new MaterialRippleLayout(context);
            layout.setRippleColor(rippleColor);
            layout.setDefaultRippleAlpha(rippleAlpha);
            layout.setRippleDelayClick(rippleDelayClick);
            layout.setRippleDiameter(AttrHelper.fp2px(rippleDiameter, context));
            layout.setRippleDuration(rippleDuration);
            layout.setRippleOverlay(rippleOverlay);
            layout.setRippleBackground(new RgbColor(rippleBackground));
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setRgbColor(RgbColor.fromArgbInt(rippleBackground));
            child.setBackground(shapeElement);
            ComponentContainer.LayoutConfig params = child.getLayoutConfig();
            ComponentContainer parent = (ComponentContainer) child.getComponentParent();
            int index = 0;

            if (parent != null && parent instanceof MaterialRippleLayout) {
                throw new IllegalStateException("MaterialRippleLayout could not be created: parent of the view already is a MaterialRippleLayout");
            }

            if (parent != null) {
                index = parent.getChildIndex(child);
                parent.removeComponent(child);
            }
            layout.addComponent(child, new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
            if (parent != null) {
                parent.addComponent(layout, index, params);
            }
            return layout;
        }
    }


}
