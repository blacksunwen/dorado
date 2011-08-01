package com.bstek.dorado.view.widget;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;

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

	String getAnimateTarget();

	void setAnimateTarget(String animateTarget);

	String getRenderTo();

	void setRenderTo(String renderTo);

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
	
	String getAnchorTarget();
	
	void setAnchorTarget(String anchorTarget);
	
	int getOffsetLeft();
	
	void setOffsetLeft(int offsetLeft);
	
	int getOffsetTop();
	
	void setOffsetTop(int offsetTop);
	
	boolean isAutoAdjustPosition();
	
	void setAutoAdjustPosition(boolean autoAdjustPosition);
	
	boolean isHandleOverflow();
	
	void setHandleOverflow(boolean handleOverflow);
	
	FloatControlAlign getAlign();
	
	void setAlign(FloatControlAlign align);
	
	FloatControlVAlign getvAlign();
	
	void setvAlign(FloatControlVAlign vAlign);
}
