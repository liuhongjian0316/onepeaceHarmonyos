package work.aijiu.onepeace.components.materialRippleLayout;

import ohos.agp.components.AttrSet;
import ohos.agp.utils.Color;

public class TypedAttrUtils {
    /**
     * 返回自定义int类型的颜色值
     *
     * @param attrs AttrSet类
     * @param attrName 自定义属性名
     * @param defValue 默认值
     * @return 自定义int类型的颜色值
     */
    public static int getIntColor(AttrSet attrs, String attrName, int defValue) {
        if (attrs.getAttr(attrName) != null && attrs.getAttr(attrName).isPresent()) {
            return attrs.getAttr(attrName).get().getColorValue().getValue();
        } else {
            return defValue;
        }
    }

    /**
     * 返回自定义Color类型的颜色值
     *
     * @param attrs AttrSet类
     * @param attrName 自定义属性名
     * @param defValue 默认值
     * @return 自定义Color类型的颜色值
     */
    public static Color getColor(AttrSet attrs, String attrName, Color defValue) {
        if (attrs.getAttr(attrName) != null && attrs.getAttr(attrName).isPresent()) {
            return attrs.getAttr(attrName).get().getColorValue();
        } else {
            return defValue;
        }
    }

    /**
     * 返回自定义布尔值
     *
     * @param attrs AttrSet类
     * @param attrName 自定义属性名
     * @param defValue 默认值
     * @return 自定义布尔值
     */
    public static boolean getBoolean(AttrSet attrs, String attrName, boolean defValue) {
        if (attrs.getAttr(attrName) != null && attrs.getAttr(attrName).isPresent()) {
            return attrs.getAttr(attrName).get().getBoolValue();
        } else {
            return defValue;
        }
    }

    /**
     * 返回自定义字符串值
     *
     * @param attrs AttrSet类
     * @param attrName 自定义属性名
     * @param defValue 默认值
     * @return 自定义字符串值
     */
    public static String getString(AttrSet attrs, String attrName, String defValue) {
        if (attrs.getAttr(attrName) != null && attrs.getAttr(attrName).isPresent()) {
            return attrs.getAttr(attrName).get().getStringValue();
        } else {
            return defValue;
        }
    }

    /**
     * 返回自定义value值
     *
     * @param attrs AttrSet类
     * @param attrName 自定义属性名
     * @param defValue 默认值
     * @return 自定义value值
     */
    public static int getDimensionPixelSize(AttrSet attrs, String attrName, int defValue) {
        if (attrs.getAttr(attrName) != null && attrs.getAttr(attrName).isPresent()) {
            return attrs.getAttr(attrName).get().getDimensionValue();
        } else {
            return defValue;
        }
    }

    /**
     * 返回自定义Integer值
     *
     * @param attrs AttrSet类
     * @param attrName 自定义属性名
     * @param defValue 默认值
     * @return 自定义Integer值
     */
    public static int getInteger(AttrSet attrs, String attrName, int defValue) {
        if (attrs.getAttr(attrName) != null && attrs.getAttr(attrName).isPresent()) {
            return attrs.getAttr(attrName).get().getIntegerValue();
        } else {
            return defValue;
        }
    }

    /**
     * 返回自定义Float值
     *
     * @param attrs AttrSet类
     * @param attrName 自定义属性名
     * @param defValue 默认值
     * @return 自定义Float值
     */
    public static float getFloat(AttrSet attrs, String attrName, float defValue) {
        if (attrs.getAttr(attrName) != null && attrs.getAttr(attrName).isPresent()) {
            return attrs.getAttr(attrName).get().getFloatValue();
        } else {
            return defValue;
        }
    }
}