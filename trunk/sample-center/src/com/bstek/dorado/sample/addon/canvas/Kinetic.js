var stage, layer;
var colors = [ 'red', 'orange', 'yellow', 'green', 'blue', 'purple' ];

// @Bind view.onReady
!function(kinetic) {
	stage = kinetic.getStage();
	layer = new Kinetic.Layer();
	for ( var n = 0; n < 10; n++) {
		var shape = new Kinetic.RegularPolygon({
			x : Math.random() * stage.getWidth(),
			y : Math.random() * stage.getHeight(),
			sides : Math.ceil(Math.random() * 5 + 3),
			radius : Math.random() * 100 + 20,
			fill : colors[Math.round(Math.random() * 5)],
			stroke : 'black',
			opacity : Math.random(),
			strokeWidth : 4,
			draggable : true
		});

		layer.add(shape);
	}
	stage.add(layer);
}

// @Bind #buttonTango.onClick
!function() {
	for ( var n = 0; n < layer.getChildren().length; n++) {
		var shape = layer.getChildren()[n];
		var stage = shape.getStage();
		shape.transitionTo({
			rotation : Math.random() * Math.PI * 2,
			radius : Math.random() * 100 + 20,
			x : Math.random() * stage.getWidth(),
			y : Math.random() * stage.getHeight(),
			opacity : Math.random(),
			duration : 1,
			easing : 'ease-in-out'
		});
	}
}