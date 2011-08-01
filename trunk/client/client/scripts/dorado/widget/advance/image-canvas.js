(function(){
    function invertOnly(canvas, image, rotated){
        try{
            netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
        }catch(e){
        }

        var context = canvas.getContext("2d");

        var width = image.width, height = image.height;

        if(rotated){
            var temp = width;
            width = height;
            height = temp;
        }
        try{
            var imageD = context.getImageData(0, 0, width, height),
            length = imageD.data.length / 4 ,imageDD = imageD.data;

            for(var i = 0;i < length;i++){
                imageDD[i * 4] = 255 -  imageDD[i * 4];
                imageDD[i * 4 + 1] = 255 - imageDD[i * 4 + 1];
                imageDD[i * 4 + 2] = 255 - imageDD[i * 4 + 2];
            }
            context.putImageData(imageD, 0, 0);
        }catch(e){
            console.log(e);
        }
    }

    function mirrorAndInvert(canvas, image, invert, rotated){
        try{
            netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
        }catch(e){
        }

        var width = image.width, height = image.height;

        if(rotated){
            var temp = width;
            width = height;
            height = temp;
        }

        var context = canvas.getContext("2d"), imageD = context.getImageData(0, 0, width, height), imageDD = imageD.data;

        var tempr, tempg, tempb, tempa;

        for(var i = 0;i < width;i++){
            for(var j = 0;j < height;j++){
                var index = j * width + i, changeIndex = index + width - 2 * i;
                if(i <= Math.floor(width / 2)){
                    tempr = imageDD[index * 4];
                    tempg = imageDD[index * 4 + 1];
                    tempb = imageDD[index * 4 + 2];
                    tempa = imageDD[index * 4 + 3];

                    if(invert){
                        imageDD[index * 4] = 255 - imageDD[changeIndex * 4] || 0;
                        imageDD[index * 4 + 1] = 255 - imageDD[changeIndex * 4 + 1] || 0;
                        imageDD[index * 4 + 2] = 255 - imageDD[changeIndex * 4 + 2] || 0;
                        imageDD[index * 4 + 3] = imageDD[changeIndex * 4 + 3] || 0;
                    }else{
                        imageDD[index * 4] = imageDD[changeIndex * 4] || 0;
                        imageDD[index * 4 + 1] = imageDD[changeIndex * 4 + 1] || 0;
                        imageDD[index * 4 + 2] = imageDD[changeIndex * 4 + 2] || 0;
                        imageDD[index * 4 + 3] = imageDD[changeIndex * 4 + 3] || 0;
                    }

                    if(invert){
                        imageDD[changeIndex * 4] = 255 - tempr;
                        imageDD[changeIndex * 4 + 1] = 255 - tempg;
                        imageDD[changeIndex * 4 + 2] = 255 - tempb;
                        imageDD[changeIndex * 4 + 3] = tempa;
                    }else{
                        imageDD[changeIndex * 4] = tempr;
                        imageDD[changeIndex * 4 + 1] = tempg;
                        imageDD[changeIndex * 4 + 2] = tempb;
                        imageDD[changeIndex * 4 + 3] = tempa;
                    }
                }
            }
        }

        try{
            context.putImageData(imageD, 0, 0);
        }catch(e){
            alert(e);
        }
    }

    function rotate(canvas, img, rot){
        try{
            netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
        }catch(e){
        }

        rot = rot || 0;

		var rotation = Math.PI * rot / 180, cos = Math.round(Math.cos(rotation) * 1000) / 1000,
            sin = Math.round(Math.sin(rotation) * 1000) / 1000, imageWidth = img.width, imageHeight = img.height;

		//旋转后canvas标签的大小
		canvas.height = Math.abs(cos * imageHeight) + Math.abs(sin * imageWidth);
		canvas.width = Math.abs(cos * imageWidth) + Math.abs(sin * imageHeight);

		var context = canvas.getContext("2d");
		context.save();
        //改变中心点
		if (rotation <= Math.PI / 2) {
			context.translate(sin * imageHeight,0);
		} else if (rotation <= Math.PI) {
			context.translate(canvas.width, -cos * imageHeight);
		} else if (rotation <= 1.5*Math.PI) {
			context.translate(-cos * imageWidth, canvas.height);
		} else {
			context.translate(0, -sin * imageWidth);
		}
		context.rotate(rotation);
		context.drawImage(img, 0, 0, imageWidth, imageHeight);
		context.restore();
	}

    var selectProxy, zmEl;

    function getZoomMagnifierEl(canvas){
        if(!zmEl){
            zmEl = $DomUtils.xCreateElement({
                tagName: "div",
                className: "image-canvas-magnifier",
                style: {
                    display: "none"
                },
                content: {
                    tagName: "img"
                }
            });

            var dom = canvas._dom;
            dom.appendChild(zmEl);
        }
        return zmEl;
    }

    function createHLArea(canvas){
        var dom = canvas._dom;
        if(dom){
            var areaEl = document.createElement("div");
            areaEl.className = "highlight-area";
            dom.appendChild(areaEl);
            canvas._areaEls.push(areaEl);
        }
    }

    var getRegionOffsets = function(region1, region2) {
		return {
			top : Math.max(region1['top'], region2['top']),
			right : Math.min(region1['right'], region2['right']),
			bottom: Math.min(region1['bottom'], region2['bottom']),
			left : Math.max(region1['left'], region2['left'])
		};
	};

	var intersect = function(element1, element2) {
		var region1 = $fly(element1).region(), region2;
		if (element2.nodeType) {
			region2 = $fly(element2).region();
		} else {
			region2 = element2;
		}
		var result = getRegionOffsets(region1, region2);
		result.inRegion = result['bottom'] >= result['top'] && result['right'] >= result['left'];
		result.width = result['right'] - result['left'];
		result.height = result['bottom'] - result['top'];
		return result;
	};

	/**
	 * @class ImageCanvas
	 * @extends dorado.widget.Control
	 */
    dorado.widget.ImageCanvas = $extend(dorado.widget.Control, {
        ATTRIBUTES: {
	        /**
	         * @attribute
	         * @type String
	         */
            image: {},

	        /**
	         * fitWidth,fitHeight,fitWidow,custom
	         * @attribute
	         * @type String
	         */
            zoomMode: {
                defaultValue: "fitWidth"
            },

	        /**
	         * @attribute
	         * @default 1
	         */
            scale: {
                defaultValue: 1
            },

	        /**
	         * @attribute
	         * @type boolean
	         */
            invert: {
                defaultValue: false
            },

	        /**
	         * @attribute
	         * @type boolean
	         */
            mirror: {
                defaultValue: false
            },

	        /**
	         * @attribute
	         * @type int
	         */
            rotation: {
                defaultValue: 0
            },

	        /**
	         * @attribute
	         * @type boolean
	         */
            selection: {
                defaultValue: false
            },

	        /**
	         * @attribute
	         * @type boolean
	         */
            zoomMagnifier: {
                defaultValue: false
            },

	        /**
	         * @attribute
	         * @type String
	         */
            selectionOperation: {
                defaultValue: ""
            },

	        /**
	         * @attribute
	         * @type Array
	         */
            highlightAreas: {
                defaultValue: []
            }
        },

        EVENTS: {
            onGetCoordinate: {}
        },

	    /**
	     * 添加高亮区域
	     * @param {Object} config 高亮区域的配置
	     */
        addHighlightArea: function(config){
            var canvas = this, areas = canvas._highlightAreas;
            areas.push(config);
            if(canvas._rendered){
                canvas.refreshHighlightAreas();
            }
        },

	    /**
	     * 移除高亮区域
	     * @param {int} index 高亮区域的索引
	     */
        removeHighlightArea: function(index){
            var canvas = this, areas = canvas._highlightAreas;
            if(areas[index]){
                areas.removeAt(index);
                canvas.refreshHighlightAreas();
            }
        },

	    /**
	     * 刷新高亮区域。
	     * @private
	     */
        refreshHighlightAreas: function(){
            var i, canvas = this, areas = canvas._highlightAreas, doms = canvas._doms, scale = canvas._scale,
                canvasEl = doms.canvas, canvasLeft = parseInt(canvasEl.style.left, 10) || 0, canvasTop = parseInt(canvasEl.style.top, 10) || 0;

            var areaCount = areas.length, areaElCount = canvas._areaEls.length;

            if(areaCount < areaElCount){
                for(i = areaElCount - 1;i >= areaCount;i--){
                    canvas._areaEls[i].style.display = "none";
                }
            }

            if(areaCount > areaElCount){
                for(i = 0;i < areaCount - areaElCount;i++){
                    createHLArea(canvas);
                }
            }

            for(i = 0; i < areaCount; i++){
                var area = areas[i], el = canvas._areaEls[i];

                $fly(el).css(jQuery.extend({
                    display: "",
                    width: scale * area.width,
                    height: scale * area.height,
                    left: canvasLeft + scale * area.left,
                    top: canvasTop + scale * area.top
                }, area.style));
            }
        },

        createDom: function(){
            var canvas = this, dom = document.createElement("div"), doms = {}, canvasEl;
            if(dorado.Browser.msie){
                canvasEl = document.createElement("img");
                canvasEl.className = "canvas";
                dom.appendChild(canvasEl);
                doms.canvas = canvasEl;
            }else{
                canvasEl = document.createElement("canvas");
                canvasEl.className = "canvas";
	            canvasEl.style.position = "absolute";
                dom.appendChild(canvasEl);
                doms.canvas = canvasEl;
            }

            $fly(canvasEl).mousemove(function(event){
	            event.preventDefault();
            });

            function magnifierMousemove(event){
                var zoomMagnifier = canvas._zoomMagnifier;
                if(zoomMagnifier){
                    var px = event.pageX, py = event.pageY, el = getZoomMagnifierEl(canvas), dom = canvas._dom,
                        domPosition = $fly(dom).offset(), canvasLeft = parseInt(canvasEl.style.left, 10), canvasTop = parseInt(canvasEl.style.top, 10);

                    var imgEl = el.firstChild, ratio = 1.5, scale = canvas._scale * ratio, width, height;

                    if(dorado.Browser.msie){
                        width = canvasEl.originalWidth, height = canvasEl.originalHeight;

                        imgEl.style.width = scale * width + "px";
                        imgEl.style.height = scale * height + "px";

                        imgEl.style.left = -1 * ratio * (px - domPosition.left - canvasLeft - 50 + dom.scrollLeft) + "px";
                        imgEl.style.top = -1 * ratio * (py - domPosition.top - canvasTop - 50 + dom.scrollTop) + "px";
                    }else{
                        var image = canvas._image, imgOrginal = canvas.getImage(image);
                        width = imgOrginal.width, height = imgOrginal.height;

                        imgEl.style.width = scale * width + "px";
                        imgEl.style.height = scale * height + "px";

                        imgEl.style.left = -1 * ratio * (px - domPosition.left - canvasLeft - 50 + dom.scrollLeft) + "px";
                        imgEl.style.top = -1 * ratio * (py - domPosition.top - canvasTop - 50 + dom.scrollTop) + "px";
                    }

                    el.style.left = (px - domPosition.left - 50 + dom.scrollLeft) + "px";
                    el.style.top = (py - domPosition.top - 50 + dom.scrollTop) + "px";
                }
            }

	        $fly(dom).hover(function(){
                var zoomMagnifier = canvas._zoomMagnifier;
                if(zoomMagnifier){
                    var el = getZoomMagnifierEl(canvas);
                    el.style.display = "";
                    el.style.width = "100px";
                    el.style.height = "100px";

                    el.firstChild.src = canvas._image;

                    dom.onmousemove = magnifierMousemove;
                }
            }, function(){
                var zoomMagnifier = canvas._zoomMagnifier;
                if(zoomMagnifier){
                    var el = getZoomMagnifierEl(canvas);
                    el.style.display = "none";

                    dom.onmousemove = null;
                }
            });

	        $DomUtils.disableUserSelection(canvasEl);

			canvas._doms = doms;
			canvas._imagesCache = [];

	        var startX, startY, startScrollLeft, startScrollTop, offsetX, offsetY;

            var drag = $fly(canvasEl).draggable({
            	containment: canvasEl,
	            helper: function() {
					if (!selectProxy) {
						selectProxy = $DomUtils.xCreateElement({
							tagName: "div",
							className: "image-canvas-select-proxy",
							style: {
								display: "none"
							}
						});

						dom.appendChild(selectProxy);
					}
		            $fly(selectProxy).bringToFront();

					return selectProxy;
				},
	            start: function(event, ui) {
					var selection = canvas._selection, proxy = ui.helper;
					if (!selection) {
						if(!$fly(dom).hasClass("image-canvas-overflow")){
							event.preventDefault();
						} else {
							proxy[0]._scrollTop = dom.scrollTop;
							proxy[0]._scrollLeft = dom.scrollLeft;
						}
					} else if(proxy){
						var offset = $fly(dom).offset();

						startScrollLeft = dom.scrollLeft;
						startScrollTop = dom.scrollTop;
						startX = event.pageX - offset.left + startScrollLeft;
						startY = event.pageY - offset.top + startScrollTop;

						proxy.css({width: 0, height: 0, left: startX, top: startY, display: "block"});
					}
				},
	            drag: function(event, ui) {
		            var selection = canvas._selection, proxy = ui.helper, position;

					if(!selection){
						var mouseX = event.pageX, mouseY = event.pageY, inst = jQuery.data(this, "draggable");

						offsetX = mouseX - inst.originalPageX;
						offsetY = mouseY - inst.originalPageY;

						dom.scrollLeft = proxy[0]._scrollLeft + offsetX;
						dom.scrollTop = proxy[0]._scrollTop + offsetY;
					} else {
						var offset = $fly(dom).offset(), relativeLeft = event.pageX - offset.left + dom.scrollLeft, relativeTop = event.pageY - offset.top + dom.scrollTop;

						offsetX = relativeLeft - startX;
						offsetY = relativeTop - startY;

						var width = Math.sqrt(Math.pow(offsetX, 2)), height = Math.sqrt(Math.pow(offsetY, 2));

						proxy.css({width: width, height: height});

						if (offsetX >= 0 && offsetY <= 0) {//第一象限
							position = {
								left: relativeLeft - width,
								top: relativeTop
							};
						} else if (offsetX <= 0 && offsetY <= 0) {//第二象限
							position = {
								left: relativeLeft,
								top: relativeTop
							};
						} else if (offsetX <= 0 && offsetY >= 0) {//第三象限
							position = {
								left: relativeLeft,
								top: relativeTop - height
							};
						} else {
							position = {
								left: startX,
								top: startY
							};
						}
					}
					ui.position = position;
	            },
	            stop: function(event, ui) {
					var selection = canvas._selection;
					if (selection) {
						var proxy = ui.helper, intersectArea = intersect(proxy[0], doms.canvas);

						proxy.css("display", "none");

						var selectionOperation = canvas._selectionOperation, fn = dorado.widget.ImageCanvas.selectionBehaviors[selectionOperation];
						if (fn) {
							var result = fn.apply(canvas, [intersectArea]);
							if (result) {
							}
						}
					}
	            }
            });

            dom.className = "image-canvas";

            canvas._areaEls = [];
            canvas.refreshHighlightAreas();

            return dom;
        },

        _refreshIEImg: function(img){
            var canvas = this, zoomMode = canvas._zoomMode, dom = img.parentNode,
                imageWidth = img.originalWidth, imageHeight = img.originalHeight, domWidth = dom.clientWidth,
                domHeight = dom.clientHeight;

            var invert = canvas._invert, mirror = canvas._mirror, rotation = canvas._rotation, rotated = (rotation % 2 == 1);

            try{
                img.filters.item("DXImageTransform.Microsoft.BasicImage").Invert = invert ? 1 : 0;
                img.filters.item("DXImageTransform.Microsoft.BasicImage").mirror = mirror ? 1 : 0;
                img.filters.item("DXImageTransform.Microsoft.BasicImage").rotation = rotation;
            }catch(e){
            }

            if(zoomMode == "fitWindow"){
                var domRatio = domWidth / domHeight, imageRatio;

                if(rotated){
                    imageRatio = imageHeight / imageWidth;
                }else{
                    imageRatio = imageWidth / imageHeight;
                }

                if(domRatio >= imageRatio){
                    zoomMode = "fitHeight";
                }else{
                    zoomMode = "fitWidth";
                }
            }

            if(zoomMode == "fitWidth"){
                if(rotated){
                    img.style.height = domWidth + "px";
                    img.style.width = domWidth * imageWidth / imageHeight + "px";
					canvas._scale = domWidth / imageHeight;
                } else {
                    img.style.width = domWidth + "px";
                    img.style.height = imageHeight * domWidth / imageWidth + "px";
					canvas._scale = domWidth / imageWidth;
                }
            } else if(zoomMode == "fitHeight"){
                if (rotated) {
                    img.style.width = domHeight + "px";
                    img.style.height = domHeight * imageHeight / imageWidth + "px";
					canvas._scale = domHeight / imageWidth;
                } else {
                    img.style.height = domHeight + "px";
                    img.style.width = imageWidth * domHeight / imageHeight + "px";
					canvas._scale = domHeight / imageHeight;
                }
            } else if(zoomMode == "custom"){
                var scale = canvas._scale;
                img.style.width = scale * imageWidth + "px";
                img.style.height = scale * imageHeight + "px";
            }

            imageWidth = parseInt(img.style.width);
            imageHeight = parseInt(img.style.height);

            var overflow = false;

            if(rotated){
                if(imageHeight <= domWidth){
                    img.style.left = (domWidth - imageHeight) / 2 + "px";
                }else{
                    overflow = true;
                    img.style.left = 0;
                }

                if(imageWidth <= domHeight){
                    img.style.top = (domHeight - imageWidth) / 2 + "px";
                }else{
                    overflow = true;
                    img.style.top = 0;
                }
            } else {
                if(imageWidth <= domWidth){
                    img.style.left = (domWidth - imageWidth) / 2 + "px";
                }else{
                    overflow = true;
                    img.style.left = 0;
                }

                if(imageHeight <= domHeight){
                    img.style.top = (domHeight - imageHeight) / 2 + "px";
                }else{
                    overflow = true;
                    img.style.top = 0;
                }
            }

            if(overflow){
                $fly(dom).addClass("image-canvas-overflow");
            }else{
                $fly(dom).removeClass(dom, "image-canvas-overflow");
            }
        },

        _refreshImg: function(img){
            var canvas = this, tempCanvas = dorado.widget.ImageCanvas._tempCanvas, doms = canvas._doms || {},
                canvasEl = doms.canvas, rotation = canvas._rotation;

            tempCanvas.width = img.width;
            tempCanvas.height = img.height;
            tempCanvas.getContext("2d").drawImage(img, 0, 0);

            rotate(tempCanvas, img, rotation * 90);

            var mirror = canvas._mirror, invert = canvas._invert;

            if(mirror){
                mirrorAndInvert(tempCanvas, img, invert, (rotation % 2 == 1));
            }else if(invert){
                invertOnly(tempCanvas, img, (rotation % 2 == 1));
            }

            var imageWidth = img.width, imageHeight = img.height, zoomMode = canvas._zoomMode, dom = canvasEl.parentNode,
                domWidth = dom.clientWidth, domHeight = dom.clientHeight;

            if(zoomMode == "fitWindow"){
                var domRatio = domWidth / domHeight, imageRatio;

                imageRatio = imageWidth / imageHeight;

                if(domRatio >= imageRatio){
                    zoomMode = "fitHeight";
                }else{
                    zoomMode = "fitWidth";
                }
            }

            if (zoomMode == "fitWidth") {
				canvas._scale = domWidth / imageWidth;
            } else if(zoomMode == "fitHeight"){
				canvas._scale = domHeight / imageHeight;
            }

            var context = canvasEl.getContext("2d"), scale = canvas._scale, canvasWidth, canvasHeight;

            canvasHeight = canvasEl.height = tempCanvas.height * scale;
			canvasWidth = canvasEl.width = tempCanvas.width * scale;

	        var newDomWidth = dom.clientWidth, newDomHeight = dom.clientHeight;
	        canvasHeight = canvasEl.height = canvasHeight + (newDomHeight - domHeight);
	        canvasWidth = canvasEl.width = canvasWidth + (newDomWidth - domWidth);
	        domWidth = newDomWidth;
	        domHeight = newDomHeight;

            context.drawImage(tempCanvas, 0, 0, canvasWidth, canvasHeight);

            var overflow = false;

            if(canvasWidth <= domWidth){
                canvasEl.style.left = (domWidth - canvasWidth) / 2 + "px";
            }else{
                overflow = true;
                canvasEl.style.left = 0;
            }

            if(canvasHeight <= domHeight){
                canvasEl.style.top = (domHeight - canvasHeight) / 2 + "px";
            }else{
                overflow = true;
                canvasEl.style.top = 0;
            }

            if(overflow){
                $fly(dom).addClass("image-canvas-overflow");
            }else{
                $fly(dom).removeClass("image-canvas-overflow");
            }
        },

        refreshDom: function(dom){
            $invokeSuper.call(this, arguments);

            var canvas = this, image = canvas._image, doms = canvas._doms, canvasEl = doms.canvas;

            if(image){
                if(dorado.Browser.msie){
                    if((canvasEl._lastImage != image) || (!canvasEl._loaded)){
                        canvasEl.style.width = "auto";
                        canvasEl.style.height = "auto";
                        canvasEl.onload = function(){
                            var imageWidth = canvasEl.width, imageHeight = canvasEl.height;
                            canvasEl._loaded = true;
                            canvasEl.originalWidth = imageWidth;
                            canvasEl.originalHeight = imageHeight;

                            canvas._refreshIEImg(canvasEl);
                            canvas.refreshHighlightAreas();
                        };
                        canvasEl.src = image;
                    }else{
                        canvas._refreshIEImg(canvasEl);
                        canvas.refreshHighlightAreas();
                    }
                    canvasEl._lastImage = image;
                }else{
                    var img = canvas.getImage(image);

                    if(img.src){
                        if(img.complete){
                            canvas._refreshImg(img);
                            canvas.refreshHighlightAreas();
                        }
                    }else{
                        canvas._imagesCache[image] = img;
                        img.onload = function(){
                            canvas._refreshImg(img);
                            canvas.refreshHighlightAreas();
                        };
                        img.src = image;
                    }
                }
            }
        },

	    /**
	     * 反色。
	     */
        invertColor: function(){
            var canvas = this;
            canvas.set("invert", canvas._invert != true);
        },

	    /**
	     * 水平翻转。
	     */
        flipHorizental: function(){
            var canvas = this;
            canvas.set("mirror", canvas._mirror != true);
        },

	    /**
	     * 垂直翻转。
	     */
        flipVertical: function(){
            var canvas = this;
            canvas.set("mirror", canvas._mirror != true);
            canvas.set("rotation", (canvas._rotation + 2) % 4);
        },

	    /**
	     * 按照给定的缩放比例缩放。
	     * @param {double} scale 缩放
	     */
        zoom: function(scale){
            var canvas = this;
            canvas.set("zoomMode", "custom");
            canvas.set("scale", scale);
        },

	    /**
	     * 放大。
	     */
        zoomIn: function(){
            var canvas = this, scale = canvas._scale;
            canvas.zoom(scale * 1.25);
        },

	    /**
	     * 缩小。
	     */
        zoomOut: function(){
            var canvas = this, scale = canvas._scale;
            canvas.zoom(scale * 0.75);
        },

	    /**
	     * 向左转。
	     */
        rotateLeft: function(){
            var canvas = this;
            canvas.set("rotation", (canvas._rotation + 3) % 4);
        },

	    /**
	     * 向右转。
	     */
        rotateRight: function(){
            var canvas = this;
            canvas.set("rotation", (canvas._rotation + 1) % 4);
        },

	    /**
	     * 旋转180度。
	     */
        rotateHalfCircle: function(){
            var canvas = this;
            canvas.set("rotation", (canvas._rotation + 2) % 4);
        },

        save: function(){
            var canvas = this;
            return {
                image: canvas._image,
                zoomMode: canvas._zoomMode,
                scale: canvas._scale,
                invert: canvas._invert,
                mirror: canvas._mirror,
                rotation: canvas._rotation
            };
        },

        restore: function(options){
            var canvas = this;
            if(options){
                canvas.set(options);
            }
        },

		getImage: function(src){
			if(!src){
				throw new Error("src can not be empty.");
			}
			var cache = this._imagesCache;
			if(cache[src]){
				return cache[src];
			}else{
				return new Image();
			}
		},

		destroy: function() {
			this._imagesCache = null;
			$invokeSuper.call(this, arguments);
		}
    });

    dorado.widget.ImageCanvas._tempCanvas = document.createElement("canvas");

    dorado.widget.ImageCanvas.selectionBehaviors = {
        zoomSelect: function(intersectArea){
            var canvas = this, dom = canvas._dom, doms = canvas._doms;
            if(intersectArea.inRegion){
                var canvasRegion = $fly(doms.canvas).region(), widthRatio = canvasRegion.width / intersectArea.width,
                    heightRatio = canvasRegion.height / intersectArea.height, ratio = widthRatio > heightRatio ? heightRatio : widthRatio;

                var scale = canvas._scale;

				canvas._zoomMode = "custom";
				canvas._scale = scale * ratio;
                canvas.refresh();

                var rleft = intersectArea.left - canvasRegion.left, rtop = intersectArea.top - canvasRegion.top;
                dom.scrollTop = rtop * ratio;
                dom.scrollLeft = rleft * ratio;
            }
        },
        getCoordinate: function(intersectArea){
            var canvas = this, doms = canvas._doms;
            if(intersectArea.inRegion){
                var canvasRegion = $fly(doms.canvas).region(), scale = canvas._scale;

                var rleft = intersectArea.left - canvasRegion.left, rtop = intersectArea.top - canvasRegion.top;
                var coordinate =  {
                    top: parseInt(rtop / scale, 10),
                    left: parseInt(rleft / scale, 10),
                    width: parseInt(intersectArea.width / scale, 10),
                    height: parseInt(intersectArea.height / scale, 10)
                };
                canvas.fireEvent("onGetCoordinate", canvas, coordinate);

                return coordinate;
            }
        }
    };
})();