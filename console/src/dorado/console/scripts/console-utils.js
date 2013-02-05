
/**
 *  Dorado 控制台 格式化辅助
 *  Alex Tong (mailto:alex.tong@bstek.com)
 */
FormaterUtils = {
	_size : function(value) {
		var _format = function(value, unit) {
			return (value.toFixed(1) + ' ' + unit).replace('.0', '');
		};
		var K = 1024, M = K * K, G = M * K, T = G * K;
		var dividers = [ T, G, M, K, 1 ], units = [ 'TB', 'GB', 'MB', 'KB', 'B' ];
		if (value == 0) {
			return '0B';
		} else if (value < 0) {
			return 'Invalid size';
		}

		var result = '', temp = 0;
		for ( var i = 0; i < dividers.length; i++) {
			var divider = dividers[i];
			if (value >= divider) {
				temp = value / divider;
				if (temp < 1.05) {
					result = _format(value,
							units[((i + 1) < units.length) ? (i + 1) : i]);
				} else {
					result = _format(temp, units[i]);
				}
				break;
			}
		}
		return result;
	},
	_date : function(date, fmt) {
		// "yyyy-MM-dd hh:mm:ss.S"==> 2006-07-02 08:09:04.423
		// "yyyy-M-d h:m:s.S" ==> 2006-7-2 8:9:4.18
		var o = {
			"M+" : date.getMonth() + 1,
			"d+" : date.getDate(),
			"h+" : date.getHours(),
			"m+" : date.getMinutes(),
			"s+" : date.getSeconds(),
			"q+" : Math.floor((date.getMonth() + 3) / 3),
			"S" : date.getMilliseconds()
		};
		if (/(y+)/.test(fmt))
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(fmt))
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
						: (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	},
	_time : function(time) {
		return this._date(new Date(time), "yyyy-MM-dd hh:mm:ss.S");
	},
	_timeLength : function(time) {
		time = Math.floor(time);
		var tmpTime = Math.floor(time / 1000), h, m, s, sf, value;
		h = Math.floor(tmpTime / 3600);
		m = Math.floor((tmpTime % 3600) / 60);
		s = Math.floor((tmpTime % 3600 % 60));
		sf = (time - h * 60 * 60 * 1000 - m * 60 * 1000 - s * 1000) / 1000;
		value = h > 0 ? h + 'h' + m + 'm' : '';
		value = h <= 0 && m > 0 ? m + 'm' : '';
		value = value + (s + sf)+'s';
		return value;
	}
}
/**
 *  Dorado 控制台数学算法辅助
 */
ALG_Utils = {
	_avg : function(nums) {
		var sum = 0;
		for ( var i = 0; i < nums.length; i++) {
			sum += nums[i];
		}
		var value = (nums.length == 0 ? '0' : sum / nums.length) + '';
		return value.indexOf(".") > 0 ? value.substring(0, value.indexOf("."))
				: value;
	},
	_percent : function(num, total) {
		num = parseFloat(num);
		total = parseFloat(total);
		if (isNaN(num) || isNaN(total)) {
			return "-";
		}
		return total <= 0 ? "0%"
				: (Math.round(num / total * 10000) / 100.00 + "%");
	}
}

/**
 * 解决小数科学计数法的问题
 */
String.prototype.expandExponential = function() {
	return this
			.replace(
					/^([+-])?(\d+).?(\d*)[eE]([-+]?\d+)$/,
					function(x, s, n, f, c) {
						var l = +c < 0, i = n.length + +c, x = (l ? n : f).length, c = ((c = Math
								.abs(c)) >= x ? c - x + l : 0), z = (new Array(
								c + 1)).join("0"), r = n + f;
						return (s || "")
								+ (l ? r = z + r : r += z).substr(0,
										i += l ? z.length : 0)
								+ (i < r.length ? "." + r.substr(i) : "");
					});
};
/** @Global */
function parseString(value) {
	if (value == null) {
		return "";
	}
	if (typeof value == "number") {
		return value.toString().expandExponential();
	}
	return value.toString();
}
/**
 * 解决小数科学计数法的问题，重写解析string的方法
 */
Number.prototype.old_toString = Number.prototype.toString;
Number.prototype.toString = function() {
	return this.old_toString().expandExponential();
}
