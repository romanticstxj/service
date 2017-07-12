package com.madhouse.platform.premiummad.validator;

import java.lang.annotation.Annotation;

public abstract interface ConstraintValidator<A extends Annotation, T> {
	public abstract void initialize(A paramA);

	public abstract boolean isValid(T paramT);
}