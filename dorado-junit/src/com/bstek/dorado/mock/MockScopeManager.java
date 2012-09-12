package com.bstek.dorado.mock;

import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.core.bean.ScopeManager;

public class MockScopeManager extends ScopeManager {

	private Map<String, Object> requestValueMap = new HashMap<String, Object>();
	private Map<String, Object> sessionValueMap = new HashMap<String, Object>();

	private Map<String, Object> getRequestContext() {
		return requestValueMap;
	}

	private Map<String, Object> getSessionContext() {
		return sessionValueMap;
	}

	@Override
	public Object getBean(Scope scope, String key) {
		if (Scope.request.equals(scope)) {
			return getRequestContext().get(key);
		} else if (Scope.session.equals(scope)) {
			return getSessionContext().get(key);
		} else {
			return super.getBean(scope, key);
		}
	}

	@Override
	public void putBean(Scope scope, String key, Object bean) {
		if (Scope.request.equals(scope)) {
			getRequestContext().put(key, bean);
		} else if (Scope.session.equals(scope)) {
			getSessionContext().put(key, bean);
		} else {
			super.putBean(scope, key, bean);
		}
	}

	@Override
	public Object removeBean(Scope scope, String key) {
		if (Scope.request.equals(scope)) {
			return getRequestContext().remove(key);
		} else if (Scope.session.equals(scope)) {
			return getSessionContext().remove(key);
		} else {
			return super.removeBean(scope, key);
		}
	}

	@Override
	public void clear(Scope scope) {
		if (Scope.request.equals(scope)) {
			getRequestContext().clear();
		} else if (Scope.session.equals(scope)) {
			getSessionContext().clear();
		} else {
			super.clear(scope);
		}
	}
	
}
