package com.zcdh.mobile.framework.nio;

import java.nio.charset.Charset;

public abstract class AbstractMessage {
	
	// 协议编号
	public abstract short getTag();

	// 数据区长度

	public abstract int getLen(Charset charset);
	
}
