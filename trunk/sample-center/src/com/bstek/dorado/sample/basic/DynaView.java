package com.bstek.dorado.sample.basic;

import org.springframework.stereotype.Component;

import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.view.widget.base.Button;
import com.bstek.dorado.view.widget.base.Panel;
import com.bstek.dorado.view.widget.layout.AnchorLayoutConstraint;
import com.bstek.dorado.view.widget.layout.AnchorMode;

@Component
public class DynaView {

	public void onViewInit(Panel panelButtons) {
		panelButtons.setCaption("此标题是通过视图拦截器设置的");

		for (int i = 1; i <= 8; i++) {
			Button button = new Button();
			button.setCaption("Button " + i);

			AnchorLayoutConstraint layoutConstraint = new AnchorLayoutConstraint();
			layoutConstraint.setAnchorLeft(AnchorMode.previous);
			layoutConstraint.setLeft("5");
			layoutConstraint.setTop("10");
			button.setLayoutConstraint(layoutConstraint);

			button.addClientEventListener(
					"onClick",
					new DefaultClientEvent(
							"dorado.MessageBox.alert('You clicked ' + self.get('caption'));"));

			panelButtons.addChild(button);
		}
	}
}
