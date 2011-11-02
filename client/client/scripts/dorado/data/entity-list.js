(function() {

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 实体对象集合。
	 * @param {Object[]|Object} [data] 用作初始化集合元素的JSON数据。<br>
	 * 如果此处传入的是一个数组，那么数组中的对象会被逐一意添加到集合中; 如果传入的单个的对象，那么该对象会被作为一个元素添加到集合中。
	 * @param {dorado.DataRepository} [dataTypeRepository] 数据类型的管理器。
	 * @param {dorado.AggregationDataType} [dataType] 集合数据类型。
	 */
	dorado.EntityList = function(data, dataTypeRepository, dataType) {
	
		/**
		 * 集合中当前数据实体。
		 * @name dorado.EntityList#current
		 * @property
		 * @type dorado.Entity
		 */
		// ======
		
		this.objId = dorado.Core.getTimestamp() + '';
		
		/**
		 * 集合的时间戳。<br>
		 * 集合的元素有增减、或者有新的页被装载时，集合都会更新自己的时间戳， 因此，时间戳可以可以用来判断集合在一段时间内有没有被修改过。
		 * @type int
		 */
		this.timestamp = dorado.Core.getTimestamp();
		
		/**
		 * 该集合中的数据类型所属的数据类型管理器。
		 * @type dorad.DataRepository
		 */
		this.dataTypeRepository = dataTypeRepository;
		
		if (data) {
			if (dataType == null) {
				if (dataTypeRepository && data.$dataType) dataType = dataTypeRepository.get(data.$dataType);
			} else {
				data.$dataType = dataType._id;
			}
		}
		
		/**
		 * 集合数据类型。
		 * @type dorado.AggregationDataType
		 */
		this.dataType = dataType;
		
		/**
		 * 集合中元素的数据类型。
		 * @type dorado.EntityDataType
		 */
		this.elementDataType = (dataType) ? dataType.getElementDataType() : null;
		
		/**
		 * 进行分页浏览时每页的记录数。默认值为0。
		 * @type int
		 */
		this.pageSize = (dataType) ? dataType._pageSize : 0;
		
		/**
		 * 当前位置所处的页号。默认值为1。
		 * @type int
		 * @default 1
		 */
		this.pageNo = 1;
		
		/**
		 * 总的页数（包含尚未装载的页）。
		 * @type int
		 */
		this.pageCount = 0;
		
		/**
		 * 总的记录数（包含尚未装载的页中的记录数）。
		 * @type int
		 */
		this.entityCount = 0;
		
		this._pages = [];
		this._keyMap = {};
		if (data != null) this.fromJSON(data);
	};
	
	dorado.EntityList._MESSAGE_CURRENT_CHANGED = 10;
	dorado.EntityList._MESSAGE_DELETED = 11;
	dorado.EntityList._MESSAGE_INSERTED = 12;
	
	/**
	 * @name dorado.EntityList#current
	 * @property
	 * @type dorado.Entity
	 * @description 当前的数据实体。
	 */
	/**
	 * @name dorado.EntityList#dataProvider
	 * @property
	 * @type dorado.DataProvider
	 * @description 获取为实体对象集合提供数据的数据提供者。
	 */
	/**
	 * @name dorado.EntityList#parameter
	 * @property
	 * @type Object
	 * @description 装载数据使用的附加参数。
	 * <p>
	 * 当集合需要通过DataProvider装载数据时，系统会将此处定义的参数合并到DataProvider原有的参数之上。 这里的合并可包含两种情况：
	 * 当parameter是一个JSON对象时，系统会将该JSON对象中的个属性复制到DataProvider原有的参数之上；
	 * 当parameter不是一个JSON对象时（如String、int等），系统会直接用此参数替换DataProvider原有的参数。
	 * </p>
	 */
	// =====
	$class(/** @scope dorado.EntityList.prototype */{
		$className: "dorado.EntityList",
		
		constructor: dorado.EntityList,
		
		_disableObserversCounter: 0,
		
		_setObserver: function(observer) {
			this._observer = observer;
			this.each(function(v) {
				if (v instanceof dorado.Entity) v._setObserver(observer);
			});
		},
		
		/**
		 * @name dorado.EntityList#disableObservers
		 * @function
		 * @description 禁止dorado.EntityList将消息发送给其观察者。
		 * <p>
		 * 该方法的主要作用是阻止与该实体对象集合关联的数据控件自动根据实体对象的变化刷新自身的显示内容，
		 * 这样做的目的一般是为了提高对实体对象集合连续进行操作时的运行效率。
		 * </p>
		 */
		disableObservers: dorado.Entity.prototype.disableObservers,
		
		/**
		 * @name dorado.EntityList#enableObservers
		 * @function
		 * @description 允许dorado.EntityList将消息发送给其观察者。
		 */
		enableObservers: dorado.Entity.prototype.enableObservers,
		
		/**
		 * @name dorado.EntityList#notifyObservers
		 * @function
		 * @description 通知dorado.EntityList的观察者刷新数据。
		 */
		notifyObservers: dorado.Entity.prototype.notifyObservers,
		
		sendMessage: function(messageCode, arg) {
			if (this._disableObserversCounter == 0 && this._observer) {
				this._observer.entityMessageReceived(messageCode, arg);
			}
		},
		
		_findPreviousEntry: function(entry, loadPage, pageNo) {
			if (pageNo == null) pageNo = this.pageNo;
			var previous = (entry) ? entry.previous : null;
			while (!(previous && previous.data.state != dorado.Entity.STATE_DELETED)) {
				if (!previous) {
					if (pageNo > 1) {
						pageNo--;
						if (this.isPageLoaded(pageNo)) {
							previous = this.getPage(pageNo, loadPage).last;
						}
					}
				} else {
					previous = previous.previous;
				}
				
				if (!previous) break;
			}
			return previous;
		},
		
		_findNextEntry: function(entry, loadPage, pageNo) {
			if (pageNo == null) pageNo = this.pageNo;
			var next = (entry) ? entry.next : null;
			while (!(next && next.data.state != dorado.Entity.STATE_DELETED)) {
				if (!next) {
					if (pageNo < this.pageCount) {
						pageNo++;
						if (this.isPageLoaded(pageNo)) {
							next = this.getPage(pageNo, loadPage).first;
						}
					}
				} else {
					next = next.next;
				}
				
				if (!next) break;
			}
			return next;
		},
		
		_throwInvalidEntity: function(entity) {
			throw new dorado.ResourceException("dorado.data.InvalidEntityToList");
		},
		
		_throwNoCurrent: function() {
			throw new dorado.ResourceException("dorado.data.NoCurrent");
		},
		
		/**
		 * 判断某一个页的数据是否已经装载。
		 * @param {int} pageNo 页号。从1开始的数字。
		 * @return {boolean} 是否已经装载。
		 */
		isPageLoaded: function(pageNo) {
			var page = this._pages[pageNo];
			return (page && page.loaded);
		},
		
		getPage: function(pageNo, loadPage, callback) {
			if (pageNo > 0 && pageNo <= this.pageCount) {
				var page = this._pages[pageNo];
				if (!page) {
					page = new dorado.EntityList.Page(this, pageNo);
					this._pages[pageNo] = page;
				}
				
				if (page.loaded) {
					if (callback) {
						$callback(callback, true, page);
						return;
					}
				} else {
					if (loadPage) {
						if (this.dataProvider) {
							var pipe = page.loadPagePipe;
							if (!pipe) {
								page.loadPagePipe = pipe = new LoadPagePipe(this, pageNo);
							}
							
							if (callback) {
								pipe.getAsync({
									scope: this,
									callback: function(success, result) {
										if (success && !page.loaded) {
											this._fillPage(page, result);
											page.loaded = true;
										}
										$callback(callback, success, ((success) ? page : result));
									}
								});
							} else {
								var result = pipe.get();
								this._fillPage(page, result);
								page.loaded = true;
							}
						} else {
							page.loaded = true;
						}
					}
				}
				return page;
			} else {
				throw new dorado.ResourceException("dorado.data.InvalidPage", pageNo);
			}
		},
		
		getPageEntityCount: function(pageNo) {
			if (pageNo > 0) {
				return this.getPage(pageNo).entityCount;
			} else {
				return this.current ? this.current.page.entityCount : 0;
			}
		},
		
		/**
		 * 将集合中的某个数据实体设置为当前数据实体。
		 * @param {dorado.Entity} current 数据实体。
		 */
		setCurrent: function(current) {
			if (this.current == current) return;
			
			if (current && (!current.page || current.page.entityList != this)) {
				this._throwInvalidEntity(current);
			}
			if (current && current.state == dorado.Entity.STATE_DELETED) {
				throw new dorado.ResourceException("dorado.data.EntityDeleted");
			}
			
			var eventArg = {
				entityList: this,
				oldCurrent: this.current,
				newCurrent: current,
				processDefault: true
			};
			
			var dataType = this.dataType, elementDataType;
			if (dataType) elementDataType = dataType.getElementDataType();
			if (elementDataType) elementDataType.fireEvent("beforeCurrentChange", elementDataType, eventArg);
			if (!eventArg.processDefault) return;
			
			this.current = current;
			this.pageNo = (current) ? current.page.pageNo : 1;
			this.timestamp = dorado.Core.getTimestamp();
			
			this.sendMessage(dorado.EntityList._MESSAGE_CURRENT_CHANGED, eventArg);
			if (elementDataType) elementDataType.fireEvent("onCurrentChange", elementDataType, eventArg);
		},
		
		/**
		 * 返回当前数据实体之前是否还存在其它数据实体。
		 * @return {boolean} 当前数据实体之前是否还存在其它数据实体。
		 */
		hasPrevious: function() {
			if (this.current) {
				var page = this.current.page;
				var entry = page.findEntry(this.current);
				entry = this._findPreviousEntry(entry, true);
				return entry != null;
			} else if (this.entityCount > 0) {
				this._throwNoCurrent();
			}
		},
		
		/**
		 * 返回当前数据实体之后是否还存在其它数据实体。
		 * @return {boolean} 当前数据实体之后是否还存在其它数据实体。
		 */
		hasNext: function() {
			if (this.current) {
				var page = this.current.page;
				var entry = page.findEntry(this.current);
				entry = this._findNextEntry(entry, true);
				return entry != null;
			} else if (this.entityCount > 0) {
				this._throwNoCurrent();
			}
		},
		
		/**
		 * 将集合中的第一个数据实体设置为当前数据实体。
		 * @return {dorado.Entity} 返回第一个数据实体。
		 */
		first: function() {
			var entry = this._findNextEntry(null, true, 0);
			var entity = (entry) ? entry.data : null;
			this.setCurrent(entity);
			return entity;
		},
		
		/**
		 * 将当前数据实体的前一个数据实体设置为当前数据实体。
		 * @return {dorado.Entity} 返回前一个数据实体。
		 */
		previous: function() {
			if (this.current) {
				var page = this.current.page;
				var entry = page.findEntry(this.current);
				entry = this._findPreviousEntry(entry, true);
				if (entry) {
					this.setCurrent(entry.data);
					return entry.data;
				}
				return null;
			} else if (this.entityCount > 0) {
				this._throwNoCurrent();
			}
		},
		
		/**
		 * 将当前数据实体的下一个数据实体设置为当前数据实体。
		 * @return {dorado.Entity} 返回下一个数据实体。
		 */
		next: function() {
			if (this.current) {
				var page = this.current.page;
				var entry = page.findEntry(this.current);
				entry = this._findNextEntry(entry, true);
				if (entry) {
					this.setCurrent(entry.data);
					return entry.data;
				}
				return null;
			} else if (this.entityCount > 0) {
				this._throwNoCurrent();
			}
		},
		
		/**
		 * 将集合中的最后一个数据实体设置为当前数据实体。
		 * @return {dorado.Entity} 返回最后一个数据实体。
		 */
		last: function() {
			var entry = this._findPreviousEntry(null, true, this.pageCount + 1);
			var entity = (entry) ? entry.data : null;
			this.setCurrent(entity);
			return entity;
		},
		
		/**
		 * 根据给定的偏移量和当前数据实体的位置，向前或向后定位到另一个数据实体并将其设置为新的当前数据实体。
		 * 例如设置offset为1，表示将当前数据实体的下一个数据实体设置为的当前数据实体。
		 * @param {int} offset 移动的步数，可以是负数。
		 */
		move: function(offset) {
			var page = this.current.page;
			var entry = page.findEntry(this.current);
			if (offset > 0) {
				for (var i = 0; i < offset; i++) {
					entry = this._findNextEntry(entry, true);
					if (!entry && this.entityCount > 0) this._throwNoCurrent();
				}
			} else if (offset < 0) {
				for (var i = 0; i > offset; i--) {
					entry = this._findPreviousEntry(entry, true);
					if (!entry && this.entityCount > 0) this._throwNoCurrent();
				}
			}
			this.setCurrent(entry.data);
			return entry.data;
		},
		
		/**
		 * 将实体集合翻到指定页中。 如果指定的页中的数据尚未装载，则将首先装载该页的数据。
		 * <p>
		 * 此方法有两种执行方式，如果不指定callback参数将按照同步模式执行，如果指定了具体的callback则按照异步模式运行。
		 * </p>
		 * @param {int} pageNo 要跳转到的页号。从1开始的数字。
		 * @param {Function|dorado.Callback} [callback] 回调对象，传入回调对象的参数即为新的页。
		 * @return {dorado.Entity} 如果是是同步模式调用的话，则返回新页中的第一个数据实体，否则返回null。
		 */
		gotoPage: function(pageNo, callback) {
			if (callback) {
				var self = this;
				this.getPage(pageNo, true, {
					callback: function(success, result) {
						if (success) {
							var entry = result.first;
							while (entry && entry.data.state == dorado.Entity.STATE_DELETED) {
								entry = entry.next;
							}
							var entity = (entry) ? entry.data : null;
							if (entity) self.setCurrent(entity);
						}
						$callback(callback, success, result);
					}
				});
			} else {
				var entry = this.getPage(pageNo, true).first;
				while (entry && entry.data.state == dorado.Entity.STATE_DELETED) {
					entry = entry.next;
				}
				var entity = (entry) ? entry.data : null;
				if (entity) this.setCurrent(entity);
				return entity;
			}
		},
		
		/**
		 * 将实体集合翻到第一页。
		 * <p>
		 * 此方法有两种执行方式，如果不指定callback参数将按照同步模式执行，如果指定了具体的callback则按照异步模式运行。
		 * </p>
		 * @param {Function|dorado.Callback} [callback] 回调对象，传入回调对象的参数即为新的页。
		 */
		firstPage: function(callback) {
			this.gotoPage(1, callback);
		},
		
		/**
		 * 将实体集合向前翻一页。
		 * <p>
		 * 此方法有两种执行方式，如果不指定callback参数将按照同步模式执行，如果指定了具体的callback则按照异步模式运行。
		 * </p>
		 * @param {Function|dorado.Callback} [callback] 回调对象，传入回调对象的参数即为新的页。
		 */
		previousPage: function(callback) {
			if (this.pageNo <= 1) return;
			this.gotoPage(this.pageNo - 1, callback);
		},
		
		/**
		 * 将实体集合向后翻一页。
		 * <p>
		 * 此方法有两种执行方式，如果不指定callback参数将按照同步模式执行，如果指定了具体的callback则按照异步模式运行。
		 * </p>
		 * @param {Function|dorado.Callback} [callback] 回调对象，传入回调对象的参数即为新的页。
		 */
		nextPage: function(callback) {
			if (this.pageNo >= this.pageCount) return;
			this.gotoPage(this.pageNo + 1, callback);
		},
		
		/**
		 * 将实体集合翻到最后一页。
		 * <p>
		 * 此方法有两种执行方式，如果不指定callback参数将按照同步模式执行，如果指定了具体的callback则按照异步模式运行。
		 * </p>
		 * @param {Function|dorado.Callback} [callback] 回调对象，传入回调对象的参数即为新的页。
		 */
		lastPage: function(callback) {
			this.gotoPage(this.pageCount, callback);
		},
		
		/**
		 * 向集合中插入一个数据实体。
		 * @param {dorado.Entity|Object} entity {optional} 要插入的数据实体或数据实体对应的JSON数据对象。
		 * 如果不指定此参数或设置其值为null，EntityList会自动根据elementDataType来创建一个新的数据实体并插入。
		 * @param {String} [insertMode] 插入方式，包含下列四种取值：
		 * <ul>
		 * <li>begin - 在集合的起始位置插入。</li>
		 * <li>before - 在refEntity参数指定的数据实体之前插入。</li>
		 * <li>after - 在refEntity参数指定的数据实体之后插入。</li>
		 * <li>end - 在集合的末尾插入。默认值。</li>
		 * </ul>
		 * @param {dorado.Entity} [refEntity] 插入位置的参照数据实体。
		 * @return 返回插入的数据实体。
		 */
		insert: function(entity, insertMode, refEntity) {
			if (entity == null) {
				entity = this.createChild(null, true);
			} else if (entity instanceof dorado.Entity) {
				if (entity.page) {
					var entityList = entity.page.entityList;
					if (entityList) entityList.remove(entity, true);
				}
			} else {
				entity = new dorado.Entity(entity, this.dataTypeRepository, this.elementDataType);
			}
			
			if (insertMode == "before" || insertMode == "after") {
				refEntity = refEntity || this.current;
				if (!refEntity) insertMode = (insertMode == "before") ? "begin" : "after";
			}
			var eventArg = {
				entityList: this,
				entity: entity,
				insertMode: insertMode,
				refEntity: refEntity,
				processDefault: true
			};
			
			var dataType = entity.dataType;
			if (dataType) dataType.fireEvent("beforeInsert", dataType, eventArg);
			if (!eventArg.processDefault) return;
			
			if (this.pageCount == 0 && this.pageNo == 1) this.pageCount = 1;
			var page = this.getPage(this.pageNo, true);
			page.insert(entity, insertMode, refEntity);
			if (entity.state != dorado.Entity.STATE_MOVED) entity.setState(dorado.Entity.STATE_NEW);
			page.entityCount++;
			this.entityCount++;
			this.timestamp = dorado.Core.getTimestamp();
			
			if (dataType) dataType.fireEvent("onInsert", dataType, eventArg);
			this.sendMessage(dorado.EntityList._MESSAGE_INSERTED, eventArg);
			
			this.setCurrent(entity);
			return entity;
		},
		
		/**
		 * 从集合中删除一个数据实体。
		 * @param {dorado.Entity} [entity] 要删除的数据实体，如果此参数为空则表示将要删除集合中的当前数据实体。
		 * @param {boolean} [detach] 是否彻底断开被删除的数据实体与集合之间的关联。默认为不断开。<br>
		 * 在通常情况下，当我们从集合中删除一个数据实体时，dorado只是在内部处理中将数据实体的状态标记为已删除状态而并没有真正的将数据实体从合集中移除掉。
		 * 这样做的目的是为了便于在今后提交时能够清晰的掌握集合中的元素究竟做过哪些改变。
		 */
		remove: function(entity, detach) {
			if (!entity) {
				if (!this.current) this._throwNoCurrent();
				entity = this.current;
			}
			if (entity.parent != this) this._throwInvalidEntity();
			
			var eventArg = {
				entity: entity,
				entityList: this,
				processDefault: true
			};
			
			var dataType = entity.dataType, simpleDetach = (entity.state == dorado.Entity.STATE_DELETED);
			if (!simpleDetach) {
				if (dataType) dataType.fireEvent("beforeRemove", dataType, eventArg);
				if (!eventArg.processDefault) return;
			}
			
			var isCurrent = (this.current == entity);
			var newCurrent = null;
			if (isCurrent) {
				var entry = entity.page.findEntry(this.current);
				var newCurrentEntry = this._findNextEntry(entry);
				if (!newCurrentEntry) newCurrentEntry = this._findPreviousEntry(entry);
				if (newCurrentEntry) newCurrent = newCurrentEntry.data;
			}
			
			var page = entity.page;
			if (simpleDetach) {
				detach = true;
			} else {
				page.entityCount--;
				this.entityCount--;
				detach = detach || entity.state == dorado.Entity.STATE_NEW;
				entity.setState(dorado.Entity.STATE_DELETED);
			}
			this.timestamp = dorado.Core.getTimestamp();
			
			if (!simpleDetach) {
				if (dataType) dataType.fireEvent("onRemove", dataType, eventArg);
				this.sendMessage(dorado.EntityList._MESSAGE_DELETED, eventArg);
			}
			if (detach) page.remove(entity);
			
			if (isCurrent) this.setCurrent(newCurrent);
		},
		
		/**
		 * 创建并返回一个子实体对象。
		 * @param {Object} [data] 新创建的实体对象要封装JSON数据对象，可以不指定此参数。
		 * @param {boolean} [detached] 是否需要返回一个游离的实体对象。
		 * 默认情况下，新创建的子实体对象会直接被设置到集合中。
		 * 通过detached参数可以指定本方法不将新的子实体对象附着到本集合中。
		 * @return {dorado.Entity} 新创建的实体对象。
		 */
		createChild: function(data, detached) {
			var elementDataType = (this.dataType) ? this.dataType.getElementDataType() : null;
			if (elementDataType && !(elementDataType instanceof dorado.EntityDataType)) {
				throw new ResourceException("dorado.data.EntityPropertyExpected", property);
			}
			var child = new dorado.Entity(data, this.dataTypeRepository, elementDataType);
			if (!detached) this.insert(child);
			return child;
		},
		
		/**
		 * 根据传入的{@link dorado.Entity}的id返回匹配的数据实体对象。
		 * @param {Object} id 数据实体的id。
		 * @return {dorado.Entity} 匹配的数据实体对象。
		 */
		getById: function(id) {
			return this._keyMap[id];
		},
		
		_fillPage: function(page, jsonArray, changeCurrent) {
			page.entityCount = 0;
			
			if (jsonArray == null) return;
			if (!(jsonArray instanceof Array)) jsonArray = [jsonArray];
			var entity, firstEntity;
			
			var dataType = this.dataType;
			if (dataType) dataType._disableObserversCounter++;
			this._disableObserversCounter++;
			try {
				var elementDataType = this.elementDataType;
				for (var i = 0; i < jsonArray.length; i++) {
					if (elementDataType != null) {
						entity = elementDataType.parse(jsonArray[i]);
					} else {
						entity = new dorado.Entity(jsonArray[i], (this.dataType) ? this.dataType.get("dataTypeRepository") : null);
					}
					if (i == 0) firstEntity = entity;
					page.insert(entity);
					if (entity.state != dorado.Entity.STATE_DELETED) {
						page.entityCount++;
						this.entityCount++;
					}
				}
				
				if (jsonArray.entityCount) this.entityCount = jsonArray.entityCount;
				if (jsonArray.pageCount) this.pageCount = jsonArray.pageCount;
				
				if (changeCurrent && firstEntity) this.setCurrent(firstEntity);
			}
			finally {
				this._disableObserversCounter--;
				if (dataType) dataType._disableObserversCounter--;
			}
			
			if (firstEntity) {
				this.timestamp = dorado.Core.getTimestamp();
				this.sendMessage(0);
			}
		},
		
		/**
		 * 取消对当前数据实体集合的各种数据操作。
		 */
		cancel: function() {
			var it = this.iterator(this), entity;
			while (it.hasNext()) {
				entity = it.next();
				entity.disableObservers();
				entity.cancel();
				entity.enableObservers();
			}
			if (entity) {
				this.timestamp = dorado.Core.getTimestamp();
				this.sendMessage(0);
			}
		},
		
		/**
		 * 清空集合中的所有数据。
		 */
		clear: function() {
			this._pages = [];
			this._keyMap = {};
			this.entityCount = 0;
			this.current = null;
			this.sendMessage(0);
		},
		
		/**
		 * 清空集合中的所有数据并重新装载当前页中的数据。
		 */
		flush: function(callback) {
			if (!this.dataProvider) throw new dorado.ResourceException("dorado.data.NoDataPipe");
			
			this._disableObserversCounter++;
			try {
				this.clear();
			}
			finally {
				this._disableObserversCounter--;
			}
			
			if (callback) {
				var self = this;
				this.getPage(this.pageNo, true, function(success, page) {
					self._disableObserversCounter++;
					try {
						if (success) {
							var entity = (page.first) ? page.first.data : null;
							self.setCurrent(entity);
							$callback(callback, true, null);
						}
					}
					finally {
						self._disableObserversCounter--;
						self.sendMessage(0);
					}
				});
			} else {
				var entry = this.getPage(this.pageNo, true).first;
				var entity = (entry) ? entry.data : null;
				this._disableObserversCounter++;
				try {
					this.setCurrent(entity);
				}
				finally {
					this._disableObserversCounter--;
					this.sendMessage(0);
				}
			}
		},
		
		/**
		 * 清空集合中的所有数据并以异步方式重新装载当前页中的数据。
		 * @param {Function|dorado.Callback} callback 回调对象。
		 */
		flushAsync: function(callback) {
			this.flush(callback);
		},
		
		/**
		 * 将给定的JSON对象中的数据转换成为数据实体并添加到集合中。
		 * @param {Object[]|Object} jsonArray 要转换的JSON对象。<br>
		 * 如果此处传入的是一个数组，那么数组中的对象会被逐一意添加到集合中; 如果传入的单个的对象，那么该对象会被作为一个元素添加到集合中。
		 */
		fromJSON: function(jsonArray) {
			if (jsonArray.pageNo) this.pageNo = jsonArray.pageNo;
			if (this.pageCount == 0) {
				if (jsonArray.pageCount) {
					this.pageCount = jsonArray.pageCount;
				} else if (this.pageNo == 1) {
					this.pageCount = 1;
				}
			}
			var page = this.getPage(this.pageNo, true);
			this._fillPage(page, jsonArray, true);
		},
		
		/**
		 * 将实体集合转换成一个JSON数组。
		 * @param {Object} [options] 转换选项。
		 * @param {String[]} [options.properties] 属性名数组，表示只转换该数组中列举过的属性。如果不指定此属性表示转换实体对象中的所有属性。
		 * 此属性对于{@link dorado.EntityList}的toJSON而言是没有意义的，但是由于options参数会自动被传递到集合中{@link dorado.Entity}的toJSON方法中，
		 * 因此它会影响内部{@link dorado.Entity}的处理过程。
		 * @param {boolean} [options.includeReferenceProperties=true] 是否转换实体对象中{@link dorado.Reference}类型的属性。默认按true进行处理。
		 * @param {boolean} [options.includeLookupProperties=true] 是否转换实体对象中{@link dorado.Lookup}类型的属性。默认按true进行处理。
		 * @param {boolean} [options.includeUnloadPage=true] 是否转换{@link dorado.EntityList}中尚未装载的页中的数据。默认按false进行处理。
		 * @param {boolean} [options.includeDeletedEntity] 是否转换那些被标记为"已删除"的数据实体。
		 * @param {boolean} [options.generateDataType] 是否在JSON对象中生成DataType信息，生成的DataType信息将被放置在名为$dataType的特殊子属性中。
		 * 此属性对于{@link dorado.EntityList}的toJSON而言是没有意义的，但是由于options参数会自动被传递到集合中{@link dorado.Entity}的toJSON方法中，
		 * 因此它会影响内部{@link dorado.Entity}的处理过程。
		 * 另外，此属性的只对集合中的第一个JSON对象有效，即dorado认为集合中的所有{@link dorado.Entity}的DataType都是相同的，因此不必为每一个{@link dorado.Entity}生成DataType信息。
		 * @param {boolean} [options.generateState] 是否在JSON对象中生成实体对象的状态信息(即新增、已更改等状态)，生成的状态信息将被放置在名为$state的特殊子属性中。
		 * 此属性对于{@link dorado.EntityList}的toJSON而言是没有意义的，但是由于options参数会自动被传递到集合中{@link dorado.Entity}的toJSON方法中，
		 * 因此它会影响内部{@link dorado.Entity}的处理过程。
		 * @param {boolean} [options.generateEntityId] 是否在JSON对象中生成实体对象的ID，生成的状态信息将被放置在名为$entityId的特殊子属性中。
		 * 此属性对于{@link dorado.EntityList}的toJSON而言是没有意义的，但是由于options参数会自动被传递到集合中{@link dorado.Entity}的toJSON方法中，
		 * 因此它会影响内部{@link dorado.Entity}的处理过程。
		 * @param {boolean} [options.generateOldData] 是否在JSON对象中生成旧数据，生成的状态信息将被放置在名为$oldData的特殊子属性中。
		 * 此属性对于{@link dorado.EntityList}的toJSON而言是没有意义的，但是由于options参数会自动被传递到集合中{@link dorado.Entity}的toJSON方法中，
		 * 因此它会影响内部{@link dorado.Entity}的处理过程。
		 * @param {Function} [options.entityFilter] 用户自定义的数据实体过滤函数，返回true/false表示是否需要将此当前数据实体转换到JSON中。
		 * 此函数的传入参数如下：
		 * @param {dorado.Entity} [options.entityFilter.entity] 当前正被过滤的数据实体。
		 * @return {Object[]} 得到的JSON数组。
		 */
		toJSON: function(options, context) {
			var result = [];
			var generateDataType = (options) ? options.generateDataType : false;
			var entityFilter = (options) ? options.entityFilter : null;
			var it = this.iterator(options);
			while (it.hasNext()) {
				var entity = it.next();
				if (entity) {
					if (!entityFilter || entityFilter(entity)) {
						result.push(entity.toJSON(options, context));
					}
				} else {
					result.push(null);
				}
				if (generateDataType) options.generateDataType = false;
			}
			if (generateDataType) options.generateDataType = true;
			if (result.length == 0 && entityFilter) result = null;
			return result;
		},
		
		/**
		 * 返回包含所有数据实体的数组。
		 * <p>
		 * 注意：返回的数组中将只包含当前已下载到客户端的数据实体。
		 * </p>
		 * @return {Object[]} 得到的Entity数组。
		 */
		toArray: function() {
			var result = [];
			this.each(function(entity) {
				result.push(entity);
			});
			return result;
			
		},
		
		/**
		 * 针对集合的每一个数据实体执行指定的函数。此方法可用于替代对集合的遍历代码。
		 * @param {Function} fn 针对每一个数据实体的函数。
		 * @param {Object} [scope] 函数脚本的宿主，即函数脚本中this的含义。如果此参数为空则表示this为集合中的某个数据实体。
		 *
		 * @example
		 * // 将每一个集合元素的name属性连接成为一个字符串
		 * var names = "";
		 * var entityList = ...
		 * entityList.each(function(entity){
		 *	 names += entity.get("name");
		 * });
		 */
		each: function(fn, scope) {
			for (var i = 1; i <= this.pageCount; i++) {
				if (this.isPageLoaded(i)) this._pages[i].each(fn, scope);
			}
		},
		
		/**
		 * 返回数据实体的迭代器。
		 * @param {Object|boolean} [options] 迭代选项。
		 * 此参数具有两种设定方式，当直接传入逻辑值true/false时，dorado会将此逻辑值直接认为是针对上述includeDeletedEntity子属性的值；
		 * 当传入的是一个对象时，dorado将尝试识别该对象子属性。
		 * @param {boolean} [options.includeDeletedEntity] 是否迭代被标记已删除的数据。
		 * @param {boolean} [options.includeUnloadPage] 是否迭代目前尚未下载的页中的数据。
		 * 如果选择是，那么迭代的过程将引起集合自动装载那些尚未下载页。
		 * @param {int} [options.nextIndex] 从第几个数据实体开始迭代。
		 * @param {int} [options.pageNo] 只遍历指定页号中的数据。从1开始的数字。
		 * @param {boolean} [options.currentPage] 只遍历当前页号中的数据。
		 * @param {boolean} [options.simulateUnloadPage] 是否对为未下载的页进行模拟遍历。
		 * 在模拟遍历的过程中，迭代器将返回一个虚假的dorado.Entity实例代替未下载页中的某个数据实体。
		 * 当启用此选项时，迭代器将强制按照options.includeUnloadPage=true的情况来进行迭代，但不会在迭代过程中引起数据装载。
		 * @return {dorado.util.Iterator} 数据实体的迭代器。
		 */
		iterator: function(options) {
			return new dorado.EntityList.EntityListIterator(this, options);
		},
		
		toText: function() {
			return this.toString();
		},
		
		toString: function() {
			return "EntityList@" + this.objId + "(" + this.entityCount + ")";
		},
		
		clone: function(deep) {
			var cloned = new dorado.EntityList(null, this.dataTypeRepository, this.dataType);
			for (var it = this.iterator(); it.hasNext();) {
				var entity = it.next();
				if (deep) entity = dorado.Core.clone(entity, deep);
				cloned.insert(entity);
			}
			return cloned;
		}
	});
	
	var Page = dorado.EntityList.Page = $extend(dorado.util.KeyedList, {
		$className: "dorado.EntityList.Page",
		
		constructor: function(entityList, pageNo) {
			$invokeSuper.call(this, [(function(entity) {
				return entity.entityId;
			})]);
			
			this.entityList = entityList;
			this.pageNo = pageNo;
			this.entityCount = entityList.pageSize;
		},
		
		insert: function(data, insertMode, refData) {
			$invokeSuper.call(this, arguments);
			data.page = this;
			data.parent = this.entityList;
			data._setObserver(this.entityList._observer);
			this.entityList._keyMap[data.entityId] = data;
			this.loaded = true;
		},
		
		remove: function(data) {
			$invokeSuper.call(this, arguments);
			data.page = null;
			data.parent = null;
			data._setObserver(null);
			delete this.entityList._keyMap[data.entityId];
		}
	});
	
	dorado.EntityList.EntityListIterator = $extend(dorado.util.Iterator, {
		$className: "dorado.EntityList.EntityListIterator",
		
		constructor: function(entityList, options) {
			this._entityList = entityList;
			
			if (options === true) {
				this._includeDeletedEntity = true;
			} else if (options instanceof Object) {
				this._includeDeletedEntity = options.includeDeletedEntity;
				this._includeUnloadPage = options.includeUnloadPage;
				this._nextIndex = options.nextIndex;
				this._fixedPageNo = options.pageNo;
				if (!this._fixedPageNo && options.currentPage) this._fixedPageNo = entityList.pageNo;
				this._simulateUnloadPage = options.simulateUnloadPage;
				if (this._simulateUnloadPage) this._includeUnloadPage = true;
			}
			this.firstOrLast();
		},
		
		firstOrLast: function(reverse) {
			var entityList = this._entityList;
			var pageCount = entityList.pageCount;
			var pageNo = this._pageNo = (this._fixedPageNo || (reverse ? pageCount : 1));
			
			this._previous = this._current = this._next = null;
			this._previousPage = this._currentPage = this._nextPage = pageNo;
			
			if (this._nextIndex) {
				var skiped = 0;
				if (!this._fixedPageNo) {
					while (pageNo > 0 && pageNo <= pageCount) {
						var page = entityList.getPage(pageNo, false);
						skiped += page.entityCount;
						if (skiped > this._nextIndex) {
							skiped -= page.entityCount;
							break;
						}
						reverse ? pageNo-- : pageNo++;
						if (skiped >= this._nextIndex) break;
					}
				}
				
				this._next = this._findFromPage(pageNo);
				if (this._next) {
					this._nextPage = this._next.pageNo;
					this._current = this._findNeighbor(this._next, this._nextPage, true);
					if (this._current) {
						this._currentPage = this._current.pageNo;
						this._previous = this._findNeighbor(this._current, this._currentPage, true);
						if (this._previous) this._previousPage = this._previous.pageNo;
					}
					
					for (var i = skiped; i < this._nextIndex; i++) {
						if (this.hasNext()) this.next();
						else break;
					}
				}
				delete this._nextIndex;
			} else {
				var result = this._findFromPage(pageNo, reverse);
				if (reverse) {
					this._previous = result;
					if (this._previous) this._previousPage = this._previous.pageNo;
				} else {
					this._next = result;
					if (this._next) this._nextPage = this._next.pageNo;
				}
			}
		},
		
		_findFromPage: function(pageNo, reverse) {
			var result = null, entityList = this._entityList, pageCount = entityList.pageCount, page;
			if (this._includeUnloadPage) {
				page = entityList.getPage(pageNo, !this._simulateUnloadPage);
			} else {
				while (pageNo > 0 && pageNo <= pageCount) {
					var p = entityList.getPage(pageNo, false);
					if (p.loaded) {
						page = p;
						break;
					}
					if (this._fixedPageNo) break;
					pageNo += (reverse ? -1 : 1);
				}
			}
			
			if (page) {
				if (page.loaded) {
					result = reverse ? page.last : page.first;
					while (result && !this._includeDeletedEntity &&
					result.data.state == dorado.Entity.STATE_DELETED) {
						result = reverse ? result.previous : result.next;
					}
				} else {
					result = {
						data: dorado.Entity.getDummyEntity(pageNo)
					};
					this._simulatePageSize = (pageNo == pageCount) ? (entityList.entityCount % entityList.pageSize) : entityList.pageSize;
					this._simulateIndex = (reverse) ? this._simulatePageSize : 0;
				}
			}
			return result;
		},
		
		_findNeighbor: function(entry, pageNo, reverse) {
			if (!entry) return null;
			var inc = reverse ? -1 : 1;
			if (entry.data && !entry.data.dummy) {
				do {
					entry = reverse ? entry.previous : entry.next;
				}
				while (entry && !this._includeDeletedEntity && entry.data.state == dorado.Entity.STATE_DELETED);
				if (entry) entry.pageNo = pageNo;
			} else {
				this._simulateIndex += inc;
				if (this._simulateIndex < 0 || this._simulateIndex >= this._simulatePageSize) {
					this._simulateIndex -= inc;
					entry = null;
				}
			}
			if (entry == null && !this._fixedPageNo) {
				pageNo += inc;
				if (pageNo > 0 && pageNo <= this._entityList.pageCount) {
					entry = this._findFromPage(pageNo, reverse);
				}
			}
			return entry;
		},
		
		_find: function(reverse) {
			var result = this._findNeighbor((reverse ? this._previous : this._next), (reverse ? this._previousPage : this._nextPage), reverse);
			if (reverse) {
				this._next = this._current;
				this._nextPage = this._currentPage;
				this._current = this._previous;
				this._currentPage = this._previousPage;
				this._previous = result;
				this._previousPage = result ? result.data.page.pageNo : 1;
			} else {
				this._previous = this._current;
				this._previousPage = this._currentPage;
				this._current = this._next;
				this._currentPage = this._nextPage;
				this._next = result;
				this._nextPage = result ? result.data.page.pageNo : this._entityList.pageCount;
			}
		},
		
		first: function() {
			this.firstOrLast();
		},
		
		last: function() {
			this.firstOrLast(true);
		},
		
		hasPrevious: function() {
			return !!this._previous;
		},
		
		hasNext: function() {
			return !!this._next;
		},
		
		previous: function() {
			if (!this._previous) {
				this._next = this._current;
				this._nextPage = this._currentPage;
				this._current = this._previous = null;
				this._currentPage = this._previousPage = 1;
				return null;
			}
			var data = this._previous.data;
			this._find(true);
			return data;
		},
		
		next: function() {
			if (!this._next) {
				this._previous = this._current;
				this._previousPage = this._currentPage;
				this._current = this._next = null;
				this._currentPage = this._nextPage = this._entityList.pageCount;
				return null;
			}
			var data = this._next.data;
			this._find(false);
			return data;
		},
		
		current: function() {
			return (this._current) ? this._current.data : null;
		},
		
		createBookmark: function() {
			return {
				previous: this._previous,
				current: this._current,
				next: this._next,
				previousPage: this._previousPage,
				currentPage: this._currentPage,
				nextPage: this._nextPage,
				simulateIndex: this._simulateIndex,
				simulatePageSize: this._simulatePageSize
			};
		},
		
		restoreBookmark: function(bookmark) {
			this._previous = bookmark.previous;
			this._current = bookmark.current;
			this._next = bookmark.next;
			this._previousPage = bookmark.previousPage;
			this._currentPage = bookmark.currentPage;
			this._nextPage = bookmark.nextPage;
			this._simulateIndex = bookmark.simulateIndex;
			this._simulatePageSize = bookmark.simulatePageSize;
		}
	});
	
	LoadPagePipe = $extend(dorado.DataPipe, {
	
		constructor: function(entityList, pageNo) {
			this.entityList = entityList;
			var dataType = entityList.dataType, view;
			if (dataType) {
				var dataTypeRepository = dataType.get("dataTypeRepository");
				this.dataTypeRepository = dataTypeRepository;
				view = dataTypeRepository ? dataTypeRepository._view : null;
			}
			
			this.dataProviderArg = {
				parameter: entityList.parameter,
				pageSize: entityList.pageSize,
				pageNo: pageNo,
				dataType: dataType,
				view: view
			};
		},
		
		doGet: function() {
			return this.doGetAsync();
		},
		
		doGetAsync: function(callback) {
			var dataProvider = this.entityList.dataProvider, dataProviderArg = this.dataProviderArg, oldSupportsEntity = dataProvider.supportsEntity;
			dataProvider.supportsEntity = false;
			try {
				if (callback) {
					dataProvider.getResultAsync(dataProviderArg, callback);
				} else {
					return dataProvider.getResult(dataProviderArg);
				}
			}
			finally {
				dataProvider.supportsEntity = oldSupportsEntity;
			}
		}
	});
	
}());
