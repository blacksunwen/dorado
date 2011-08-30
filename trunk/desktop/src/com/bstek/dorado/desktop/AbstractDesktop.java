package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.view.widget.Control;

@ClientEvents({ @ClientEvent(name = "onShortcutContextMenu")})
public class AbstractDesktop extends Control {
}
