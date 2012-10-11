package com.bstek.dorado.view.widget;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.IdeProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-6-2
 */
@ClientEvents({ @ClientEvent(name = "beforeShow"),
		@ClientEvent(name = "onShow"), @ClientEvent(name = "beforeHide"),
		@ClientEvent(name = "onHide"), @ClientEvent(name = "beforeClose"),
		@ClientEvent(name = "onClose") })
public interface FloatControl {

	FloatControlAnimateType getAnimateType();

	void setAnimateType(FloatControlAnimateType animateType);

	FloatControlAnimateType getShowAnimateType();

	void setShowAnimateType(FloatControlAnimateType showAnimateType);

	FloatControlAnimateType getHideAnimateType();

	void setHideAnimateType(FloatControlAnimateType hideAnimateType);

	@IdeProperty(visible = false)
	@Deprecated
	String getAnimateTarget();

	@Deprecated
	void setAnimateTarget(String animateTarget);

	boolean isCenter();

	void setCenter(boolean center);

	boolean isModal();

	void setModal(boolean modal);

	ModalType getModalType();

	void setModalType(ModalType modalType);

	FloatControlShadowMode getShadowMode();

	void setShadowMode(FloatControlShadowMode shadowMode);

	boolean isFocusAfterShow();

	void setFocusAfterShow(boolean focusAfterShow);

	boolean isFloating();

	void setFloating(boolean floating);

	String getFloatingClassName();

	void setFloatingClassName(String floatingClassName);

	int getLeft();

	void setLeft(int left);

	int getTop();

	void setTop(int top);

	@IdeProperty(visible = false)
	@Deprecated
	String getAnchorTarget();

	@Deprecated
	void setAnchorTarget(String anchorTarget);

	int getOffsetLeft();

	void setOffsetLeft(int offsetLeft);

	int getOffsetTop();

	void setOffsetTop(int offsetTop);

	boolean isAutoAdjustPosition();

	void setAutoAdjustPosition(boolean autoAdjustPosition);

	@IdeProperty(visible = false)
	@Deprecated
	boolean isHandleOverflow();

	@Deprecated
	void setHandleOverflow(boolean handleOverflow);

	@IdeProperty(visible = false)
	@Deprecated
	FloatControlAlign getAlign();

	@Deprecated
	void setAlign(FloatControlAlign align);

	@IdeProperty(visible = false)
	@Deprecated
	FloatControlVAlign getvAlign();

	@Deprecated
	void setvAlign(FloatControlVAlign vAlign);
}
