/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
function extend(source, target) {
    for (var prop in target) {
        source.prototype[prop] = target[prop];
    }
}

/*dorado.touch.FormElement = */extend(dorado.widget.FormElement, {
    createEditor: function(type) {
        switch (type) {
            case "textArea":{
                return new dorado.widget.TextArea();
            }
            case "checkBox":{
                return new dorado.widget.CheckBox();
            }
            case "radioGroup":{
                return new dorado.widget.RadioGroup({
                    layout: "flow"
                });
            }
            case "label":{
                return new dorado.widget.DataLabel();
            }
            default:
            {
                return new dorado.touch.TextEditor();
            }
        }
    }
});
