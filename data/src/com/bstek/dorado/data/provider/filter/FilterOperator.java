package com.bstek.dorado.data.provider.filter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-3-1
 */
public enum FilterOperator {
	eq {
		public String toString() {
			return "=";
		}
	},
	ne {
		public String toString() {
			return "<>";
		}
	},
	gt {
		public String toString() {
			return ">";
		}
	},
	lt {
		public String toString() {
			return "<";
		}
	},
	le {
		public String toString() {
			return "<=";
		}
	},
	ge {
		public String toString() {
			return ">=";
		}
	},
	like, likeStart {
		public String toString() {
			return "like*";
		}
	},
	likeEnd {
		public String toString() {
			return "*like";
		}
	},
	between, in
}