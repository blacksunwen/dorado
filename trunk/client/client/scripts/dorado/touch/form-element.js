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