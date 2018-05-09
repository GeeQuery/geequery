package com.github.geequery.accelerator.asm.commons;

import com.github.geequery.accelerator.asm.FieldVisitor;
import com.github.geequery.tools.Assert;

public abstract class FieldExtCallback {
	
	public FieldExtCallback(FieldVisitor v){
		this.visitor=v;
		Assert.notNull(v);
	}
	
	FieldVisitor visitor;
	
	
	public abstract void onFieldRead(FieldExtDef info);
}
