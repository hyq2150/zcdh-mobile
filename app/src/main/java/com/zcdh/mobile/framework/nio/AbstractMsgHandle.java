/**
 * 
 * @author danny, 2013-9-16 下午5:45:20
 */
package com.zcdh.mobile.framework.nio;

import java.lang.reflect.Type;

/**
 * @author danny, 2013-9-16 下午5:45:20
 */
public abstract  class AbstractMsgHandle implements IMsgHandle {
    private Type type;//返回数据类型

	public AbstractMsgHandle(Type type) {
		super();
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.core.network.nio.IMsgHandle#handleMessage(java.lang.Boolean, java.lang.Object)
	 */
	@Override
	public abstract void handleMessage( Boolean isSuccess,Object data);

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	
}
