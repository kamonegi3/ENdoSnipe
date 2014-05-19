/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.web.explorer.template.meta;

/**
 * プロパティクラス
 * @author kamo
 *
 */
public class Property
{
    /** 線の種類 */
    private String strokeDasharray_;

    /** 枠線の色 */
    private String stroke_;

    /** 枠線の幅 */
    private Integer strokeWidth_;

    /** 塗りつぶし色 */
    private String fill_;

    /** 図形名 */
    private String shapeName_;

    /** フォントサイズ */
    private Integer fontSize_;

    /** テキスト位置 */
    private String textAnchor_;

    /** フォントファミリー */
    private String fontFamily_;

    /** テキスト */
    private String text_;

    /** リソースID */
    private String resourceId_;

    /** ターゲットの正規表現 */
    private String target_;

    /** 名前 */
    private String name_;

    /** 段階 */
    private Integer level_;

    /** 閾値 */
    private String threshold_;

    /** 周期 */
    private Integer period_;

    /** タイプ */
    private String objectType_;

    /** AND か OR か */
    private Integer method_;

    /** 枠線のプロパティ（コンポジットパターン） */
    private Property border_;

    /** ラベルのプロパティ（コンポジットパターン） */
    private Property label_;

    /** シグナルのプロパティ（コンポジットパターン） */
    private Property signal_;

    /**
     * コンストラクタ
     */
    public Property()
    {
    }

    /**
     * 線の種類を取得する
     * @return 線の種類
     */
    public String getStrokeDasharray()
    {
        return strokeDasharray_;
    }

    /**
     * 線の種類を設定する
     * @param strokeDasharray 線の種類
     */
    public void setStrokeDasharray(final String strokeDasharray)
    {
        this.strokeDasharray_ = strokeDasharray;
    }

    /**
     * 枠線の色を取得する
     * @return 枠線の色
     */
    public String getStroke()
    {
        return stroke_;
    }

    /**
     * 枠線の色を設定する
     * @param stroke 枠線の色
     */
    public void setStroke(final String stroke)
    {
        this.stroke_ = stroke;
    }

    /**
     * 枠線の幅を取得する
     * @return 枠線の幅
     */
    public Integer getStrokeWidth()
    {
        return strokeWidth_;
    }

    /**
     * 枠線の幅を設定する
     * @param strokeWidth 枠線の幅
     */
    public void setStrokeWidth(final Integer strokeWidth)
    {
        this.strokeWidth_ = strokeWidth;
    }

    /**
     * 塗りつぶし色を取得する
     * @return 塗りつぶし色
     */
    public String getFill()
    {
        return fill_;
    }

    /**
     * 塗りつぶし色を設定する
     * @param fill 塗りつぶし色
     */
    public void setFill(final String fill)
    {
        this.fill_ = fill;
    }

    /**
     * 図形名を取得する
     * @return 図形名
     */
    public String getShapeName()
    {
        return shapeName_;
    }

    /**
     * 図形名を設定する
     * @param shapeName 図形名
     */
    public void setShapeName(final String shapeName)
    {
        this.shapeName_ = shapeName;
    }

    /**
     * フォントサイズを取得する
     * @return フォントサイズ
     */
    public Integer getFontSize()
    {
        return fontSize_;
    }

    /**
     * フォントサイズを設定する
     * @param fontSize フォントサイズ
     */
    public void setFontSize(final Integer fontSize)
    {
        this.fontSize_ = fontSize;
    }

    /**
     * テキスト位置を取得する
     * @return テキスト位置
     */
    public String getTextAnchor()
    {
        return textAnchor_;
    }

    /**
     * テキスト位置を設定する
     * @param textAnchor テキスト位置
     */
    public void setTextAnchor(final String textAnchor)
    {
        this.textAnchor_ = textAnchor;
    }

    /**
     * フォントファミリーを取得する
     * @return フォントファミリー
     */
    public String getFontFamily()
    {
        return fontFamily_;
    }

    /**
     * フォントファミリーを設定する
     * @param fontFamily フォントファミリー
     */
    public void setFontFamily(final String fontFamily)
    {
        this.fontFamily_ = fontFamily;
    }

    /**
     * テキストを取得する
     * @return テキスト
     */
    public String getText()
    {
        return text_;
    }

    /**
     * テキストを設定する
     * @param text テキスト
     */
    public void setText(final String text)
    {
        this.text_ = text;
    }

    /**
     * リソースIDを取得する
     * @return リソースID
     */
    public String getResourceId()
    {
        return resourceId_;
    }

    /**
     * リソースIDを設定する
     * @param resourceId リソースID
     */
    public void setResourceId(final String resourceId)
    {
        this.resourceId_ = resourceId;
    }

    /**
     * ターゲットを取得する
     * @return ターゲット
     */
    public String getTarget()
    {
        return target_;
    }

    /**
     * ターゲットを設定する
     * @param target ターゲット
     */
    public void setTarget(final String target)
    {
        this.target_ = target;
    }

    /**
     * 名前を取得する
     * @return 名前
     */
    public String getName()
    {
        return name_;
    }

    /**
     * 名前を設定する
     * @param name 名前
     */
    public void setName(final String name)
    {
        this.name_ = name;
    }

    /**
     * 段階を取得する
     * @return 段階
     */
    public Integer getLevel()
    {
        return level_;
    }

    /**
     * 段階を設定する
     * @param level 段階
     */
    public void setLevel(final Integer level)
    {
        this.level_ = level;
    }

    /**
     * 閾値を取得する
     * @return 閾値
     */
    public String getThreshold()
    {
        return threshold_;
    }

    /**
     * 閾値を設定する
     * @param threshold 閾値
     */
    public void setThreshold(final String threshold)
    {
        this.threshold_ = threshold;
    }

    /**
     * 更新周期を取得する
     * @return 更新周期
     */
    public Integer getPeriod()
    {
        return period_;
    }

    /**
     * 更新周期を設定する
     * @param period 更新周期
     */
    public void setPeriod(final Integer period)
    {
        this.period_ = period;
    }

    /**
     * タイプを取得する
     * @return タイプ
     */
    public String getObjectType()
    {
        return objectType_;
    }

    /**
     * タイプを設定する
     * @param type タイプ
     */
    public void setObjectType(final String type)
    {
        this.objectType_ = type;
    }

    /**
     * シグナルの判定方法を取得する
     * @return シグナルの判定方法
     */
    public Integer getMethod()
    {
        return method_;
    }

    /**
     * シグナルの判定方法を設定する
     * @param method シグナルの判定方法
     */
    public void setMethod(final Integer method)
    {
        this.method_ = method;
    }

    /**
     * 枠線のプロパティを取得する
     * @return 枠線のプロパティ
     */
    public Property getBorder()
    {
        return border_;
    }

    /**
     * 枠線のプロパティを設定する
     * @param border 枠線のプロパティ
     */
    public void setBorder(final Property border)
    {
        this.border_ = border;
    }

    /**
     * ラベルのプロパティを取得する
     * @return ラベルのプロパティ
     */
    public Property getLabel()
    {
        return label_;
    }

    /**
     * ラベルのプロパティを設定する
     * @param label ラベルのプロパティ
     */
    public void setLabel(final Property label)
    {
        this.label_ = label;
    }

    /**
     * シグナルのプロパティを取得する
     * @return シグナルのプロパティ
     */
    public Property getSignal()
    {
        return signal_;
    }

    /**
     * シグナルのプロパティを設定する
     * @param signal シグナルのプロパティ
     */
    public void setSignal(final Property signal)
    {
        this.signal_ = signal;
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((border_ == null) ? 0 : border_.hashCode());
        result = PRIME * result + ((fill_ == null) ? 0 : fill_.hashCode());
        result = PRIME * result + ((fontFamily_ == null) ? 0 : fontFamily_.hashCode());
        result = PRIME * result + ((fontSize_ == null) ? 0 : fontSize_.hashCode());
        result = PRIME * result + ((label_ == null) ? 0 : label_.hashCode());
        result = PRIME * result + ((level_ == null) ? 0 : level_.hashCode());
        result = PRIME * result + ((method_ == null) ? 0 : method_.hashCode());
        result = PRIME * result + ((name_ == null) ? 0 : name_.hashCode());
        result = PRIME * result + ((period_ == null) ? 0 : period_.hashCode());
        result = PRIME * result + ((resourceId_ == null) ? 0 : resourceId_.hashCode());
        result = PRIME * result + ((shapeName_ == null) ? 0 : shapeName_.hashCode());
        result = PRIME * result + ((signal_ == null) ? 0 : signal_.hashCode());
        result = PRIME * result + ((stroke_ == null) ? 0 : stroke_.hashCode());
        result = PRIME * result + ((strokeDasharray_ == null) ? 0 : strokeDasharray_.hashCode());
        result = PRIME * result + ((strokeWidth_ == null) ? 0 : strokeWidth_.hashCode());
        result = PRIME * result + ((target_ == null) ? 0 : target_.hashCode());
        result = PRIME * result + ((text_ == null) ? 0 : text_.hashCode());
        result = PRIME * result + ((textAnchor_ == null) ? 0 : textAnchor_.hashCode());
        result = PRIME * result + ((threshold_ == null) ? 0 : threshold_.hashCode());
        result = PRIME * result + ((objectType_ == null) ? 0 : objectType_.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Property other = (Property)obj;
        if (border_ == null)
        {
            if (other.border_ != null)
            {
                return false;
            }
        }
        else if (!border_.equals(other.border_))
        {
            return false;
        }
        if (fill_ == null)
        {
            if (other.fill_ != null)
            {
                return false;
            }
        }
        else if (!fill_.equals(other.fill_))
        {
            return false;
        }
        if (fontFamily_ == null)
        {
            if (other.fontFamily_ != null)
            {
                return false;
            }
        }
        else if (!fontFamily_.equals(other.fontFamily_))
        {
            return false;
        }
        if (fontSize_ == null)
        {
            if (other.fontSize_ != null)
            {
                return false;
            }
        }
        else if (!fontSize_.equals(other.fontSize_))
        {
            return false;
        }
        if (label_ == null)
        {
            if (other.label_ != null)
            {
                return false;
            }
        }
        else if (!label_.equals(other.label_))
        {
            return false;
        }
        if (level_ == null)
        {
            if (other.level_ != null)
            {
                return false;
            }
        }
        else if (!level_.equals(other.level_))
        {
            return false;
        }
        if (method_ == null)
        {
            if (other.method_ != null)
            {
                return false;
            }
        }
        else if (!method_.equals(other.method_))
        {
            return false;
        }
        if (name_ == null)
        {
            if (other.name_ != null)
            {
                return false;
            }
        }
        else if (!name_.equals(other.name_))
        {
            return false;
        }
        if (period_ == null)
        {
            if (other.period_ != null)
            {
                return false;
            }
        }
        else if (!period_.equals(other.period_))
        {
            return false;
        }
        if (resourceId_ == null)
        {
            if (other.resourceId_ != null)
            {
                return false;
            }
        }
        else if (!resourceId_.equals(other.resourceId_))
        {
            return false;
        }
        if (shapeName_ == null)
        {
            if (other.shapeName_ != null)
            {
                return false;
            }
        }
        else if (!shapeName_.equals(other.shapeName_))
        {
            return false;
        }
        if (signal_ == null)
        {
            if (other.signal_ != null)
            {
                return false;
            }
        }
        else if (!signal_.equals(other.signal_))
        {
            return false;
        }
        if (stroke_ == null)
        {
            if (other.stroke_ != null)
            {
                return false;
            }
        }
        else if (!stroke_.equals(other.stroke_))
        {
            return false;
        }
        if (strokeDasharray_ == null)
        {
            if (other.strokeDasharray_ != null)
            {
                return false;
            }
        }
        else if (!strokeDasharray_.equals(other.strokeDasharray_))
        {
            return false;
        }
        if (strokeWidth_ == null)
        {
            if (other.strokeWidth_ != null)
            {
                return false;
            }
        }
        else if (!strokeWidth_.equals(other.strokeWidth_))
        {
            return false;
        }
        if (target_ == null)
        {
            if (other.target_ != null)
            {
                return false;
            }
        }
        else if (!target_.equals(other.target_))
        {
            return false;
        }
        if (text_ == null)
        {
            if (other.text_ != null)
            {
                return false;
            }
        }
        else if (!text_.equals(other.text_))
        {
            return false;
        }
        if (textAnchor_ == null)
        {
            if (other.textAnchor_ != null)
            {
                return false;
            }
        }
        else if (!textAnchor_.equals(other.textAnchor_))
        {
            return false;
        }
        if (threshold_ == null)
        {
            if (other.threshold_ != null)
            {
                return false;
            }
        }
        else if (!threshold_.equals(other.threshold_))
        {
            return false;
        }
        if (objectType_ == null)
        {
            if (other.objectType_ != null)
            {
                return false;
            }
        }
        else if (!objectType_.equals(other.objectType_))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Property [strokeDasharray=" + strokeDasharray_ + ", stroke=" + stroke_
                + ", strokeWidth=" + strokeWidth_ + ", fill=" + fill_ + ", shapeName=" + shapeName_
                + ", fontSize=" + fontSize_ + ", textAnchor=" + textAnchor_ + ", fontFamily="
                + fontFamily_ + ", text=" + text_ + ", resourceId=" + resourceId_ + ", target="
                + target_ + ", name=" + name_ + ", level=" + level_ + ", threshold=" + threshold_
                + ", period=" + period_ + ", objectType=" + objectType_ + ", method=" + method_ + ", border="
                + border_ + ", label=" + label_ + ", signal=" + signal_ + "]";
    }

}
