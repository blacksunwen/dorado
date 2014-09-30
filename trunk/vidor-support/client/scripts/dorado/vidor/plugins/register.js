/**
 * Created by alex on 14-5-20.
 */
(function () {
	var types = {
		LABEL_PARSER: "labelParser",
		NODE_EDITOR: "nodeEditor",
		WIZARD: "wizard",
		PROPERTY_EDITOR: "propertyEditor",
		OUTLINE: "outline"
	};
	cloudo.plugins = {
		type: types, _map: {},
		/**
		 *
		 * @param type
		 *        可选值:"LabelParser", "NodeEditor","Wizard","PropertyEditor"
		 * @param config详细配置如下
		 *        labelParser:{
		 * 			rid:"AutoForm",
		 * 			parser:{
		 * 				parse:function(node){
		 * 					var label="";
		 * 					//代码实现
		 * 					return label;
		 * 				}
		 * 			},
		 * 			description:String|HtmlElement
		 * 		},
		 *        nodeEditor:{
		 * 			rid:"AutoForm",
		 * 			factory:function(config){
		 * 				var instance= new cloudo.widget.DrawPad(config);
		 * 				return instance;
		 * 			},
		 * 			description:String|HtmlElement
		 * 		},
		 *        wizard:同NodeEditor,
		 *        outline:同NodeEditor,
		 *        propertyEditor:{
		 * 			expression:"name[dataType]",
		 * 			className:"cloudo.widget.DataTypeSelector",
		 * 			description:String|HtmlElement
		 * 		}
		 */
		register: function (type, config) {
			var id = type;
			switch (type) {
				case types.LABEL_PARSER:
				{
					cloudo[type].register(config.rid, config.parser);
					id += config.rid;
					break;
				}
				case types.WIZARD:
				{
					cloudo[type].register(config.rid, config.factory);
					id += config.rid;
					break;
				}
				case types.NODE_EDITOR:
				{
					cloudo[type].register(config.rid, config.factory);
					id += config.rid;
					break;
				}
				case types.PROPERTY_EDITOR:
				{
					cloudo[type].register(config.expression, config.className);
					id += config.expression;
					break;
				}
				case types.OUTLINE:
				{
					cloudo[type].register(config.rid, config.factory);
					id += config.rid;
					break;
				}
			}
			this._map[id] = config;
		}
	};

	/**
	 * Examples
	 * cloudo.plugins.register(cloudo.plugins.type.NODE_EDITOR, {
	 *	rid: "Button",
	 *	factory: function (config) {
	 *		console.log(config);
	 *		return new cloudo.widget.DrawEditor(config)
	 *	},
	 *	description:"Button单开编辑器……"
	 * });
	 */

})();