package com.madhouse.platform.premiummad.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.madhouse.platform.premiummad.validator.ConstraintValidator;

@Target({ java.lang.annotation.ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidConstraint {
	public abstract Class<? extends ConstraintValidator<?, ?>> validatedBy();
}